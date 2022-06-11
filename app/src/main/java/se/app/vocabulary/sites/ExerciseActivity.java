package se.app.vocabulary.sites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;

import se.app.vocabulary.MainActivity;
import se.app.vocabulary.R;
import se.app.vocabulary.adapters.ExerciseAdapter;
import se.app.vocabulary.adapters.WordsAdapter;
import se.app.vocabulary.controller.Service;
import se.app.vocabulary.model.Vocabulary;
import se.app.vocabulary.model.Words;

public class ExerciseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String LOG_TITLE = "ExerciseActivity";

    Service service = new Service(this);
    ArrayList<Words> wordList = new ArrayList<>();
    ArrayList<Words> chosenWords;
    ArrayList<Vocabulary> vocabulariesList = new ArrayList<>();
    int vocabularyId = 0;

    //GUI
    TextView title;
    ImageView backBtn;
    Button generate;
    RecyclerView recyclerView;
    EditText numbersInput;
    Spinner spinner;

    ExerciseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        //Maradjon aktív a képernyő
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Inicializálás
        title = findViewById(R.id.exercise_title_gui);
        backBtn = findViewById(R.id.exercise_back_btn_gui);
        generate = findViewById(R.id.generate_words_gui);
        recyclerView = findViewById(R.id.exercise_recycler_gui);
        numbersInput = findViewById(R.id.exercise_num_of_words);
        spinner = findViewById(R.id.excercise_spinner_gui);

        //Adatok áttöltése
        getIntentData();

        //Spinner feltöltése
        String[] vocabulariesArray = vocabulariesList.stream().map(Vocabulary::getName).toArray(String[]::new);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, vocabulariesArray);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);


        //Visszalépés
        backBtn.setOnClickListener(v -> {
            Intent main = new Intent(this, MainActivity.class);
            finish();
            startActivity(main);
            Log.i(LOG_TITLE, "Back to the Main Screen");
        });

        //Szavak lekérése
        generate.setOnClickListener(v -> {
            int numOfWords = 5;
            String number = numbersInput.getText().toString();
            if(!isNumeric(number)) {
                Toast.makeText(this, "Ez nem szám!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!number.equals("")) {
                numOfWords = Integer.parseInt(numbersInput.getText().toString());
                if(numOfWords >= wordList.size()) {
                    Toast.makeText(this, "Kicsit kevesebbet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(numOfWords > 10) {
                    Toast.makeText(this, "10 a max megengedett", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            //
            chosenWords = new ArrayList<>();
            Random rnd = new Random();
            int randomId;
            for(int i = 0; i < numOfWords; i++) {
                randomId = rnd.nextInt(wordList.size() - 1) + 1;
                while(chosenWords.contains(wordList.get(randomId))) randomId = rnd.nextInt(wordList.size() - 1) + 1;
                chosenWords.add(wordList.get(randomId));
            }
            showWords();
        });
    }

    private void getIntentData() {
        title.setText(getString(R.string.exercise_title));
        wordList = service.getWords();

        vocabulariesList = service.getVocabularies();
        vocabulariesList.add(new Vocabulary(-1, "Összes"));
        vocabulariesList.sort(Comparator.comparing(Vocabulary::getId));
        System.out.println(vocabulariesList);
    }

    private void showWords() {
        adapter = new ExerciseAdapter(this, chosenWords);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ((TextView) adapterView.getChildAt(0)).setTextSize(20);
        String spinnerTitle = "Szavak a(z) " + vocabulariesList.get(i).getName() + " szótárból";
        ((TextView) adapterView.getChildAt(0)).setText(spinnerTitle);
        vocabularyId = i;

        wordList.clear();
        if(vocabularyId == 0) {
            wordList = service.getWords();
        } else {
            wordList = service.getWords(vocabularyId);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
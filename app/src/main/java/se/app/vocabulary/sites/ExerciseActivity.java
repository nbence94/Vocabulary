package se.app.vocabulary.sites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import se.app.vocabulary.MainActivity;
import se.app.vocabulary.R;
import se.app.vocabulary.adapters.ExerciseAdapter;
import se.app.vocabulary.adapters.WordsAdapter;
import se.app.vocabulary.controller.Service;
import se.app.vocabulary.model.Words;

public class ExerciseActivity extends AppCompatActivity {

    private final String LOG_TITLE = "ExerciseActivity";

    Service service = new Service(this);
    ArrayList<Words> wordList = new ArrayList<>();
    ArrayList<Words> chosenWords;
    int vocabularyId;

    //GUI
    TextView title;
    ImageView backBtn;
    Button generate;
    RecyclerView recyclerView;
    EditText numbersInput;

    ExerciseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        //Maradjon aktív a képernyő
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        title = findViewById(R.id.exercise_title_gui);
        backBtn = findViewById(R.id.exercise_back_btn_gui);
        generate = findViewById(R.id.generate_words_gui);
        recyclerView = findViewById(R.id.exercise_recycler_gui);
        numbersInput = findViewById(R.id.exercise_num_of_words);

        getIntentData();

        backBtn.setOnClickListener(v -> {
            Intent main = new Intent(this, MainActivity.class);
            finish();
            startActivity(main);
            Log.i(LOG_TITLE, "Back to the Main Screen");
        });

        generate.setOnClickListener(v -> {
            int numOfWords = 5;
            if(!numbersInput.getText().toString().equals("")) {
                numOfWords = Integer.parseInt(numbersInput.getText().toString());
                if(numOfWords >= wordList.size()) {
                    Toast.makeText(this, "Nincs ennyi szó", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

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
        if(getIntent().hasExtra("id")) {
            vocabularyId = getIntent().getIntExtra("id", -1);
            title.setText(getIntent().getStringExtra("name"));
            Log.i(LOG_TITLE, "VOCABULARY-ID: " + vocabularyId);
            wordList = service.getWords(vocabularyId);
        } else {
            title.setText(getString(R.string.exercise_title));
            wordList = service.getWords();
        }
    }

    private void showWords() {
        adapter = new ExerciseAdapter(this, chosenWords);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
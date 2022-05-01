package se.app.vocabulary.sites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.controls.Control;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import se.app.vocabulary.MainActivity;
import se.app.vocabulary.R;
import se.app.vocabulary.controller.Controller;
import se.app.vocabulary.model.Words;

public class QuizActivity extends AppCompatActivity {

    private final String LOG_TITLE = "QuizActivity";
    int vocabulary_id;
    ArrayList<Words> list;
    Controller controller = new Controller(this);

    //GUI
    TextView title, actual_word, score;
    EditText input_solution;
    Button ok_btn;
    ImageView back_btn;

    int chosen_word_id;
    int mod = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        list = new ArrayList<>();

        title = findViewById(R.id.quiz_title_gui);
        back_btn = findViewById(R.id.quiz_back_btn_gui);
        actual_word = findViewById(R.id.actual_word_gui);
        score = findViewById(R.id.words_stat_gui);
        input_solution = findViewById(R.id.guess_the_word_gui);
        ok_btn = findViewById(R.id.agree_button_gui);

        getIntentData();
        actual_word.setText(chooseAWord());

        ok_btn.setOnClickListener(v -> check());

        input_solution.setOnKeyListener((view, code, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN)
            {
                switch (code)
                {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        check();
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });

        back_btn.setOnClickListener(v -> {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        });
    }

    private void check() {
        String guess = input_solution.getText().toString();
        String solution;

        if(mod == 0) solution = list.get(chosen_word_id).getEnglish();
        else solution = list.get(chosen_word_id).getHungarian();

        String msg;
        if(guess.toLowerCase().equals(solution.toLowerCase())) {
            msg = "Helyes!";
        } else {
            msg = "Nem jó! Helyesen: " + solution;
        }
        score.setText(msg);
        actual_word.setText(chooseAWord());
        input_solution.setText("");
    }

    private String chooseAWord() {
        Random r = new Random();
        chosen_word_id = r.nextInt(list.size());
        Log.i(LOG_TITLE, "Random: " + chosen_word_id + " - Size:" + list.size());
        Log.e("DEBUG", "" + r.nextInt() % 2);
        mod = r.nextInt() % 2;
        if(mod == 0) {
            return list.get(chosen_word_id).getHungarian();
        } else {
            return list.get(chosen_word_id).getEnglish();
        }
    }

    private void getIntentData() {
        if(getIntent().hasExtra("id")) {
            vocabulary_id = getIntent().getIntExtra("id", -1);
            title.setText(getIntent().getStringExtra("name"));
            Log.i(LOG_TITLE, "QUIZ-ID: " + vocabulary_id);
            list = controller.getWords(vocabulary_id);
        }
    }
}
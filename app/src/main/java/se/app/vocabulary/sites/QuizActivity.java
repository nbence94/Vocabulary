package se.app.vocabulary.sites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    TextView title, actual_word, solution_msg, score, num_of_words;
    FloatingActionButton check_btn, hint_btn;
    EditText input_solution;
    ImageView back_btn;

    int chosen_word_id;
    int mod = 0;
    int scorepoint = 0, word = 0;
    int hint_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        list = new ArrayList<>();

        title = findViewById(R.id.quiz_title_gui);
        back_btn = findViewById(R.id.quiz_back_btn_gui);
        actual_word = findViewById(R.id.actual_word_gui);
        solution_msg = findViewById(R.id.words_stat_gui);
        input_solution = findViewById(R.id.guess_the_word_gui);
        check_btn = findViewById(R.id.quiz_check_button_gui);
        hint_btn = findViewById(R.id.hint_button_gui);
        score = findViewById(R.id.scores_gui);
        num_of_words = findViewById(R.id.number_of_words);

        getIntentData();
        actual_word.setText(chooseAWord());

        check_btn.setOnClickListener(v -> check());

        String words_amount = "Szavak száma: " + list.size();
        num_of_words.setText(words_amount);

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

        hint_btn.setOnClickListener(v -> {
            String hint_word = "";
            Random rnd = new Random();
            if(mod == 0) hint_word = list.get(chosen_word_id).getEnglish();
            else hint_word = list.get(chosen_word_id).getHungarian();

            int random_number;
            for(int i = 0; i < hint_word.length(); i++) {
                random_number = rnd.nextInt(10 - 1 + 1) + 1;
                if(random_number % 2 == 0) hint_word = hint_word.replace(hint_word.charAt(i), '_');
            }
            //random.nextInt(max - min + 1) + min
            input_solution.setHint(hint_word);
            hint_count++;
        });

        back_btn.setOnClickListener(v -> {
            Intent main = new Intent(this, MainActivity.class);
            finish();
            startActivity(main);
        });
    }

    private void check() {
        String guess = input_solution.getText().toString().trim();
        String solution;

        if(mod == 0) solution = list.get(chosen_word_id).getEnglish();
        else solution = list.get(chosen_word_id).getHungarian();

        String msg;
        if(guess.toLowerCase(Locale.ROOT).equals(solution.toLowerCase())) {
            msg = "Helyes!";
            scorepoint++;
        } else {
            msg = "Megoldás: " + solution;
            if(!solution.equals("")) msg += "\nTipped: " + guess;
        }

        score.setText(setScore(scorepoint, ++word));

        solution_msg.setText(msg);
        actual_word.setText(chooseAWord());
        input_solution.setText("");
        input_solution.setHint("Mi a szó jelentése?");
        hint_count = 0;
    }

    private String setScore(int score, int word) {
        return score + "/" + word;
    }

    private String chooseAWord() {
        Random r = new Random();
        chosen_word_id = r.nextInt(list.size());
        Log.i(LOG_TITLE, "Chosen wordId: " + chosen_word_id + " - Size:" + list.size());
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
            Log.i(LOG_TITLE, "QUIZ-MODE: ONLY (" + vocabulary_id + ")");
            list = controller.getWords(vocabulary_id);
        } else {
            Log.i(LOG_TITLE, "QUIZ-MODE: ALL");
            list = controller.getWords();
        }
        title.setText(getIntent().getStringExtra("name"));
    }
}
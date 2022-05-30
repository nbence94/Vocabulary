package se.app.vocabulary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import se.app.vocabulary.adapters.VocabularyAdapter;
import se.app.vocabulary.controller.Service;
import se.app.vocabulary.data.DatabaseHelper;
import se.app.vocabulary.model.Vocabulary;
import se.app.vocabulary.model.Words;
import se.app.vocabulary.sites.ExerciseActivity;
import se.app.vocabulary.sites.QuizActivity;
import se.app.vocabulary.sites.VocabularyActivity;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton mainButton, addNewVocabularyButton, startQuizButton, startExerciseButton;
    RecyclerView recyclerView;
    VocabularyAdapter adapter;
    Service service = new Service(this);
    DatabaseHelper dh = new DatabaseHelper(this);

    ArrayList<Vocabulary> list = new ArrayList<>();

    //Animáció
    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation toBottom;
    private Animation fromBottom;
    private Animation toRight;
    private Animation fromRight;
    private Animation toDiagonal;
    private Animation fromDiagonal;

    boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rotateOpen = AnimationUtils.loadAnimation(getBaseContext(), R.anim.open);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.close);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom);
        toRight= AnimationUtils.loadAnimation(this, R.anim.to_right);
        fromRight = AnimationUtils.loadAnimation(this, R.anim.from_right);
        toDiagonal = AnimationUtils.loadAnimation(this, R.anim.to_diagonal);
        fromDiagonal = AnimationUtils.loadAnimation(this, R.anim.from_diagonal);

        mainButton = findViewById(R.id.main_main_button);
        mainButton.setOnClickListener(v -> {
            setVisibility(clicked);
            setAnimation(clicked);
            clicked = !clicked;

            //Felesleges szavak törlése (Amik nincsenek szótárakba (bugfix))
            /*List<Words> a = service.getData("SELECT * FROM Words WHERE ID NOT IN (" +
                    "SELECT wordID FROM Connection);");

            for(Words w : a) {
                dh.delete(dh.WORDS, "ID", String.valueOf(w.getId()));
            }*/

        });

        addNewVocabularyButton = findViewById(R.id.main_add_vocabulary_gui);
        addNewVocabularyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VocabularyActivity.class);
            startActivity(intent);
        });

        startQuizButton = findViewById(R.id.main_start_complete_quiz);
        startQuizButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("name", "Összesített szavak");
            startActivity(intent);
        });

        startExerciseButton = findViewById(R.id.exercises_btn);
        startExerciseButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExerciseActivity.class);
            intent.putExtra("name", "Mondat gyakorló");
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.main_recycler_gui);
        loadList();
        loadRecyler();
    }

    private void setVisibility(boolean clicked) {
        if(!clicked) {
            addNewVocabularyButton.setVisibility(View.VISIBLE);
            startQuizButton.setVisibility(View.VISIBLE);
            startExerciseButton.setVisibility(View.VISIBLE);
        } else {
            addNewVocabularyButton.setVisibility(View.GONE);
            startQuizButton.setVisibility(View.GONE);
            startExerciseButton.setVisibility(View.GONE);
        }
    }

    private void setAnimation(boolean clicked) {
        if(!clicked) {
            addNewVocabularyButton.startAnimation(fromDiagonal);
            startQuizButton.startAnimation(fromRight);
            startExerciseButton.startAnimation(fromBottom);

            mainButton.startAnimation(rotateOpen);
            //getString(R.string.vocabulary_update)
            //mainButton.setImageDrawable(R.drawable.x_icon);
            mainButton.setImageResource(R.drawable.x_icon);
        } else {
            addNewVocabularyButton.startAnimation(toDiagonal);
            startQuizButton.startAnimation(toRight);
            startExerciseButton.startAnimation(toBottom);

            mainButton.startAnimation(rotateClose);
            mainButton.setImageResource(R.drawable.menu_icon);
        }
    }

    private void loadList() {
        list = service.getVocabularies();
    }

    private void loadRecyler() {
        adapter = new VocabularyAdapter(this, this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
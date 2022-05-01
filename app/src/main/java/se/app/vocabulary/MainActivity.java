package se.app.vocabulary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.stream.Collectors;

import se.app.vocabulary.adapters.VocabularyAdapter;
import se.app.vocabulary.controller.Controller;
import se.app.vocabulary.model.Vocabulary;
import se.app.vocabulary.sites.VocabularyActivity;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton add_new_vocabulary_btn;
    RecyclerView recycler_view;
    VocabularyAdapter adapter;
    Controller controller = new Controller(this);

    ArrayList<Vocabulary> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_new_vocabulary_btn = findViewById(R.id.main_add_vocabulary_gui);
        add_new_vocabulary_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VocabularyActivity.class);
            startActivity(intent);
        });

        recycler_view = findViewById(R.id.main_recycler_gui);
        loadList();
        loadRecyler();
    }

    private void loadList() {
        list = controller.getVocabularies();
        Log.i("Nevek: ", list.stream().map(Vocabulary::getName).collect(Collectors.joining(",")));
    }

    private void loadRecyler() {
        adapter = new VocabularyAdapter(this, this, list);
        recycler_view.setAdapter(adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }
}
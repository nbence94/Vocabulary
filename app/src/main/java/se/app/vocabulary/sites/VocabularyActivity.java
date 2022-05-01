package se.app.vocabulary.sites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import se.app.vocabulary.MainActivity;
import se.app.vocabulary.R;
import se.app.vocabulary.adapters.WordsAdapter;
import se.app.vocabulary.controller.Controller;
import se.app.vocabulary.data.DatabaseHelper;
import se.app.vocabulary.model.Words;

public class VocabularyActivity extends AppCompatActivity {

    private final String LOG_TITLE = "VocabularyActivity";
    DatabaseHelper dh = new DatabaseHelper(this);
    Controller controller = new Controller(this);

    ImageView back_button;
    TextView title;
    EditText input_name;
    FloatingActionButton save_button;
    Button add_new;

    ArrayList<Words> list = new ArrayList<>();

    //RecyclerView
    RecyclerView recyclerView;
    WordsAdapter adapter;

    //Szerkesztés rész
    boolean update_mode = false;
    int vocabulary_id = -1;
    String vocabulary_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);



        back_button = findViewById(R.id.vocabulary_back_btn_gui);
        title = findViewById(R.id.vocabulary_title_gui);
        input_name= findViewById(R.id.vocabulary_name_gui);
        save_button= findViewById(R.id.vocabulary_save_btn_gui);
        add_new = findViewById(R.id.add_new_word_gui);

        GetIntentData();

        back_button.setOnClickListener(v -> {
            Intent intent = new Intent(VocabularyActivity.this, MainActivity.class);
            startActivity(intent);
        });

        String the_title;
        if(!update_mode) the_title = getString(R.string.vocabulary_new);
        else the_title = getString(R.string.vocabulary_update);
        title.setText(the_title);

        //Új szó hozzáadása
        add_new.setOnClickListener(v -> {
            AlertDialog.Builder et_dialog = new AlertDialog.Builder(this);
            et_dialog.setTitle("Szó konfiguráció");

            Context context = this;
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText en_word = new EditText(context);
            en_word.setHint("Angol jelentés");
            layout.addView(en_word);

            final EditText hun_word = new EditText(context);
            hun_word.setHint("Magyar jelentés");
            layout.addView(hun_word);

            et_dialog.setPositiveButton("Oké", (dialog, whichButton) -> {

                if(!en_word.getText().toString().equals("") && !hun_word.getText().toString().equals("")) {
                    list.add(new Words(list.size(), en_word.getText().toString(), hun_word.getText().toString()));
                    Log.i(LOG_TITLE, "Új szó felvéve!");
                    loadRecyclerView();
                }
            });

            et_dialog.setNegativeButton("Mégse", (dialog, whichButton) -> {

            });

            et_dialog.setView(layout);
            et_dialog.show();
        });

        recyclerView = findViewById(R.id.vocabulary_recycler_gui);
        loadRecyclerView();

        //Mentés gomb
        save_button.setOnClickListener(v -> {
            String vocabulary_name = input_name.getText().toString();

            if(vocabulary_name.equals("")) {
                Toast.makeText(this, "Adj nevet a szótárnak!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(list.size() == 0) {
                Toast.makeText(this, "Tegyél bele legalább 1 szót!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(!update_mode) {
                if (dh.insert(dh.VOCABULARY, new String[]{vocabulary_name}))
                    vocabulary_id = controller.getTheNewID(dh.VOCABULARY);
                else {
                    Log.e(LOG_TITLE, "Nem sikerült menteni a szótárat!");
                    Toast.makeText(this, "Mentés sikertelen!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(LOG_TITLE, "Az új szótár ID: " + vocabulary_id);
            } else {
                if (!dh.update(dh.VOCABULARY, "ID", new String[]{vocabulary_name}, String.valueOf(vocabulary_id))) {
                    Log.e(LOG_TITLE, "Nem sikerült menteni a szótárat!");
                    Toast.makeText(this, "Mentés sikertelen!", Toast.LENGTH_SHORT).show();
                    return;
                }

                dh.delete(dh.CONNECT, "VocabularyID", String.valueOf(vocabulary_id));
            }

            for (int i = 0; i < list.size(); i++) {
                dh.insert(dh.WORDS, new String[]{list.get(i).getEnglish(), list.get(i).getHungarian()});
                dh.insert(dh.CONNECT, new String[]{String.valueOf(controller.getTheNewID(dh.WORDS)), String.valueOf(vocabulary_id)});
            }


            Toast.makeText(this, "Sikeres mentés!", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TITLE, "NEW-VOCABULARY: id(" + vocabulary_id + "), elemek-száma(" + list.size() + ")");
            Intent intent = new Intent(VocabularyActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                list.remove(viewHolder.getAdapterPosition());
                adapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void GetIntentData() {
        if(getIntent().hasExtra("id")) {
            update_mode = true;

            vocabulary_id = getIntent().getIntExtra("id", -1);
            input_name.setText(getIntent().getStringExtra("name"));
            Log.i(LOG_TITLE, "VOCABULARY-UPDATE-ID: " + vocabulary_id);
            list = controller.getWords(vocabulary_id);
        }
    }

    private void loadRecyclerView() {
        adapter = new WordsAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
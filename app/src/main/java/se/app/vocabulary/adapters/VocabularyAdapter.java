package se.app.vocabulary.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import se.app.vocabulary.R;
import se.app.vocabulary.controller.Service;
import se.app.vocabulary.model.Vocabulary;
import se.app.vocabulary.sites.QuizActivity;
import se.app.vocabulary.sites.VocabularyActivity;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.MyViewHolder>{

    ArrayList<Vocabulary> list;
    Context context;
    Activity activity;
    Service service;

    public VocabularyAdapter(Activity activity, Context context, ArrayList<Vocabulary> list) {
        this.context = context;
        this.activity = activity;
        this.list = list;
        service = new Service(context);
    }

    @NonNull
    @Override
    public VocabularyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(context);
        View view = inf.inflate(R.layout.simple_text_element_layout, parent, false);
        return new VocabularyAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull VocabularyAdapter.MyViewHolder holder, int position) {
        String element_text = list.get(position).getName();
        holder.title.setText(element_text);

        String number = "" + service.getNumberOfWords(list.get(position).getId());
        holder.amount_of_words.setText(number);

        holder.element.setOnClickListener(v -> {
            Intent quiz = new Intent(activity, QuizActivity.class);
            quiz.putExtra("id", list.get(position).getId());
            quiz.putExtra("name", list.get(position).getName());
            activity.startActivityForResult(quiz, 1);
            activity.finish();
        });

        holder.element.setOnLongClickListener(v -> {
            Intent update = new Intent(activity, VocabularyActivity.class);
            update.putExtra("id", list.get(position).getId());
            update.putExtra("name", list.get(position).getName());
            activity.startActivityForResult(update, 1);
            activity.finish();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView element;
        TextView title, amount_of_words;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            element = itemView.findViewById(R.id.element_gui);
            title = itemView.findViewById(R.id.element_text_gui);
            amount_of_words = itemView.findViewById(R.id.more_info_gui);

        }
    }
}

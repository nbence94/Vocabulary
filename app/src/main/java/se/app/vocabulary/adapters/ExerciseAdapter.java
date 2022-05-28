package se.app.vocabulary.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import se.app.vocabulary.R;
import se.app.vocabulary.model.Words;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.MyViewHolder> {

    ArrayList<Words> list;
    Context context;

    public ExerciseAdapter(Context context, ArrayList<Words> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ExerciseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(context);
        View view = inf.inflate(R.layout.simple_text_element_layout, parent, false);
        return new MyViewHolder(view);

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.MyViewHolder holder, int position) {
        String element_text;
        element_text = list.get(position).getEnglish();
        holder.title.setText(element_text);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView element;
        TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            element = itemView.findViewById(R.id.element_gui);
            title = itemView.findViewById(R.id.element_text_gui);

        }
    }
}

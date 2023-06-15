package it.unimib.ciaet.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.unimib.ciaet.R;
import it.unimib.ciaet.model.lesson;

public class lessonadpter extends RecyclerView.Adapter<lessonadpter.ViewHolder>{
    private ArrayList<lesson> lessonArrayList;
    private NavController navController;


    public lessonadpter(ArrayList<lesson> lessonArrayList, NavController navController) {
        this.lessonArrayList = lessonArrayList;
        this.navController = navController;

    }

    @NonNull
    @Override
    public lessonadpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new lessonadpter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlessonlist,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull lessonadpter.ViewHolder holder, int position) {
        lesson Item = lessonArrayList.get(position);
        holder.lessonName.setText(Item.getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("lessonNo",Item.getKey());
                Navigation.findNavController(view).navigate(R.id.action_fragment_lesson_to_fragment_lesson_detail,bundle);
            }
        });

        Log.e("TAG", "onBindViewHolder: "+Item.getKey() );


    }

    @Override
    public int getItemCount() {
        return lessonArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView lessonName;
        private CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonName=itemView.findViewById(R.id.lesson_name);
           cardView=itemView.findViewById(R.id.card1);
        }
    }


}

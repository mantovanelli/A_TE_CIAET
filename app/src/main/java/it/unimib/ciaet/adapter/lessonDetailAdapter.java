package it.unimib.ciaet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import it.unimib.ciaet.R;
import it.unimib.ciaet.model.LessonDetail;
import it.unimib.ciaet.model.lesson;

public class lessonDetailAdapter extends RecyclerView.Adapter<lessonDetailAdapter.ViewHolder>{
    private final ArrayList<LessonDetail> lessonArrayList;
    private final Context context;


    public lessonDetailAdapter(Context context ,ArrayList<LessonDetail> lessonArrayList) {
        this.lessonArrayList = lessonArrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public lessonDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new lessonDetailAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlessondetaillist,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull lessonDetailAdapter.ViewHolder holder, int position) {
        LessonDetail Item = lessonArrayList.get(position);
        holder.lessonName.setText(Item.getName());
        holder.lessondetail.setText(Item.getText());
        Glide.with(context)
                .load(Item.getImage().toString())
                .into(holder.lessonImageView);



    }

    @Override
    public int getItemCount() {
        return lessonArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private  TextView lessonName,lessondetail;
        private ImageView lessonImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonName=itemView.findViewById(R.id.lesson_name);
            lessondetail=itemView.findViewById(R.id.lesson_text);
            lessonImageView=itemView.findViewById(R.id.lessonImage);

        }
    }


}

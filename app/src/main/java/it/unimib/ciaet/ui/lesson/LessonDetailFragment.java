package it.unimib.ciaet.ui.lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.unimib.ciaet.R;
import it.unimib.ciaet.adapter.lessonadpter;
import it.unimib.ciaet.database.LocalDB;
import it.unimib.ciaet.model.LessonDetail;
import it.unimib.ciaet.model.lesson;


public class LessonDetailFragment extends Fragment {
    String coursNo;
    int childCount;
    int count;

    String lessonNo;

    FirebaseDatabase database;

    private TextView lessonName, lessondetail;
    private ImageView lessonImageView;
    Button back;

    ImageView backward, forward;


    public LessonDetailFragment() {

    }

    public static LessonDetailFragment newInstance(String param1, String param2) {
        LessonDetailFragment fragment = new LessonDetailFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lesson_detail, container, false);
        lessonNo = getArguments().getString("lessonNo");
        count = Integer.parseInt(lessonNo);
        LocalDB localDB= new LocalDB(requireContext());


        coursNo = LessonActivity.courseNo;
        lessonName = view.findViewById(R.id.lesson_name);
        lessondetail = view.findViewById(R.id.lesson_text);
        lessonImageView = view.findViewById(R.id.lessonImage);
        back = view.findViewById(R.id.back_button);
        backward = view.findViewById(R.id.backImageView);
        forward = view.findViewById(R.id.nextImageView);
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("lesson");
        DatabaseReference checkRef = database.getReference("lesson");
        DatabaseReference progressRef = database.getReference("User");
        DatabaseReference newRef=progressRef.child(localDB.getUserNodeID()).child("Course").child(coursNo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_lesson_detail_to_fragment_lesson);
            }
        });


        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count--;

                if (count <= 1) {
                    backward.setVisibility(View.INVISIBLE);
                } else if (count >= childCount) {
                    forward.setVisibility(View.INVISIBLE);
                } else {
                    forward.setVisibility(View.VISIBLE);

                    backward.setVisibility(View.VISIBLE);

                }
                myRef.child(coursNo).child(String.valueOf(count)).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            String name = snapshot.child("name").getValue().toString();
                            String image = snapshot.child("image").getValue().toString();
                            String text = snapshot.child("text").getValue().toString();

                            lessonName.setText(name);
                            lessondetail.setText(text);
                            Glide.with(requireContext())
                                    .load(image.toString())
                                    .into(lessonImageView);
                        DatabaseReference databaseReference= newRef.child(String.valueOf(count));
                            databaseReference.setValue("");

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;

                if (count <= 1) {
                    backward.setVisibility(View.INVISIBLE);
                } else if (count >= childCount) {
                    forward.setVisibility(View.INVISIBLE);
                } else {
                    forward.setVisibility(View.VISIBLE);
                    backward.setVisibility(View.VISIBLE);

                }
                myRef.child(coursNo).child(String.valueOf(count)).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            String name = snapshot.child("name").getValue().toString();
                            String image = snapshot.child("image").getValue().toString();
                            String text = snapshot.child("text").getValue().toString();

                            lessonName.setText(name);
                            lessondetail.setText(text);
                            Glide.with(requireContext())
                                    .load(image.toString())
                                    .into(lessonImageView);
                            DatabaseReference databaseReference= newRef.child(String.valueOf(count));
                            databaseReference.setValue("");
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


        checkRef.child(coursNo).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                childCount = (int) snapshot.getChildrenCount();
                DatabaseReference databaseReference = newRef.child("count");
                databaseReference.setValue(childCount);
                if (childCount == 1) {

                    backward.setVisibility(View.INVISIBLE);
                    forward.setVisibility(View.INVISIBLE);
                }else {
                    if (count <= 1) {
                        backward.setVisibility(View.INVISIBLE);
                        forward.setVisibility(View.VISIBLE);
                    } else if (count >= childCount) {
                        backward.setVisibility(View.VISIBLE);
                        forward.setVisibility(View.INVISIBLE);
                    }else {

                        backward.setVisibility(View.VISIBLE);
                        forward.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef.child(coursNo).child(lessonNo).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String name = snapshot.child("name").getValue().toString();
                    String image = snapshot.child("image").getValue().toString();
                    String text = snapshot.child("text").getValue().toString();

                    lessonName.setText(name);
                    lessondetail.setText(text);
                    Glide.with(requireContext())
                            .load(image.toString())
                            .into(lessonImageView);
                    DatabaseReference databaseReference= newRef.child(String.valueOf(count));
                    databaseReference.setValue("");


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }




}
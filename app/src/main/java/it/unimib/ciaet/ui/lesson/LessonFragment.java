package it.unimib.ciaet.ui.lesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.unimib.ciaet.R;
import it.unimib.ciaet.database.LocalDB;
import it.unimib.ciaet.model.lesson;
import it.unimib.ciaet.adapter.lessonadpter;


public class LessonFragment extends Fragment{

    String coursNo;
    RecyclerView lessonRecycler;
    ArrayList<lesson> list;
    lessonadpter lessonAdapte;
    FirebaseDatabase database;
    ProgressBar progressBar;
    Button back;


    public LessonFragment() {

    }

    public static LessonFragment newInstance(String param1, String param2) {
        LessonFragment fragment = new LessonFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lesson, container, false);
        coursNo = LessonActivity.courseNo;
        lessonRecycler = view.findViewById(R.id.lessonRecyclerView);
        progressBar=view.findViewById(R.id.progressBar);

        back=view.findViewById(R.id.back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().finish();
            }
        });


        lessonRecycler.setHasFixedSize(false);
        lessonRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));


        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Courses");


        list = new ArrayList<>();

        myRef.child(coursNo).child("lesson").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (snapshot.exists()) {
                        lesson lessonModel = new lesson();
                        lessonModel.setKey((dataSnapshot.getKey().toString()));
                        lessonModel.setName(dataSnapshot.child("name").getValue().toString());

                        list.add(lessonModel);
                    }

                    lessonAdapte = new lessonadpter(list, NavHostFragment.findNavController(requireParentFragment()));
                    lessonRecycler.setAdapter(lessonAdapte);
                    lessonAdapte.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        LocalDB localDB= new LocalDB(requireContext());

        DatabaseReference mainRef = database.getReference("User");
        DatabaseReference courseRef = mainRef.child(localDB.getUserNodeID()).child("Course").child(coursNo);

        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    int childCourseOneCount = (int) snapshot.getChildrenCount();
                    int finalCount = childCourseOneCount - 1;

                    int childCount = snapshot.child("count").getValue(Integer.class);

                    float result = (finalCount * 1.0f / childCount) * 100;  // Convert to float before division
                    int progressValue = (int) result;  // Convert the result back to integer


                    progressBar.setProgress(progressValue);





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}
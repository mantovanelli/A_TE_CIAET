package it.unimib.ciaet.ui.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.unimib.ciaet.R;
import it.unimib.ciaet.database.LocalDB;
import it.unimib.ciaet.ui.lesson.LessonActivity;
import it.unimib.ciaet.ui.welcome.Activity_Main;

public class Fragment_Home extends Fragment {

    private TextView firstName;
    FirebaseDatabase database;

    private ConstraintLayout course1,course2;

   private ProgressBar pOne,ptwo;

    public Fragment_Home() {
        // Required empty public constructor
    }


    public static Fragment_Home newInstance(String param1, String param2) {
        Fragment_Home fragment = new Fragment_Home();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

         course1=view.findViewById(R.id.courseOne);
         course2=view.findViewById(R.id.coursetwo);
        firstName= view.findViewById(R.id.firstNametv);
        pOne=view.findViewById(R.id.progressBarCourseOne);
        ptwo=view.findViewById(R.id.progressBarCourseTwo);

        database = FirebaseDatabase.getInstance();


        course1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Bundle bundle = new Bundle();
                 bundle.putString("courseNo", "1");
                 Intent intent = new Intent(getActivity(), LessonActivity.class);
                 intent.putExtras(bundle);
                 startActivity(intent);


             }
         });

        course2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("courseNo", "2");
                Intent intent = new Intent(getActivity(), LessonActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalDB localDB= new LocalDB(requireContext());
        firstName.setText(localDB.getUSER_FIRST_NAME());


        DatabaseReference mainRef = database.getReference("User");
        DatabaseReference courseOneRef = mainRef.child(localDB.getUserNodeID()).child("Course").child("1");
        DatabaseReference courseTwoRef = mainRef.child(localDB.getUserNodeID()).child("Course").child("2");

        courseOneRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    int childCourseOneCount = (int) snapshot.getChildrenCount();
                    int finalCount = childCourseOneCount - 1;

                    int childCount = snapshot.child("count").getValue(Integer.class);

                    float result = (finalCount * 1.0f / childCount) * 100;  // Convert to float before division
                    int progressValue = (int) result;  // Convert the result back to integer


                    pOne.setProgress(progressValue);





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        courseTwoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int childCourseTwoCount = (int) snapshot.getChildrenCount();
                    int finalCount=childCourseTwoCount-1;

                    int childCount = snapshot.child("count").getValue(Integer.class);

                    float result = (finalCount * 1.0f / childCount) * 100;  // Convert to float before division
                    int progressValue = (int) result;  // Convert the result back to integer


                    ptwo.setProgress(progressValue);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
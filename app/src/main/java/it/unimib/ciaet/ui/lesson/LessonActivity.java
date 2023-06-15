package it.unimib.ciaet.ui.lesson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import it.unimib.ciaet.R;

public class LessonActivity extends AppCompatActivity {

    public  static String courseNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            courseNo = bundle.getString("courseNo");


        }


    }
}
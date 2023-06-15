package it.unimib.ciaet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.unimib.ciaet.database.LocalDB;
import it.unimib.ciaet.ui.welcome.Activity_Main;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT=5000;
    private  FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String uid = mAuth.getUid();
            if(mAuth!=null) {
                FirebaseDatabase database;
                database = FirebaseDatabase.getInstance();

                DatabaseReference myRef = database.getReference("User").child(uid);

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String fName = snapshot.child("FirstName").getValue().toString();
                            String lName = snapshot.child("LastName").getValue().toString();
                            String email = snapshot.child("email").getValue().toString();
                            String image = snapshot.child("Image").getValue().toString();

                            Log.e("TAG", "onStart: " + image);

                            LocalDB localDB = new LocalDB(SplashActivity.this);
                            localDB.saveLocalUserData(uid, fName, lName, email, image);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SplashActivity.this, "error", Toast.LENGTH_SHORT).show();

                    }
                };
                myRef.addListenerForSingleValueEvent(valueEventListener);

            }
        }

        new Handler().postDelayed(new Runnable() {
            public void run()
            {
                Intent intent=new Intent(SplashActivity.this, Activity_Main.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);

    }

}
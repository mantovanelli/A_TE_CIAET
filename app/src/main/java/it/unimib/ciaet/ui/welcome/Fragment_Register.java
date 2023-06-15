package it.unimib.ciaet.ui.welcome;



import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Calendar;
import java.util.Objects;

import it.unimib.ciaet.R;
import it.unimib.ciaet.database.LocalDB;
import it.unimib.ciaet.ui.app.Activity_App;

public class Fragment_Register extends Fragment {

    private TextInputLayout firstNameTIL, lastNameTIL, emailTIL, passwordTIL;
    private TextInputEditText firstName,lastName, email, password;
    private TextView dateTextView;
    private TextView login;
    private Button signup;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private ProgressBar progressBar;

    private ConstraintLayout dob;

    public Fragment_Register() {
    }

    public static Fragment_Register newInstance() {
        Fragment_Register fragment = new Fragment_Register();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_register, container, false);
        progressBar = view.findViewById(R.id.idPBLoading);


        firstName = view.findViewById(R.id.et_firstName);
        lastName = view.findViewById(R.id.et_LastName);
        dateTextView=view.findViewById(R.id.tv_dob);
        email = view.findViewById(R.id.et_email);
        password = view.findViewById(R.id.et_password);

        firstNameTIL = view.findViewById(R.id.layout_first_name);
        lastNameTIL = view.findViewById(R.id.layout_last_name);
       dob = view.findViewById(R.id.layout_dob);
        emailTIL = view.findViewById(R.id.layout_email);
        passwordTIL = view.findViewById(R.id.layout_password);
        signup = view.findViewById(R.id.signUpBtn);
        login = view.findViewById(R.id.go_to_login);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();


        myRef=database.getReference("User");

        signup.setOnClickListener(v -> {

            if(validate(firstName, firstNameTIL, lastName, lastNameTIL, email, emailTIL, password, passwordTIL)){

                registerUser();
            }

        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {

                                String selectedDate = selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear;
                                dateTextView.setText(selectedDate);
                            }
                        }, year, month, dayOfMonth);


                datePickerDialog.show();
            }
        });

        login.setOnClickListener(v -> {

            Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment);

        });

        return view;
    }

    private void registerUser() {
        final String FNames = firstName.getText().toString();
        final String LNames = lastName.getText().toString();
        final String dobs = dateTextView.getText().toString();
        final String emails= email.getText().toString();

        final String passwords = password.getText().toString().trim();

        if (TextUtils.isEmpty(dateTextView.getText().toString())) {
            Toast.makeText(requireContext(), "Please Select Your Date of Birth", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);


        mAuth.createUserWithEmailAndPassword(emails, passwords)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);

                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String uid = user.getUid();

                                        try {

                                            myRef.child(uid).child("FirstName").setValue(FNames);
                                            myRef.child(uid).child("LastName").setValue(LNames);
                                            myRef.child(uid).child("Image").setValue("");
                                            myRef.child(uid).child("email").setValue(emails);
                                            myRef.child(uid).child("DoB").setValue(dobs);

                                            myRef.child(uid).child("id").setValue(uid);

                                            LocalDB localDB= new LocalDB(requireContext());
                                            localDB.saveLocalUserData(uid,FNames,LNames,emails,"");

                                            Intent intent = new Intent(requireContext(), Activity_App.class);
                                            startActivity(intent);
                                            requireActivity().finish();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(requireContext(), "Signup failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


 private boolean validate(TextInputEditText a1, TextInputLayout b1, TextInputEditText a2, TextInputLayout b2, TextInputEditText a4, TextInputLayout b4, TextInputEditText a5, TextInputLayout b5) {

            if (azione3(a1) && azione3(a2) && azione4(a4) && azione3(a5)) {
            azione2(a1, b1);
            azione2(a2, b2);

            azione2(a4, b4);
            azione2(a5, b5);
            return true;
        }
        else
        {
            if(azione3(a1)) {azione2(a1, b1);} else {azione1(a1, b1, getText(R.string.error3));}
            if(azione3(a2)) {azione2(a2, b2);} else {azione1(a2, b2, getText(R.string.error4));}
            if(azione4(a4)) {azione2(a4, b4);} else {azione1(a4, b4, getText(R.string.error6));}
            if(azione3(a4)) {azione2(a4, b4);} else {azione1(a4, b4, getText(R.string.error1));}
            if(azione3(a5)) {azione2(a5, b5);} else {azione1(a5, b5, getText(R.string.error2));}
            return false;
        }

    }

    private void azione1(TextInputEditText a1, TextInputLayout a2, CharSequence t1){
        a1.requestFocus();a2.setError(t1);}
    private void azione2(TextInputEditText a1, TextInputLayout a2){
        a1.requestFocus();a2.setErrorEnabled(false);}
    private Boolean azione3(TextInputEditText a){
        if(Objects.requireNonNull(a.getText()).toString().trim().length() > 0){return true;} else {return false;}}
    private Boolean azione4(TextInputEditText a){
        if(!EmailValidator.getInstance().isValid(a.getText().toString()))
        {return true;}
        else {return true;
        }
    }





}

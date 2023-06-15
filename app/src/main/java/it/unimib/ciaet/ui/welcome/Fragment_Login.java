package it.unimib.ciaet.ui.welcome;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.unimib.ciaet.R;
import it.unimib.ciaet.SplashActivity;
import it.unimib.ciaet.database.LocalDB;
import it.unimib.ciaet.ui.app.Activity_App;


public class Fragment_Login extends Fragment {

    private TextInputLayout emailTIL, passwordTIL;
    private TextInputEditText email, password;
    private TextView signup;
    private Button login;

    private static final int RC_SIGN_IN = 123;


    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton googleSignIn;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private ProgressBar progressBar;

    public Fragment_Login() {
    }


    public static Fragment_Login newInstance() {
        Fragment_Login fragment = new Fragment_Login();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        progressBar = view.findViewById(R.id.idPBLoading);
        email = view.findViewById(R.id.et_email);
        password = view.findViewById(R.id.et_password);
        emailTIL = view.findViewById(R.id.layout_Email);
        passwordTIL = view.findViewById(R.id.layout_password);
        login = view.findViewById(R.id.loginBtn);
        googleSignIn = view.findViewById(R.id.bt_sign_in);
        signup = view.findViewById(R.id.signUp);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        LocalDB localDB= new LocalDB(requireContext());
        myRef = database.getReference("User");


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


        login.setOnClickListener(v -> {
            if (validate(email, emailTIL, password, passwordTIL)) {

                loginUser();
            }
            Log.d(TAG, "Log in");

        });


        signup.setOnClickListener(v -> {

            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_registerFragment);

        });


        return view;
    }

    private void loginUser() {
        final String emails = email.getText().toString();
        final String passwords = password.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(emails, passwords).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();
                    String uid= mAuth.getUid().toString();

                    DatabaseReference ref = database.getReference("User").child(uid);

                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                progressBar.setVisibility(View.GONE);

                                String fName = snapshot.child("FirstName").getValue().toString();
                                String lName = snapshot.child("LastName").getValue().toString();
                                String email = snapshot.child("email").getValue().toString();
                                String image = snapshot.child("Image").getValue().toString();


                                LocalDB localDB= new LocalDB(requireContext());
                                localDB.saveLocalUserData(uid,fName,lName,email,image);
                                Intent intent = new Intent(requireContext(), Activity_App.class);
                                startActivity(intent);
                                requireActivity().finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();

                        }
                    };
                    ref.addListenerForSingleValueEvent(valueEventListener);





                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "Signin failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private boolean validate(TextInputEditText email, TextInputLayout emailTIL, TextInputEditText password, TextInputLayout passwordTIL) {

        if (azione3(email) && azione3(password)) {
            azione2(email, emailTIL);
            azione2(password, passwordTIL);
            return true;
        } else {
            if (azione3(email)) {
                azione2(email, emailTIL);
            } else {
                azione1(email, emailTIL, getText(R.string.error1));
            }
            if (azione3(password)) {
                azione2(password, passwordTIL);
            } else {
                azione1(password, passwordTIL, getText(R.string.error2));
            }
            return false;
        }
    }

    private void azione1(TextInputEditText a1, TextInputLayout a2, CharSequence t1) {
        a1.requestFocus();
        a2.setError(t1);
    }

    private void azione2(TextInputEditText a1, TextInputLayout a2) {
        a1.requestFocus();
        a2.setErrorEnabled(false);
    }

    private Boolean azione3(TextInputEditText a) {
        if (a.getText().toString().trim().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.e("TAG", "Google sign-in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            FirebaseUser user = mAuth.getCurrentUser();
                            // User is signed in, you can perform further operations
                            String uid = user.getUid();
                            // TODO: Handle the signed-up user
                            try {
                                if (user != null) {
                                    String firstName = acct.getGivenName();
                                    String lastName = acct.getFamilyName();
                                    String email = acct.getEmail();
                                    Uri imageUrl = acct.getPhotoUrl();
                                    DatabaseReference databaseReference= myRef.child(uid);
                                    ValueEventListener valueEventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                String fName = snapshot.child("FirstName").getValue().toString();
                                                String lName = snapshot.child("LastName").getValue().toString();
                                                String email = snapshot.child("email").getValue().toString();
                                                String image = snapshot.child("Image").getValue().toString();
                                                LocalDB localDB= new LocalDB(requireContext());
                                                localDB.saveLocalUserData(uid,fName,lName,email,image);
                                                Intent intent = new Intent(requireContext(), Activity_App.class);
                                                startActivity(intent);
                                                requireActivity().finish();

                                            }else {
                                                myRef.child(uid).child("FirstName").setValue(firstName);
                                                myRef.child(uid).child("LastName").setValue(lastName);
                                                myRef.child(uid).child("Image").setValue(imageUrl.toString());
                                                myRef.child(uid).child("email").setValue(email);
                                                myRef.child(uid).child("DoB").setValue("");
                                                myRef.child(uid).child("id").setValue(uid);
                                                LocalDB localDB= new LocalDB(requireContext());
                                                localDB.saveLocalUserData(uid,firstName,lastName,email,imageUrl.toString());
                                                // Use the obtained user information as required
                                                Intent intent = new Intent(requireContext(), Activity_App.class);
                                                startActivity(intent);
                                                requireActivity().finish();

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();

                                        }
                                    };
                                    databaseReference.addListenerForSingleValueEvent(valueEventListener);



                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(requireContext(), Activity_App.class);
            startActivity(intent);
            requireActivity().finish();
        }
    }


}
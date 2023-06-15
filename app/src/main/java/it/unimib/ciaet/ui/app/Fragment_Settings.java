package it.unimib.ciaet.ui.app;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import it.unimib.ciaet.R;
import it.unimib.ciaet.SplashActivity;
import it.unimib.ciaet.database.LocalDB;
import it.unimib.ciaet.ui.welcome.Activity_Main;

public class Fragment_Settings extends Fragment {

    private FirebaseAuth mAuth;
    private TextView firstName,lastName;
    private  String uid;
    private ImageView userImage;

    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database;

    private ActivityResultLauncher<Intent> launcher;

    public Fragment_Settings() {

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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button logOut = view.findViewById(R.id.logoutBtn);
        firstName= view.findViewById(R.id.firstNametv);
        lastName=view.findViewById(R.id.lastNametv);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();


        userImage = view.findViewById(R.id.profileImageView);
        LocalDB localDB= new LocalDB(requireContext());


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                localDB.clearSharedPreference();
                Intent intent = new Intent(requireContext(), Activity_Main.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mAuth.getCurrentUser()!=null) {
                    uid = mAuth.getUid().toString();
                    selectImage();

                }else {
                    Toast.makeText(requireContext(), "Some thing went wrong", Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;

    }



    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(Intent.createChooser(intent, "Select Image from here..."));
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the launcher in onViewCreated()
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK
                            && result.getData() != null
                            && result.getData().getData() != null) {
                        filePath = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().getContentResolver(),
                                    filePath);

                            if (filePath != null) {
                                ProgressDialog progressDialog
                                        = new ProgressDialog(requireContext());
                                progressDialog.setTitle("Uploading...");
                                progressDialog.show();
                                progressDialog.setCancelable(false);


                                StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());


                                UploadTask uploadTask;
                                uploadTask = ref.putFile(filePath);
                                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }


                                        return ref.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        Uri downloadUri = task.getResult();
                                        String imageUrl = downloadUri.toString();


                                        DatabaseReference ref = database.getReference("User").child(uid);

                                        ValueEventListener valueEventListener = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    String fName = snapshot.child("FirstName").getValue().toString();
                                                    String lName = snapshot.child("LastName").getValue().toString();
                                                    String email = snapshot.child("email").getValue().toString();


                                                    DatabaseReference imageRef = ref.child("Image");
                                                    imageRef.setValue(imageUrl);
                                                    Glide.with(requireActivity())
                                                            .load(imageUrl)
                                                            .circleCrop()
                                                            .into(userImage);
                                                    LocalDB localDB= new LocalDB(requireContext());
                                                    localDB.saveLocalUserData(uid,fName,lName,email,imageUrl);
                                                    progressDialog.dismiss();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();

                                            }
                                        };
                                        ref.addListenerForSingleValueEvent(valueEventListener);



                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast
                                                .makeText(requireContext(),
                                                        "Failed to upload Image " + e.getMessage(),
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                    }

                                });

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalDB localDB= new LocalDB(requireContext());
        firstName.setText(localDB.getUSER_FIRST_NAME());
        lastName.setText(localDB.getUSER_LAST_NAME());

        if (localDB.getUSER_IMAGE().equals("")) {
            userImage.setImageResource(R.drawable.baseline_person_24);
        }else {
            Glide.with(requireActivity())
                    .load(localDB.getUSER_IMAGE())
                    .circleCrop()
                    .into(userImage);
        }
    }
}
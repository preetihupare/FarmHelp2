package com.ynot.farmhelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFarm extends AppCompatActivity {

    public static final String TAG = "TAG";
    private static final int MY_REQUEST_CODE = 1;
    EditText F_Name, M_Number, E_Add, F_Area, F_Location;
    Spinner F_City, F_Crop, F_Market, F_Soil;
    Button saveBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    StorageReference storageReference;

    private View parent_view;

    LinearLayout upload, uploadpresentatation;
    private Intent data;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_farm);
//        getSupportActionBar().setTitle("My Farm");

//        // Here we are initialising the progress dialog box
//        dialog = new ProgressDialog(this);
//        dialog.setTitle("File is Selected");
//        dialog.setMessage("Please Wait...");

        parent_view = findViewById(android.R.id.content);

        Intent data = getIntent();
        final String fName = data.getStringExtra("fName");
        String email = data.getStringExtra("email");
        String phone = data.getStringExtra("phone");

        String area = data.getStringExtra("farmArea");
        String location = data.getStringExtra("farmLocation");
//        final  String department = data.getStringExtra("department");
//        final  String learning_year = data.getStringExtra("year");
//        final String paper_name = data.getStringExtra("PaperName");
//        String college_name = data.getStringExtra("collegeName");

//        String Paper_URL = data.getStringExtra("Paper_URL");
//        String Profile_IMG = data.getStringExtra("Profile_IMG");


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        F_Name = findViewById(R.id.fullName);
        M_Number = findViewById(R.id.mobile_number);
        E_Add = findViewById(R.id.email_address);
        F_Area = findViewById(R.id.farm_area);
        F_Location = findViewById(R.id.farm_location);


        saveBtn = findViewById(R.id.saveBtn);

//        upload = findViewById(R.id.uploadpdf);
//        uploadpresentatation = findViewById(R.id.upload_presentation_pdf);

//        profileState = findViewById(R.id.state);
//        profileCity = findViewById(R.id.district);
//
//        profileDepartment = findViewById(R.id.department);
//        profileYear= findViewById(R.id.year);
//        profilePaperName= findViewById(R.id.paper_name);


//        uploadpresentatation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Not Started Yett!!.", Toast.LENGTH_SHORT).show();
//            }
//        });

//        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
//        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).into(profileImageView);
//                Profile_IMG = String.valueOf(uri);
//            }
//        });


//        profileImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(openGalleryIntent,1000);
//            }
//        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(F_Name.getText().toString().isEmpty() || E_Add.getText().toString().isEmpty() || M_Number.getText().toString().isEmpty() || F_Area.getText().toString().isEmpty() || F_Location.getText().toString().isEmpty()){
                    Snackbar.make(parent_view, "One or many fields are empty.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                final String email = E_Add.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        final ProgressDialog progress = new ProgressDialog(MyFarm.this);

                        DocumentReference docRef = fStore.collection("users").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("email",E_Add);
                        edited.put("fName",F_Name.getText().toString());
                        edited.put("phone",M_Number.getText().toString());

//                        edited.put("Paper_URL",result);
                        edited.put("farmArea",F_Area.getText().toString());
                        edited.put("farmLocation",F_Location.getText().toString());
//                        edited.put("collegeName",profileCollege.getText().toString());
//                        edited.put("Profile_IMG",Profile_IMG);


                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(EditProfile.this, "Profile Image Updated", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getApplicationContext(),my_account.class));
//                                finish();
                            }
                        });

                        progress.setTitle("Profile Data Updating...");
                        progress.setMessage("Please wait..");
                        progress.show();

                        Runnable progressRunnable = new Runnable() {
                            @Override
                            public void run() {
                                progress.cancel();
                                Snackbar.make(parent_view, "Profile Data Updated Successfully", Snackbar.LENGTH_SHORT).show();
                            }
                        };
                        Handler pdCanceller = new Handler();
                        pdCanceller.postDelayed(progressRunnable, 3000);

//                        Snackbar.make(parent_view, "Profile Data Updated Successfully", Snackbar.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MyFarm.this,   e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        E_Add.setText(email);
        F_Name.setText(fName);
        M_Number.setText(phone);

        //profileCollege.setText(college_name);

        F_Area.setText(area);
        F_Location.setText(location);
//        profileDepartment.setText(department);
//        profileYear.setText(learning_year);
//        profilePaperName.setText(paper_name);

//        profileCoFullName.setText(CoFullName);
//        profileCoEmail.setText(CoEmail);
//        profileCoCollegeName.setText(CoCollegeName);

        Log.d(TAG, "onCreate: " + fName + " " + email + " " + phone+ " " + area + " " + location);


//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(MyFarm.this, TestingActivity.class);
//                startActivity(i);
//            }
//        });
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 1000){
//            if(resultCode == Activity.RESULT_OK){
//                Uri uri = data.getData();
//
//                uploadImageToFirebase(uri);
////                uploadImage(imageUri);
//
//            }
//        }
//    }

//    private void uploadImageToFirebase(Uri uri) {
//
//        StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
//        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Picasso.get().load(uri).into(profileImageView);
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }


}
package com.ynot.farmhelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.annotation.Nullable;

public class MyData extends AppCompatActivity {
    TextView F_Name, M_Number, E_Add, F_Area, F_Location, F_City, F_Crop, F_Market, F_Soil;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button changeProfile, updatePWD;
    FirebaseUser user;
    StorageReference storageReference;

//    TextView CoFullNameHead,CoEmailHead,CoCollegeNameHaed;
//    LinearLayout CoAuthorName_Layout,CoAuthorEmail_Layout,CoAuthorCollegeName_Layout;
//    String CoFullName,CoEmail,CoCollegeName;

    private View parent_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);
//        getSupportActionBar().setTitle("My Account");

        parent_view = findViewById(android.R.id.content);

//        AppUpdateLink = findViewById(R.id.AppUpdateLink);
//        UpdateName = findViewById(R.id.UpdateName);

        M_Number = findViewById(R.id.f_number);
        F_Name = findViewById(R.id.fname);
        E_Add    = findViewById(R.id.f_email);

        F_Area = findViewById(R.id.f_area);
        F_Location = findViewById(R.id.f_location);

        F_City = findViewById(R.id.f_city);
        F_Crop = findViewById(R.id.s_crop);

        F_Market = findViewById(R.id.s_market);
        F_Soil = findViewById(R.id.s_soil);

        changeProfile = findViewById(R.id.updateprofileData);

        updatePWD = findViewById(R.id.resetPWD);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

//        resendCode = findViewById(R.id.resendCode);
//        verifyMsg = findViewById(R.id.verifyMsg);
//        note_verify = findViewById(R.id.note_verify);

//        if(!user.isEmailVerified() ){
//            verifyMsg.setVisibility(View.VISIBLE);
//            resendCode.setVisibility(View.VISIBLE);
//            note_verify.setVisibility(View.VISIBLE);
//
//            resendCode.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View v) {
//
//                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Snackbar.make(parent_view, "Verification Email Has Been Sent. Please Check Your Mail Box", Snackbar.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.d("tag", "onFailure: Email not sent " + e.getMessage());
//                        }
//                    });
//                }
//            });
//        }else
//        {
//            verifyMsg.setVisibility(View.GONE);
//            resendCode.setVisibility(View.GONE);
//            note_verify.setVisibility(View.GONE);
//        }




        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    M_Number.setText(documentSnapshot.getString("phone"));
                    F_Name.setText(documentSnapshot.getString("fName"));
                    E_Add.setText(documentSnapshot.getString("email"));
                    F_Area.setText(documentSnapshot.getString("farmArea"));
                    F_Location.setText(documentSnapshot.getString("farmLocation"));
                    F_Market.setText(documentSnapshot.getString("marketName"));
                    F_Soil.setText(documentSnapshot.getString("soilName"));
                    F_City.setText(documentSnapshot.getString("city"));
                    F_Crop.setText(documentSnapshot.getString("cropName"));

                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });

        updatePWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetPassword = new EditText(v.getContext());

                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter new password it must contains at least 6 characters");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String newPassword = resetPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MyData.this, "Password Reset Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MyData.this, "Password Reset Failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close
                    }
                });

                passwordResetDialog.create().show();

            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gallery
                Intent i = new Intent(v.getContext(),MyFarm.class);
                i.putExtra("fName",F_Name.getText().toString());
                i.putExtra("email",E_Add.getText().toString());
                i.putExtra("phone",M_Number.getText().toString());
                i.putExtra("farmArea", F_Area.getText().toString());
                i.putExtra("farmLocation", F_Location.getText().toString());
                i.putExtra("marketName",F_Market.getText().toString());
                i.putExtra("soilName", F_Soil.getText().toString());
                i.putExtra("city",F_City.getText().toString());
                i.putExtra("cropName",F_Crop.getText().toString());
                startActivity(i);
            }
        });

    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        Intent i =new Intent(getApplicationContext(),Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("EXIT", true);
        startActivity(i);
        finish();
    }

    //Custome Tab
    public static void openCustomTabs(Activity activity, CustomTabsIntent customTabsIntent, Uri uri){
        String PackageName = "com.android.chrome";
        if(PackageName != null){
            customTabsIntent.intent.setPackage(PackageName);
            customTabsIntent.launchUrl(activity,uri);
        }else{
            activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

}

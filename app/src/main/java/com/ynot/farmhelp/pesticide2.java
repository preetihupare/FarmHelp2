package com.ynot.farmhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

public class pesticide2 extends AppCompatActivity {
    TextView data;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userId;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesticide2);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();


        TextView pestid, pestir, cropN;
        pestid = findViewById(R.id.pestid);
        pestir = findViewById(R.id.pestir);
        cropN = findViewById(R.id.cropN);
        Intent data = getIntent();
        String CropNameD = data.getStringExtra("cropN");
        cropN.setText(CropNameD);


        if (CropNameD.equals("Tomato")) {
            DocumentReference documentReference = fStore.collection("Pesticides/").document("tomato");
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {
                        pestid.setText(documentSnapshot.getString("pestid"));
                        pestir.setText(documentSnapshot.getString("pestir"));

                    } else {
                        Log.d("tag", "onEvent: Document do not exists");
                    }
                }
            });
        } else if (CropNameD.equals("Onion")) {
            DocumentReference documentReference = fStore.collection("Pesticides/").document("onion");
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {
                        pestid.setText(documentSnapshot.getString("pestid"));
                        pestir.setText(documentSnapshot.getString("pestir"));
                    } else {
                        Log.d("tag", "onEvent: Document do not exists");
                    }
                }
            });
        } else if (CropNameD.equals("Potato")) {
            DocumentReference documentReference = fStore.collection("Pesticides/").document("potato");
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {
                        pestid.setText(documentSnapshot.getString("pestid"));
                        pestir.setText(documentSnapshot.getString("pestir"));
                    } else {
                        Log.d("tag", "onEvent: Document do not exists");
                    }
                }
            });
        }
    }
}
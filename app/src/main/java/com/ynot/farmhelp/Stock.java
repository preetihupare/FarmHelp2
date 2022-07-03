package com.ynot.farmhelp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Stock extends AppCompatActivity {
    private static final String TAG = "Stock";
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    DocumentReference documentReference;
    HashMap<String, Long> cropData = new HashMap<>();
    TextView tomato, onion, potato, corn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        tomato = findViewById(R.id.tomato);
        potato = findViewById(R.id.potato);
        onion = findViewById(R.id.onion);
        corn = findViewById(R.id.corn);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        documentReference = firestore.collection("users").document(userId);

        tomato.setOnClickListener(v -> getCropQtnFromDialog("tomato"));
        potato.setOnClickListener(v -> getCropQtnFromDialog("potato"));
        corn.setOnClickListener(v -> getCropQtnFromDialog("corn"));
        onion.setOnClickListener(v -> getCropQtnFromDialog("onion"));
    }

    private void getCropQtnFromDialog(String cropName) {
        EditText editText = new EditText(this);
        editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Update " + cropName + " quantity")
                .setMessage("Enter New Quantity")
                .setView(editText)
                .setPositiveButton("Add", (dialogInterface, i) -> {
                    Long qtn = Long.parseLong(editText.getText().toString());
                    Map<String, Long> cropData = this.cropData;
                    cropData.put(cropName, qtn);
                    Map<String, Object> data = new HashMap<>();
                    data.put("cropData", cropData);
                    documentReference.update(data)
                            .addOnSuccessListener(s -> {
                                Log.d(TAG, "documentReference: updated: ");
                                Toast.makeText(Stock.this, "Data updated", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            })
                            .addOnFailureListener(f -> {
                                Log.d(TAG, "documentReference: failed: " + f.getLocalizedMessage());
                                Toast.makeText(Stock.this, "Failed", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            });
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        documentReference.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }

            String source = snapshot != null && snapshot.getMetadata().hasPendingWrites() ? "Local" : "Server";

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, source + " data: " + snapshot.getData());
                HashMap<String, Long> data = (HashMap<String, Long>) snapshot.getData().get("cropData");
                if (data != null) {
                    Log.d(TAG, "crops" + " data: " + data);
                    cropData = data;
                    handleStocksUi(data);
                }
            } else {
                Log.d(TAG, source + " data: null");
                insertDefaultData();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void handleStocksUi(HashMap<String, Long> data) {
        try {
            if (data != null) {
                Long tomatoQtn = data.get("tomato");
                if (tomatoQtn != null) {
                    tomato.setText(tomatoQtn + "Qtls");
                }
                Long potatoQtn = data.get("potato");
                if (potatoQtn != null) {
                    potato.setText(potatoQtn + "Qtls");
                }
                Long onionQtn = data.get("onion");
                if (onionQtn != null) {
                    onion.setText(onionQtn + "Qtls");
                }
                Long cornQtn = data.get("corn");
                if (cornQtn != null) {
                    corn.setText(cornQtn + "Qtls");
                }
            } else {
                insertDefaultData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertDefaultData(){
        Map<String, Long> cropData = new HashMap<>();
        cropData.put("tomato", 0L);
        cropData.put("onion", 0L);
        cropData.put("potato", 0L);
        cropData.put("corn", 0L);
        Map<String, Object> data = new HashMap<>();
        data.put("cropData", cropData);
        documentReference.update(data);
    }
}
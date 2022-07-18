package com.ynot.farmhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

public class Pesticides extends AppCompatActivity {
    TextView click_t,click_o,click_p;
    ImageView tvtomato,tvpotato,tvonion;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesticides);

        tvtomato=findViewById(R.id.tomatoi);
        tvonion=findViewById(R.id.onioni);
        tvpotato=findViewById(R.id.potatoi);
        click_t=findViewById(R.id.tomato_click);
        click_o=findViewById(R.id.onion_click);
        click_p=findViewById(R.id.potato_click);

        TextView tomato,onion,potato;
        tomato=findViewById(R.id.tomato);
        onion=findViewById(R.id.onion);
        potato=findViewById(R.id.potato);


        click_t.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
            Intent send = new Intent(Pesticides.this, pesticide2.class);
            send.putExtra("cropN", tomato.getText().toString());
            startActivity(send);
        }
    });
        click_o.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent send = new Intent(Pesticides.this, pesticide2.class);
            send.putExtra("cropN", onion.getText().toString());
            startActivity(send);
        }
    });

        click_p.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent send = new Intent(Pesticides.this, pesticide2.class);
            send.putExtra("cropN", potato.getText().toString());
            startActivity(send);
        }
    });
    }
}
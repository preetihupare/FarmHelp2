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

public class Care extends AppCompatActivity {
    ImageView tvtomato,tvpotato,tvonion;
    TextView click_t,click_o,click_p;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care);

        tvtomato=findViewById(R.id.tomatoimg);
        tvonion=findViewById(R.id.onionimg);
        tvpotato=findViewById(R.id.potatoimg);
         click_t=findViewById(R.id.t_click);
         click_o=findViewById(R.id.o_click);
         click_p=findViewById(R.id.p_click);

    click_t.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent send = new Intent(Care.this, Care2.class);
        startActivity(send);
    }
    });
        click_o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent send = new Intent(Care.this, Care2.class);
                startActivity(send);
            }
        });

        click_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent send = new Intent(Care.this, Care2.class);
                startActivity(send);
            }
        });
    }

}
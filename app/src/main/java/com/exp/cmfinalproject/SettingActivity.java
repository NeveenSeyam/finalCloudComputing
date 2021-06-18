package com.exp.cmfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.exp.cmfinalproject.redesign.CardColor;
import static com.exp.cmfinalproject.redesign.fontColor;
import static com.exp.cmfinalproject.redesign.fontSize;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    int fontColor1;
    int carddColor;
    String cardColorTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //spinner
        Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        Spinner spColor = findViewById(R.id.spColor);
        ArrayAdapter<CharSequence> adapterColor = ArrayAdapter.createFromResource(this,
                R.array.colors, android.R.layout.simple_spinner_item);
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spColor.setAdapter(adapterColor);
        spColor.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        Spinner spCard = findViewById(R.id.cardColor);
        ArrayAdapter<CharSequence> adapterCard = ArrayAdapter.createFromResource(this,
                R.array.cardcol, android.R.layout.simple_spinner_item);
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCard.setAdapter(adapterCard);
        spCard.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);


        Button btn1 = findViewById(R.id.button2);
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fontSize = Float.parseFloat(spinner.getSelectedItem().toString());
                String color = spColor.getSelectedItem().toString();
                String cardColor1 = spCard.getSelectedItem().toString();


                switch (cardColor1) {
                    case "white":
                        carddColor = R.color.white;
                        break;
                    case "red":
                        carddColor = R.color.redCard;
                        break;
                    case "blue":
                        carddColor = R.color.blueCard;
                        break;
                    case "yellow":
                        carddColor = R.color.yellowCard;
                        break;
                    case "grey":
                        carddColor = R.color.grey;
                        break;
                    case "pink":
                        carddColor = R.color.pinkCard;
                        break;
                    case "green":
                        carddColor = R.color.greenCard;
                        break;
                }

                switch (color) {

                    case "red":
                        fontColor1 = R.color.red;
                        break;
                    case "blue":
                        fontColor1 = R.color.blue;
                        break;
                    case "yellow":
                        fontColor1 = R.color.yellow;
                        break;
                    case "black":
                        fontColor1 = R.color.black;
                        break;
                    case "pink":
                        fontColor1 = R.color.colorAccent;
                        break;
                    case "green":
                        fontColor1 = R.color.green;
                        break;

                }
                fontColor = fontColor1;
                CardColor = carddColor;
                Map<String, Object> data
                        = new HashMap<String, Object>();
                data.put("fontSize",fontSize );
                data.put("fontColor", color);
                data.put("CardColor", cardColor1);

                DocumentReference contact = db.collection("redesign").document("fontdata");
                contact.update(data)

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(getApplicationContext(), "Updated Successfully",
                                        Toast.LENGTH_SHORT).show();


                            }
                        });

            }
        });
    }

    @Override
    public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(android.widget.AdapterView<?> parent) {
        String text = parent.getItemAtPosition(0).toString();

    }
}
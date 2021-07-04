package com.exp.cmfinalproject;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import static com.exp.cmfinalproject.redesign.CardColor;
import static com.exp.cmfinalproject.redesign.fontSize;
import static com.exp.cmfinalproject.redesign.fontColor;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class splash extends AppCompatActivity {
    final String PREFS_NAME = "MyPrefsFile";
    Controllerdb controllerdb ;
    SQLiteDatabase database;
    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        controllerdb =new Controllerdb(this);
        database=controllerdb.getWritableDatabase();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");

            // first time task

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }else{
            database.execSQL("INSERT INTO Details(Text)" +
                    "VALUES('"+"The city of Jerusalem is one of the important civilized and holy cities. Its first landmarks were founded on the hills of appearance that overlooks Silwan in the southeastern side of Al-Aqsa Mosque. Its geographical extension at the present time starts from the southern side of the Hebron Mountains, the northern side of the Nablus Mountains, and reaches the eastern side. belonging to the Mediterranean Sea, and its height above sea level is approximately 775 m"+"')" );

        }
        db.collection("redesign").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e !=null)
                {

                }

                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges())
                {
                    fontSize = Float.parseFloat(documentChange.getDocument().getData().get("fontSize").toString());
                    String color  = documentChange.getDocument().getData().get("fontColor").toString();
                    String colorCard  = documentChange.getDocument().getData().get("CardColor").toString();

                    //  fontColor   =  Integer.parseInt(color ) ;
                    if(color.equals("black")){
                        fontColor= R.color.black;
                    } if (color.equals("pink")){
                    fontColor= R.color.colorAccent;
                }
                    if (color.equals("colorPrimaryDark")){
                        fontColor= R.color.colorPrimaryDark;
                    }
                    if(color.equals("red")){
                        fontColor= R.color.red;
                    } if (color.equals("green")){
                    fontColor= R.color.green;
                }
                    if (color.equals("yellow")){
                        fontColor= R.color.yellow;
                    }

                    //card color
                    if(colorCard.equals("grey"))
                        CardColor=R.color.grey;

                    if(colorCard.equals("white"))
                        CardColor=R.color.white;
                   if (colorCard.equals("pink")){
                    CardColor= R.color.pinkCard;
                }
                    if (colorCard.equals("blue")){
                        CardColor= R.color.blueCard;
                    }
                    if(colorCard.equals("red")){
                        CardColor= R.color.redCard;
                    } if (colorCard.equals("green")){
                    CardColor= R.color.greenCard;
                }
                    if (colorCard.equals("yellow")){
                        CardColor= R.color.yellowCard;
                    }


                }
                new Handler().postDelayed(new Runnable() {



// Using handler with postDelayed called runnable run method

                    @Override

                    public void run() {
                        if (fauth.getCurrentUser() != null) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {

                            Intent i = new Intent(splash.this, welcome.class);

                            startActivity(i);

                            // close this activity

                            finish();

                        }
                    }

                }, 4000);
            }
        });
        // wait for 5 seconds
    }
}
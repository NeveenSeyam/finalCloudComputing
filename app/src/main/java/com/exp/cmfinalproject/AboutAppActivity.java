package com.exp.cmfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;

public class AboutAppActivity extends AppCompatActivity {
    Controllerdb controllerdb =new Controllerdb(this);
    SQLiteDatabase database;
    TextView textView;
    android.widget.VideoView VideoView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        textView = findViewById(R.id.textView);
        database=controllerdb.getWritableDatabase();
        displayData();




        MediaController mediaController = new MediaController(this);
        VideoView = findViewById(R.id.VideoView) ;
        VideoView.setMediaController(mediaController);
        mediaController.setAnchorView(VideoView);

        Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/projectlab2-a5e52.appspot.com/o/فيديو%20عن%20القدس.mp4?alt=media&token=533f35c4-adf0-4560-9d20-71b7bd42c982");
        VideoView.setVideoURI(uri);
        VideoView.start();



    }

    private void displayData() {
        database = controllerdb.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM  Details",null);

        if (cursor.moveToFirst()) {
            do {
                textView.setText(cursor.getString(cursor.getColumnIndex("Text")));

            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
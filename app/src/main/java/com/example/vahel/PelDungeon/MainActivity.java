package com.example.vahel.PelDungeon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);

        boolean insertFortMelee = db.insertFortMelee("XL", 2, 3, 2.2);
        if(insertFortMelee==true) {
            Toast.makeText(getApplicationContext(), "Successfully inserted for Melee", Toast.LENGTH_SHORT).show();
        }

        Integer deleteForMelee = db.deleteDublicateForMelee("");
        if(deleteForMelee==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for Melee", Toast.LENGTH_SHORT).show();
        }

        Integer deleteForMissile = db.deleteDuplicateForMissile("");
        if(deleteForMissile==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for Missile", Toast.LENGTH_SHORT).show();
        }

        Integer deleteForRings = db.deleteDublicatForRings("");
        if(deleteForRings==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for Rings", Toast.LENGTH_SHORT).show();
        }

        Integer  deleteForWands = db.deleteDublicatForWands("");
        if(deleteForWands==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for Wands", Toast.LENGTH_SHORT).show();
        }

        Integer  deleteForScrolls = db.deleteDublicatForScrolls("");
        if(deleteForScrolls==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for Scrolls", Toast.LENGTH_SHORT).show();
        }

        Integer deleteForOtherItems = db.deleteDublicatForOtherItems("");
        if(deleteForOtherItems==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for OtherItems", Toast.LENGTH_SHORT).show();
        }
    }
}

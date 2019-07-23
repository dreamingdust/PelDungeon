package com.example.vahel.PelDungeon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * @author Vahel, Denise and YX
 * @author Version 1.0.
 */
public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        //Werte für Tabellename Weapons Melee werden festegelegt
        boolean insertFortMelee = db.insertFortMelee("XL", 2, 3, 2.2);
        if(insertFortMelee==true) {
            Toast.makeText(getApplicationContext(), "Successfully inserted for Melee", Toast.LENGTH_SHORT).show();
        }
        //Dublicate von der Tabelle Weapons Melee werden gelöscht.
        Integer deleteForMelee = db.deleteDublicateForMelee("");
        if(deleteForMelee==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for Melee", Toast.LENGTH_SHORT).show();
        }
        //Dublicate von der Tabelle Weapons Missile werden gelöscht.
        Integer deleteForMissile = db.deleteDuplicateForMissile("");
        if(deleteForMissile==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for Missile", Toast.LENGTH_SHORT).show();
        }
        //Dublicate von der Tabelle Rings werden gelöscht.
        Integer deleteForRings = db.deleteDublicatForRings("");
        if(deleteForRings==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for Rings", Toast.LENGTH_SHORT).show();
        }
        //Dublicate von der Tabelle Wand werden gelöscht.
        Integer  deleteForWands = db.deleteDublicatForWands("");
        if(deleteForWands==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for Wands", Toast.LENGTH_SHORT).show();
        }
        //Dublicate von der Tabelle Wands werden gelöscht.
        Integer  deleteForScrolls = db.deleteDublicatForScrolls("");
        if(deleteForScrolls==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for Scrolls", Toast.LENGTH_SHORT).show();
        }
        //Dublicate von der Tabelle Wands werden gelöscht.
        Integer deleteForOtherItems = db.deleteDublicatForOtherItems("");
        if(deleteForOtherItems==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for OtherItems", Toast.LENGTH_SHORT).show();
        }
    }
}

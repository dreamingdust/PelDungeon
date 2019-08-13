package com.example.vahel.PelDungeon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * <h1> Hauptbasis Klasse </h1>
 * @author Vahel, Denise and YX
 * @author Version 1.0.
 */
public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    @Override
    /**
     * @params savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        /**
         *<p>
         *Fuer die Tabelle <b> Weapons Melee </b> werden die Attribute gefuellt.
         *</p>
         */
        boolean insertFortMelee = db.insertFortMelee("XL", 2, 3, 2.2);
        if(insertFortMelee==true) {
            Toast.makeText(getApplicationContext(), "Successfully inserted for Melee", Toast.LENGTH_SHORT).show();
        }
        /**
         * <p>
         * Dublicate werden von der Tabelle <b> Weapons Melee </b> gelöscht.
         * </p>
         */
        Integer deleteForMelee = db.deleteDublicateForMelee("");
        if(deleteForMelee==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for Melee", Toast.LENGTH_SHORT).show();
        }
        /**
         * <p>
         * Dublicate werden von der Tabelle <b> Weapons Missile </b> gelöscht.
         * </p>
         */        Integer deleteForMissile = db.deleteDuplicateForMissile("");
        if(deleteForMissile==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for Missile", Toast.LENGTH_SHORT).show();
        }
        /**
         * <p>
         * Dublicate werden von der Tabelle <b> Rings </b> gelöscht.
         * </p>
         */        Integer deleteForRings = db.deleteDublicatForRings("");
        if(deleteForRings==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for Rings", Toast.LENGTH_SHORT).show();
        }
        /**
         * <p>
         * Dublicate werden von der Tabelle <b> Wands </b> gelöscht.
         * </p>
         */        Integer  deleteForWands = db.deleteDublicatForWands("");
        if(deleteForWands==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for Wands", Toast.LENGTH_SHORT).show();
        }
        /**
         * <p>
         * Dublicate werden von der Tabelle <b> Scrolls </b> gelöscht.
         * </p>
         */        Integer  deleteForScrolls = db.deleteDublicatForScrolls("");
        if(deleteForScrolls==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for Scrolls", Toast.LENGTH_SHORT).show();
        }
        /**
         * <p>
         * Dublicate werden von der Tabelle <b> Other Items </b> gelöscht.
         * </p>
         */        Integer deleteForOtherItems = db.deleteDublicatForOtherItems("");
        if(deleteForOtherItems==null) {
            Toast.makeText(getApplicationContext(), "Successfully deleted dublicate for OtherItems", Toast.LENGTH_SHORT).show();
        }
    }
}

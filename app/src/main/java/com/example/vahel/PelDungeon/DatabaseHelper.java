/*
* Vahel Hassan
* SQLite
* Datenbank für das Spiel Pixeldungeon
* Tabellen: Weapons, Armors, Rings, Wands, Potions, Scrolls, and Other Items.
* */

package com.example.vahel.PelDungeon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "Items.db";
    /*-----------------------------------------------WEAPONS-----------------------------------------------*/
    /* Tabelle für Weapons erzeugen: Weapons Melee */
    public final static String TABLE_MELEE = "weapons_melee";
    public final static String COL_1 = "ID";
    public final static String COL_2 = "NAME";
    public final static String COL_3 = "BASE_STRENGTH";
    public final static String COL_4 = "ACCURACY";
    public final static String COL_5 = "DELAY";

    /* Tabelle für Weapons erzeugen: Weapons Melee */
    public final static String TABLE_MISSILE  = "weapons_missile";
    public final static String COL_6 = "ID";
    public final static String COL_7 = "NAME";
    public final static String COL_8 = "REQUIRED_STRENGTH";
    public final static String COL_9 = "AVERAGE_HIT";
    public final static String COL_10 = "ACCURACY";
    public final static String COL_11 = "DELAY";

    /* Tabelle für Weapons erzeugen: Weapons Enchantments */
    public final static String TABLE_ENCHANTMENTS  = "weapons_enchantments";
    public final static String COL_12 = "ID";
    public final static String COL_13 = "NAME";
    /*-----------------------------------------------ARMORS-----------------------------------------------*/
    /* Tabelle für Armors erzeugen*/
    public final static String TABLE_ARMORS  = "all_armors";
    public final static String COL_14 = "ID";
    public final static String COL_15 = "NAME";
    public final static String COL_16 = "LEVEL";
    public final static String COL_17 = "REQUIRED_STRENGTH";
    public final static String COL_18 = "ARMOR";
    public final static String COL_19 = "EFFECTIVE_ABSORPTION";
    /*-----------------------------------------------RINGS-----------------------------------------------*/
    /* Tabelle für Rings erzeugen*/
    public final static String TABLE_RINGS  = "all_rings";
    public final static String COL_20 = "ID";
    public final static String COL_21 = "NAME";
    public final static String COL_22 = "LEVEL";
    public final static String COL_23 = "DURATION";
    public final static String COL_24 = "STEALTH";
    public final static String COL_25 = "DURATION_REDUCTION_FACTOR";
    public final static String COL_26 = "RESISTENCE_PROBABILITY";
    /*-----------------------------------------------WANDS-----------------------------------------------*/
    /* Tabelle für Wands erzeugen*/
    public final static String TABLE_WANDS  = "all_wands";
    public final static String COL_27 = "ID";
    public final static String COL_28 = "NAME";
    public final static String COL_29 = "LEVEL";
    public final static String COL_30 = "DURATION";
    public final static String COL_31 = "MIN";
    public final static String COL_32 = "MAX";
    public final static String COL_33 = "AVG";
    /*-----------------------------------------------POTIONS-----------------------------------------------*/
    /* Tabelle für Potions erzeugen*/
    public final static String TABLE_POTIONS  = "all_potions";
    public final static String COL_34 = "ID";
    public final static String COL_35 = "NAME";
    /*-----------------------------------------------Scroll-----------------------------------------------*/
    /* Tabelle für Scrolls erzeugen*/
    public final static String TABLE_SCROLLS  = "all_scrolls";
    public final static String COL_36 = "ID";
    public final static String COL_37 = "NAME";
    /*-----------------------------------------------Other Items-----------------------------------------------*/
    /* Tabelle für Other Items erzeugen*/
    public final static String TABLE_OTHERITEMS  = "all_otheritems";
    public final static String COL_38 = "ID";
    public final static String COL_39 = "NAME";


    //Datenbank erzeugen
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    //Tabelename und die dazugehörigen Attribute erzeugen.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_MELEE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT, BASE_STRENGTH INTEGER, ACCURACY INTEGER, DELAY REAL)");
        db.execSQL("create table " + TABLE_MISSILE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT, REQUIRED_STRENGTH INTEGER, AVERAGE_HIT INTEGER, ACCURACY INTEGER, DELAY REAL)");
        db.execSQL("create table " + TABLE_ENCHANTMENTS +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT)");
        db.execSQL("create table " + TABLE_ARMORS +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT, LEVEL INTEGER, REQUIRED_STRENGTH INTEGER, ARMOR INTEGER, EFFECTIVE_ABSORPTION INTEGER)");
        db.execSQL("create table " + TABLE_RINGS +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT, LEVEL INTEGER, DURATION INTEGER, STEALTH INTEGER, DURATION_REDUCTION_FACTOR INTEGER, RESISTENCE_PROBABILITY INTEGER )");
        db.execSQL("create table " + TABLE_WANDS +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT, LEVEL INTEGER, DURATION INTEGER, MIN INTEGER, MAX INTEGER, AVG REAL)");
        db.execSQL("create table " + TABLE_POTIONS +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT)");
        db.execSQL("create table " + TABLE_SCROLLS +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT)");
        db.execSQL("create table " + TABLE_OTHERITEMS +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT)");
    }

    //Dublikate löschen.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MELEE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_MISSILE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ENCHANTMENTS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ARMORS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_RINGS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WANDS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_SCROLLS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_OTHERITEMS);
        onCreate(db);
    }
}

package com.example.vahel.PelDungeon;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * @author Vahel
 * @version 1.0.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

     //Datenbankname wird festgelegt.
    public final static String DATABASE_NAME = "Items.db";
    /*-----------------------------------------------WEAPONS-----------------------------------------------*/
    //Tabellename und Attribute für Weapons Melee wird festgelegt.
    public final static String TABLE_MELEE = "weapons_melee";
    public final static String COL_1 = "ID";
    public final static String COL_2 = "NAME";
    public final static String COL_3 = "BASE_STRENGTH";
    public final static String COL_4 = "ACCURACY";
    public final static String COL_5 = "DELAY";

    //Tabellename und Attribute für Weapons Missile wird festgelegt.
    public final static String TABLE_MISSILE = "weapons_missile";
    public final static String COL_6 = "ID";
    public final static String COL_7 = "NAME";
    public final static String COL_8 = "REQUIRED_STRENGTH";
    public final static String COL_9 = "AVERAGE_HIT";
    public final static String COL_10 = "ACCURACY";
    public final static String COL_11 = "DELAY";

    //Tabellename und Attribute für Weapons Enchantments wird festgelegt.
    public final static String TABLE_ENCHANTMENTS = "weapons_enchantments";
    public final static String COL_12 = "ID";
    public final static String COL_13 = "NAME";
    /*-----------------------------------------------ARMORS-----------------------------------------------*/
    //Tabellename und Attribute für Armor wird festgelegt.
    public final static String TABLE_ARMORS = "all_armors";
    public final static String COL_14 = "ID";
    public final static String COL_15 = "NAME";
    public final static String COL_16 = "LEVEL";
    public final static String COL_17 = "REQUIRED_STRENGTH";
    public final static String COL_18 = "ARMOR";
    public final static String COL_19 = "EFFECTIVE_ABSORPTION";
    /*-----------------------------------------------RINGS-----------------------------------------------*/
    //Tabellename und Attribute für Rings wird festgelegt.
    public final static String TABLE_RINGS = "all_rings";
    public final static String COL_20 = "ID";
    public final static String COL_21 = "NAME";
    public final static String COL_22 = "LEVEL";
    public final static String COL_23 = "DURATION";
    public final static String COL_24 = "STEALTH";
    public final static String COL_25 = "DURATION_REDUCTION_FACTOR";
    public final static String COL_26 = "RESISTENCE_PROBABILITY";
    /*-----------------------------------------------WANDS-----------------------------------------------*/
    //Tabellename und Attribute für Weapons Wands wird festgelegt.
    public final static String TABLE_WANDS = "all_wands";
    public final static String COL_27 = "ID";
    public final static String COL_28 = "NAME";
    public final static String COL_29 = "LEVEL";
    public final static String COL_30 = "DURATION";
    public final static String COL_31 = "MIN";
    public final static String COL_32 = "MAX";
    public final static String COL_33 = "AVG";
    /*-----------------------------------------------POTIONS-----------------------------------------------*/
    //Tabellename und Attribute für Potions wird festgelegt.
    public final static String TABLE_POTIONS = "all_potions";
    public final static String COL_34 = "ID";
    public final static String COL_35 = "NAME";
    /*-----------------------------------------------Scroll-----------------------------------------------*/
    //Tabellename und Attribute für  Scrolls wird festgelegt.
    public final static String TABLE_SCROLLS = "all_scrolls";
    public final static String COL_36 = "ID";
    public final static String COL_37 = "NAME";
    /*-----------------------------------------------Other Items-----------------------------------------------*/
    //Tabellename und Attribute für  Other Items wird festgelegt.
    public final static String TABLE_OTHERITEMS = "all_otheritems";
    public final static String COL_38 = "ID";
    public final static String COL_39 = "NAME";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     *
     * @param db Wird benötigt, um execSQL auszuführen und dadurch dann Tebelle mit den jeweligen Attributen zu erzeugen.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_MELEE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT, BASE_STRENGTH INTEGER, ACCURACY INTEGER, DELAY REAL)");
        db.execSQL("create table " + TABLE_MISSILE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT, REQUIRED_STRENGTH INTEGER, AVERAGE_HIT INTEGER, ACCURACY INTEGER, DELAY REAL)");
        db.execSQL("create table " + TABLE_ENCHANTMENTS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT)");
        db.execSQL("create table " + TABLE_ARMORS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT, LEVEL INTEGER, REQUIRED_STRENGTH INTEGER, ARMOR INTEGER, EFFECTIVE_ABSORPTION INTEGER)");
        db.execSQL("create table " + TABLE_RINGS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT, LEVEL INTEGER, DURATION INTEGER, STEALTH INTEGER, DURATION_REDUCTION_FACTOR INTEGER, RESISTENCE_PROBABILITY INTEGER )");
        db.execSQL("create table " + TABLE_WANDS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT, LEVEL INTEGER, DURATION INTEGER, MIN INTEGER, MAX INTEGER, AVG REAL)");
        db.execSQL("create table " + TABLE_POTIONS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT)");
        db.execSQL("create table " + TABLE_SCROLLS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT)");
        db.execSQL("create table " + TABLE_OTHERITEMS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT)");
    }

    /**
     *
     * @param db Wird benötigt, um execSQLI auszuführen und dadurch dann Tabellen die nicht mehr verwendet werden zu löschen.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MELEE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MISSILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCHANTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARMORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WANDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCROLLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OTHERITEMS);
        onCreate(db);
    }

    /**
     *
     * @param name Wird benötigt, damit wir den Attribute dann mit einem Wert zu füllen.
     * @param base_strength Wird benötigt, damit wir den Attribute dann mit einem Wert zu füllen.
     * @param accuracy benötigt, damit wir den Attribute dann mit einem Wert zu füllen.
     * @param delay Wird benötigt, damit wir den Attribute dann mit einem Wert zu füllen.
     * @return false oder true
     */
    public boolean insertFortMelee(String name, Integer base_strength, Integer accuracy, Double delay) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("BASE_STRENGTH", base_strength);
        contentValues.put("ACCURACY", accuracy);
        contentValues.put("DELAY", delay);
        long result = db.insert("weapons_melee", null, contentValues);
        if (result == 1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     *
     * @param id Der Tring wird benötigt, weil wir ein Integer zurückgeben müssen.
     * @return Integer die leer ist.
     */
    public Integer deleteDublicateForMelee(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TABLE_MELEE,"ID NOT IN (SELECT MIN(ID ) FROM weapons_melee GROUP BY NAME)", null);
    }

    /**
     *
     * @param id Der Tring wird benötigt, weil wir ein Integer zurückgeben müssen.
     * @return Integer die leer ist.
     */
    public Integer deleteDuplicateForMissile(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MISSILE,"ID not in (SELECT MIN(ID ) FROM weapons_melee GROUP BY NAME)", null);
    }

    /**
     *
     * @param id Der Tring wird benötigt, weil wir ein Integer zurückgeben müssen.
     * @return Integer die leer ist.
     */
    public Integer deleteDublicatForRings(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_RINGS,"ID not in (SELECT MIN(ID ) FROM weapons_melee GROUP BY NAME)", null);
    }

    /**
     *
     * @param id Der Tring wird benötigt, weil wir ein Integer zurückgeben müssen.
     * @return Integer die leer ist.
     */
    public Integer deleteDublicatForWands(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_WANDS,"ID not in (SELECT MIN(ID ) FROM weapons_melee GROUP BY NAME)", null);
    }

    /**
     *
     * @param id Der Tring wird benötigt, weil wir ein Integer zurückgeben müssen.
     * @return Integer die leer ist.
     */
    public Integer deleteDublicatForScrolls(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SCROLLS,"ID not in (SELECT MIN(ID ) FROM weapons_melee GROUP BY NAME)", null);
    }

    /**
     *
     * @param id Der Tring wird benötigt, weil wir ein Integer zurückgeben müssen.
     * @return Integer die leer ist.
     */
    public Integer deleteDublicatForOtherItems(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_OTHERITEMS,"ID not in (SELECT MIN(ID ) FROM weapons_melee GROUP BY NAME)", null);
    }
}


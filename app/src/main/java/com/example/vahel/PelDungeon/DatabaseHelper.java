package com.example.vahel.PelDungeon;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 *<h1> SQLite Datenbank </h1>
 *<p> Die SQLite Datenbank wird fuer die Nutzung der Items erzeugt, um die Werte von verschiedenen Items zu speicheren.</p>
 * @author Vahel
 * @version 1.0.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     <p>
     Der Name von unsere Datenbank wird festgelegt <b> Items.db </b>
     </p>
     */
    public final static String DATABASE_NAME = "Items.db";
    /*-----------------------------------------------WEAPONS-----------------------------------------------*/
    /**
     <p>
     Der Tabellename fuer <b> Weapon Melee </b> wird festgelegt. <br>
     Die Attributnamen fuer die Tabelle wird festgelegt: <b> ID, NAME, BASE_STRENGTH, DELAY </b>
     </p>
     */
    public final static String TABLE_MELEE = "weapons_melee";
    public final static String COL_1 = "ID";
    public final static String COL_2 = "NAME";
    public final static String COL_3 = "BASE_STRENGTH";
    public final static String COL_4 = "ACCURACY";
    public final static String COL_5 = "DELAY";

    /**
     *<p>
     *Der Tabellename fuer <b> Weapons Missile </b> wird festgelegt. <br>
     *Die Attributnamen fuer die Tabelle wird festgelegt: <b> ID, NAME, REQUIRED_STRENGTH, AVERAGE_HIT, ACCURACY, DELAY </b>
     *</p>
     */
    public final static String TABLE_MISSILE = "weapons_missile";
    public final static String COL_6 = "ID";
    public final static String COL_7 = "NAME";
    public final static String COL_8 = "REQUIRED_STRENGTH";
    public final static String COL_9 = "AVERAGE_HIT";
    public final static String COL_10 = "ACCURACY";
    public final static String COL_11 = "DELAY";

    /**
     *<p>
     *Der Tabellename fuer <b> Weapons Enchanctments </b> wird festgelegt. <br>
     *Die Attributnamen fuer die Tabelle wird festgelegt: <b> ID, NAME </b>
     *</p>
     */
    public final static String TABLE_ENCHANTMENTS = "weapons_enchantments";
    public final static String COL_12 = "ID";
    public final static String COL_13 = "NAME";
    /*-----------------------------------------------ARMORS-----------------------------------------------*/
    /**
     *<p>
     *Der Tabellename fuer <b> Armors </b> wird festgelegt. <br>
     *Die Attributnamen fuer die Tabelle wird festgelegt: <b> Id, Name, Level, Required Strength, Armor, Eeffective Absorption </b>
     *</p>
     */
    public final static String TABLE_ARMORS = "all_armors";
    public final static String COL_14 = "ID";
    public final static String COL_15 = "NAME";
    public final static String COL_16 = "LEVEL";
    public final static String COL_17 = "REQUIRED_STRENGTH";
    public final static String COL_18 = "ARMOR";
    public final static String COL_19 = "EFFECTIVE_ABSORPTION";
    /*-----------------------------------------------RINGS-----------------------------------------------*/
    /**
     *<p>
     *Der Tabellename fuer <b> Rings </b> wird festgelegt. <br>
     *Die Attributnamen fuer die Tabelle wird festgelegt: <b> Id, Name, Level, Duration, stealth, Duration Reduction Factor, Resistence Probability </b>
     *</p>
     */
    public final static String TABLE_RINGS = "all_rings";
    public final static String COL_20 = "ID";
    public final static String COL_21 = "NAME";
    public final static String COL_22 = "LEVEL";
    public final static String COL_23 = "DURATION";
    public final static String COL_24 = "STEALTH";
    public final static String COL_25 = "DURATION_REDUCTION_FACTOR";
    public final static String COL_26 = "RESISTENCE_PROBABILITY";
    /*-----------------------------------------------WANDS-----------------------------------------------*/
    /**
     *<p>
     *Der Tabellename fuer <b> Wands </b> wird festgelegt. <br>
     *Die Attributnamen fuer die Tabelle wird festgelegt: <b> Id, Name, Level, Duration, Min, Max, Avg </b>
     *</p>
     */
    public final static String TABLE_WANDS = "all_wands";
    public final static String COL_27 = "ID";
    public final static String COL_28 = "NAME";
    public final static String COL_29 = "LEVEL";
    public final static String COL_30 = "DURATION";
    public final static String COL_31 = "MIN";
    public final static String COL_32 = "MAX";
    public final static String COL_33 = "AVG";
    /*-----------------------------------------------POTIONS-----------------------------------------------*/
    /**
     *<p>
     *Der Tabellename fuer <b> Potions </b> wird festgelegt. <br>
     *Die Attributnamen fuer die Tabelle wird festgelegt: <b> Id, Name </b>
     *</p>
     */
    public final static String TABLE_POTIONS = "all_potions";
    public final static String COL_34 = "ID";
    public final static String COL_35 = "NAME";
    /*-----------------------------------------------Scroll-----------------------------------------------*/
    /**
     *<p>
     *Der Tabellename fuer <b> Scrolls </b> wird festgelegt. <br>
     *Die Attributnamen fuer die Tabelle wird festgelegt: <b> Id, Name </b>
     *</p>
     */
    public final static String TABLE_SCROLLS = "all_scrolls";
    public final static String COL_36 = "ID";
    public final static String COL_37 = "NAME";
    /*-----------------------------------------------Other Items-----------------------------------------------*/
    /**
     *<p>
     *Der Tabellename fuer <b> Other Items </b> wird festgelegt. <br>
     *Die Attributnamen fuer die Tabelle wird festgelegt: <b> Id, Name </b>
     *</p>
     */
    public final static String TABLE_OTHERITEMS = "all_otheritems";
    public final static String COL_38 = "ID";
    public final static String COL_39 = "NAME";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    /**
     *<p>
     *Die Tabelle fuer <b> Weapons Melee, Wepons Missile etc. </b> und <br>
     *die dazugehoerigen Attribute <b> Weapons Melee: Name, Bas Strenght etc. </b> werden erzeugt. <br>
     *</p>
     *@param db Wird benoetigt, um execSQL auszufuehren und dadurch dann Tebelle mit den jeweligen Attributen zu erzeugen.
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
     * @param db Wird benoetigt, um execSQLI auszuführen und dadurch dann Tabellen die nicht mehr verwendet werden zu loeschen.
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
     * @param name Wird benoetigt, damit wir den Attribute dann mit einem Wert zu fuellen.
     * @param base_strength Wird benoetigt, damit wir den Attribute dann mit einem Wert zu fuellen.
     * @param accuracy benoetigt, damit wir den Attribute dann mit einem Wert zu fuellen.
     * @param delay Wird benoetigt, damit wir den Attribute dann mit einem Wert zu fuellen.
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
     * @param id Der String wird benoetigt, weil wir ein Integer zurueckgeben muessen.
     * @return Integer die leer ist.
     */
    public Integer deleteDublicateForMelee(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TABLE_MELEE,"ID NOT IN (SELECT MIN(ID ) FROM weapons_melee GROUP BY NAME)", null);
    }

    /**
     *
     * @param id Der String wird benoetigt, weil wir ein Integer zurueckgeben muessen.
     * @return Integer die leer ist.
     */
    public Integer deleteDuplicateForMissile(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MISSILE,"ID not in (SELECT MIN(ID ) FROM weapons_melee GROUP BY NAME)", null);
    }

    /**
     *
     * @param id Der String wird benoetigt, weil wir ein Integer zurueckgeben muessen.
     * @return Integer die leer ist.
     */
    public Integer deleteDublicatForRings(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_RINGS,"ID not in (SELECT MIN(ID ) FROM weapons_melee GROUP BY NAME)", null);
    }

    /**
     *
     * @param id Der String wird benoetigt, weil wir ein Integer zurueckgeben muessen.
     * @return Integer die leer ist.
     */
    public Integer deleteDublicatForWands(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_WANDS,"ID not in (SELECT MIN(ID ) FROM weapons_melee GROUP BY NAME)", null);
    }

    /**
     *
     * @param id Der String wird benoetigt, weil wir ein Integer zurueckgeben muessen.
     * @return Integer die leer ist.
     */
    public Integer deleteDublicatForScrolls(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SCROLLS,"ID not in (SELECT MIN(ID ) FROM weapons_melee GROUP BY NAME)", null);
    }

    /**
     *
     * @param id Der String wird benoetigt, weil wir ein Integer zurueckgeben muessen.
     * @return Integer die leer ist.
     */
    public Integer deleteDublicatForOtherItems(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_OTHERITEMS,"ID not in (SELECT MIN(ID ) FROM weapons_melee GROUP BY NAME)", null);
    }
}


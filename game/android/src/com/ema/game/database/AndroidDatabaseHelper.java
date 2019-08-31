package com.ema.game.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ema.game.components.ArmorComponent;
import com.ema.game.components.PotionComponent;
import com.ema.game.components.WeaponComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class AndroidDatabaseHelper extends SQLiteOpenHelper implements com.ema.game.DatabaseHelper {

    private static final String DATABASE_NAME = "Items.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;
    Random rand = new Random(System.currentTimeMillis());

    /*-----------------------------------------------WEAPONS-----------------------------------------------*/

    public final static String TABLE_MELEE = "all_weapons_melee";
    public final static String COL_1 = "ID";
    public final static String COL_2 = "NAME";
    public final static String COL_3 = "BASE_STRENGTH";
    public final static String COL_4 = "ACCURACY";
    public final static String COL_5 = "LEVEL";

    /*-----------------------------------------------ARMORS-----------------------------------------------*/

    public final static String TABLE_ARMORS = "all_armors";
    public final static String COL_14 = "ID";
    public final static String COL_15 = "NAME";
    public final static String COL_16 = "LEVEL";
    public final static String COL_17 = "REQUIRED_STRENGTH";
    public final static String COL_18 = "ARMOR";
    public final static String COL_19 = "BONUS_HP";
    /*-----------------------------------------------POTIONS-----------------------------------------------*/

    public final static String TABLE_POTIONS = "all_potions";
    public final static String COL_34 = "ID";
    public final static String COL_35 = "NAME";
    public final static String COL_40 = "VALUE";

    /*-----------------------------------------------Scroll-----------------------------------------------*/

    public final static String TABLE_SCROLLS = "all_scrolls";
    public final static String COL_36 = "ID";
    public final static String COL_37 = "NAME";
    public final static String COL_41 = "VALUE";

    public AndroidDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;


//        SQLiteDatabase db = this.getWritableDatabase();

//        if (context.deleteDatabase())
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            context.deleteDatabase(DATABASE_NAME);
        }


        SQLiteDatabase db = this.getReadableDatabase();


        addWeaponsMelee(db);
        addArmors(db);
        addPotions(db);
        addScrolls(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_MELEE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT, BASE_STRENGTH INTEGER, ACCURACY REAL, LEVEL INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_ARMORS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT, LEVEL INTEGER, ARMOR INTEGER, BONUS_HP INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_SCROLLS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT, VALUE REAL)");
        db.execSQL("CREATE TABLE " + TABLE_POTIONS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TXT, HEALTH INTEGER, STRENGTH INTEGER, ARMOR INTEGER)");

        addWeaponsMelee(db);
        addArmors(db);
        addPotions(db);
        addScrolls(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MELEE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARMORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCROLLS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POTIONS);

        onCreate(db);
    }

    private void addWeaponsMelee(SQLiteDatabase db) {
        BufferedReader bufferedReader = csvReader("csv/all_weapons_melee.csv");
        String line;
        db.beginTransaction();

        try {
            line = bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length != 5) {
                    Log.d("allWeaponMelee", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("NAME", columns[1].trim());
                contentValues.put("BASE_STRENGTH", columns[2].trim());
                contentValues.put("ACCURACY", columns[3].trim());
                contentValues.put("LEVEL", columns[4].trim());
                db.insert(TABLE_MELEE, null, contentValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void addArmors(SQLiteDatabase db) {
        BufferedReader bufferedReader = csvReader("csv/all_armors.csv");
        String line;
        db.beginTransaction();

        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length != 5) {
                    Log.d("allArmors", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("NAME", columns[1].trim());
                contentValues.put("LEVEL", columns[2].trim());
                contentValues.put("ARMOR", columns[3].trim());
                contentValues.put("BONUS_HP", columns[4].trim());
                db.insert(TABLE_ARMORS, null, contentValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void addPotions(SQLiteDatabase db) {
        BufferedReader bufferedReader = csvReader("csv/all_potions.csv");
        String line;
        db.beginTransaction();

        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length != 5) {
                    Log.d("allPotions", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("NAME", columns[1].trim());
                contentValues.put("HEALTH", columns[2].trim());
                contentValues.put("STRENGTH", columns[3].trim());
                contentValues.put("ARMOR", columns[4].trim());
                db.insert(TABLE_POTIONS, null, contentValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void addScrolls(SQLiteDatabase db) {
        BufferedReader bufferedReader = csvReader("csv/all_scrolls.csv");
        String line;
        db.beginTransaction();

        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length != 3) {
                    Log.d("allScrolls", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("NAME", columns[1].trim());
                contentValues.put("VALUE", columns[2].trim());
                db.insert(TABLE_SCROLLS, null, contentValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }



    private BufferedReader csvReader(String csvPath) {
        BufferedReader bufferedReader;
        try {
            InputStream is = context.getAssets().open(csvPath);
            bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            return bufferedReader;
        } catch (Exception e) {
            System.out.println(csvPath);
        }
        return null;
    }

    public WeaponComponent getWeapon() {
        WeaponComponent weapon = new WeaponComponent();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MELEE, null);

        if (cursor.moveToPosition(rand.nextInt(cursor.getCount()))) {
            weapon.id = cursor.getInt(0);
            weapon.name = cursor.getString(1);
            weapon.base_strength = cursor.getInt(2);
        }

        cursor.close();

        return weapon;
    }

    public ArmorComponent getArmor() {
        ArmorComponent armor = new ArmorComponent();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ARMORS, null);

        if (cursor.moveToPosition(rand.nextInt(cursor.getCount()))) {
            armor.id = cursor.getInt(0);
            armor.name = cursor.getString(1);
            armor.level = cursor.getInt(2);
            armor.armor = cursor.getInt(3);
            armor.bonus_hp = cursor.getInt(4);
        }
        cursor.close();
        return armor;
    }

    public PotionComponent getPotion() {
        PotionComponent potion = new PotionComponent();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_POTIONS, null);

        if (cursor.moveToPosition(rand.nextInt(cursor.getCount()))) {
            potion.id = cursor.getInt(0);
            potion.name = cursor.getString(1);
            potion.health = cursor.getInt(2);
            potion.strength = cursor.getInt(3);
            potion.armor = cursor.getInt(4);
        }
        cursor.close();
        return potion;
    }
}
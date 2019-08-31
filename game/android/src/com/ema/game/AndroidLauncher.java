package com.ema.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ema.game.database.AndroidDatabaseHelper;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		DatabaseHelper dbHelper = new AndroidDatabaseHelper(this);
		initialize(new Dungeon(dbHelper), config);
	}
}

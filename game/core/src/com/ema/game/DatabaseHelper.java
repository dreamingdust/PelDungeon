package com.ema.game;

import com.ema.game.components.ArmorComponent;
import com.ema.game.components.PotionComponent;
import com.ema.game.components.WeaponComponent;

public interface DatabaseHelper {
    // Created to make the connection between the AndroidDatabaseHelper class and the
    // core, where all the functionality is.
    public ArmorComponent getArmor();
    public WeaponComponent getWeapon();
    public PotionComponent getPotion();
}

package modtweaker2.mods.randomthings;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.randomthings.handlers.ImbuingStation;

/**
 * Created by Jared on 7/16/2016.
 */
public class RandomThings {

    public RandomThings() {
        MineTweakerAPI.registerClass(ImbuingStation.class);
    }

}

package modtweaker.utils;

import net.minecraftforge.fml.common.Loader;

import java.util.Map;
import java.util.TreeMap;

public class TweakerPlugin {
    private static Map<String, Boolean> mods = new TreeMap<String, Boolean>();

    public static void register(String mod, Boolean enabled, Class<?> clazz) {
        if (Loader.isModLoaded(mod)) {
            load(mod, enabled, clazz);
        }
    }

    public static void load(String mod, boolean enabled, Class<?> clazz) {
        try {
            clazz.newInstance();
            mods.put(mod, enabled);
        } catch (Exception e) {
            mods.remove(mod);
        }
    }

    public static boolean isLoaded(String string) {
        return mods.containsKey(string);
    }

    public void setState(String mod, boolean state) {
        mods.put(mod, state);
    }

    public boolean getState(String mod) {
        return mods.get(mod);
    }

    public static Map<String, Boolean> getMods() {
        return mods;
    }
}

package modtweaker.utils;

import net.minecraftforge.fml.common.Loader;

import java.util.LinkedList;
import java.util.List;

public class TweakerPlugin {
    private static List<String> mods = new LinkedList<String>();

    public static void register(String mod, Class<?> clazz) {
        if (Loader.isModLoaded(mod)) {
            load(mod, clazz);
        }
    }

    public static void load(String mod, Class<?> clazz) {
        try {
            clazz.newInstance();
            mods.add(mod);
        } catch (Exception e) {
            mods.remove(mod);
        }
    }

    public static boolean isLoaded(String string) {
        return mods.contains(string);
    }

    public static List<String> getMods() {
        return mods;
    }
}

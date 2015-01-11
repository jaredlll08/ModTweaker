package modtweaker.util;

import java.util.ArrayList;

import cpw.mods.fml.common.Loader;

public class TweakerPlugin {
    private static ArrayList<String> isLoaded = new ArrayList();

    public static void register(String mod, Class clazz) {
        if (Loader.isModLoaded(mod)) {
            try {
                clazz.newInstance();
                isLoaded.add(mod);
            } catch (Exception e) {
                isLoaded.remove(mod);
            }
        }
    }

    public static boolean isLoaded(String string) {
        return isLoaded.contains(string);
    }
}

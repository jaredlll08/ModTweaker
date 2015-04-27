package modtweaker2.utils;

import java.util.ArrayList;

import cpw.mods.fml.common.Loader;

public class TweakerPlugin {
    private static ArrayList<String> isLoaded = new ArrayList();
    
    public static boolean hasInit = false;

    public static void register(String mod, Class clazz) {
        if (Loader.isModLoaded(mod)) {
            load(mod, clazz);
        }
    }

    public static void load(String mod, Class clazz) {
        try {
            clazz.newInstance();
            isLoaded.add(mod);
        } catch (Exception e) {
            isLoaded.remove(mod);
        }
    }

    public static boolean isLoaded(String string) {
        return isLoaded.contains(string);
    }
    
    public static boolean hasInit(){
    	return hasInit;
    }
}

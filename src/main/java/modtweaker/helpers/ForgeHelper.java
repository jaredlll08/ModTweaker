package modtweaker.helpers;

import static modtweaker.helpers.ReflectionHelper.getFinalObject;
import static modtweaker.helpers.ReflectionHelper.getStaticObject;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.ForgeHooks;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ForgeHelper {
    public static Map translate = null;
    public static List seeds = null;
    public static HashMap<String, ChestGenHooks> loot = null;

    static {
        try {
            seeds = getStaticObject(ForgeHooks.class, "seedList");
            loot = getStaticObject(ChestGenHooks.class, "chestInfo");
            translate = getFinalObject(getStaticObject(StatCollector.class, "localizedName", "field_74839_a"), "languageList", "field_74816_c");
        } catch (Exception e) {}
    }

    private ForgeHelper() {}

    public static Object getSeedEntry(ItemStack stack, int weight) {
        try {
            Class clazz = Class.forName("net.minecraftforge.common.ForgeHooks$SeedEntry");
            Constructor constructor = clazz.getDeclaredConstructor(ItemStack.class, int.class);
            constructor.setAccessible(true);
            return constructor.newInstance(stack, weight);
        } catch (Exception e) {
            throw new NullPointerException("Failed to instantiate SeedEntry");
        }
    }

    public static boolean isLangActive(String lang) {
        return FMLCommonHandler.instance().getSide() == Side.SERVER ? null : FMLClientHandler.instance().getCurrentLanguage().equals(lang);
    }
}

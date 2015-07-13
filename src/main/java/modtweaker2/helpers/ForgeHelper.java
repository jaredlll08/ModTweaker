package modtweaker2.helpers;

import static modtweaker2.helpers.ReflectionHelper.getConstructor;
import static modtweaker2.helpers.ReflectionHelper.getFinalObject;
import static modtweaker2.helpers.ReflectionHelper.getInstance;
import static modtweaker2.helpers.ReflectionHelper.getStaticObject;

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
    @SuppressWarnings("rawtypes")
    public static Map translate = null;
    @SuppressWarnings("rawtypes")
    public static List seeds = null;
    public static HashMap<String, ChestGenHooks> loot = null;

    static {
        try {
            seeds = getStaticObject(ForgeHooks.class, "seedList");
            loot = getStaticObject(ChestGenHooks.class, "chestInfo");
            translate = getFinalObject(getStaticObject(StatCollector.class, "localizedName", "field_74839_a"), "languageList", "field_74816_c");
        } catch (Exception e) { }
    }

    private ForgeHelper() {
    }

    public static Object getSeedEntry(ItemStack stack, int weight) {
        Object seedEntry = getInstance(getConstructor("net.minecraftforge.common.ForgeHooks$SeedEntry",
                        ItemStack.class, int.class), stack, weight);

        if (seedEntry == null) {
            throw new NullPointerException("Failed to instantiate SeedEntry");
        }

        return seedEntry;
    }

    public static boolean isLangActive(String lang) {
        return FMLCommonHandler.instance().getSide() == Side.SERVER ? null
                : FMLClientHandler.instance().getCurrentLanguage().equals(lang);
    }
}

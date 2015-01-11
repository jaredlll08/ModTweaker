package modtweaker.mods.mariculture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mariculture.api.core.MaricultureHandlers;
import mariculture.api.fishery.Fishing;
import mariculture.api.fishery.Loot;
import mariculture.api.fishery.Loot.Rarity;
import modtweaker.helpers.ReflectionHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class MaricultureHelper {
    public static Map fuels = null;
    public static HashMap<Rarity, ArrayList<Loot>> loot = null;

    static {
        try {
            fuels = ReflectionHelper.getFinalObject(MaricultureHandlers.crucible, "fuels");
            loot = ReflectionHelper.getFinalObject(Fishing.fishing, "fishing_loot");
        } catch (Exception e) {}
    }

    private MaricultureHelper() {}

    //Helper for getting the key that is used when adding/removing fuels
    public static String getKey(Object o) {
        if (o instanceof String) return (String) o;
        else if (o instanceof ItemStack) {
            String name = "";
            ItemStack stack = (ItemStack) o;
            if (OreDictionary.getOreIDs(stack).length > 0) {
                name = OreDictionary.getOreName(OreDictionary.getOreIDs(stack)[0]);
            } else if (stack.isItemStackDamageable()) {
                name = Item.itemRegistry.getNameForObject(stack.getItem()) + "|ignore";
            } else {
                name = Item.itemRegistry.getNameForObject(stack.getItem()) + "|" + stack.getItemDamage();
            }

            return name;
        } else if (o instanceof FluidStack) {
            return ((FluidStack) o).getFluid().getName();
        } else return null;
    }
}

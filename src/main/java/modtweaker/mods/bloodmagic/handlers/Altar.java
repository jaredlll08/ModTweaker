package modtweaker.mods.bloodmagic.handlers;

import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.altar.EnumAltarTier;
import WayofTime.bloodmagic.api.registry.AltarRecipeRegistry;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker.helpers.LogHelper;
import modtweaker.helpers.ReflectionHelper;
import modtweaker.mods.bloodmagic.BloodMagicHelper;
import modtweaker.utils.BaseMapAddition;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import net.minecraft.item.ItemStack;

import static modtweaker.helpers.InputHelper.*;
import static modtweaker.helpers.StackHelper.matches;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@ZenClass("mods.bloodmagic.Altar")
public class Altar
{
    protected static final String name = "Blood Magic Altar";

    private static final EnumAltarTier[] altarTiers = EnumAltarTier.values();

    @ZenMethod
    public static void addRecipe(IItemStack output, int minTier, int syphon, int consumeRate, int drainRate, IItemStack[] input)
    {
        if (output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
            return;
        }
        else if(minTier <= 0 || minTier > altarTiers.length)
        {
            LogHelper.logWarning(String.format("Invalid altar tier (%d) required for %s Recipe", minTier, Altar.name));
            return;
        }
        else if(syphon < 0)
        {
            LogHelper.logWarning(String.format("Syphon can't be below 0 (%d) for %s Recipe", syphon, Altar.name));
            return;
        }
        else if(consumeRate < 0)
        {
            LogHelper.logWarning(String.format("Consume rate can't be below 0 (%d) for %s Recipe", consumeRate, Altar.name));
            return;
        }
        else if(drainRate < 0)
        {
            LogHelper.logWarning(String.format("Drain rate can't be below 0 (%d) for %s Recipe", drainRate, Altar.name));
            return;
        }

        List<ItemStack> inputs = Arrays.asList(toStacks(input));
        AltarRecipeRegistry.AltarRecipe temp = new AltarRecipeRegistry.AltarRecipe(inputs, toStack(output), altarTiers[minTier-1], syphon, consumeRate, drainRate);
        MineTweakerAPI.apply(new Add(temp.getInput(), temp, BloodMagicHelper.altarBiMap));
    }

    private static class Add extends BaseMapAddition<List<ItemStackWrapper>, AltarRecipeRegistry.AltarRecipe>
    {
        public Add(List<ItemStackWrapper> inputs, AltarRecipeRegistry.AltarRecipe altarRecipe, Map<List<ItemStackWrapper>, AltarRecipeRegistry.AltarRecipe> list)
        {
            super(Altar.name, list);
            this.recipes.put(inputs, altarRecipe);
        }

        @Override
        public String getRecipeInfo(Entry<List<ItemStackWrapper>, AltarRecipeRegistry.AltarRecipe> recipe)
        {
            ItemStack output = ReflectionHelper.getFinalObject(recipe.getValue(), "output");
            return LogHelper.getStackDescription(output);
        }
    }
}

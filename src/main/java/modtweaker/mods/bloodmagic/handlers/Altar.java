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

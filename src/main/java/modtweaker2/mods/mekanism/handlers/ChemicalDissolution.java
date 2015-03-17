package modtweaker2.mods.mekanism.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;
import static modtweaker2.mods.mekanism.MekanismHelper.toGas;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mekanism.api.gas.GasStack;
import mekanism.common.recipe.RecipeHandler.Recipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import modtweaker2.helpers.InputHelper;
import modtweaker2.mods.mekanism.gas.IGasStack;
import modtweaker2.mods.mekanism.util.AddMekanismRecipe;
import modtweaker2.mods.mekanism.util.RemoveMekanismRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mekanism.chemical.Dissolution")
public class ChemicalDissolution {
    @ZenMethod
    public static void addRecipe(IItemStack input, IGasStack output) {
        MineTweakerAPI.apply(new AddMekanismRecipe("CHEMICAL_DISSOLUTION_CHAMBER", Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get(), toStack(input), toGas(output)));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        MineTweakerAPI.apply(new Remove("CHEMICAL_DISSOLUTION_CHAMBER", Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get(), toStack(input)));
    }

    @ZenMethod
    public static void removeRecipe(IOreDictEntry input) {
        List<ItemStack> stacks = OreDictionary.getOres(InputHelper.toString(input));
        for (ItemStack stack : stacks) {
            MineTweakerAPI.apply(new Remove("CHEMICAL_DISSOLUTION_CHAMBER", Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get(), stack));
        }
    }

    @ZenMethod
    public static void removeRecipe(IGasStack input) {
        MineTweakerAPI.apply(new Remove("CHEMICAL_DISSOLUTION_CHAMBER", Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get(), toGas(input)));
    }

    private static class Remove extends RemoveMekanismRecipe {
        public Remove(String string, Map map, Object key) {
            super(string, map, key);
        }

        @Override
        public void apply() {
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                ItemStack key = (ItemStack) pairs.getKey();
                GasStack value = (GasStack) pairs.getValue();
                if (key != null) {
                    if (this.key instanceof ItemStack && areEqual(key, (ItemStack) this.key)) {
                        this.key = key;
                        break;
                    } else if (this.key instanceof GasStack && value.isGasEqual((GasStack) this.key)) {
                        this.key = key;
                        break;
                    }
                }

            }

            recipe = map.get(key);
            map.remove(key);
        }
    }
}

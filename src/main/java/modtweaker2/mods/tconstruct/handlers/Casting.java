package modtweaker2.mods.tconstruct.handlers;

import java.util.ArrayList;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.mods.tconstruct.TConstructHelper;
import modtweaker2.util.BaseListAddition;
import modtweaker2.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tconstruct.library.crafting.CastingRecipe;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.StackHelper.*;

@ZenClass("mods.tconstruct.Casting")
public class Casting {
    //Adding a TConstruct Casting recipe
    @ZenMethod
    public static void addBasinRecipe(IItemStack output, ILiquidStack metal, @Optional IItemStack cast, @Optional boolean consume, int delay) {
        MineTweakerAPI.apply(new Add(new CastingRecipe(toStack(output), toFluid(metal), toStack(cast), consume, delay, null), TConstructHelper.basinCasting));
    }

    @ZenMethod
    public static void addTableRecipe(IItemStack output, ILiquidStack metal, @Optional IItemStack cast, @Optional boolean consume, int delay) {
        MineTweakerAPI.apply(new Add(new CastingRecipe(toStack(output), toFluid(metal), toStack(cast), consume, delay, null), TConstructHelper.tableCasting));
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition {
        public Add(CastingRecipe recipe, ArrayList list) {
            super("TConstruct Casting", list, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((CastingRecipe) recipe).output.getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a TConstruct Casting recipe
    @ZenMethod
    public static void removeBasinRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove((toStack(output)), TConstructHelper.basinCasting));
    }

    @ZenMethod
    public static void removeTableRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove((toStack(output)), TConstructHelper.tableCasting));
    }

    //Removes a recipe, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack output, ArrayList list) {
            super("TConstruct Casting", list, output);
        }

        //Loops through the registry, to find the item that matches, saves that recipe then removes it
        @Override
        public void apply() {
            for (CastingRecipe r : (ArrayList<CastingRecipe>) list) {
                if (r.output != null && areEqual(r.output, stack)) {
                    recipe = r;
                    break;
                }
            }

            list.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}

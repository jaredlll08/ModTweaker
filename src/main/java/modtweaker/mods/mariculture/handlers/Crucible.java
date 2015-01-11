package modtweaker.mods.mariculture.handlers;

import static modtweaker.helpers.InputHelper.toFluid;
import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.areEqual;
import mariculture.api.core.FuelInfo;
import mariculture.api.core.MaricultureHandlers;
import mariculture.api.core.RecipeSmelter;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker.mods.mariculture.MaricultureHelper;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import modtweaker.util.BaseMapAddition;
import modtweaker.util.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mariculture.Crucible")
public class Crucible {

    /********************************************** Mariculture Crucible Recipes **********************************************/

    //Adding a Mariculture Crucible recipe
    @ZenMethod
    public static void addRecipe(int temp, IItemStack input, ILiquidStack fluid, @Optional IItemStack output, @Optional int chance) {
        ItemStack out = output != null ? toStack(output) : null;
        MineTweakerAPI.apply(new AddRecipe(new RecipeSmelter(toStack(input), null, temp, toFluid(fluid), out, chance)));
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class AddRecipe extends BaseListAddition {
        public AddRecipe(RecipeSmelter recipe) {
            super("Mariculture Crucible", MaricultureHandlers.crucible.getRecipes(), recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((RecipeSmelter) recipe).input.getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Mariculture Crucible recipe
    @ZenMethod
    public static void removeRecipe(IItemStack input) {
        MineTweakerAPI.apply(new RemoveRecipe(toStack(input)));
    }

    //Removes a recipe, apply is never the same for anything, so will always need to override it
    private static class RemoveRecipe extends BaseListRemoval {
        public RemoveRecipe(ItemStack stack) {
            super("Mariculture Crucible", MaricultureHandlers.crucible.getRecipes(), stack);
        }

        //Loops through the registry, to find the item that matches, saves that recipe then removes it
        @Override
        public void apply() {
            for (RecipeSmelter r : MaricultureHandlers.crucible.getRecipes()) {
                if (r.input != null && areEqual(r.input, stack)) {
                    recipe = r;
                    break;
                }
            }

            MaricultureHandlers.crucible.getRecipes().remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }

    /********************************************** Crucible Fuels **********************************************/
    @ZenMethod
    public static void addFuel(IItemStack input, int max, int per, int time) {
        MineTweakerAPI.apply(new AddFuel(toStack(input), new FuelInfo(max, per, time)));
    }

    @ZenMethod
    public static void addFuel(ILiquidStack input, int max, int per, int time) {
        MineTweakerAPI.apply(new AddFuel(toFluid(input), new FuelInfo(max, per, time)));
    }

    @ZenMethod
    public static void addFuel(String input, int max, int per, int time) {
        MineTweakerAPI.apply(new AddFuel(input, new FuelInfo(max, per, time)));
    }

    //Passes the list to the base map implementation, and adds the recipe
    private static class AddFuel extends BaseMapAddition {
        public AddFuel(Object o, FuelInfo info) {
            super("Mariculture Crucible Fuel", MaricultureHelper.fuels, MaricultureHelper.getKey(o), info);
        }

        @Override
        public String getRecipeInfo() {
            return (String) key;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @ZenMethod
    public static void removeFuel(IItemStack fuel) {
        MineTweakerAPI.apply(new RemoveFuel(fuel));
    }

    @ZenMethod
    public static void removeFuel(ILiquidStack fuel) {
        MineTweakerAPI.apply(new RemoveFuel(fuel));
    }

    @ZenMethod
    public static void removeFuel(String fuel) {
        MineTweakerAPI.apply(new RemoveFuel(fuel));
    }

    //Removes a recipe, will always remove the key, so all should be good
    private static class RemoveFuel extends BaseMapRemoval {
        public RemoveFuel(Object o) {
            super("Mariculture Crucible Fuel", MaricultureHelper.fuels, MaricultureHelper.getKey(o), null);
        }

        @Override
        public String getRecipeInfo() {
            return (String) key;
        }
    }
}

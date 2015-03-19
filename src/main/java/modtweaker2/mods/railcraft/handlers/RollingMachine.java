package modtweaker2.mods.railcraft.handlers;

import static modtweaker2.helpers.InputHelper.toObjects;
import static modtweaker2.helpers.InputHelper.toShapedObjects;
import static modtweaker2.helpers.InputHelper.toStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import modtweaker2.helpers.InputHelper;
import modtweaker2.mods.railcraft.RailcraftHelper;
import modtweaker2.utils.BaseCraftingAddition;
import modtweaker2.utils.BaseCraftingRemoval;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.railcraft.Rolling")
public class RollingMachine {
    @ZenMethod
    public static void addShaped(IItemStack output, IIngredient[][] ingredients) {
        MineTweakerAPI.apply(new Add(false, toStack(output), toShapedObjects(ingredients)));
    }

    @ZenMethod
    public static void addShapeless(IItemStack output, IIngredient[] ingredients) {
        MineTweakerAPI.apply(new Add(true, toStack(output), toObjects(ingredients)));
    }

    private static class Add extends BaseCraftingAddition {
        public Add(boolean shapeless, ItemStack output, Object... recipe) {
            super("Rolling Machine", shapeless, RailcraftHelper.rolling, output, recipe);
        }

        @Override
        public void applyShaped() {
            RailcraftHelper.rolling.add(new ShapedOreRecipe(output, recipe));
        }

        @Override
        public void applyShapeless() {
            RailcraftHelper.rolling.add(new ShapelessOreRecipe(output, recipe));
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Remove extends BaseCraftingRemoval {
        public Remove(ItemStack stack) {
            super("Rolling Machine", RailcraftHelper.rolling, stack);
        }
    }
}

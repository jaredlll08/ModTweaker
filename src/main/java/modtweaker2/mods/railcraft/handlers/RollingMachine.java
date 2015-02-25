package modtweaker2.mods.railcraft.handlers;

import appeng.recipes.game.ShapedRecipe;
import appeng.recipes.game.ShapelessRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import modtweaker2.helpers.InputHelper;
import modtweaker2.mods.railcraft.RailcraftHelper;
import modtweaker2.util.BaseCraftingAddition;
import modtweaker2.util.BaseCraftingRemoval;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.StackHelper.*;

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
            RailcraftCraftingManager.rollingMachine.addRecipe(output, recipe);
        }

        @Override
        public void applyShapeless() {
            RailcraftCraftingManager.rollingMachine.addShapelessRecipe(output, recipe);
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

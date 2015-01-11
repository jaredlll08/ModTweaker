package modtweaker.mods.bloodmagic.handlers;

import static modtweaker.helpers.InputHelper.toObjects;
import static modtweaker.helpers.InputHelper.toShapedObjects;
import static modtweaker.helpers.InputHelper.toStack;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker.util.BaseCraftingAddition;
import modtweaker.util.BaseCraftingRemoval;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import WayofTime.alchemicalWizardry.api.items.ShapedBloodOrbRecipe;
import WayofTime.alchemicalWizardry.api.items.ShapelessBloodOrbRecipe;

@ZenClass("mods.bloodmagic.BloodOrb")
public class BloodOrb {
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
            super("Blood Orb", shapeless, CraftingManager.getInstance().getRecipeList(), output, recipe);
        }

        @Override
        public void applyShaped() {
            list.add(new ShapedBloodOrbRecipe(output, recipe));
        }

        @Override
        public void applyShapeless() {
            list.add(new ShapelessBloodOrbRecipe(output, recipe));
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Remove extends BaseCraftingRemoval {
        public Remove(ItemStack stack) {
            super("Blood Orb", CraftingManager.getInstance().getRecipeList(), stack);
        }
    }
}

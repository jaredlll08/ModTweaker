package modtweaker2.mods.hee.handlers;

import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.areEqual;

import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.ReflectionHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import chylex.hee.mechanics.essence.handler.DragonEssenceHandler;
import chylex.hee.mechanics.essence.handler.dragon.AltarItemRecipe;

@ZenClass("mods.hee.EssenceAltar")
public class EssenceAltar {
    @ZenMethod
    public static void addRecipe(IItemStack source, IItemStack result, int cost) {
        MineTweakerAPI.apply(new Add(toStack(result), new AltarItemRecipe(toStack(source), toStack(result), cost)));
    }

    private static class Add extends BaseListAddition {
        private final ItemStack result;

        public Add(ItemStack result, AltarItemRecipe recipe) {
            super("Essence Altar", DragonEssenceHandler.recipes, recipe);
            this.result = result;
        }

        @Override
        public String getRecipeInfo() {
            return result.getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Essence Altar", DragonEssenceHandler.recipes, stack);
        }

        //Returns the output ItemStack
        private ItemStack getOutput(Object o) {
            return (ItemStack) ReflectionHelper.getObject(o, "result");
        }

        @Override
        public void apply() {
            for (AltarItemRecipe r : (List<AltarItemRecipe>) list) {
                ItemStack output = getOutput(r);
                if (output != null && areEqual(output, stack)) {
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

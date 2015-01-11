package modtweaker.mods.exnihilo.handlers;

import static modtweaker.helpers.InputHelper.isABlock;
import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.areEqual;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Smashable;

@ZenClass("mods.exnihilo.Hammer")
public class Hammer {
    //Adding a Ex Nihilo Hammer recipe
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, double chance, double luck) {
        if (isABlock(input)) {
            Block theBlock = Block.getBlockFromItem(toStack(input).getItem());
            int theMeta = toStack(input).getItemDamage();
            MineTweakerAPI.apply(new Add(new Smashable(theBlock, theMeta, toStack(output).getItem(), toStack(output).getItemDamage(), (float) chance, (float) luck)));
        }
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition {
        public Add(Smashable recipe) {
            super("ExNihilo Hammer", HammerRegistry.rewards, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return new ItemStack(((Smashable) recipe).source, 1, ((Smashable) recipe).sourceMeta).getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Ex Nihilo Hammer recipe
    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    //Removes a recipe, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("ExNihilo Hammer", HammerRegistry.rewards, stack);
        }

        //Loops through the registry, to find the item that matches, saves that recipe then removes it
        @Override
        public void apply() {
            for (Smashable r : HammerRegistry.rewards) {
                ItemStack check = new ItemStack(r.item, 1, r.meta);
                if (check != null && areEqual(check, stack)) {
                    recipe = r;
                    break;
                }
            }

            HammerRegistry.rewards.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }
}

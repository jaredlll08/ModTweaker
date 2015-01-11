package modtweaker.mods.exnihilo.handlers;

import static modtweaker.helpers.InputHelper.isABlock;
import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.areEqual;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseMultipleListRemoval;
import modtweaker.util.BaseMultipleListRemoval.Position;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import exnihilo.registries.SieveRegistry;
import exnihilo.registries.helpers.SiftReward;

@ZenClass("mods.exnihilo.Sieve")
public class Sieve {
    //Adding a Ex Nihilo Sieve recipe
    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack output, int rarity) {
        if (isABlock(input)) {
            Block theBlock = Block.getBlockFromItem(toStack(input).getItem());
            int theMeta = toStack(input).getItemDamage();
            MineTweakerAPI.apply(new Add(new SiftReward(theBlock, theMeta, toStack(output).getItem(), toStack(output).getItemDamage(), rarity)));
        }
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class Add extends BaseListAddition {
        public Add(SiftReward recipe) {
            super("ExNihilo Sieve", SieveRegistry.rewards, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return new ItemStack(((SiftReward) recipe).source, 1, ((SiftReward) recipe).sourceMeta).getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a Ex Nihilo Sieve recipe
    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output), Position.ALL));
    }

    @ZenMethod
    public static void removeFirst(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output), Position.FIRST));
    }

    @ZenMethod
    public static void removeLast(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output), Position.LAST));
    }

    //Removes a recipe, apply is never the same for anything, so will always need to override it
    private static class Remove extends BaseMultipleListRemoval {
        public Remove(ItemStack stack, Position pos) {
            super("ExNihilo Sieve", SieveRegistry.rewards, stack, pos);
        }

        @Override
        protected boolean isEqual(Object recipe, Object search) {
            SiftReward r = (SiftReward) recipe;
            ItemStack c = new ItemStack(r.item, 1, r.meta);
            if (c != null && areEqual(c, (ItemStack) search)) {
                return true;
            } else return false;
        }

        @Override
        public String getRecipeInfo() {
            return ((ItemStack) search).getDisplayName();
        }
    }
}

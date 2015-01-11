package modtweaker.mods.thaumcraft.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.InputHelper.toStacks;
import static modtweaker.helpers.StackHelper.areEqual;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker.mods.thaumcraft.ThaumcraftHelper;
import modtweaker.mods.thaumcraft.recipe.MTInfusionRecipe;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.InfusionEnchantmentRecipe;
import thaumcraft.api.crafting.InfusionRecipe;

@ZenClass("mods.thaumcraft.Infusion")
public class Infusion {
    @ZenMethod
    public static void addRecipe(String key, IItemStack input, IItemStack[] recipe, String aspects, IItemStack result, int instability) {
        MineTweakerAPI.apply(new Add(new InfusionRecipe(key, toStack(result), instability, ThaumcraftHelper.parseAspects(aspects), toStack(input), toStacks(recipe))));
    }

    //A version that allows you to specify whether the detection should be fuzzy or not
    @ZenMethod
    public static void addRecipe(String key, IItemStack input, IItemStack[] recipe, String aspects, IItemStack result, int instability, boolean fuzzyCentre, boolean[] fuzzyRecipe) {
        MineTweakerAPI.apply(new Add(new MTInfusionRecipe(key, toStack(result), instability, ThaumcraftHelper.parseAspects(aspects), toStack(input), toStacks(recipe), fuzzyCentre, fuzzyRecipe)));
    }

    @ZenMethod
    public static void addEnchantment(String key, int enchantID, int instability, String aspects, IItemStack[] recipe) {
        MineTweakerAPI.apply(new AddEnchant(new InfusionEnchantmentRecipe(key, Enchantment.enchantmentsList[enchantID], instability, ThaumcraftHelper.parseAspects(aspects), toStacks(recipe))));
    }

    private static class Add extends BaseListAddition {
        public Add(InfusionRecipe recipe) {
            super("Thaumcraft Infusion", ThaumcraftApi.getCraftingRecipes(), recipe);
        }

        @Override
        public String getRecipeInfo() {
            Object out = ((InfusionRecipe) recipe).getRecipeOutput();
            if (out instanceof ItemStack) {
                return ((ItemStack) out).getDisplayName();
            } else return super.getRecipeInfo();
        }
    }

    private static class AddEnchant implements IUndoableAction {
        InfusionEnchantmentRecipe recipe;

        public AddEnchant(InfusionEnchantmentRecipe inp) {
            recipe = inp;
        }

        @Override
        public void apply() {
            ThaumcraftApi.getCraftingRecipes().add(recipe);
        }

        @Override
        public String describe() {
            return "Adding Infusion Enchantment Recipe: " + recipe.enchantment.getName();
        }

        @Override
        public boolean canUndo() {
            return recipe != null;
        }

        @Override
        public void undo() {
            ThaumcraftApi.getCraftingRecipes().remove(recipe);
        }

        @Override
        public String describeUndo() {
            return "Removing Infusion Enchantment Recipe: " + recipe.enchantment.getName();
        }

        @Override
        public String getOverrideKey() {
            return null;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    @ZenMethod
    public static void removeEnchant(int id) {
        MineTweakerAPI.apply(new RemoveEnchant(Enchantment.enchantmentsList[id]));
    }

    private static class Remove extends BaseListRemoval {
        public Remove(ItemStack stack) {
            super("Thaumcraft Infusion", ThaumcraftApi.getCraftingRecipes(), stack);
        }

        @Override
        public void apply() {
            for (Object o : ThaumcraftApi.getCraftingRecipes()) {
                if (o instanceof InfusionRecipe) {
                    InfusionRecipe r = (InfusionRecipe) o;
                    if (r.getRecipeOutput() != null && r.getRecipeOutput() instanceof ItemStack && areEqual((ItemStack) r.getRecipeOutput(), stack)) {
                        recipe = r;
                        break;
                    }
                }
            }

            ThaumcraftApi.getCraftingRecipes().remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return stack.getDisplayName();
        }
    }

    private static class RemoveEnchant implements IUndoableAction {
        Enchantment enchant;
        InfusionEnchantmentRecipe removed;

        public RemoveEnchant(Enchantment ench) {
            enchant = ench;
        }

        @Override
        public void apply() {
            for (Object recipe : ThaumcraftApi.getCraftingRecipes()) {
                if (recipe instanceof InfusionEnchantmentRecipe) {
                    InfusionEnchantmentRecipe enchRecipe = (InfusionEnchantmentRecipe) recipe;
                    if (enchRecipe.getEnchantment() == enchant) {
                        removed = enchRecipe;
                        ThaumcraftApi.getCraftingRecipes().remove(enchRecipe);
                    }
                }
            }
        }

        @Override
        public String describe() {
            return "Removing Infusion Enchantment Recipe: " + enchant.getName();
        }

        @Override
        public boolean canUndo() {
            return removed != null;
        }

        @Override
        public void undo() {
            ThaumcraftApi.getCraftingRecipes().add(removed);
        }

        @Override
        public String describeUndo() {
            return "Restoring Infusion Enchantment Recipe: " + enchant.getName();
        }

        @Override
        public String getOverrideKey() {
            return null;
        }
    }
}

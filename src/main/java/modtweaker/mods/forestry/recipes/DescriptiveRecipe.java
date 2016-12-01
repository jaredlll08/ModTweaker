package modtweaker.mods.forestry.recipes;

import forestry.api.recipes.IDescriptiveRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

public class DescriptiveRecipe implements IDescriptiveRecipe {

    private final int width;
    private final int height;
    private final ShapedOreRecipe recipe;
    private final ItemStack[] remainingItems;

    public DescriptiveRecipe(int width, int height, Object[] ingredients, ItemStack output, ItemStack[] remainingItems) {
        this.width = width;
        this.height = height;
        this.recipe = new ShapedOreRecipe(output, ingredients);
        this.remainingItems = remainingItems;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return recipe.getRecipeOutput();
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return remainingItems;
    }

    @Override
    public Object[] getIngredients() {
        return recipe.getInput();
    }
    
    @Nonnull
    @Override
    public ItemStack func_77571_b() {
        return recipe.getRecipeOutput();
    }
    
    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        return recipe.matches(inventoryCrafting, world);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        return recipe.getCraftingResult(inventoryCrafting);
    }

    @Override
    public int getRecipeSize() {
        return width * height;
    }
}

package naruto1310.extendedWorkbench.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ExtendedShapedRecipes implements IExtendedRecipe
{
    private int maxRecipeSize = 6;

    /** How many horizontal slots this recipe is wide. */
    private int recipeWidth;

    /** How many vertical slots this recipe uses. */
    private int recipeHeight;

    /** Is a array of ItemStack that composes the recipe. */
    private ItemStack[] recipeItems;

    /** Is the ItemStack that you get when craft the recipe. */
    private ItemStack recipeOutput;

    public ExtendedShapedRecipes(int par1, int par2, ItemStack[] par3ArrayOfItemStack, ItemStack par4ItemStack)
    {
        this.recipeWidth = par1;
        this.recipeHeight = par2;
        this.recipeItems = par3ArrayOfItemStack;
        this.recipeOutput = par4ItemStack;
    }

    @Override
	public ItemStack getRecipeOutput()
    {
        return this.recipeOutput;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    @Override
	public boolean matches(InventoryCrafting par1InventoryCrafting, World world)
    {
        for(int x = 0; x <= this.maxRecipeSize - this.recipeWidth; ++x)
            for(int y = 0; y <= this.maxRecipeSize - this.recipeHeight; ++y)
                if(this.checkMatch(par1InventoryCrafting, x, y, true) || this.checkMatch(par1InventoryCrafting, x, y, false))
                    return true;

        return false;
    }

    /**
     * Checks if the region of a crafting inventory is match for the recipe.
     */
	private boolean checkMatch(InventoryCrafting par1InventoryCrafting, int x, int y, boolean mirrored)
    {
        for(int i = 0; i < this.maxRecipeSize; ++i)
        {
            for(int j = 0; j < this.maxRecipeSize; ++j)
            {
                int var7 = i - x;
                int var8 = j - y;
                ItemStack stackFound = null;

                if(var7 >= 0 && var8 >= 0 && var7 < this.recipeWidth && var8 < this.recipeHeight)
                    if(mirrored)
                        stackFound = this.recipeItems[this.recipeWidth - var7 - 1 + var8 * this.recipeWidth];
                    else
                        stackFound = this.recipeItems[var7 + var8 * this.recipeWidth];

                ItemStack stackExcpected = par1InventoryCrafting.getStackInRowAndColumn(i, j);

                if(stackFound == null && stackExcpected == null)
                	continue;
                if((stackExcpected == null && stackFound != null) || (stackExcpected != null && stackFound == null))
                    return false;
                if(stackFound.getItem() != stackExcpected.getItem())
                    return false;
                if(stackFound.getItemDamage() != 32767 && stackFound.getItemDamage() != stackExcpected.getItemDamage())
                    return false;
            }
        }

        return true;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Override
	public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting)
    {
        return this.getRecipeOutput().copy();
    }

    /**
     * Returns the size of the recipe area
     */
    @Override
	public int getRecipeSize()
    {
        return this.recipeWidth * this.recipeHeight;
    }
}

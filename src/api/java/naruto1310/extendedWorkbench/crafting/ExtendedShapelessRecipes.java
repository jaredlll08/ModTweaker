package naruto1310.extendedWorkbench.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ExtendedShapelessRecipes implements IExtendedRecipe
{
    private int maxRecipeSize = 6;

    /** Is the ItemStack that you get when craft the recipe. */
    private final ItemStack recipeOutput;

    /** Is a List of ItemStack that composes the recipe. */
    private final List<ItemStack> recipeItems;

    public ExtendedShapelessRecipes(ItemStack par1ItemStack, List<ItemStack> par2List)
    {
        this.recipeOutput = par1ItemStack;
        this.recipeItems = par2List;
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
        ArrayList<ItemStack> var2 = new ArrayList<ItemStack>(this.recipeItems);

        for (int var3 = 0; var3 < this.maxRecipeSize; ++var3)
        {
            for (int var4 = 0; var4 < this.maxRecipeSize; ++var4)
            {
                ItemStack var5 = par1InventoryCrafting.getStackInRowAndColumn(var4, var3);

                if (var5 != null)
                {
                    boolean var6 = false;
                    Iterator<ItemStack> var7 = var2.iterator();

                    while (var7.hasNext())
                    {
                        ItemStack var8 = var7.next();

                        if (var5 == var8 && (var8.getItemDamage() == 32767 || var5.getItemDamage() == var8.getItemDamage()))
                        {
                            var6 = true;
                            var2.remove(var8);
                            break;
                        }
                    }

                    if (!var6)
                    {
                        return false;
                    }
                }
            }
        }

        return var2.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Override
	public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting)
    {
        return this.recipeOutput.copy();
    }

    /**
     * Returns the size of the recipe area
     */
    @Override
	public int getRecipeSize()
    {
        return this.recipeItems.size();
    }
}

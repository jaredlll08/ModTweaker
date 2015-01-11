package naruto1310.extendedWorkbench.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ExtendedCraftingManager
{
    /** The static instance of this class */
    private static final ExtendedCraftingManager instance = new ExtendedCraftingManager();

    /** A list of all the recipes added */
    private List<IRecipe> recipes = new ArrayList<IRecipe>();

    /**
     * Returns the static instance of this class
     */
    public static final ExtendedCraftingManager getInstance()
    {
        return instance;
    }

    private void addExtendedRecipe(ItemStack par1ItemStack, Object ... par2ArrayOfObj)
    {
        String var3 = "";
        int var4 = 0;
        int var5 = 0;
        int var6 = 0;

        if(par2ArrayOfObj[var4] instanceof String[])
        {
            String[] var7 =((String[])par2ArrayOfObj[var4++]);

            for(int var8 = 0; var8 < var7.length; ++var8)
            {
                String var9 = var7[var8];
                ++var6;
                var5 = var9.length();
                var3 = var3 + var9;
            }
        }
        else
        {
            while(par2ArrayOfObj[var4] instanceof String)
            {
                String var11 =(String)par2ArrayOfObj[var4++];
                ++var6;
                var5 = var11.length();
                var3 = var3 + var11;
            }
        }

        HashMap<Character, ItemStack> var12;

        for(var12 = new HashMap<Character, ItemStack>(); var4 < par2ArrayOfObj.length; var4 += 2)
        {
            Character var13 =(Character)par2ArrayOfObj[var4];
            ItemStack var14 = null;

            if(par2ArrayOfObj[var4 + 1] instanceof Item)
                var14 = new ItemStack((Item)par2ArrayOfObj[var4 + 1], 1, 32767);
            if(par2ArrayOfObj[var4 + 1] instanceof Block)
                var14 = new ItemStack((Block)par2ArrayOfObj[var4 + 1], 1, 32767);
            if(par2ArrayOfObj[var4 + 1] instanceof ItemStack)
                var14 =(ItemStack)par2ArrayOfObj[var4 + 1];

            var12.put(var13, var14);
        }

        ItemStack[] var15 = new ItemStack[var5 * var6];

        for(int var16 = 0; var16 < var5 * var6; ++var16)
        {
            char var10 = var3.charAt(var16);

            if(var12.containsKey(Character.valueOf(var10)))
                var15[var16] = var12.get(Character.valueOf(var10)).copy();
            else
                var15[var16] = null;
        }

        this.recipes.add(new ExtendedShapedRecipes(var5, var6, var15, par1ItemStack));
    }

    private void addExtendedShapelessRecipe(ItemStack par1ItemStack, Object ... par2ArrayOfObj)
    {
        ArrayList<ItemStack> var3 = new ArrayList<ItemStack>();
        Object[] var4 = par2ArrayOfObj;
        int var5 = par2ArrayOfObj.length;

        for(int var6 = 0; var6 < var5; var6++)
        {
            Object var7 = var4[var6];

            if(var7 instanceof Item)
                var3.add(new ItemStack((Item)var7, 1, 32767));
            else if(var7 instanceof Block)
                var3.add(new ItemStack((Block)var7, 1, 32767));
            else if(var7 instanceof ItemStack)
            	var3.add(((ItemStack)var7).copy());
            else throw new RuntimeException("Invalid shapeless recipy!");
        }

        this.recipes.add(new ExtendedShapelessRecipes(par1ItemStack, var3));
    }

    public ItemStack findMatchingRecipe(InventoryCrafting inv, World world)
    {
        for(int i = 0; i < this.recipes.size(); i++)
        {
            IRecipe recipe = this.recipes.get(i);
            
            if(recipe.matches(inv, world))
            	return recipe.getCraftingResult(inv);
        }

        return null;
    }

    public List<IRecipe> getRecipeList()
    {
        return this.recipes;
    }

    public static void addRecipe(ItemStack output, Object ... input)
    {
        ExtendedCraftingManager.getInstance().addExtendedRecipe(output, input);
    }

    public static void addShapelessRecipe(ItemStack output, Object ... input)
    {
        ExtendedCraftingManager.getInstance().addExtendedShapelessRecipe(output, input);
    }
}

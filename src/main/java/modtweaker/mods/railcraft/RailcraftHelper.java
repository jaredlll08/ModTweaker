package modtweaker.mods.railcraft;

import java.lang.reflect.Constructor;
import java.util.List;

import mods.railcraft.api.crafting.IBlastFurnaceRecipe;
import mods.railcraft.api.crafting.ICokeOvenRecipe;
import mods.railcraft.api.crafting.IRockCrusherRecipe;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidStack;

public class RailcraftHelper {
    public static List<? extends IBlastFurnaceRecipe> furnace = null;
    public static List<? extends ICokeOvenRecipe> oven = null;
    public static List<? extends IRockCrusherRecipe> crusher = null;
    public static List<IRecipe> rolling = null;
    static {
        try {
            furnace = RailcraftCraftingManager.blastFurnace.getRecipes();
            oven = RailcraftCraftingManager.cokeOven.getRecipes();
            crusher = RailcraftCraftingManager.rockCrusher.getRecipes();
            rolling = RailcraftCraftingManager.rollingMachine.getRecipeList();
        } catch (Exception e) {}
    }

    private RailcraftHelper() {}

    public static IBlastFurnaceRecipe getBlastFurnaceRecipe(ItemStack input, boolean matchDamage, boolean matchNBT, int cookTime, ItemStack output) {
        try {
            Class clazz = Class.forName("mods.railcraft.common.util.crafting.BlastFurnaceCraftingManager$BlastFurnaceRecipe");
            Constructor constructor = clazz.getDeclaredConstructor(ItemStack.class, boolean.class, boolean.class, int.class, ItemStack.class);
            constructor.setAccessible(true);
            return (IBlastFurnaceRecipe) constructor.newInstance(input, matchDamage, matchNBT, cookTime, output);
        } catch (Exception e) {
            throw new NullPointerException("Failed to instantiate BlastFurnaceRecipe");
        }
    }

    public static ICokeOvenRecipe getCokeOvenRecipe(ItemStack input, boolean matchDamage, boolean matchNBT, ItemStack output, FluidStack fluidOutput, int cookTime) {
        try {
            Class clazz = Class.forName("mods.railcraft.common.util.crafting.CokeOvenCraftingManager$CokeOvenRecipe");
            Constructor constructor = clazz.getDeclaredConstructor(ItemStack.class, boolean.class, boolean.class, ItemStack.class, FluidStack.class, int.class);
            constructor.setAccessible(true);
            return (ICokeOvenRecipe) constructor.newInstance(input, matchDamage, matchNBT, output, fluidOutput, cookTime);
        } catch (Exception e) {
            throw new NullPointerException("Failed to instantiate CokeOvenRecipe");
        }
    }

    public static IRockCrusherRecipe getRockCrusherRecipe(ItemStack stack, boolean matchDamage, boolean matchNBT) {
        try {
            Class clazz = Class.forName("mods.railcraft.common.util.crafting.RockCrusherCraftingManager$CrusherRecipe");
            Constructor constructor = clazz.getDeclaredConstructor(ItemStack.class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            return (IRockCrusherRecipe) constructor.newInstance(stack, matchDamage, matchNBT);
        } catch (Exception e) {
            throw new NullPointerException("Failed to instantiate CrusherRecipe");
        }
    }
}

package modtweaker2.mods.railcraft;

import java.util.List;

import mods.railcraft.api.crafting.IBlastFurnaceRecipe;
import mods.railcraft.api.crafting.ICokeOvenRecipe;
import mods.railcraft.api.crafting.IRockCrusherRecipe;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import mods.railcraft.common.util.crafting.BlastFurnaceCraftingManager.BlastFurnaceRecipe;
import mods.railcraft.common.util.crafting.CokeOvenCraftingManager.CokeOvenRecipe;
import mods.railcraft.common.util.crafting.RockCrusherCraftingManager.CrusherRecipe;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.ReflectionHelper;
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
        } catch (Exception e) { LogHelper.logError("Error in RailcraftHelper", e); }
    }

    private RailcraftHelper() {}

    public static IBlastFurnaceRecipe getBlastFurnaceRecipe(ItemStack input, boolean matchDamage, boolean matchNBT, int cookTime, ItemStack output) {
        return ReflectionHelper.getInstance(ReflectionHelper.getConstructor(BlastFurnaceRecipe.class,ItemStack.class, boolean.class, boolean.class, int.class, ItemStack.class),
                input,
                matchDamage,
                matchNBT,
                cookTime,
                output);
    }

    public static ICokeOvenRecipe getCokeOvenRecipe(ItemStack input, boolean matchDamage, boolean matchNBT, ItemStack output, FluidStack fluidOutput, int cookTime) {
        return ReflectionHelper.getInstance(ReflectionHelper.getConstructor(CokeOvenRecipe.class, ItemStack.class, boolean.class, boolean.class, ItemStack.class, FluidStack.class, int.class),
                input,
                matchDamage,
                matchNBT,
                output,
                fluidOutput,
                cookTime);
    }

    public static IRockCrusherRecipe getRockCrusherRecipe(ItemStack stack, boolean matchDamage, boolean matchNBT) {
        return ReflectionHelper.getInstance(ReflectionHelper.getConstructor(CrusherRecipe.class, ItemStack.class, boolean.class, boolean.class),
                stack,
                matchDamage,
                matchNBT);
    }
}

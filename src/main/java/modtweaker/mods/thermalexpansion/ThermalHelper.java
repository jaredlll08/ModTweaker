package modtweaker.mods.thermalexpansion;

import static modtweaker.helpers.ReflectionHelper.getConstructor;
import static modtweaker.helpers.ReflectionHelper.getStaticObject;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import thermalexpansion.util.crafting.SmelterManager.RecipeSmelter;
import thermalexpansion.util.crafting.TransposerManager.RecipeTransposer;
import cofh.lib.inventory.ComparableItemStackSafe;

public class ThermalHelper {
    private static Map<List, RecipeSmelter> smelter;
    public static Set<ComparableItemStackSafe> smelterValid;
    private static Map crucible;

    private static Map<List, RecipeTransposer> transposerFill;
    private static Map<ComparableItemStackSafe, RecipeTransposer> transposerEmpty;
    public static Set<ComparableItemStackSafe> transposerValid;

    public static Constructor smelterRecipe;
    public static Constructor transposerRecipe;

    static {
        try {
            smelterRecipe = getConstructor("thermalexpansion.util.crafting.SmelterManager$RecipeSmelter", ItemStack.class, ItemStack.class, ItemStack.class, ItemStack.class, int.class, int.class);
            transposerRecipe = getConstructor("thermalexpansion.util.crafting.TransposerManager$RecipeTransposer", ItemStack.class, ItemStack.class, FluidStack.class, int.class, int.class);
        } catch (Exception e) {}
    }

    /** Need to perform this reflection on the fly as the map is ALWAYS changing, thanks to the way that te handles stuff */
    public static Map<List, RecipeSmelter> getSmelterMap() {
        try {
            smelter = getStaticObject(Class.forName("thermalexpansion.util.crafting.SmelterManager"), "recipeMap");
            smelterValid = getStaticObject(Class.forName("thermalexpansion.util.crafting.SmelterManager"), "validationSet");
        } catch (Exception e) {}

        return smelter;
    }

    public static Map<List, RecipeTransposer> getFillMap() {
        try {
            transposerFill = getStaticObject(Class.forName("thermalexpansion.util.crafting.TransposerManager"), "recipeMapFill");
            transposerValid = getStaticObject(Class.forName("thermalexpansion.util.crafting.TransposerManager"), "validationSet");
        } catch (Exception e) {}

        return transposerFill;
    }

    public static Map<ComparableItemStackSafe, RecipeTransposer> getExtractMap() {
        try {
            transposerEmpty = getStaticObject(Class.forName("thermalexpansion.util.crafting.TransposerManager"), "recipeMapExtraction");
            transposerValid = getStaticObject(Class.forName("thermalexpansion.util.crafting.TransposerManager"), "validationSet");
        } catch (Exception e) {}

        return transposerEmpty;
    }

    public static boolean removeCrucibleRecipe(ItemStack input) {
        try {
            crucible = getStaticObject(Class.forName("thermalexpansion.util.crafting.CrucibleManager"), "recipeMap");
        } catch (Exception e) {}

        return crucible.remove(new ComparableItemStackSafe(input)) != null;
    }

    public static Object getTERecipe(Constructor constructor, Object... items) {
        try {
            return constructor.newInstance(items);
        } catch (Exception e) {}

        return null;
    }
}

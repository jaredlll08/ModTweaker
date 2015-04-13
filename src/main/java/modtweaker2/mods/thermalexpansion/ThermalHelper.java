package modtweaker2.mods.thermalexpansion;

import static modtweaker2.helpers.ReflectionHelper.getConstructor;
import static modtweaker2.helpers.ReflectionHelper.getStaticObject;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Set;

import appeng.api.AEApi;
import appeng.core.Registration;
import appeng.core.features.registries.InscriberRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import cofh.lib.inventory.ComparableItemStackSafe;
import cofh.thermalexpansion.util.crafting.SmelterManager.RecipeSmelter;
import cofh.thermalexpansion.util.crafting.TransposerManager.RecipeTransposer;

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
            smelterRecipe = getConstructor("cofh.thermalexpansion.util.crafting.SmelterManager$RecipeSmelter", ItemStack.class, ItemStack.class, ItemStack.class, ItemStack.class, int.class, int.class);
            transposerRecipe = getConstructor("cofh.thermalexpansion.util.crafting.TransposerManager$RecipeTransposer", ItemStack.class, ItemStack.class, FluidStack.class, int.class, int.class);
        } catch (Exception e) {}
    }

    /** Need to perform this reflection on the fly as the map is ALWAYS changing, thanks to the way that te handles stuff */
    public static Map<List, RecipeSmelter> getSmelterMap() {
        try {
            smelter = getStaticObject(Class.forName("cofh.thermalexpansion.util.crafting.SmelterManager"), "recipeMap");
            smelterValid = getStaticObject(Class.forName("cofh.thermalexpansion.util.crafting.SmelterManager"), "validationSet");
        } catch (Exception e) {}

        return smelter;
    }

    public static Map<List, RecipeTransposer> getFillMap() {
        try {
            transposerFill = getStaticObject(Class.forName("cofh.thermalexpansion.util.crafting.TransposerManager"), "recipeMapFill");
            transposerValid = getStaticObject(Class.forName("cofh.thermalexpansion.util.crafting.TransposerManager"), "validationSet");
        } catch (Exception e) {}

        return transposerFill;
    }

    public static Map<ComparableItemStackSafe, RecipeTransposer> getExtractMap() {
        try {
            transposerEmpty = getStaticObject(Class.forName("cofh.thermalexpansion.util.crafting.TransposerManager"), "recipeMapExtraction");
            transposerValid = getStaticObject(Class.forName("cofh.thermalexpansion.util.crafting.TransposerManager"), "validationSet");
        } catch (Exception e) {}

        return transposerEmpty;
    }

    public static boolean removeCrucibleRecipe(ItemStack input) {
        try {
            crucible = getStaticObject(Class.forName("cofh.thermalexpansion.util.crafting.CrucibleManager"), "recipeMap");
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

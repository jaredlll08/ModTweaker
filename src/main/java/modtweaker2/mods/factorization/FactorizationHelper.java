package modtweaker2.mods.factorization;

import static modtweaker2.helpers.ReflectionHelper.getStaticObject;

import java.util.List;

import modtweaker2.helpers.ReflectionHelper;
import net.minecraft.item.ItemStack;

public class FactorizationHelper {
    public static List lacerator = null;
    public static List slag = null;
    public static List crystallizer = null;

    static {
        try {
            lacerator = getStaticObject(Class.forName("factorization.oreprocessing.TileEntityGrinder"), "recipes");
            slag = getStaticObject(Class.forName("factorization.oreprocessing.TileEntitySlagFurnace$SlagRecipes"), "smeltingResults");
            crystallizer = getStaticObject(Class.forName("factorization.oreprocessing.TileEntityCrystallizer"), "recipes");
        } catch (Exception e) {}
    }

    private FactorizationHelper() {}

    @SuppressWarnings("unchecked")
    public static Object getLaceratorRecipe(ItemStack input, ItemStack output, float probability) {
        return ReflectionHelper.getInstance(ReflectionHelper.getConstructor("factorization.oreprocessing.TileEntityGrinder$GrinderRecipe", Object.class, ItemStack.class, float.class),
                input,
                output,
                probability);
    }

    @SuppressWarnings("unchecked")
    public static Object getSlagFurnaceRecipe(ItemStack input, float chance1, ItemStack output1, float chance2, ItemStack output2) {
        return ReflectionHelper.getInstance(ReflectionHelper.getConstructor("factorization.oreprocessing.TileEntitySlagFurnace$SmeltingResult", ItemStack.class, float.class, ItemStack.class, float.class, ItemStack.class),
                input,
                chance1,
                output1,
                chance2,
                output2);
    }

    @SuppressWarnings("unchecked")
    public static Object getCrystallizerRecipe(ItemStack input, ItemStack output, ItemStack solution, float output_count) {
        return ReflectionHelper.getInstance(ReflectionHelper.getConstructor("factorization.oreprocessing.TileEntityCrystallizer$CrystalRecipe", ItemStack.class, ItemStack.class, float.class, ItemStack.class),
                input,
                output,
                output_count,
                solution);
    }
}
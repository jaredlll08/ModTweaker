package modtweaker.mods.factorization;

import static modtweaker.helpers.ReflectionHelper.getStaticObject;

import java.lang.reflect.Constructor;
import java.util.List;

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

    public static Object getLaceratorRecipe(ItemStack input, ItemStack output, float probability) {
        try {
            Class clazz = Class.forName("factorization.oreprocessing.TileEntityGrinder$GrinderRecipe");
            Constructor constructor = clazz.getDeclaredConstructor(Object.class, ItemStack.class, float.class);
            constructor.setAccessible(true);
            return constructor.newInstance(input, output, probability);
        } catch (Exception e) {
            throw new NullPointerException("Failed to instantiate Lacerator Recipe");
        }
    }

    public static Object getSlagFurnaceRecipe(ItemStack input, float chance1, ItemStack output1, float chance2, ItemStack output2) {
        try {
            Class clazz = Class.forName("factorization.oreprocessing.TileEntitySlagFurnace$SmeltingResult");
            Constructor constructor = clazz.getDeclaredConstructor(ItemStack.class, float.class, ItemStack.class, float.class, ItemStack.class);
            constructor.setAccessible(true);
            return constructor.newInstance(input, chance1, output1, chance2, output2);
        } catch (Exception e) {
            throw new NullPointerException("Failed to instantiate SmeltingResult");
        }
    }

    public static Object getCrystallizerRecipe(ItemStack input, ItemStack output, ItemStack solution, float output_count) {
        try {
            Class clazz = Class.forName("factorization.oreprocessing.TileEntityCrystallizer$CrystalRecipe");
            Constructor constructor = clazz.getDeclaredConstructor(ItemStack.class, ItemStack.class, float.class, ItemStack.class);
            constructor.setAccessible(true);
            return constructor.newInstance(input, output, output_count, solution);
        } catch (Exception e) {
            throw new NullPointerException("Failed to instantiate CrystalRecipe");
        }
    }
}
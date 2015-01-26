package modtweaker.mods.forestry;

import java.lang.reflect.Field;
import java.util.HashSet;

import net.minecraftforge.fluids.Fluid;

public class ForestryHelper {

	@SuppressWarnings("unchecked")
	public static void addCarpenterRecipeFluids(Fluid newFluid) {
		Class clazz;
		try {
			clazz = Class.forName("forestry.factory.gadgets.MachineCarpenter$RecipeManager");
			Field field_recipeFluids = clazz.getDeclaredField("recipeFluids");
			field_recipeFluids.setAccessible(true);
			
			HashSet<Fluid> recipeFluids = (HashSet<Fluid>) field_recipeFluids.get(clazz);
			recipeFluids.add(newFluid);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}

package modtweaker.mods.forestry;

import com.blamejared.mtlib.helpers.ReflectionHelper;
import forestry.api.recipes.*;
import forestry.factory.recipes.*;

import java.util.*;

public class ForestryHelper {
	
	public static List<ICarpenterRecipe> carpenter = null;
	public static List<ICentrifugeRecipe> centrifuge = null;
	public static List<IFermenterRecipe> fermenter = null;
	public static List<IMoistenerRecipe> moistener = null;
	public static List<ISqueezerRecipe> squeezer = null;
	public static List<IStillRecipe> still = null;
	public static List<IFabricatorRecipe> fabricator = null;
	public static List<IFabricatorSmeltingRecipe> fabricatorSmelting = null;
	
	
	static {
		try {
			carpenter = new ArrayList<>(ReflectionHelper.getStaticObject(CarpenterRecipeManager.class, "recipes"));
			centrifuge = new ArrayList<>(ReflectionHelper.getStaticObject(CentrifugeRecipeManager.class, "recipes"));
			fermenter = new ArrayList<>(ReflectionHelper.getStaticObject(FermenterRecipeManager.class, "recipes"));
			moistener = new ArrayList<>(ReflectionHelper.getStaticObject(MoistenerRecipeManager.class, "recipes"));
			squeezer = new ArrayList<>(ReflectionHelper.getStaticObject(SqueezerRecipeManager.class, "recipes"));
			still = new ArrayList<>(ReflectionHelper.getStaticObject(SqueezerRecipeManager.class, "recipes"));
			fabricator = new ArrayList<>(ReflectionHelper.getStaticObject(FabricatorRecipeManager.class, "recipes"));
			fabricatorSmelting = new ArrayList<>(ReflectionHelper.getStaticObject(StillRecipeManager.class, "recipes"));
		} catch(Exception e) {
		}
	}
	
}

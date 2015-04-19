package modtweaker2.mods.tfcraft;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.bioxx.tfc.api.Crafting.AnvilManager;
import com.bioxx.tfc.api.Crafting.AnvilRecipe;
import com.bioxx.tfc.api.Crafting.BarrelManager;
import com.bioxx.tfc.api.Crafting.BarrelRecipe;
import com.bioxx.tfc.api.Crafting.KilnCraftingManager;
import com.bioxx.tfc.api.Crafting.KilnRecipe;
import com.bioxx.tfc.api.Crafting.QuernManager;
import com.bioxx.tfc.api.Crafting.QuernRecipe;

public class TFCHelper {

	private TFCHelper(){}
	
	public static List<AnvilRecipe> anvilRecipes = AnvilManager.getInstance().getRecipeList();
	public static List<AnvilRecipe> anvilWeldRecipes = AnvilManager.getInstance().getWeldRecipeList();
	public static List<BarrelRecipe> barrelRecipes = BarrelManager.getInstance().getRecipes();
	public static List<KilnRecipe> kilnRecipes = KilnCraftingManager.getInstance().getRecipeList();
	public static List<QuernRecipe> quernRecipes = QuernManager.getInstance().getRecipes();
	public static List<ItemStack> quernVaildItems = QuernManager.getInstance().getValidItems();
	
}

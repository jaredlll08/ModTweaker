package modtweaker.mods.tconstruct;

import gnu.trove.map.hash.THashMap;
import modtweaker.helpers.ReflectionHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.DryingRecipe;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TConstructHelper {
    public static List<AlloyRecipe> alloys = null;
    public static List<CastingRecipe> basinCasting = null;
    public static List<CastingRecipe> tableCasting = null;
    public static List<MeltingRecipe> smeltingList = null;
    //        public static Map<ItemMetaWrapper, Integer> temperatureList = null;
    public static List<FluidStack> fuelList = new ArrayList<FluidStack>();
    public static Map<String, IModifier> modifiers = new THashMap<String, IModifier>();
    public static Map<String, IModifier> modifiers_clone = null;

    static {
        try {
            alloys = ReflectionHelper.getStaticObject(TinkerRegistry.class, "alloyRegistry");//TinkerRegistry.getAlloys();
            smeltingList = TinkerRegistry.getAllMeltingRecipies();
            basinCasting = TinkerRegistry.getAllBasinCastingRecipes();
            tableCasting = TinkerRegistry.getAllTableCastingRecipes();
            modifiers = ReflectionHelper.getStaticObject(TinkerRegistry.class, "modifiers");
            modifiers_clone = new THashMap<String, IModifier>(modifiers);
            fuelList.addAll(TinkerRegistry.getSmelteryFuels());

        } catch (Exception e) {
        }
    }

    private TConstructHelper() {

    }

    //Returns a Drying Recipe, using reflection as the constructor is not visible
    public static DryingRecipe getDryingRecipe(ItemStack output, RecipeMatch input, int time) {
        return ReflectionHelper.getInstance(ReflectionHelper.getConstructor(DryingRecipe.class, RecipeMatch.class, ItemStack.class, int.class),
                input,
                output,
                time);
    }

}

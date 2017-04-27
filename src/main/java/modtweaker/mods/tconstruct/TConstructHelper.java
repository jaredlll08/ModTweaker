package modtweaker.mods.tconstruct;

import gnu.trove.map.hash.THashMap;
import com.blamejared.mtlib.helpers.ReflectionHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.DryingRecipe;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

import java.util.*;

public class TConstructHelper {
    public static List<AlloyRecipe> alloys = null;
    public static List<ICastingRecipe> basinCasting = null;
    public static List<ICastingRecipe> tableCasting = null;
    public static List<MeltingRecipe> smeltingList = null;
    public static List<DryingRecipe> dryingList = null;

    //        public static Map<ItemMetaWrapper, Integer> temperatureList = null;
    public static Map<FluidStack, Integer> fuelMap = new HashMap<>();
    public static Map<String, IModifier> modifiers = new THashMap<String, IModifier>();
    public static Map<String, IModifier> modifiers_clone = null;
    public static Map<String, FluidStack> entityMeltingRegistry;
    
    static {
        try {
            alloys = ReflectionHelper.getStaticObject(TinkerRegistry.class, "alloyRegistry");
            smeltingList = ReflectionHelper.getStaticObject(TinkerRegistry.class, "meltingRegistry");
            dryingList = ReflectionHelper.getStaticObject(TinkerRegistry.class, "dryingRegistry");
            basinCasting = ReflectionHelper.getStaticObject(TinkerRegistry.class, "basinCastRegistry");
            tableCasting = ReflectionHelper.getStaticObject(TinkerRegistry.class, "tableCastRegistry");
            modifiers = ReflectionHelper.getStaticObject(TinkerRegistry.class, "modifiers");
            entityMeltingRegistry = ReflectionHelper.getStaticObject(TinkerRegistry.class, "entityMeltingRegistry");
            fuelMap = ReflectionHelper.getStaticObject(TinkerRegistry.class, "smelteryFuels");
    
            modifiers_clone = new THashMap<String, IModifier>(modifiers);
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

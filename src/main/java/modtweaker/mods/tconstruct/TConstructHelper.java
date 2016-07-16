package modtweaker.mods.tconstruct;

import modtweaker.helpers.ReflectionHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.DryingRecipe;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

import java.util.ArrayList;
import java.util.List;

public class TConstructHelper {
    public static List<AlloyRecipe> alloys = null;
    public static List<CastingRecipe> basinCasting = null;
    public static List<CastingRecipe> tableCasting = null;
    public static List<MeltingRecipe> smeltingList = null;
    //    public static Map<ItemMetaWrapper, Integer> temperatureList = null;
//    public static Map<ItemMetaWrapper, ItemStack> renderIndex = null;
    public static List<FluidStack> fuelList = new ArrayList<FluidStack>();
    public static List<IModifier> modifiers = new ArrayList<IModifier>();
    public static List<IModifier> modifiers_clone = null;

    static {
        try {
            alloys = TinkerRegistry.getAlloys();
            smeltingList = TinkerRegistry.getAllMeltingRecipies();
//            temperatureList = tconstruct.library.crafting.Smeltery.getTemperatureList();
//            renderIndex = tconstruct.library.crafting.Smeltery.getRenderIndex();
            basinCasting = TinkerRegistry.getAllBasinCastingRecipes();
            tableCasting = TinkerRegistry.getAllTableCastingRecipes();
            modifiers.addAll(TinkerRegistry.getAllModifiers());
            modifiers_clone = new ArrayList<IModifier>(modifiers);
            fuelList.addAll(TinkerRegistry.getSmelteryFuels());

        } catch (Exception e) {
        }
    }

    private TConstructHelper() {
    }

    //Returns a Drying Recipe, using reflection as the constructor is not visible
    public static DryingRecipe getDryingRecipe(ItemStack input, int time, ItemStack output) {
        return ReflectionHelper.getInstance(ReflectionHelper.getConstructor(DryingRecipe.class, ItemStack.class, int.class, ItemStack.class),
                input,
                time,
                output);
    }

}

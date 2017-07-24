package com.blamejared.compat.tconstruct;

import gnu.trove.map.hash.THashMap;
import com.blamejared.mtlib.helpers.ReflectionHelper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.recipe.IRecipeCategory;
import minetweaker.api.entity.IEntityDefinition;
import minetweaker.mods.jei.JEIAddonPlugin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.DryingRecipe;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.plugin.jei.CastingRecipeCategory;
import slimeknights.tconstruct.plugin.jei.CastingRecipeWrapper;

import java.util.*;

/**
 * Created by Jared for 1.10.2.
 * Adapted by Rinart73 on 24.07.17 for 1.11.2
 */
public class TConstructHelper {
    public static List<AlloyRecipe> alloys = null;
    public static LinkedList<ICastingRecipe> basinCasting = null;
    public static LinkedList<ICastingRecipe> tableCasting = null;
    public static List<MeltingRecipe> smeltingList = null;
    public static List<DryingRecipe> dryingList = null;

    public static Map<FluidStack, Integer> fuelMap = new HashMap<>();
    public static Map<String, IModifier> modifiers = new THashMap<>();
    public static Map<ResourceLocation, FluidStack> entityMeltingRegistry;

    //this should bring a 'bridge' between IEntityDefinition and ResourceLocation
    public static Map<String, ResourceLocation> entityLocationsByClassPath = new HashMap<>();

    //allows to add casting recipes to the JEI, it contains images for table and basin
    public static CastingRecipeCategory castingRecipeCategory = null;

    //allows to delete casting recipes from JEI
    public static Map<ICastingRecipe, CastingRecipeWrapper> castingRecipeWrappers = new HashMap<>();


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

            //Get ResourceLocations for Entities class paths and names
            for(ResourceLocation r : EntityList.getEntityNameList()) {
                Class<? extends Entity> clazz = EntityList.getClass(r);
                if(clazz == null) continue;
                entityLocationsByClassPath.put(clazz.getName(), r);
            }

            //Get all CastingRecipeWrapper-s and get CastingRecipes for them
            //It's a terrible and slow way(~4ms), but TConstruct leaves no way to get CastingRecipeWrapper from CastingRecipe
            ArrayList<String> categoryName = new ArrayList<>();
            categoryName.add(CastingRecipeCategory.CATEGORY);
            List<IRecipeCategory> categories = JEIAddonPlugin.recipeRegistry.getRecipeCategories(categoryName);
            if(categories.size() != 0) {
                List<CastingRecipeWrapper> wrappers = JEIAddonPlugin.recipeRegistry.getRecipeWrappers(categories.get(0));
                for(CastingRecipeWrapper wrapper : wrappers) {
                    Object recipe = ReflectionHelper.getObject(wrapper, "recipe");
                    if(recipe instanceof ICastingRecipe)
                        castingRecipeWrappers.put((ICastingRecipe) recipe, wrapper);
                }
            }

        } catch (Exception e) {
        }
    }

    private TConstructHelper() {

    }

    //Returns a Drying Recipe, using reflection as the constructor is not visible
    public static DryingRecipe getDryingRecipe(RecipeMatch input, ItemStack output, int time) {
        return ReflectionHelper.getInstance(ReflectionHelper.getConstructor(DryingRecipe.class, RecipeMatch.class, ItemStack.class, int.class),
                input,
                output,
                time);
    }

    public static ResourceLocation getEntityResourceLocation(IEntityDefinition entity) {
        return entityLocationsByClassPath.get(entity.getId());
    }

    //Returns a CastingRecipeCategory used to add Casting recipes to JEI, using reflection as the constructor is not visible
    public static CastingRecipeCategory getCastingRecipeCategory(IGuiHelper guiHelper) {
        return ReflectionHelper.getInstance(ReflectionHelper.getConstructor(CastingRecipeCategory.class, IGuiHelper.class), guiHelper);
    }
}
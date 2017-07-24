package com.blamejared.compat.tconstruct.handlers;

import com.blamejared.api.annotations.*;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.entity.IEntityDefinition;
import minetweaker.api.item.*;
import minetweaker.api.liquid.ILiquidStack;
import com.blamejared.compat.tconstruct.TConstructHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import slimeknights.tconstruct.plugin.jei.AlloyRecipeCategory;
import slimeknights.tconstruct.plugin.jei.SmeltingRecipeCategory;
import stanhebben.zenscript.annotations.*;
import stanhebben.zenscript.annotations.Optional;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

/**
 * Created by Jared for 1.10.2.
 * Adapted by Rinart73 on 24.07.17 for 1.11.2
 */
@ZenClass("mods.tconstruct.Smeltery")
@Handler("tconstruct")
public class Smeltery {

    public static final String nameFuel = "TConstruct Smeltery - Fuel";
    public static final String nameMelting = "TConstruct Smeltery - Melting";
    public static final String nameAlloy = "TConstruct Smeltery - Alloy";
    public static final String nameEntityMelting = "TConstruct Smeltery - Entity Melting";


    /**********************************************
     * TConstruct Entity Melting Recipes
     **********************************************/

    // Adding a TConstruct Entity Melting recipe
    @ZenMethod
    @Document({"output", "entity"})
    public static void addEntityMelting(ILiquidStack output, IEntityDefinition entity) {
        if(entity == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", nameEntityMelting));
            return;
        }

        ResourceLocation location = TConstructHelper.getEntityResourceLocation(entity);
        if(location == null) {
            LogHelper.logError(String.format("Entity passed to %s Recipe doesn't exist.", nameEntityMelting));
            return;
        }
        MineTweakerAPI.apply(new AddEntityMelting(location, toFluid(output)));
    }

    private static class AddEntityMelting extends BaseMapAddition<ResourceLocation, FluidStack> {

        protected AddEntityMelting(ResourceLocation entityLocation, FluidStack fluid) {
            super("TConstruct Smeltery - Entity Melting", TConstructHelper.entityMeltingRegistry);
            this.recipes.put(entityLocation, fluid);
        }

        @Override
        protected String getRecipeInfo(Map.Entry<ResourceLocation, FluidStack> recipe) {
            return LogHelper.getStackDescription(recipe);
        }
    }


    // Removing a TConstruct Entity Melting recipe
    @ZenMethod
    @Document({"entity"})
    public static void removeEntityMelting(IEntityDefinition entity) {
        ResourceLocation location = TConstructHelper.getEntityResourceLocation(entity);
        if(location == null) {
            LogHelper.logError(String.format("Entity passed to %s Recipe doesn't exist.", nameEntityMelting));
            return;
        }

        Map<ResourceLocation, FluidStack> recipes = new HashMap<>();

        TConstructHelper.entityMeltingRegistry.forEach((key, val) -> {
            if (key.equals(location))
                recipes.put(key, val);
        });

        if(!recipes.isEmpty())
            MineTweakerAPI.apply(new RemoveEntityMelting(recipes));
        else
            LogHelper.logWarning(String.format("No %s recipes found for %s. Command ignored!", nameEntityMelting, location.toString()));
    }

    private static class RemoveEntityMelting extends BaseMapRemoval<ResourceLocation, FluidStack> {

        protected RemoveEntityMelting(Map<ResourceLocation, FluidStack> map) {
            super("TConstruct Smeltery - Entity Melting", TConstructHelper.entityMeltingRegistry, map);
        }

        @Override
        protected String getRecipeInfo(Map.Entry<ResourceLocation, FluidStack> recipe) {
            return LogHelper.getStackDescription(recipe);
        }
    }


    /**********************************************
     * TConstruct Alloy Recipes
     **********************************************/

    // Adding a TConstruct Alloy recipe
    @ZenMethod
    @Document({"output", "input"})
    public static void addAlloy(ILiquidStack output, ILiquidStack[] input) {
        if(input == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", nameAlloy));
            return;
        }

        MineTweakerAPI.apply(new AddAlloy(new AlloyRecipe(toFluid(output), toFluids(input))));
    }

    // Passes the list to the base list implementation, and adds the recipe
    private static class AddAlloy extends BaseListAddition<AlloyRecipe> {

        public AddAlloy(AlloyRecipe recipe) {
            super(nameAlloy, TConstructHelper.alloys);
            this.recipes.add(recipe);
        }

        @Override
        protected String getRecipeInfo(AlloyRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getResult());
        }

        @Override
        public String getJEICategory(AlloyRecipe recipe) {
            return AlloyRecipeCategory.CATEGORY;
        }
    }


    // Removing a TConstruct Alloy recipe
    @ZenMethod
    @Document({"output"})
    public static void removeAlloy(ILiquidStack output) {
        List<AlloyRecipe> recipes = new LinkedList<>();

        for(AlloyRecipe r : TConstructHelper.alloys) {
            if(r != null && matches(output, toILiquidStack(r.getResult()))) {
                recipes.add(r);
            }
        }

        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveAlloy(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipes found for %s. Command ignored!", nameAlloy, output.toString()));
        }

    }

    private static class RemoveAlloy extends BaseListRemoval<AlloyRecipe> {

        public RemoveAlloy(List<AlloyRecipe> recipes) {
            super(nameAlloy, TConstructHelper.alloys, recipes);
        }

        @Override
        protected String getRecipeInfo(AlloyRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getResult());
        }

        @Override
        public String getJEICategory(AlloyRecipe recipe) {
            return AlloyRecipeCategory.CATEGORY;
        }
    }


    /**********************************************
     * TConstruct Melting Recipes
     **********************************************/

    // Adding a TConstruct Melting recipe
    @ZenMethod
    @Document({"output", "input", "temp"})
    public static void addMelting(ILiquidStack output, IIngredient input, @Optional int temp) {
        if(input == null || output == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", nameMelting));
            return;
        }

        List<MeltingRecipe> recipes = new LinkedList<>();

        for(IItemStack in : input.getItems()) {
            if(temp <= 0)
                recipes.add(new MeltingRecipe(RecipeMatch.of(toStack(in), output.getAmount()), toFluid(output)));
            else
                recipes.add(new MeltingRecipe(RecipeMatch.of(toStack(in), output.getAmount()), toFluid(output), temp));
        }

        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new AddMelting(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s recipes could be added for input %s.", nameMelting, input.toString()));
        }
    }

    private static class AddMelting extends BaseListAddition<MeltingRecipe> {

        public AddMelting(List<MeltingRecipe> recipes) {
            super(nameMelting, TConstructHelper.smeltingList, recipes);
        }

        @Override
        public String getRecipeInfo(MeltingRecipe recipe) {
            return LogHelper.getStackDescription(recipe.input);
        }

        @Override
        public String getJEICategory(MeltingRecipe recipe) {
            return SmeltingRecipeCategory.CATEGORY;
        }
    }


    // Removing a TConstruct Melting recipe
    @ZenMethod
    @Document({"input"})
    public static void removeMelting(IItemStack input) {
        List<MeltingRecipe> recipes = new LinkedList<>();

        MeltingRecipe r = TinkerRegistry.getMelting(toStack(input));
        if(r != null) {
            recipes.add(r);
        }

        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveMelting(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", nameMelting, input.toString()));
        }
    }

    private static class RemoveMelting extends BaseListRemoval<MeltingRecipe> {

        public RemoveMelting(List<MeltingRecipe> recipes) {
            super(nameMelting, TConstructHelper.smeltingList, recipes);
        }

        @Override
        public String getRecipeInfo(MeltingRecipe recipe) {
            return LogHelper.getStackDescription(recipe.getResult());
        }

        @Override
        public String getJEICategory(MeltingRecipe recipe) {
            return SmeltingRecipeCategory.CATEGORY;
        }
    }


    /**********************************************
     * TConstruct Fuels
     **********************************************/

    // Adding TConstruct Fuel
    @ZenMethod
    @Document({"liquid", "temp"})
    public static void addFuel(ILiquidStack liquid, @Optional int temp) {
        if(liquid == null) {
            LogHelper.logError(String.format("Required parameters missing for %s Recipe.", nameFuel));
            return;
        }
        Map<FluidStack, Integer> recipes = new HashMap<>();
        recipes.put(toFluid(liquid), temp !=0 ? temp : toFluid(liquid).getFluid().getTemperature());
        MineTweakerAPI.apply(new AddFuel(recipes));
    }

    public static class AddFuel extends BaseMapAddition<FluidStack, Integer> {

        protected AddFuel(Map<FluidStack, Integer> recipes) {
            super(Smeltery.nameFuel, TConstructHelper.fuelMap, recipes);
        }

        @Override
        protected String getRecipeInfo(Map.Entry<FluidStack, Integer> recipe) {
            return LogHelper.getStackDescription(recipe);
        }
    }


    // Removing TConstruct Fuel
    @ZenMethod
    @Document({"input"})
    public static void removeFuel(IIngredient input) {
        Map<FluidStack, Integer> recipes = new HashMap<>();
        for(Map.Entry<FluidStack, Integer> entry : TConstructHelper.fuelMap.entrySet()) {
            if(entry != null && matches(input, toILiquidStack(entry.getKey()))) {
                recipes.put(entry.getKey(), entry.getValue());
            }
        }
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveFuel(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe for %s found. Command ignored!", Smeltery.nameFuel, input.toString()));
        }
    }

    public static class RemoveFuel extends BaseMapRemoval<FluidStack, Integer> {

        protected RemoveFuel(Map<FluidStack, Integer> recipes) {
            super(Smeltery.nameFuel, TConstructHelper.fuelMap, recipes);
        }

        @Override
        protected String getRecipeInfo(Map.Entry<FluidStack, Integer> recipe) {
            return LogHelper.getStackDescription(recipe);
        }
    }
}
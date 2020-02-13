package com.blamejared.compat.tconstruct;

import com.blamejared.ModTweaker;
import com.blamejared.compat.tconstruct.recipes.MeltingRecipeTweaker;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import com.google.common.collect.*;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.*;
import crafttweaker.api.entity.IEntityDefinition;
import crafttweaker.api.item.*;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.oredict.OreDictionary;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.events.TinkerRegisterEvent;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.*;

import java.util.*;
import java.util.stream.Collectors;

@ZenClass("mods.tconstruct.Melting")
@ZenRegister
@ModOnly("tconstruct")
public class Melting {
    
    public static final Multimap<ILiquidStack, IItemStack> REMOVED_RECIPES = LinkedListMultimap.create();
    public static final List<EntityEntry> REMOVED_ENTITIES = new LinkedList<>();
    public static final Map<EntityEntry, FluidStack> ADDED_ENTITIES = new LinkedHashMap<>();
    private static boolean init = false;
    
    private static void init() {
        if(!init) {
            MinecraftForge.EVENT_BUS.register(new Melting());
            init = true;
        }
    }
    
    @ZenMethod
    public static void addRecipe(ILiquidStack output, IIngredient input, @Optional int temp) {
        init();
        ModTweaker.LATE_ADDITIONS.add(new Melting.Add(InputHelper.toFluid(output), input, temp));
    }
    
    
    @ZenMethod
    public static void addEntityMelting(IEntityDefinition entity, ILiquidStack stack) {
        init();
        ModTweaker.LATE_ADDITIONS.add(new AddEntityMelting((EntityEntry) entity.getInternal(), InputHelper.toFluid(stack)));
    }
    
    @ZenMethod
    public static void removeRecipe(ILiquidStack output, @Optional IItemStack input) {
        init();
        CraftTweakerAPI.apply(new Melting.Remove(output, input));
    }
    
    @ZenMethod
    public static void removeEntityMelting(IEntityDefinition entity) {
        init();
        CraftTweakerAPI.apply(new RemoveEntityMelting((EntityEntry) entity.getInternal()));
    }
    
    private static class Add extends BaseAction {
        
        private final FluidStack output;
        private final IIngredient ingredient;
        private final int temp;
        
        public Add(FluidStack output, IIngredient ingredient, int temp) {
            super("Melting");
            this.output = output;
            this.temp = temp;
            this.ingredient = ingredient;
        }
        
        @Override
        public void apply() {
            List<ItemStack> validIngredients = ingredient.getItems().stream().map(CraftTweakerMC::getItemStack).collect(Collectors.toList());
            if (validIngredients.isEmpty()) {
                CraftTweakerAPI.logInfo("Could not find matching items for " + ingredient.toString() + ". Ignoring Melting recipe for " + output.getLocalizedName());
                return;
            }
            RecipeMatch rm;
            if (validIngredients.size() == 1 && validIngredients.get(0).getMetadata() != OreDictionary.WILDCARD_VALUE) {
                rm = RecipeMatch.ofNBT(validIngredients.get(0), output.amount);
            } else {
                rm = RecipeMatch.of(validIngredients, output.amount);
            }
            
            if (temp == 0) {
                TinkerRegistry.registerMelting(new MeltingRecipeTweaker(rm, output));
            } else {
                TinkerRegistry.registerMelting(new MeltingRecipeTweaker(rm, output, temp));
            }
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class AddEntityMelting extends BaseAction {
        
        private EntityEntry entity;
        private FluidStack output;
        
        public AddEntityMelting(EntityEntry entity, FluidStack input) {
            super("EntityMelting");
            this.entity = entity;
            this.output = input;
        }
        
        @Override
        public void apply() {
            TinkerRegistry.registerEntityMelting(entity.getEntityClass(), output);
            ADDED_ENTITIES.put(entity, output);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class Remove extends BaseAction {
        
        private ILiquidStack output;
        private IItemStack input;
        
        protected Remove(ILiquidStack output, IItemStack input) {
            super("Melting");
            this.output = output;
            this.input = input;
        }
        
        @Override
        public void apply() {
            REMOVED_RECIPES.put(output, input);
        }
        
        @Override
        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(output);
        }
    }
    
    private static class RemoveEntityMelting extends BaseAction {
        
        private EntityEntry entity;
        
        protected RemoveEntityMelting(EntityEntry input) {
            super("EntityMelting");
            this.entity = input;
        }
        
        @Override
        public void apply() {
            REMOVED_ENTITIES.add(entity);
        }
        
        @Override
        protected String getRecipeInfo() {
            if(entity == null) {
                return "null";
            }
            return entity.getName();
        }
    }
    
    @SubscribeEvent
    public void onTinkerRegister(TinkerRegisterEvent.MeltingRegisterEvent event) {
        if(event.getRecipe() instanceof MeltingRecipeTweaker) {
            return;
        }
        for(Map.Entry<ILiquidStack, IItemStack> ent : REMOVED_RECIPES.entries()) {
            if(event.getRecipe().getResult().isFluidEqual(((FluidStack) ent.getKey().getInternal()))) {
                if(ent.getValue() != null) {
                    if(event.getRecipe().input.matches(NonNullList.withSize(1, (ItemStack) ent.getValue().getInternal())).isPresent()) {
                        event.setCanceled(true);
                    }
                } else
                    event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public void onTinkerRegister(TinkerRegisterEvent.EntityMeltingRegisterEvent event) {
        boolean remove = true;
        for(Map.Entry<EntityEntry, FluidStack> entry : ADDED_ENTITIES.entrySet()) {
            if(event.getRecipe().equals(entry.getKey().getEntityClass())) {
                remove = false;
                event.setNewFluidStack(entry.getValue());
            }
        }
        if(remove) {
            for(EntityEntry entity : REMOVED_ENTITIES) {
                if(entity.getEntityClass().equals(event.getRecipe())) {
                    event.setCanceled(true);
                }
            }
        }
    }
}

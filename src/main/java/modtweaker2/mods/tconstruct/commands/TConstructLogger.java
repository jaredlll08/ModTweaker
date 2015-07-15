package modtweaker2.mods.tconstruct.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import mantle.utils.ItemMetaWrapper;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.StringHelper;
import modtweaker2.mods.tconstruct.TConstructHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import tconstruct.library.crafting.AlloyMix;
import tconstruct.library.crafting.CastingRecipe;
import tconstruct.library.crafting.DryingRackRecipes;
import tconstruct.library.crafting.DryingRackRecipes.DryingRecipe;

public class TConstructLogger implements ICommandFunction {
    private static final List<String> validArguments = new LinkedList<String>();
    
    static {
        validArguments.add("casting");
        validArguments.add("drying");
        validArguments.add("smeltery");
    }
    
    @Override
    public void execute(String[] arguments, IPlayer player) {
        List<String> args = StringHelper.toLowerCase(Arrays.asList(arguments));
        
        if(!validArguments.containsAll(args)) {
            if(player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("Invalid arguments for command. Valid arguments: " + StringHelper.join(validArguments, ", ")));
            }
        } else {
            if(args.isEmpty() || args.contains("casting")) {
                for(CastingRecipe recipe : TConstructHelper.basinCasting) {
                    MineTweakerAPI.logCommand(String.format("mods.tconstruct.Casting.addBasinRecipe(%s, %s, %s, %s, %d);",
                            LogHelper.getStackDescription(recipe.output),
                            LogHelper.getStackDescription(recipe.castingMetal),
                            LogHelper.getStackDescription(recipe.cast),
                            recipe.consumeCast,
                            recipe.coolTime));
                }
                
                for(CastingRecipe recipe : TConstructHelper.tableCasting) {
                    MineTweakerAPI.logCommand(String.format("mods.tconstruct.Casting.addTableRecipe(%s, %s, %s, %s, %d);",
                            LogHelper.getStackDescription(recipe.output),
                            LogHelper.getStackDescription(recipe.castingMetal),
                            LogHelper.getStackDescription(recipe.cast),
                            recipe.consumeCast,
                            recipe.coolTime));
                }
            }
            
            if(args.isEmpty() || args.contains("drying")) {
                for(DryingRecipe recipe : DryingRackRecipes.recipes) {
                    MineTweakerAPI.logCommand(String.format("mods.tconstruct.Drying.addRecipe(%s, %s, %d);",
                            LogHelper.getStackDescription(recipe.input),
                            LogHelper.getStackDescription(recipe.result),
                            recipe.time));
                }
            }
            
            if(args.isEmpty() || args.contains("smeltery")) {
                
                for(Entry<ItemMetaWrapper, FluidStack> recipe : TConstructHelper.smeltingList.entrySet()) {
                    int temperature = TConstructHelper.temperatureList.get(recipe.getKey());
                    ItemStack renderItem = TConstructHelper.renderIndex.get(recipe.getKey());
                    
                    MineTweakerAPI.logCommand(String.format("mods.tconstruct.Smeltery.addMelting(%s, %s, %d, %s);",
                            LogHelper.getStackDescription(new ItemStack(recipe.getKey().item, 1, recipe.getKey().meta)),
                            LogHelper.getStackDescription(recipe.getValue()),
                            temperature,
                            LogHelper.getStackDescription(renderItem)));
                }
                
                for(AlloyMix recipe : TConstructHelper.alloys) {
                    MineTweakerAPI.logCommand(String.format("mods.tconstruct.Smeltery.addAlloy(%s, %s);",
                            LogHelper.getStackDescription(recipe.result),
                            LogHelper.getListDescription(recipe.mixers)));
                }
                
                for(Entry<Fluid, Integer[]> fuel : TConstructHelper.fuelList.entrySet()) {
                    MineTweakerAPI.logCommand(String.format("mods.tconstruct.Smeltery.addFuel(%s, %d, %d);",
                            LogHelper.getStackDescription(new FluidStack(fuel.getKey(), 1)),
                            fuel.getValue()[0],
                            fuel.getValue()[1]));
                }
            }
            
            if (player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
            }
        }
    }
}

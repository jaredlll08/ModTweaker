package modtweaker2.mods.thermalexpansion.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.StringHelper;
import modtweaker2.mods.thermalexpansion.ThermalHelper;
import cofh.thermalexpansion.util.crafting.SmelterManager.RecipeSmelter;
import cofh.thermalexpansion.util.crafting.TransposerManager.RecipeTransposer;

public class ThermalExpansionLogger implements ICommandFunction {
    private static final List<String> validArguments = new LinkedList<String>();
    
    static {
        validArguments.add("crucible");
        validArguments.add("furnace");
        validArguments.add("pulverizer");
        validArguments.add("sawmill");
        validArguments.add("transposer");
    }
    
    @Override
    public void execute(String[] arguments, IPlayer player) {
        List<String> args = StringHelper.toLowerCase(Arrays.asList(arguments));
        
        if(!validArguments.containsAll(args)) {
            if(player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("Invalid arguments for command. Valid arguments: " + StringHelper.join(validArguments, ", ")));
            }
        } else {
            if(args.isEmpty() || args.contains("smelter")) {
                for(RecipeSmelter recipe : ThermalHelper.getSmelterMap().values()) {
                    if(recipe.getSecondaryOutput() != null) {
                        MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Smelter.addRecipe(%d, %s, %s, %s, %s, %d);",
                                recipe.getEnergy(),
                                InputHelper.getStackDescription(recipe.getPrimaryInput()),
                                InputHelper.getStackDescription(recipe.getSecondaryInput()),
                                InputHelper.getStackDescription(recipe.getPrimaryOutput()),
                                InputHelper.getStackDescription(recipe.getSecondaryOutput()),
                                recipe.getSecondaryOutputChance()));
                    } else {
                        MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Smelter.addRecipe(%d, %s, %s, %s);",
                                recipe.getEnergy(),
                                InputHelper.getStackDescription(recipe.getPrimaryInput()),
                                InputHelper.getStackDescription(recipe.getSecondaryInput()),
                                InputHelper.getStackDescription(recipe.getPrimaryOutput())));                        
                    }
                }
            }
            
            if(args.isEmpty() || args.contains("transposer")) {
                for(RecipeTransposer recipe : ThermalHelper.getFillMap().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Transposer.addFillRecipe(%d, %s, %s, %s);",
                            recipe.getEnergy(),
                            InputHelper.getStackDescription(recipe.getInput()),
                            InputHelper.getStackDescription(recipe.getOutput()),
                            InputHelper.getStackDescription(recipe.getFluid())));  
                }
                
                for(RecipeTransposer recipe : ThermalHelper.getExtractMap().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Transposer.addExtractRecipe(%d, %s, %s, %s, %d);",
                            recipe.getEnergy(),
                            InputHelper.getStackDescription(recipe.getInput()),
                            InputHelper.getStackDescription(recipe.getOutput()),
                            InputHelper.getStackDescription(recipe.getFluid()),
                            recipe.getChance()));
                }
            }
            
            if (player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
            }
        }
    }
}

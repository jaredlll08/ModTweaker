package modtweaker2.mods.thermalexpansion.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.StringHelper;
import cofh.thermalexpansion.util.crafting.CrucibleManager;
import cofh.thermalexpansion.util.crafting.CrucibleManager.RecipeCrucible;
import cofh.thermalexpansion.util.crafting.FurnaceManager;
import cofh.thermalexpansion.util.crafting.FurnaceManager.RecipeFurnace;
import cofh.thermalexpansion.util.crafting.InsolatorManager;
import cofh.thermalexpansion.util.crafting.InsolatorManager.RecipeInsolator;
import cofh.thermalexpansion.util.crafting.PulverizerManager;
import cofh.thermalexpansion.util.crafting.PulverizerManager.RecipePulverizer;
import cofh.thermalexpansion.util.crafting.SawmillManager;
import cofh.thermalexpansion.util.crafting.SawmillManager.RecipeSawmill;
import cofh.thermalexpansion.util.crafting.SmelterManager;
import cofh.thermalexpansion.util.crafting.SmelterManager.RecipeSmelter;
import cofh.thermalexpansion.util.crafting.TransposerManager;
import cofh.thermalexpansion.util.crafting.TransposerManager.RecipeTransposer;

public class ThermalExpansionLogger implements ICommandFunction {
    private static final List<String> validArguments = new LinkedList<String>();
    
    static {
        validArguments.add("Crucible");
        validArguments.add("Furnace");
        validArguments.add("Insolator");
        validArguments.add("Pulverizer");
        validArguments.add("Sawmill");
        validArguments.add("Smelter");
        validArguments.add("Transposer");
    }
    
    @Override
    public void execute(String[] arguments, IPlayer player) {
        List<String> args = StringHelper.toLowerCase(Arrays.asList(arguments));
        
        if(!validArguments.containsAll(args)) {
            if(player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("Invalid arguments for command. Valid arguments: " + StringHelper.join(validArguments, ", ")));
            }
        } else {
            if(args.isEmpty() || args.contains("Crucible")) {
                for(RecipeCrucible recipe : CrucibleManager.getRecipeList()) {
                    MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Crucible.addRecipe(%d, %s, %s);",
                            recipe.getEnergy(),
                            LogHelper.getStackDescription(recipe.getInput()),
                            LogHelper.getStackDescription(recipe.getOutput())));
                }
            }
            
            if(args.isEmpty() || args.contains("Furnace")) {
                for(RecipeFurnace recipe : FurnaceManager.getRecipeList()) {
                    MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Furnace.addRecipe(%d, %s, %s);",
                            recipe.getEnergy(),
                            LogHelper.getStackDescription(recipe.getInput()),
                            LogHelper.getStackDescription(recipe.getOutput())));
                }
            }
            
            if(args.isEmpty() || args.contains("Insolator")) {
                for(RecipeInsolator recipe : InsolatorManager.getRecipeList()) {
                    if(recipe.getSecondaryOutput() != null) {
                        MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Insolator.addRecipe(%d, %s, %s, %s, %s, %d);",
                                recipe.getEnergy(),
                                LogHelper.getStackDescription(recipe.getPrimaryInput()),
                                LogHelper.getStackDescription(recipe.getSecondaryInput()),
                                LogHelper.getStackDescription(recipe.getPrimaryOutput()),
                                LogHelper.getStackDescription(recipe.getSecondaryOutput()),
                                recipe.getSecondaryOutputChance()));
                    } else {
                        MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Insolator.addRecipe(%d, %s, %s, %s);",
                                recipe.getEnergy(),
                                LogHelper.getStackDescription(recipe.getPrimaryInput()),
                                LogHelper.getStackDescription(recipe.getSecondaryInput()),
                                LogHelper.getStackDescription(recipe.getPrimaryOutput())));                        
                    }
                }
            }
            
            if(args.isEmpty() || args.contains("Pulverizer")) {
                for(RecipePulverizer recipe : PulverizerManager.getRecipeList()) {
                    if(recipe.getSecondaryOutput() != null) {
                        MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Pulverizer.addRecipe(%d, %s, %s, %s, %d);",
                                recipe.getEnergy(),
                                LogHelper.getStackDescription(recipe.getInput()),
                                LogHelper.getStackDescription(recipe.getPrimaryOutput()),
                                LogHelper.getStackDescription(recipe.getSecondaryOutput()),
                                recipe.getSecondaryOutputChance()));
                    } else {
                        MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Pulverizer.addRecipe(%d, %s, %s);",
                                recipe.getEnergy(),
                                LogHelper.getStackDescription(recipe.getInput()),
                                LogHelper.getStackDescription(recipe.getPrimaryOutput())));                        
                    }
                }
            }
            
            if(args.isEmpty() || args.contains("Sawmill")) {
                for(RecipeSawmill recipe : SawmillManager.getRecipeList()) {
                    if(recipe.getSecondaryOutput() != null) {
                        MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Sawmill.addRecipe(%d, %s, %s, %s, %d);",
                                recipe.getEnergy(),
                                LogHelper.getStackDescription(recipe.getInput()),
                                LogHelper.getStackDescription(recipe.getPrimaryOutput()),
                                LogHelper.getStackDescription(recipe.getSecondaryOutput()),
                                recipe.getSecondaryOutputChance()));
                    } else {
                        MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Sawmill.addRecipe(%d, %s, %s);",
                                recipe.getEnergy(),
                                LogHelper.getStackDescription(recipe.getInput()),
                                LogHelper.getStackDescription(recipe.getPrimaryOutput())));                        
                    }
                }
            }
            
            if(args.isEmpty() || args.contains("Smelter")) {
                for(RecipeSmelter recipe : SmelterManager.getRecipeList()) {
                    if(recipe.getSecondaryOutput() != null) {
                        MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Smelter.addRecipe(%d, %s, %s, %s, %s, %d);",
                                recipe.getEnergy(),
                                LogHelper.getStackDescription(recipe.getPrimaryInput()),
                                LogHelper.getStackDescription(recipe.getSecondaryInput()),
                                LogHelper.getStackDescription(recipe.getPrimaryOutput()),
                                LogHelper.getStackDescription(recipe.getSecondaryOutput()),
                                recipe.getSecondaryOutputChance()));
                    } else {
                        MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Smelter.addRecipe(%d, %s, %s, %s);",
                                recipe.getEnergy(),
                                LogHelper.getStackDescription(recipe.getPrimaryInput()),
                                LogHelper.getStackDescription(recipe.getSecondaryInput()),
                                LogHelper.getStackDescription(recipe.getPrimaryOutput())));                        
                    }
                }
            }
            
            if(args.isEmpty() || args.contains("Transposer")) {
                for(RecipeTransposer recipe : TransposerManager.getFillRecipeList()) {
                    MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Transposer.addFillRecipe(%d, %s, %s, %s);",
                            recipe.getEnergy(),
                            LogHelper.getStackDescription(recipe.getInput()),
                            LogHelper.getStackDescription(recipe.getOutput()),
                            LogHelper.getStackDescription(recipe.getFluid())));  
                }
                
                for(RecipeTransposer recipe : TransposerManager.getExtractionRecipeList()) {
                    MineTweakerAPI.logCommand(String.format("mods.thermalexpansion.Transposer.addExtractRecipe(%d, %s, %s, %s, %d);",
                            recipe.getEnergy(),
                            LogHelper.getStackDescription(recipe.getInput()),
                            LogHelper.getStackDescription(recipe.getOutput()),
                            LogHelper.getStackDescription(recipe.getFluid()),
                            recipe.getChance()));
                }
            }
            
            if (player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
            }
        }
    }
}

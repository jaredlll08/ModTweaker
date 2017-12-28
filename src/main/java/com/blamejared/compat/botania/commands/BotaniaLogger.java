package com.blamejared.compat.botania.commands;

import java.util.Arrays;
import java.util.List;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.helpers.StringHelper;
import com.google.common.collect.ImmutableList;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.mc1120.commands.CraftTweakerCommand;
import crafttweaker.mc1120.commands.SpecialMessagesChat;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.recipe.RecipeRuneAltar;

public class BotaniaLogger extends CraftTweakerCommand {
    
    private static final List<String> ARGS = ImmutableList.of("apothecary", "brews", "trades", "infusions", "daisy", "altar");
    
    public BotaniaLogger() {
        super("botania");
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] arguments) {
        
        List<String> args = Arrays.asList(arguments);
        
        if(args.contains("apothecary")) {
            for(RecipePetals recipe : BotaniaAPI.petalRecipes) {
                CraftTweakerAPI.logCommand("Botania Apothecary Recipes");
                CraftTweakerAPI.logCommand(String.format("mods.botania.Apothecary.addRecipe(%s, %s);", LogHelper.getStackDescription(recipe.getOutput()), LogHelper.getListDescription(recipe.getInputs()) // Need to resolve "petalXXX" to an item
                ));
            }
        } else if(args.contains("brews")) {
            for(RecipeBrew recipe : BotaniaAPI.brewRecipes) {
                CraftTweakerAPI.logCommand("Botania Brewing Recipes");
                CraftTweakerAPI.logCommand(String.format("mods.botania.Brew.addRecipe(%s, \"%s\");", LogHelper.getListDescription(recipe.getInputs()), recipe.getBrew().getKey()));
            }
        } else if(args.contains("trades")) {
            for(RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
                CraftTweakerAPI.logCommand("Botania Trading Recipes");
                CraftTweakerAPI.logCommand(String.format("mods.botania.ElvenTrade.addRecipe(%s, %s);",
                        //TODO CHECK THIS
                        LogHelper.getStackDescription(recipe.getOutputs()), LogHelper.getListDescription(recipe.getInputs())));
            }
        } else if(args.contains("infusions")) {
            for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
                CraftTweakerAPI.logCommand("Botania Infusion Recipes");
                CraftTweakerAPI.logCommand(String.format("mods.botania.ManaInfusion.add%s(%s, %s, %d);", recipe.isAlchemy() ? "Alchemy" : recipe.isConjuration() ? "Conjuration" : "Infusion", LogHelper.getStackDescription(recipe.getOutput()), LogHelper.getStackDescription(recipe.getInput()), recipe.getManaToConsume()));
            }
        } else if(args.contains("daisy")) {
            for(RecipePureDaisy recipe : BotaniaAPI.pureDaisyRecipes) {
                CraftTweakerAPI.logCommand("Botania Daisy Recipes");
                CraftTweakerAPI.logCommand(String.format("mods.botania.PureDaisy.addRecipe(%s, %s);", LogHelper.getStackDescription(recipe.getInput()),
                        //TODO CHECK THIS
                        LogHelper.getStackDescription(new ItemStack(recipe.getOutputState().getBlock(), 1))));
            }
        } else if(args.contains("altar")) {
            for(RecipeRuneAltar recipe : BotaniaAPI.runeAltarRecipes) {
                CraftTweakerAPI.logCommand("Botania Altar Recipes");
                CraftTweakerAPI.logCommand(String.format("mods.botania.RuneAltar.addRecipe(%s, %s, %d);", LogHelper.getStackDescription(recipe.getOutput()), LogHelper.getListDescription(recipe.getInputs()), recipe.getManaUsage()));
            }
        } else {
            if(sender != null) {
                sender.sendMessage(new TextComponentString("Invalid arguments for command. Valid arguments: " + StringHelper.join(ARGS, ", ")));
            }
            return;
        }
        
        if(sender != null) {
            sender.sendMessage(SpecialMessagesChat.getLinkToCraftTweakerLog("List generated;", sender));
        }
    }
    
    @Override
    protected void init() {
    }
}

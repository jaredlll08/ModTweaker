package modtweaker2.mods.botania.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.StringHelper;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;

public class BotaniaLogger implements ICommandFunction {
    
    private static final List<String> validArguments = new LinkedList<String>();
    
    static {
        validArguments.add("apothecary");
        validArguments.add("elventrade");
        validArguments.add("manainfusion");
        validArguments.add("runealtar");
    }

    @Override
    public void execute(String[] arguments, IPlayer player) {
        List<String> args = StringHelper.toLowerCase(Arrays.asList(arguments));
        
        if(!validArguments.containsAll(args)) {
            if(player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("Invalid arguments for command. Valid arguments: " + StringHelper.join(validArguments, ", ")));
            }
        } else {
            if(args.isEmpty() || args.contains("apothecary")) {
                for(RecipePetals recipe : BotaniaAPI.petalRecipes) {
                    MineTweakerAPI.logCommand(String.format("mods.botania.Apothecary.addRecipe(%s, %s);",
                            InputHelper.getStackDescription(recipe.getOutput()),
                            InputHelper.getArrayDescription(recipe.getInputs()) // Need to resolve "petalXXX" to an item
                            ));
                }
            }
            
            if(args.isEmpty() || args.contains("elventrade")) {
                for(RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
                    MineTweakerAPI.logCommand(String.format("mods.botania.ElvenTrade.addRecipe(%s, %s);",
                            InputHelper.getStackDescription(recipe.getOutput()),
                            InputHelper.getArrayDescription(recipe.getInputs()
                                    )));
                }
            }
            
            if(args.isEmpty() || args.contains("manainfusion")) {
                for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
                    MineTweakerAPI.logCommand(String.format("mods.botania.ManaInfusion.add%s(%s, %s, %d);",
                            recipe.isAlchemy() ? "Alchemy" : recipe.isConjuration() ? "Conjuration" : "Infusion",
                            InputHelper.getStackDescription(recipe.getOutput()),
                            InputHelper.getStackDescription(recipe.getInput()),
                            recipe.getManaToConsume()
                            ));
                }
            }
            
            if(args.isEmpty() || args.contains("runealtar")) {
                for(RecipeRuneAltar recipe : BotaniaAPI.runeAltarRecipes) {
                    MineTweakerAPI.logCommand(String.format("mods.botania.RuneAltar.addRecipe(%s, %s, %d);",
                            InputHelper.getStackDescription(recipe.getOutput()),
                            InputHelper.getArrayDescription(recipe.getInputs()),
                            recipe.getManaUsage()
                            ));
                }
            }
            
            if (player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
            }
        }
    }
}

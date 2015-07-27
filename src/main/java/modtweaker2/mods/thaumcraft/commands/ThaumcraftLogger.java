package modtweaker2.mods.thaumcraft.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.StringHelper;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;

public class ThaumcraftLogger implements ICommandFunction {

    private static final List<String> validArguments = new LinkedList<String>();
    
    static {
        validArguments.add("crucible");
    }
    
    @Override
    public void execute(String[] arguments, IPlayer player) {
        List<String> args = StringHelper.toLowerCase(Arrays.asList(arguments));
        
        if(!validArguments.containsAll(args)) {
            if(player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("Invalid arguments for command. Valid arguments: " + StringHelper.join(validArguments, ", ")));
            }
        } else {
            if(args.isEmpty() || args.contains("arcane")) {
                for(Object o : ThaumcraftApi.getCraftingRecipes()) {
                    if(o instanceof IArcaneRecipe) {
                        
                    }
                }
            }
            
            if(args.isEmpty() || args.contains("crucible")) {
                for(Object o : ThaumcraftApi.getCraftingRecipes()) {
                    if(o instanceof CrucibleRecipe) {
                        CrucibleRecipe recipe = (CrucibleRecipe)o;
                        MineTweakerAPI.logCommand(String.format("mods.thaumcraft.Crucible(%s, %s, %s, %s);",
                                recipe.key,
                                LogHelper.getStackDescription(recipe.getRecipeOutput()),
                                LogHelper.getStackDescription(recipe.catalyst),
                                LogHelper.getStackDescription(recipe.aspects)));
                    }
                }
            }
            
            
            if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }
}
}

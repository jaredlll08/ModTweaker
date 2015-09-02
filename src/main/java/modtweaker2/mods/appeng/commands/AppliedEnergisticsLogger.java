package modtweaker2.mods.appeng.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.StringHelper;
import appeng.api.AEApi;
import appeng.api.features.IGrinderEntry;
import appeng.api.features.IInscriberRecipe;

public class AppliedEnergisticsLogger implements ICommandFunction {

    private static final List<String> validArguments = new LinkedList<String>();
    
    static {
        validArguments.add("Grinder");
        validArguments.add("Inscriber");
    }
    
	@Override
	public void execute(String[] arguments, IPlayer player) {
		List<String> args = StringHelper.toLowerCase(Arrays.asList(arguments));
		
		if(!validArguments.containsAll(args)) {
		    if(player != null) {
		        player.sendChat(MineTweakerImplementationAPI.platform.getMessage("Invalid arguments for command. Valid arguments: " + StringHelper.join(validArguments, ", ")));
		    }
		} else {
    		if(args.isEmpty() || args.contains("Grinder")) {
    			for(IGrinderEntry recipe : AEApi.instance().registries().grinder().getRecipes()) {
    				MineTweakerAPI.logCommand(String.format("mods.appeng.Grinder.addRecipe(%s, %s, %d, %s, %s, %s, %s);", 
    						LogHelper.getStackDescription(recipe.getInput()),
    						LogHelper.getStackDescription(recipe.getOutput()),
    						recipe.getEnergyCost(),
    						LogHelper.getStackDescription(recipe.getOptionalOutput()),
    						recipe.getOptionalChance(),
    						LogHelper.getStackDescription(recipe.getSecondOptionalOutput()),
    						recipe.getSecondOptionalChance()));
    			}
    		}
    		
    		if(args.isEmpty() || args.contains("Inscriber")) {
    			for(IInscriberRecipe recipe : AEApi.instance().registries().inscriber().getRecipes()) {
    				MineTweakerAPI.logCommand(String.format("mods.appeng.Inscriber.addRecipe(%s, %s, %s, %s, \"%s\");",
    						LogHelper.getListDescription(recipe.getInputs()),
    						LogHelper.getStackDescription(recipe.getTopOptional().orNull()),
    						LogHelper.getStackDescription(recipe.getBottomOptional().orNull()),
    						LogHelper.getStackDescription(recipe.getOutput()),
    						recipe.getProcessType().toString()));
    			}
    		}
    			
    		if (player != null) {
    			player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
    		}
		}
	}
}

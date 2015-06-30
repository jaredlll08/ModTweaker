package modtweaker2.mods.appeng.commands;

import java.util.LinkedList;
import java.util.List;

import java.util.Arrays;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import appeng.api.AEApi;
import appeng.api.features.IGrinderEntry;
import appeng.api.features.IInscriberRecipe;

public class AppliedEnergisticsLogger implements ICommandFunction {

    private static final List<String> validArguments = new LinkedList<String>();
    
    static {
        validArguments.add("grinder");
        validArguments.add("inscriber");
    }
    
	@Override
	public void execute(String[] arguments, IPlayer player) {
		List<String> args = LogHelper.toLowerCase(Arrays.asList(arguments));
		
		if(!validArguments.containsAll(args)) {
		    player.sendChat(MineTweakerImplementationAPI.platform.getMessage("Invalid arguments for command. Valid arguments: " + LogHelper.join(validArguments, ", ")));
		} else {
    		if(args.isEmpty() || args.contains("grinder")) {
    			for(IGrinderEntry recipe : AEApi.instance().registries().grinder().getRecipes()) {
    				MineTweakerAPI.logCommand(String.format("mods.appeng.Grinder.addRecipe(%s, %s, %d, %s, %s, %s, %s);", 
    						InputHelper.getStackDescription(recipe.getInput()),
    						InputHelper.getStackDescription(recipe.getOutput()),
    						recipe.getEnergyCost(),
    						InputHelper.getStackDescription(recipe.getOptionalOutput()),
    						recipe.getOptionalChance(),
    						InputHelper.getStackDescription(recipe.getSecondOptionalOutput()),
    						recipe.getSecondOptionalChance()));
    			}
    		}
    		
    		if(args.isEmpty() || args.contains("inscriber")) {
    			for(IInscriberRecipe recipe : AEApi.instance().registries().inscriber().getRecipes()) {
    				MineTweakerAPI.logCommand(String.format("mods.appeng.Inscriber.addRecipe(%s, %s, %s, %s, \"%s\");",
    						LogHelper.getArrayDescription(recipe.getInputs()),
    						InputHelper.getStackDescription(recipe.getTopOptional().orNull()),
    						InputHelper.getStackDescription(recipe.getBottomOptional().orNull()),
    						InputHelper.getStackDescription(recipe.getOutput()),
    						recipe.getProcessType().toString()));
    			}
    		}
    			
    		if (player != null) {
    			player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
    		}
		}
	}
}

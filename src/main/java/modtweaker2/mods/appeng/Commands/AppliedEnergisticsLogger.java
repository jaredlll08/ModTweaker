package modtweaker2.mods.appeng.Commands;

import java.util.LinkedList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.helpers.InputHelper;
import net.minecraft.item.ItemStack;
import appeng.api.AEApi;
import appeng.api.features.IGrinderEntry;
import appeng.api.features.IInscriberRecipe;

public class AppliedEnergisticsLogger implements ICommandFunction {

	@Override
	public void execute(String[] arguments, IPlayer player) {
		List<String> args = new LinkedList<String>();
		
		for(String arg : arguments) {
			args.add(arg.toLowerCase());
		}
		
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
						getArrayDescription(recipe.getInputs()),
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
	
	private String getArrayDescription(List<ItemStack> stacks) {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for(ItemStack stack : stacks) {
			sb.append(InputHelper.getStackDescription(stack)).append(", ");
		}
		sb.setLength(sb.length() - 2);
		sb.append(']');
		
		return sb.toString();
	}
}

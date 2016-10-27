package modtweaker.mods.randomthings.commands;

import lumien.randomthings.recipes.imbuing.ImbuingRecipe;
import lumien.randomthings.recipes.imbuing.ImbuingRecipeHandler;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.helpers.StringHelper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RandomThingsLogger implements ICommandFunction {
    private static final List<String> validArguments = new LinkedList<String>();

    static {
        validArguments.add("Imbuing");
    }

    @Override
    public void execute(String[] arguments, IPlayer player) {
        List<String> args = StringHelper.toLowerCase(Arrays.asList(arguments));

        if (!validArguments.containsAll(args)) {
            if (player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("Invalid arguments for command. Valid arguments: " + StringHelper.join(validArguments, ", ")));
            }
        } else {
            if (args.isEmpty() || args.contains("Imbuing")) {
                for (ImbuingRecipe recipe : ImbuingRecipeHandler.imbuingRecipes) {
                    MineTweakerAPI.logCommand(String.format("mods.randomthings.ImbuingStation.add(%s, %s, %s, %s, %d);",
                            LogHelper.getStackDescription(recipe.getResult()),
                            LogHelper.getStackDescription(recipe.toImbue()),
                            LogHelper.getStackDescription(recipe.getIngredients().get(0)),
                            LogHelper.getStackDescription(recipe.getIngredients().get(1)),
                            LogHelper.getStackDescription(recipe.getIngredients().get(2))));
                }
            }

            if (player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
            }
        }
    }
}

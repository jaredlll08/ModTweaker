package modtweaker2.mods.botanicaladdons.commands;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.helpers.LogHelper;
import ninja.shadowfox.shadowfox_botany.api.ShadowFoxAPI;
import ninja.shadowfox.shadowfox_botany.api.recipe.RecipeTreeCrafting;

public class BotanicalAddonsDendricLogger implements ICommandFunction{

    @Override
    public void execute(String[] arguments, IPlayer player) {
        System.out.println("Recipes: " + ShadowFoxAPI.treeRecipes.size());
        for (RecipeTreeCrafting recipe : ShadowFoxAPI.treeRecipes) {
            MineTweakerAPI.logCommand(String.format("mods.botanicaladdons.DendricSuffuser.addRecipe(%s, %s, %s, %s);",
                    LogHelper.getStackDescription(recipe.getOutput()),
                    recipe.getManaUsage(),
                    recipe.getThrottle(),
                    LogHelper.getListDescription(recipe.getInputs())
            ));
        }

        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }
}

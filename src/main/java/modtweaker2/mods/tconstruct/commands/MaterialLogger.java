package modtweaker2.mods.tconstruct.commands;

import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;

import static modtweaker2.helpers.LogHelper.logPrinted;

public class MaterialLogger implements ICommandFunction {
    @Override
    public void execute(String[] arguments, IPlayer player) {
        MineTweakerAPI.logCommand(TinkerRegistry.getAllMaterials().size() + " Materials:");
        for (Material entry : TinkerRegistry.getAllMaterials()) {
            MineTweakerAPI.logCommand(entry.getLocalizedName());
        }
        logPrinted(player);
    }
}

package modtweaker.mods.tconstruct.commands;

import com.blamejared.mtlib.commands.CommandLogger;
import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.blamejared.mtlib.helpers.LogHelper.logPrinted;

public class MaterialLogger extends CommandLogger {
    @Override
    public void execute(String[] arguments, IPlayer player) {
        MineTweakerAPI.logCommand(TinkerRegistry.getAllMaterials().size() + " Materials:");
        for(Material entry : TinkerRegistry.getAllMaterials()) {
            MineTweakerAPI.logCommand(entry.getLocalizedName());
        }
        logPrinted(player);
    }

    @Override
    public Collection<? extends String> getList() {
        Set<String> keys = new HashSet<>();
        TinkerRegistry.getAllMaterials().forEach(mat -> keys.add(mat.getLocalizedName()));
        return keys;
    }

    @Override
    public String getName() {
        return "Materials";
    }
}

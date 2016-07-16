package modtweaker.commands;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker.ModTweaker;
import net.minecraft.entity.EntityList;

import java.util.Set;

public class EntityMappingLogger implements ICommandFunction {

    @Override
    public void execute(String[] arguments, IPlayer player) {

        @SuppressWarnings("unchecked")
        Set<String> keys = EntityList.NAME_TO_CLASS.keySet();

        System.out.println("Mob Keys: " + keys.size());

        for (String key : keys) {
            ModTweaker.logger.info("Mob Key "  + key);
            MineTweakerAPI.logCommand("Mob Key " + key);

        }

        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }
}

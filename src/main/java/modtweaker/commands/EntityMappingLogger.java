package modtweaker.commands;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityList;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;

public class EntityMappingLogger implements ICommandFunction{

    @Override
    public void execute(String[] arguments, IPlayer player) {
    	Set<String> keys=EntityList.stringToClassMapping.keySet();
        System.out.println("Mob Keys: " + keys.size());
        for (String key : keys) {
            System.out.println("Mob Key " + key);
            MineTweakerAPI.logCommand(key);
        }

        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }
}

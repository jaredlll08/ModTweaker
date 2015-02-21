package modtweaker2.mods.thaumcraft.research.commands;

import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import thaumcraft.api.research.ResearchCategories;
import static modtweaker2.helpers.InputHelper.*;
import static modtweaker2.helpers.LogHelper.*;

public class ResearchLogger implements ICommandFunction {
    @Override
    public void execute(String[] arguments, IPlayer player) {
        if (arguments == null || arguments.length <= 0 || arguments[0] == null) {
            System.out.println("Research Categories:");
            MineTweakerAPI.logCommand("Research Categories:");
            for (String tab : ResearchCategories.researchCategories.keySet()) {
                System.out.println(tab);
                MineTweakerAPI.logCommand(tab);
            }

            logPrinted(player);
        } else if (arguments[0] != null && ResearchCategories.researchCategories.containsKey(arguments[0])) {
            System.out.println("Research Keys for " + arguments[0] + ":");
            MineTweakerAPI.logCommand("Research Keys for " + arguments[0] + ":");
            for (String key : ResearchCategories.researchCategories.get(arguments[0]).research.keySet()) {
                System.out.println(key);
                MineTweakerAPI.logCommand(key);
            }

            logPrinted(player);
        } else if (arguments[0] != null && player != null) {
            log(player, "Cannot find research category " + arguments[0]);
        }
    }
}

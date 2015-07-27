package modtweaker2.mods.thaumcraft.commands;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import thaumcraft.api.aspects.Aspect;

public class AspectLogger implements ICommandFunction{

    @Override
    public void execute(String[] arguments, IPlayer player) {
   	
    	for(Aspect aspect : Aspect.getPrimalAspects()) {
    	    MineTweakerAPI.logCommand("<aspect:" + aspect.getName() + "> // " + aspect.getLocalizedDescription());
    	}
    	
    	for(Aspect aspect : Aspect.getCompoundAspects()) {
    	    MineTweakerAPI.logCommand("<aspect:" + aspect.getName() + "> // " + aspect.getLocalizedDescription() + " [<aspect:" + aspect.getComponents()[0].getName() + ">, <aspect:" + aspect.getComponents()[1].getName() + ">]");
    	}
      
        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }
}

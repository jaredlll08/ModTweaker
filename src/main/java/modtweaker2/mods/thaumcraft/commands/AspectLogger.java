package modtweaker2.mods.thaumcraft.commands;

import java.util.ArrayList;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import thaumcraft.api.aspects.Aspect;

public class AspectLogger implements ICommandFunction{

    @Override
    public void execute(String[] arguments, IPlayer player) {
    	ArrayList<Aspect> aspects = new ArrayList<Aspect>();
		aspects.addAll(Aspect.getPrimalAspects());
		aspects.addAll(Aspect.getCompoundAspects());
		 System.out.println("Aspects: " + aspects.size());
		for (Aspect set : aspects) {
			if (!set.isPrimal()) {
				System.out.println("Aspect:[ AspectName: " + set.getName() + ", description" + set.getLocalizedDescription() + ", Components:[" + set.getComponents()[0].getName() + ", " + set.getComponents()[1].getName() + "]\n");
				MineTweakerAPI.logCommand("Aspect:[ AspectName: " + set.getName() + ", description" + set.getLocalizedDescription() + ", Components:[" + set.getComponents()[0].getName() + ", " + set.getComponents()[1].getName() + "]\n");
			} else {
				System.out.println("Aspect:[ AspectName: " + set.getName() + ", description" + set.getLocalizedDescription() + ", ]\n");
				MineTweakerAPI.logCommand("Aspect:[ AspectName: " + set.getName() + ", description" + set.getLocalizedDescription() + ", ]\n");

			}
		}
       
        if (player != null) {
            player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
        }
    }
}

package modtweaker2.mods.exnihilo.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.StringHelper;
import modtweaker2.mods.exnihilo.ExNihiloHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import exnihilo.registries.CompostRegistry;
import exnihilo.registries.CrucibleRegistry;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.SieveRegistry;
import exnihilo.registries.helpers.Compostable;
import exnihilo.registries.helpers.Meltable;
import exnihilo.registries.helpers.SiftingResult;
import exnihilo.registries.helpers.Smashable;
import exnihilo.utils.ItemInfo;

public class ExNihiloLogger implements ICommandFunction {

    private static final List<String> validArguments = new LinkedList<String>();
    
    static {
        validArguments.add("compost");
        validArguments.add("crucible");
        validArguments.add("hammer");
        validArguments.add("sieve");
    }
    
    @Override
    public void execute(String[] arguments, IPlayer player) {
        List<String> args = StringHelper.toLowerCase(Arrays.asList(arguments));
        
        if(!validArguments.containsAll(args)) {
            if(player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("Invalid arguments for command. Valid arguments: " + StringHelper.join(validArguments, ", ")));
            }
        } else {
            if(args.isEmpty() || args.contains("compost")) {
                for(Entry<ItemInfo, Compostable> recipe : CompostRegistry.entries.entrySet()) {
                    MineTweakerAPI.logCommand(String.format("mods.exnihilo.Composting.addRecipe(%s, %s, \"%s\");",
                            LogHelper.getStackDescription(recipe.getKey().getStack()),
                            recipe.getValue().value,
                            Integer.toHexString(recipe.getValue().color.toInt()).substring(2)));
                }
            }
            
            if(args.isEmpty() || args.contains("crucible")) {
                for(Meltable recipe : CrucibleRegistry.entries.values()) {
                    MineTweakerAPI.logCommand(String.format("mods.exnihilo.Crucible.addRecipe(%s, %s);",
                            LogHelper.getStackDescription(new ItemStack(recipe.block, 1, recipe.meta)),
                            LogHelper.getStackDescription(new FluidStack(recipe.fluid, (int)recipe.fluidVolume))));
                }
                
                for(Entry<ItemInfo, Float> recipe : ExNihiloHelper.getHeatMap().entrySet()) {
                    MineTweakerAPI.logCommand(String.format("mods.exnihilo.Crucible.addHeatSource(%s, %s);",
                            LogHelper.getStackDescription(recipe.getKey().getStack()),
                            recipe.getValue()));
                }
            }
            
            if(args.isEmpty() || args.contains("hammer")) {
                for(Entry<ItemInfo, ArrayList<Smashable>> entry : HammerRegistry.getRewards().entrySet()) {
                    for(Smashable recipe : entry.getValue()) {
                        MineTweakerAPI.logCommand(String.format("mods.exnihilo.Hammer.addRecipe(%s, %s, %s, %s);",
                                LogHelper.getStackDescription(entry.getKey().getStack()),
                                LogHelper.getStackDescription(new ItemStack(recipe.item, 1, recipe.meta)),
                                recipe.chance,
                                recipe.luckMultiplier));
                    }
                }
            }
            
            if(args.isEmpty() || args.contains("sieve")) {
                for(Entry<ItemInfo, ArrayList<SiftingResult>> entry : SieveRegistry.getSiftables().entrySet()) {
                    for(SiftingResult recipe : entry.getValue()) {
                        MineTweakerAPI.logCommand(String.format("mods.exnihilo.Sieve.addRecipe(%s, %s, %s);",
                                LogHelper.getStackDescription(entry.getKey().getStack()),
                                LogHelper.getStackDescription(new ItemStack(recipe.item, 1, recipe.meta)),
                                recipe.rarity));
                    }
                }
            }
            
            if (player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
            }
        }
    }
}

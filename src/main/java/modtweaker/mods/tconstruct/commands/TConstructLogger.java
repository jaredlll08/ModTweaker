package modtweaker.mods.tconstruct.commands;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker.helpers.LogHelper;
import modtweaker.helpers.StringHelper;
import modtweaker.mods.tconstruct.TConstructHelper;
import slimeknights.tconstruct.library.DryingRecipe;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TConstructLogger implements ICommandFunction {
    private static final List<String> validArguments = new LinkedList<String>();

    static {
        validArguments.add("Casting");
        validArguments.add("Drying");
        validArguments.add("Smeltery");
    }

    @Override
    public void execute(String[] arguments, IPlayer player) {
        List<String> args = StringHelper.toLowerCase(Arrays.asList(arguments));

        if (!validArguments.containsAll(args)) {
            if (player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("Invalid arguments for command. Valid arguments: " + StringHelper.join(validArguments, ", ")));
            }
        } else {
            if (args.isEmpty() || args.contains("Casting")) {
                for (ICastingRecipe recipe : TConstructHelper.basinCasting) {
                    MineTweakerAPI.logCommand(String.format("mods.tconstruct.Casting.addBasinRecipe(%s, %s, %s, %s, %d);",
                            LogHelper.getStackDescription(((CastingRecipe) recipe).getResult()),
                            LogHelper.getStackDescription(((CastingRecipe) recipe).getFluid()),
                            LogHelper.getStackDescription(((CastingRecipe) recipe).cast),
                            recipe.consumesCast(),
                            recipe.getTime()));
                }

                for (ICastingRecipe recipe : TConstructHelper.tableCasting) {
                    MineTweakerAPI.logCommand(String.format("mods.tconstruct.Casting.addTableRecipe(%s, %s, %s, %s, %d);",
                            LogHelper.getStackDescription(((CastingRecipe) recipe).getResult()),
                            LogHelper.getStackDescription(((CastingRecipe) recipe).getFluid()),
                            LogHelper.getStackDescription(((CastingRecipe) recipe).cast),
                            recipe.consumesCast(),
                            recipe.getTime()));
                }
            }

            if (args.isEmpty() || args.contains("Drying")) {
                for (DryingRecipe recipe : TinkerRegistry.getAllDryingRecipes()) {
                    MineTweakerAPI.logCommand(String.format("mods.tconstruct.Drying.addRecipe(%s, %s, %d);",
                            LogHelper.getStackDescription(recipe.input),
                            LogHelper.getStackDescription(recipe.getResult()),
                            recipe.time));
                }
            }

//            if(args.isEmpty() || args.contains("Smeltery")) {
//                for(MeltingRecipe recipe : TConstructHelper.smeltingList) {
//                    int temperature = recipe.getTemperature();
//                    ItemStack renderItem = TConstructHelper.renderIndex.get(recipe.getKey());
//
//                    MineTweakerAPI.logCommand(String.format("mods.tconstruct.Smeltery.addMelting(%s, %s, %d, %s);",
//                            LogHelper.getStackDescription(new ItemStack(recipe.getKey().item, 1, recipe.getKey().meta)),
//                            LogHelper.getStackDescription(recipe.getValue()),
//                            temperature,
//                            LogHelper.getStackDescription(renderItem)));
//                }

            for (AlloyRecipe recipe : TConstructHelper.alloys) {
                MineTweakerAPI.logCommand(String.format("mods.tconstruct.Smeltery.addAlloy(%s, %s);",
                        LogHelper.getStackDescription(recipe.getResult()),
                        LogHelper.getListDescription(recipe.getFluids())));
            }

//                for(FluidStack fuel : TConstructHelper.fuelList) {
//                    MineTweakerAPI.logCommand(String.format("mods.tconstruct.Smeltery.addFuel(%s, %d, %d);",
//                            LogHelper.getStackDescription(new FluidStack(fuel.getFluid(), 1)),
//                            fuel[0],
//                            fuel.getValue()[1]));
//                }

            if (player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
            }
        }
    }
}

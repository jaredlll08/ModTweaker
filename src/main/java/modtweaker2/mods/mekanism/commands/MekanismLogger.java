package modtweaker2.mods.mekanism.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import mekanism.api.gas.GasStack;
import mekanism.common.recipe.RecipeHandler.Recipe;
import mekanism.common.recipe.machines.ChemicalInfuserRecipe;
import mekanism.common.recipe.machines.CombinerRecipe;
import mekanism.common.recipe.machines.CrusherRecipe;
import mekanism.common.recipe.machines.CrystallizerRecipe;
import mekanism.common.recipe.machines.DissolutionRecipe;
import mekanism.common.recipe.machines.EnrichmentRecipe;
import mekanism.common.recipe.machines.InjectionRecipe;
import mekanism.common.recipe.machines.MetallurgicInfuserRecipe;
import mekanism.common.recipe.machines.OxidationRecipe;
import mekanism.common.recipe.machines.PressurizedRecipe;
import mekanism.common.recipe.machines.PurificationRecipe;
import mekanism.common.recipe.machines.SawmillRecipe;
import mekanism.common.recipe.machines.SeparatorRecipe;
import mekanism.common.recipe.machines.SmeltingRecipe;
import mekanism.common.recipe.machines.SolarEvaporationRecipe;
import mekanism.common.recipe.machines.SolarNeutronRecipe;
import mekanism.common.recipe.machines.WasherRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.helpers.LogHelper;
import modtweaker2.helpers.StringHelper;

public class MekanismLogger implements ICommandFunction {

    private static final List<String> validArguments = new LinkedList<String>();
    
    static {
        validArguments.add("Combiner");
        validArguments.add("Compressor");
        validArguments.add("Crusher");
        validArguments.add("Crystallizer");
        validArguments.add("Dissolution");
        validArguments.add("Enrichment");
        validArguments.add("SolarEvaporation");
        validArguments.add("Infuser");
        validArguments.add("Injection");
        validArguments.add("SolarNeutronActivator");
        validArguments.add("Oxidizer");
        validArguments.add("Purification");
        validArguments.add("Reaction");
        validArguments.add("Sawmill");
        validArguments.add("Separator");
        validArguments.add("Washer");
    }
    
    @SuppressWarnings({ "unchecked" })
    @Override
    public void execute(String[] arguments, IPlayer player) {
        List<String> args = StringHelper.toLowerCase(Arrays.asList(arguments));
        
        if(!validArguments.containsAll(args)) {
            if(player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("Invalid arguments for command. Valid arguments: " + StringHelper.join(validArguments, ", ")));
            }
        } else {
            if(args.isEmpty() || args.contains("Crystallizer")) {
                for(CrystallizerRecipe recipe : (Collection<CrystallizerRecipe>)Recipe.CHEMICAL_CRYSTALLIZER.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.chemical.Crystallizer.addRecipe(%s, %s);",
                            LogHelper.getStackDescription(recipe.recipeInput.ingredient),
                            LogHelper.getStackDescription(recipe.recipeOutput.output)));
                }
            }
            
            if(args.isEmpty() || args.contains("Dissolution")) {
                for(DissolutionRecipe recipe : (Collection<DissolutionRecipe>)Recipe.CHEMICAL_DISSOLUTION_CHAMBER.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.chemical.Dissolution.addRecipe(%s, %s);",
                            LogHelper.getStackDescription(recipe.recipeInput.ingredient),
                            LogHelper.getStackDescription(recipe.recipeOutput.output)));
                }
            }
            
            if(args.isEmpty() || args.contains("Infuser")) {
                for(ChemicalInfuserRecipe recipe : (Collection<ChemicalInfuserRecipe>)Recipe.CHEMICAL_INFUSER.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.chemical.Infuser.addRecipe(%s, %s, %s);",
                            LogHelper.getStackDescription(recipe.recipeInput.leftGas),
                            LogHelper.getStackDescription(recipe.recipeInput.rightGas),
                            LogHelper.getStackDescription(recipe.recipeOutput.output)));
                }
            }
            
            if(args.isEmpty() || args.contains("Injection")) {
                for(InjectionRecipe recipe : (Collection<InjectionRecipe>)Recipe.CHEMICAL_INJECTION_CHAMBER.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.chemical.Injection.addRecipe(%s, %s, %s);",
                            LogHelper.getStackDescription(recipe.recipeInput.itemStack),
                            LogHelper.getStackDescription(new GasStack(recipe.recipeInput.gasType, 1)),
                            LogHelper.getStackDescription(recipe.recipeOutput.output)));
                }
            }
            
            if(args.isEmpty() || args.contains("Oxidizer")) {
                for(OxidationRecipe recipe : (Collection<OxidationRecipe>)Recipe.CHEMICAL_OXIDIZER.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.chemical.Oxidizer.addRecipe(%s, %s);",
                            LogHelper.getStackDescription(recipe.recipeInput.ingredient),
                            LogHelper.getStackDescription(recipe.recipeOutput.output)));
                }
            }
            
            if(args.isEmpty() || args.contains("Washer")) {
                for(WasherRecipe recipe : (Collection<WasherRecipe>)Recipe.CHEMICAL_WASHER.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.chemical.Washer.addRecipe(%s, %s);",
                            LogHelper.getStackDescription(recipe.recipeInput.ingredient),
                            LogHelper.getStackDescription(recipe.recipeOutput.output)));
                }
            }
            
            if(args.isEmpty() || args.contains("Combiner")) {
                for(CombinerRecipe recipe : (Collection<CombinerRecipe>)Recipe.COMBINER.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.Combiner.addRecipe(%s, %s, %s);",
                            LogHelper.getStackDescription(recipe.recipeInput.itemStack),
                            LogHelper.getStackDescription(recipe.recipeOutput.output),
                            LogHelper.getStackDescription(new GasStack(recipe.recipeInput.gasType, 1))));
                }
            }
            
            if(args.isEmpty() || args.contains("Compressor")) {
                for(CombinerRecipe recipe : (Collection<CombinerRecipe>)Recipe.COMBINER.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.Compressor.addRecipe(%s, %s, %s);",
                            LogHelper.getStackDescription(recipe.recipeInput.itemStack),
                            LogHelper.getStackDescription(new GasStack(recipe.recipeInput.gasType, 1)),
                            LogHelper.getStackDescription(recipe.recipeOutput.output)));
                }
            }
            
            if(args.isEmpty() || args.contains("Crusher")) {
                for(CrusherRecipe recipe : (Collection<CrusherRecipe>)Recipe.CRUSHER.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.Crusher.addRecipe(%s, %s);",
                            LogHelper.getStackDescription(recipe.recipeInput.ingredient),
                            LogHelper.getStackDescription(recipe.recipeOutput.output)));
                }
            }
            
            if(args.isEmpty() || args.contains("Smelter")) {
                for(SmeltingRecipe recipe : (Collection<SmeltingRecipe>)Recipe.ENERGIZED_SMELTER.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.Smelter.addRecipe(%s, %s);",
                            LogHelper.getStackDescription(recipe.recipeInput.ingredient),
                            LogHelper.getStackDescription(recipe.recipeOutput.output)));
                }
            }
            
            if(args.isEmpty() || args.contains("Enrichment")) {
                for(EnrichmentRecipe recipe : (Collection<EnrichmentRecipe>)Recipe.ENRICHMENT_CHAMBER.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.Enrichment.addRecipe(%s, %s);",
                            LogHelper.getStackDescription(recipe.recipeInput.ingredient),
                            LogHelper.getStackDescription(recipe.recipeOutput.output)));
                }
            }
            
            if(args.isEmpty() || args.contains("Infuser")) {
                for(MetallurgicInfuserRecipe recipe : (Collection<MetallurgicInfuserRecipe>)Recipe.METALLURGIC_INFUSER.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.Infuser.addRecipe(\"%s\", %d, %s, %s);",
                            recipe.getInput().infuse.type.name,
                            recipe.getInput().infuse.amount,
                            LogHelper.getStackDescription(recipe.recipeInput.inputStack),
                            LogHelper.getStackDescription(recipe.recipeOutput.output)));
                }
            }
            
            if(args.isEmpty() || args.contains("Purification")) {
                for(PurificationRecipe recipe : (Collection<PurificationRecipe>)Recipe.PURIFICATION_CHAMBER.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.Purification.addRecipe(%s, %s, %s);",
                            LogHelper.getStackDescription(recipe.recipeInput.itemStack),
                            LogHelper.getStackDescription(new GasStack(recipe.recipeInput.gasType, 1)),
                            LogHelper.getStackDescription(recipe.recipeOutput.output)));                   
                }
            }
            
            if(args.isEmpty() || args.contains("Reaction")) {
                for(PressurizedRecipe recipe : (Collection<PressurizedRecipe>)Recipe.PRESSURIZED_REACTION_CHAMBER.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.Reaction.addRecipe(%s, %s, %s, %s, %s, %s, %d);",
                            LogHelper.getStackDescription(recipe.recipeInput.getSolid()),
                            LogHelper.getStackDescription(recipe.recipeInput.getFluid()),
                            LogHelper.getStackDescription(recipe.recipeInput.getGas()),
                            LogHelper.getStackDescription(recipe.recipeOutput.getItemOutput()),
                            LogHelper.getStackDescription(recipe.recipeOutput.getGasOutput()),
                            recipe.extraEnergy,
                            recipe.ticks));
                }
            }
            
            if(args.isEmpty() || args.contains("Sawmill")) {
                for(SawmillRecipe recipe : (Collection<SawmillRecipe>)Recipe.PRECISION_SAWMILL.get().values()) {
                    if(recipe.getOutput().hasSecondary()) {
                        MineTweakerAPI.logCommand(String.format("mods.mekanism.Sawmill.addRecipe(%s, %s, %s, %s);",
                                LogHelper.getStackDescription(recipe.recipeInput.ingredient),
                                LogHelper.getStackDescription(recipe.recipeOutput.primaryOutput),
                                LogHelper.getStackDescription(recipe.recipeOutput.secondaryOutput),
                                recipe.recipeOutput.secondaryChance));
                    } else {
                        MineTweakerAPI.logCommand(String.format("mods.mekanism.Sawmill.addRecipe(%s, %s);",
                                LogHelper.getStackDescription(recipe.recipeInput.ingredient),
                                LogHelper.getStackDescription(recipe.recipeOutput.primaryOutput)));
                    }
                }
            }
            
            if(args.isEmpty() || args.contains("Separator")) {
                for(SeparatorRecipe recipe : (Collection<SeparatorRecipe>)Recipe.ELECTROLYTIC_SEPARATOR.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.Separator.addRecipe(%s, %s, %s, %s);",
                            LogHelper.getStackDescription(recipe.recipeInput.ingredient),
                            recipe.energyUsage,
                            LogHelper.getStackDescription(recipe.recipeOutput.leftGas),
                            LogHelper.getStackDescription(recipe.recipeOutput.rightGas)));
                }
            }
            
            if(args.isEmpty() || args.contains("SolarEvaporation")) {
                for(SolarEvaporationRecipe recipe : (Collection<SolarEvaporationRecipe>)Recipe.SOLAR_EVAPORATION_PLANT.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.SolarEvaporation.addRecipe(%s, %s);",
                            LogHelper.getStackDescription(recipe.recipeInput.ingredient),
                            LogHelper.getStackDescription(recipe.recipeOutput.output)));
                }
            }
            
            if(args.isEmpty() || args.contains("SolarNeutronActivator")) {
                for(SolarNeutronRecipe recipe : (Collection<SolarNeutronRecipe>)Recipe.SOLAR_NEUTRON_ACTIVATOR.get().values()) {
                    MineTweakerAPI.logCommand(String.format("mods.mekanism.SolarNeutronActivator.addRecipe(%s, %s);",
                            LogHelper.getStackDescription(recipe.recipeInput.ingredient),
                            LogHelper.getStackDescription(recipe.recipeOutput.output)));
                }
            }
                
            if (player != null) {
                player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
            }
        }
    }
}

package modtweaker.mods.tconstruct.handlers;

import static modtweaker.helpers.InputHelper.isABlock;
import static modtweaker.helpers.InputHelper.toFluid;
import static modtweaker.helpers.InputHelper.toFluids;
import static modtweaker.helpers.InputHelper.toStack;

import java.util.ArrayList;
import java.util.Arrays;

import mantle.utils.ItemMetaWrapper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.oredict.IOreDictEntry;
import modtweaker.mods.tconstruct.TConstructHelper;
import modtweaker.util.BaseDescriptionAddition;
import modtweaker.util.BaseDescriptionRemoval;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tconstruct.library.crafting.AlloyMix;

@ZenClass("mods.tconstruct.Smeltery")
public class Smeltery {

    /********************************************** TConstruct Alloy Recipes **********************************************/

    //Adding a TConstruct Alloy recipe
    @ZenMethod
    public static void addAlloy(ILiquidStack output, ILiquidStack[] input) {
        MineTweakerAPI.apply(new AddAlloy(new AlloyMix(toFluid(output), new ArrayList<FluidStack>(Arrays.asList(toFluids(input))))));
    }

    //Passes the list to the base list implementation, and adds the recipe
    private static class AddAlloy extends BaseListAddition {
        public AddAlloy(AlloyMix recipe) {
            super("Smeltery - Alloy", TConstructHelper.alloys, recipe);
        }

        @Override
        public String getRecipeInfo() {
            return ((AlloyMix) recipe).result.getFluid().getName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a TConstruct Alloy recipe
    @ZenMethod
    public static void removeAlloy(ILiquidStack output) {
        MineTweakerAPI.apply(new RemoveAlloy((toFluid(output))));
    }

    //Removes a recipe, apply is never the same for anything, so will always need to override it
    private static class RemoveAlloy extends BaseListRemoval {
        public RemoveAlloy(FluidStack output) {
            super("Smeltery - Alloy", TConstructHelper.alloys, output);
        }

        //Loops through the registry, to find the item that matches, saves that recipe then removes it
        @Override
        public void apply() {
            for (AlloyMix r : TConstructHelper.alloys) {
                if (r.result != null && r.result.isFluidStackIdentical(fluid)) {
                    recipe = r;
                    break;
                }
            }

            TConstructHelper.alloys.remove(recipe);
        }

        @Override
        public String getRecipeInfo() {
            return fluid.getFluid().getName();
        }
    }

    /********************************************** TConstruct Melting Recipes **********************************************/

    //Adding a TConstruct Melting recipe
    @ZenMethod
    public static void addMelting(IItemStack input, ILiquidStack output, int temp, @Optional IItemStack block) {
        if (block == null) block = input;
        if (isABlock(block)) {
            Block theBlock = Block.getBlockFromItem(toStack(block).getItem());
            int theMeta = toStack(block).getItemDamage();
            MineTweakerAPI.apply(new AddMelting(toStack(input), theBlock, theMeta, temp, toFluid(output)));
        }
    }

    @ZenMethod
    public static void addMelting(IOreDictEntry input, ILiquidStack output, int temp, @Optional IItemStack block) {
        for (ItemStack stack : OreDictionary.getOres(input.getName())) {
            addMelting(MineTweakerMC.getIItemStack(stack), output, temp, block);
        }
    }

    //Takes all the variables and saves them in place
    private static class AddMelting extends BaseDescriptionAddition {
        private final ItemStack input;
        private final Block block;
        private final int meta;
        private final int temp;
        private final FluidStack output;

        public AddMelting(ItemStack input, Block block, int meta, int temp, FluidStack output) {
            super("Smeltery - Melting");
            this.input = input;
            this.block = block;
            this.meta = meta;
            this.temp = temp;
            this.output = output;
        }

        //Adds the Melting recipe
        @Override
        public void apply() {
            tconstruct.library.crafting.Smeltery.instance.addMelting(input, block, meta, temp, output);
        }

        //Removes the Melting recipe from the hashmaps
        @Override
        public void undo() {
            ItemMetaWrapper in = new ItemMetaWrapper(input);
            TConstructHelper.smeltingList.remove(in);
            TConstructHelper.temperatureList.remove(in);
            TConstructHelper.renderIndex.remove(in);
        }

        @Override
        public String getRecipeInfo() {
            return input.getDisplayName();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Removing a TConstruct Melting recipe
    @ZenMethod
    public static void removeMelting(IItemStack input) {
        MineTweakerAPI.apply(new RemoveMelting((toStack(input))));
    }

    @ZenMethod
    public static void removeMelting(IOreDictEntry input) {
        for (ItemStack stack : OreDictionary.getOres(input.getName())) {
            removeMelting(MineTweakerMC.getIItemStack(stack));
        }
    }

    //Removes a recipe, apply is never the same for anything, so will always need to override it
    private static class RemoveMelting extends BaseDescriptionRemoval {
        private final ItemStack input;
        private FluidStack fluid;
        private Integer temp;
        private ItemStack renderer;

        public RemoveMelting(ItemStack input) {
            super("Smeltery - Melting");
            this.input = input;
        }

        //Gets the current values, and saves, them removes them from the hashmaps
        @Override
        public void apply() {
            ItemMetaWrapper in = new ItemMetaWrapper(input);
            fluid = TConstructHelper.smeltingList.get(in);
            temp = TConstructHelper.temperatureList.get(in);
            renderer = TConstructHelper.renderIndex.get(in);
            TConstructHelper.smeltingList.remove(in);
            TConstructHelper.temperatureList.remove(in);
            TConstructHelper.renderIndex.remove(in);
        }

        //Readds the Melting recipe
        @Override
        public void undo() {
            tconstruct.library.crafting.Smeltery.instance.addMelting(input, Block.getBlockFromItem(renderer.getItem()), renderer.getItemDamage(), temp, fluid);
        }

        @Override
        public String getRecipeInfo() {
            return input.getDisplayName();
        }
    }
}

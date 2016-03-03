package modtweaker2.mods.botanicaladdons.handlers;


import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import ninja.shadowfox.shadowfox_botany.api.ShadowFoxAPI;
import ninja.shadowfox.shadowfox_botany.api.trees.IIridescentSaplingVariant;
import ninja.shadowfox.shadowfox_botany.api.trees.IridescentSaplingBaseVariant;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.LinkedList;
import java.util.List;

@ZenClass("mods.botanicaladdons.IridescentTree")
public class IridescentTree {

    protected static final String name = "Botanical Addons Iridescent Tree";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void addVariant(IItemStack blockSoil, IItemStack blockWood, IItemStack blockLeaves, int metaMin, int metaMax, int metaShift) {
        Object soil = InputHelper.toObject(blockSoil);
        Object wood = InputHelper.toObject(blockWood);
        Object leaves = InputHelper.toObject(blockLeaves);
        if (soil == null || !(soil instanceof ItemStack) || !InputHelper.isABlock((ItemStack)soil)) {
            LogHelper.logError("Soil must be a block.");
            return;
        }
        else if (wood == null || !(wood instanceof ItemStack) || !InputHelper.isABlock((ItemStack)wood)) {
            LogHelper.logError("Wood must be a block.");
            return;
        }
        else if(leaves == null || !(leaves instanceof ItemStack) || !InputHelper.isABlock((ItemStack)leaves)) {
            LogHelper.logError("Leaves must be a block.");
            return;
        }
        Block soilBlock = Block.getBlockFromItem(((ItemStack)soil).getItem());
        Block woodBlock = Block.getBlockFromItem(((ItemStack)wood).getItem());
        Block leavesBlock = Block.getBlockFromItem(((ItemStack)leaves).getItem());
        MineTweakerAPI.apply(new Add(new IridescentSaplingBaseVariant(soilBlock, woodBlock, leavesBlock, metaMin, metaMax, metaShift)));
    }

    @ZenMethod
    public static void addVariant(IItemStack blockSoil, IItemStack blockWood, IItemStack blockLeaves, int metaMin, int metaMax) {
        addVariant(blockSoil, blockWood, blockLeaves, metaMin, metaMax, 0);
    }

    @ZenMethod
    public static void addVariant(IItemStack blockSoil, IItemStack blockWood, IItemStack blockLeaves, int meta) {
        addVariant(blockSoil, blockWood, blockLeaves, meta, meta, 0);
    }

    @ZenMethod
    public static void addVariant(IItemStack blockSoil, IItemStack blockWood, IItemStack blockLeaves) {
        addVariant(blockSoil, blockWood, blockLeaves, 0, 15, 0);
    }

    private static class Add extends BaseListAddition<IIridescentSaplingVariant> {
        public Add(IIridescentSaplingVariant recipe) {
            super(IridescentTree.name, ShadowFoxAPI.treeVariants);

            recipes.add(recipe);
        }

        @Override
        public String getRecipeInfo(IIridescentSaplingVariant recipe) {
            return LogHelper.getStackDescription(new ItemStack(recipe.getAcceptableSoils().get(0)));
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    public static void removeRecipe(IItemStack blockSoil) {
        // Get list of existing recipes, matching with parameter

        Object soil = InputHelper.toObject(blockSoil);
        if (soil == null || !(soil instanceof ItemStack) || !InputHelper.isABlock((ItemStack)soil)) {
            LogHelper.logError("Soil must be a block.");
            return;
        }
        Block soilBlock = Block.getBlockFromItem(((ItemStack)soil).getItem());

        List<IIridescentSaplingVariant> recipes = new LinkedList<IIridescentSaplingVariant>();

        for (IIridescentSaplingVariant r : ShadowFoxAPI.treeVariants) {
            if (r != null && r instanceof IridescentSaplingBaseVariant && r.getAcceptableSoils().get(0) == soilBlock) {
                recipes.add(r);
            }
        }

        // Check if we found the recipes and apply the action
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", IridescentTree.name, soilBlock.toString()));
        }
    }

    private static class Remove extends BaseListRemoval<IIridescentSaplingVariant> {
        public Remove(List<IIridescentSaplingVariant> recipes) {
            super(IridescentTree.name, ShadowFoxAPI.treeVariants, recipes);
        }

        @Override
        public String getRecipeInfo(IIridescentSaplingVariant recipe) {
            return LogHelper.getStackDescription(new ItemStack(recipe.getAcceptableSoils().get(0)));
        }
    }
}


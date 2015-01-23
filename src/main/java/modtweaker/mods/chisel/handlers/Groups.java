package modtweaker.mods.chisel.handlers;

import static modtweaker.helpers.InputHelper.toObjects;
import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.areEqual;

import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker.mods.chisel.ChiselHelper;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.chisel.Groups")
public class Groups {
    @ZenMethod
    public static void addTransformation(String groupName, IItemStack stack) {
    	ICarvingGroup group=ChiselHelper.getGroup(groupName);
    	ICarvingVariation variation=ChiselHelper.getVariation(stack);
    	if(group==null)
    	{
    		MineTweakerAPI.getLogger().logError("Cannot find group " + groupName);
    		return;
    	}
    	if(variation==null)
    	{
    		MineTweakerAPI.getLogger().logError("Can create variation from " + stack);
    		return;
    	}
    	group.addVariation(variation);
    }

    @ZenMethod
    public static void removeTransformation(String groupName, IItemStack stack) {
    	ICarvingGroup group=ChiselHelper.getGroup(groupName);
    	ICarvingVariation variation=ChiselHelper.getVariation(stack);
    	if(group==null)
    	{
    		MineTweakerAPI.getLogger().logError("Cannot find group " + groupName);
    		return;
    	}
    	if(variation==null)
    	{
    		MineTweakerAPI.getLogger().logError("Can create variation from " + stack);
    		return;
    	}
    	if(!group.getVariations().contains(variation))
    	{
    		MineTweakerAPI.getLogger().logError("Variation doesn't exist in group " + groupName);
    		return;
    	}
    	group.removeVariation(variation.getBlock(),variation.getBlockMeta());
    }
}

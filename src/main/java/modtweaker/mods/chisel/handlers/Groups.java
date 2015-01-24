package modtweaker.mods.chisel.handlers;

import static modtweaker.helpers.InputHelper.toObjects;
import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.areEqual;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingVariation;

import minetweaker.IUndoableAction;
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
    public static void addVariation(String groupName, IItemStack stack) {
    	ICarvingGroup group=ChiselHelper.getGroup(groupName);
    	ICarvingVariation variation=ChiselHelper.makeVariation(stack);
    	if(group==null)
    	{
    		MineTweakerAPI.getLogger().logError("Cannot find group " + groupName);
    		return;
    	}
    	if(variation==null)
    	{
    		MineTweakerAPI.getLogger().logError("Can't create variation from " + stack);
    		return;
    	}
    	MineTweakerAPI.apply(new AddVariation(group,variation, stack.toString()));
    }
    
    static class AddVariation implements IUndoableAction {
    	
    	ICarvingGroup group;
    	ICarvingVariation variation;
    	String variationName;

        public AddVariation(ICarvingGroup group,ICarvingVariation variation, String variationName) {
            this.group=group;
            this.variation=variation;
            this.variationName=variationName;
        }

        @Override
    	public void apply() {
        	CarvingUtils.getChiselRegistry().addVariation(group.getName(),variation);
    	}

    	@Override
    	public boolean canUndo() {
            return group != null && variation != null;
    	}
    	
    	@Override
    	public String describe() {
            return "Adding Variation: " + variationName;
    	}
    	
    	@Override
    	public String describeUndo() {
            return "Removing Variation: " + variationName;
    	}
    	
    	@Override
    	public void undo() {
    		CarvingUtils.getChiselRegistry().removeVariation(variation.getBlock(),variation.getBlockMeta());
    	}
    	
    	@Override
    	public Object getOverrideKey() {
    		return null;
    	}
    }


    @ZenMethod
    public static void removeVariation(IItemStack stack) {
    	ICarvingVariation variation=ChiselHelper.getVariation(stack);
    	if(variation==null)
    	{
    		MineTweakerAPI.getLogger().logError("Can't find variation from " + stack);
    		return;
    	}
    	MineTweakerAPI.apply(new RemoveVariation(variation, stack.toString()));
    }
    
    
    static class RemoveVariation implements IUndoableAction {
    	
    	ICarvingVariation variation;
    	String variationName;
    	ICarvingGroup group;

        public RemoveVariation(ICarvingVariation variation, String variationName) {
            this.variation=variation;
            this.variationName=variationName;
        }

        @Override
    	public void apply() {
        	group=CarvingUtils.getChiselRegistry().getGroup(variation.getBlock(),variation.getBlockMeta());
        	CarvingUtils.getChiselRegistry().removeVariation(variation.getBlock(),variation.getBlockMeta());
    	}

    	@Override
    	public boolean canUndo() {
            return group!= null && variation != null;
    	}
    	
    	@Override
    	public String describe() {
            return "Removing Variation: " + variationName;
    	}
    	
    	@Override
    	public String describeUndo() {
            return "Adding Variation: " + variationName;
    	}
    	
    	@Override
    	public void undo() {
    		CarvingUtils.getChiselRegistry().addVariation(group.getName(),variation);
    	}
    	
    	@Override
    	public Object getOverrideKey() {
    		return null;
    	}
    }

    
}

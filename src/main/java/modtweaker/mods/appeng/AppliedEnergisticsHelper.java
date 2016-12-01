package modtweaker.mods.appeng;

import appeng.api.features.*;
import appeng.core.Api;
import com.blamejared.mtlib.helpers.ReflectionHelper;
import net.minecraft.item.ItemStack;

import java.util.*;

import static com.blamejared.mtlib.helpers.StackHelper.areEqualOrNull;

public class AppliedEnergisticsHelper {
	
	
	public static List<IInscriberRecipe> inscriber = null;
//	public static List<IGrinderEntry> grinder = null;
	
	
	static {
		try {
			inscriber = new ArrayList<>(ReflectionHelper.getFinalObject(Api.INSTANCE.registries().inscriber(), "recipes"));
//			grinder = new ArrayList<>(ReflectionHelper.getFinalObject(Api.INSTANCE.registries().grinder(), "recipes"));
		} catch(Exception e) {
		}
	}
	
	/**
	 * Compares two IInscriberRecipe objects, if they are the same or have the same inputs
	 */
	public static boolean equals(IInscriberRecipe r1, IInscriberRecipe r2) {
		if(r1 == r2) {
			return true;
		}
		
		if(r1 == null || r2 == null) {
			return false;
		}
		
		if(!areEqualOrNull(r1.getTopOptional().orElse(null), r2.getTopOptional().orElse(null))) {
			return false;
		}
		
		if(!areEqualOrNull(r1.getBottomOptional().orElse(null), r2.getBottomOptional().orElse(null))) {
			return false;
		}
		
		if(r1.getInputs() == null && r2.getInputs() != null || r1.getInputs() != null && r2.getInputs() == null) {
			return false;
		}
		
		if(r1.getInputs() != null && r2.getInputs() != null) {
			if(r1.getInputs().size() != r2.getInputs().size()) {
				return false;
			}
			
			// Check if every item in recipe 1 is in recipe 2
			for(ItemStack i1 : r1.getInputs()) {
				boolean found = false;
				
				for(ItemStack i2 : r2.getInputs()) {
					if(areEqualOrNull(i1, i2)) {
						found = true;
					}
				}
				
				if(!found) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Compares two IGrinderEntry objects, if they are the same or have the same inputs
	 */
	public static boolean equals(IGrinderEntry r1, IGrinderEntry r2) {
		if(r1 == r2) {
			return true;
		}
		
		if(r1 == null || r2 == null) {
			return false;
		}
		
		if(!areEqualOrNull(r1.getInput(), r2.getInput())) {
			return false;
		}
		
		return true;
	}
	
	
}

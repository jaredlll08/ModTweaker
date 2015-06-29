package modtweaker2.mods.appeng;

import static modtweaker2.helpers.StackHelper.areEqualOrNull;
import net.minecraft.item.ItemStack;
import appeng.api.features.IGrinderEntry;
import appeng.api.features.IInscriberRecipe;

public class AppliedEnergisticsHelper {
    /**
     * Compares two IInscriberRecipe objects, if they are the same or have the same inputs
     */
    public static boolean equals(IInscriberRecipe r1, IInscriberRecipe r2) {
        if(r1 == r2) {
            return true;
        }
        
        if (r1 == null || r2 == null) {
            return false;
        }

        if(!areEqualOrNull(r1.getTopOptional().orNull(), r2.getTopOptional().orNull())) {
            return false; 
        }
        
        if(!areEqualOrNull(r1.getBottomOptional().orNull(), r2.getBottomOptional().orNull())) {
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
        
        if (r1 == null || r2 == null) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getInput(), r2.getInput())) {
            return false;
        }

        return true;
    }
    

}

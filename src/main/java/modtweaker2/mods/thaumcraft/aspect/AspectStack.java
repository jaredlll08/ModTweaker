package modtweaker2.mods.thaumcraft.aspect;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class AspectStack {
    public Aspect aspect;
    public int amount;
    
    public AspectStack(Aspect aspect, int amount) {
        this.aspect = aspect;
        this.amount = amount;
    }
    
    public Aspect getAspect() {
        return aspect;
    }
    
    public AspectList getAspectList() {
        AspectList aspectList = new AspectList();
        aspectList.add(aspect, amount);
        return aspectList;
    }
    
    public static AspectList join(AspectList... aspectLists) {
        AspectList result = new AspectList();
        
        for(AspectList aspectList : aspectLists) {
            result.add(aspectList);
        }
        
        return result;
    }
    
    public static AspectList join(AspectStack... aspectStacks) {
        AspectList result = new AspectList();
        
        for(AspectStack stack : aspectStacks) {
            result.add(stack.getAspectList());
        }
        
        return result;
    }
}

package modtweaker2.mods.thaumcraft.aspect;

import java.util.Collections;
import java.util.List;

import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemCondition;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.IItemTransformer;
import minetweaker.api.item.IngredientOr;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.player.IPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.AspectList;


public class MCAspectStack implements IAspectStack {
    
    private final AspectStack stack;
    private final List<IAspectStack> aspects;
    
    public MCAspectStack(AspectStack stack) {
        if(stack == null) throw new IllegalArgumentException("stack cannot be null");
        
        this.stack = stack;
        this.aspects = Collections.<IAspectStack>singletonList(this);
    }
    
    @Override
    public IItemStack applyTransform(IItemStack arg0, IPlayer arg1) {
        return null;
    }

    @Override
    public boolean contains(IIngredient ingredient) {
        if(ingredient instanceof MCAspectStack) {
            List<IAspectStack> aspects = ((MCAspectStack)ingredient).getAspects();
            
            if((aspects == null) || (aspects.size() != 1)) {
                return false;
            }
            
            return matches(aspects.get(0));
        } else {
            List<IItemStack> iitems = ingredient.getItems();
            if ((iitems == null) || (iitems.size() != 1)) {
              return false;
            }
            return matches((IItemStack)iitems.get(0));
        }
    }

    @Override
    public int getAmount() {
        return this.stack.amount;
    }

    @Override
    public Object getInternal() {
        return this.stack;
    }

    @Override
    public List<IItemStack> getItems() {
        return null;
    }
    
    @Override
    public List<IAspectStack> getAspects() {
        return this.aspects;
    }

    @Override
    public List<ILiquidStack> getLiquids() {
        return Collections.emptyList();
    }

    @Override
    public String getMark() {
        return null;
    }

    @Override
    public boolean hasTransformers() {
        return false;
    }

    @Override
    public IIngredient marked(String arg0) {
        return null;
    }

    @Override
    public boolean matches(IItemStack item) {
        ItemStack internal = MineTweakerMC.getItemStack(item);
        
        if(internal != null) {
            AspectList itemAspectList = new AspectList(internal);
            
            if(itemAspectList.aspects.keySet().contains(stack.aspect.getTag())) {
                return true;
            }
        }
        
        return false;
    }
    
    public boolean matches(IAspectStack aspect) {
        if(aspect != null) {
            Object internal = aspect.getInternal();
            
            if(internal != null && internal instanceof AspectStack) {
                if(this.stack.aspect.getTag().equals(((AspectStack)internal).aspect.getTag())) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public boolean matches(ILiquidStack arg0) {
        return false;
    }

    @Override
    public IIngredient only(IItemCondition arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IIngredient or(IIngredient ingredient) {
        return new IngredientOr(this, ingredient);
    }

    @Override
    public IIngredient transform(IItemTransformer arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IAspectDefinition getDefinition() {
        return new MCAspectDefinition(stack);
    }

    @Override
    public String getName() {
        return this.stack.aspect.getTag();
    }

    @Override
    public String getDisplayName() {
        return this.stack.aspect.getName();
    }

    @Override
    public IAspectStack amount(int amount) {
        return withAmount(amount);
    }

    @Override
    public IAspectStack withAmount(int amount) {
        AspectStack result = new AspectStack(this.stack.aspect, amount);
        return new MCAspectStack(result);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<aspect:").append(stack.aspect.getName()).append('>');
            
        if(stack.amount > 1) {
            sb.append(" * ").append(stack.amount);
        }
        
        return sb.toString();
    }
}

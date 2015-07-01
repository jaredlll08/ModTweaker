package modtweaker2.helpers;

import java.util.ArrayList;
import java.util.List;

import minetweaker.api.entity.IEntity;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.oredict.IOreDictEntry;
import minetweaker.mc1710.data.NBTConverter;
import minetweaker.mc1710.item.MCItemStack;
import minetweaker.mc1710.liquid.MCLiquidStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class InputHelper {
	public static boolean isABlock(IItemStack block) {
		if (!(isABlock(toStack(block)))) {
			LogHelper.logError("Item must be a block, or you must specify a block to render as when adding a TConstruct Melting recipe");
			return false;
		} else
			return true;
	}
	
	/**
	 * Returns a string representation of the item which can also be used in scripts
	 */
	public static String getStackDescription(Object object) {
	    if(object instanceof ItemStack) {
	        ItemStack stack = (ItemStack)object;
    	    StringBuilder sb = new StringBuilder();
    	    
    	    // Creates a name like <minecraft:piston> or <appliedenergistics2:item.ItemMultiMaterial:156>
    	    sb.append('<');
    	    sb.append(Item.itemRegistry.getNameForObject(stack.getItem()));
    	    if(stack.getItemDamage() == 32767) {
    	        sb.append(":*");
    	    } else if (stack.getItemDamage() > 0) {
    	        sb.append(":").append(stack.getItemDamage());
    	    }
    	    sb.append('>');
    	    
    	    // Do we have a tag? (e.g. <Botania:specialFlower>.withTag({}) )
    	    if(stack.getTagCompound() != null)
    	    {
    	        sb.append(".withTag(");
    	        sb.append(NBTConverter.from(stack.getTagCompound(), true));
    	        sb.append(')');
    	    }
    	    
    	    // Do we have a stack size > 1?
    	    if(stack.stackSize > 1) {
    	        sb.append(" * ").append(stack.stackSize);
    	    }
    	    
    	    return sb.toString();
	    } else if (object instanceof FluidStack) {
	        FluidStack stack = (FluidStack)object;
	        StringBuilder sb = new StringBuilder();
	        
	        sb.append("<liquid:").append(stack.getFluid().getName()).append('>');
	        
	        if(stack.amount > 1) {
	            sb.append(" * ").append(stack.amount);
	        }
	        
	        return sb.toString();
	    } else if (object instanceof String) {
	        List<ItemStack> ores = OreDictionary.getOres((String)object);
	        
	        // Check if string specifies an oredict entry
	        if(!ores.isEmpty()) {
	            return "<ore:" + (String)object + ">";
	        } else {
	            return "\"" + (String)object + "\"";
	        }
	    } else if (object != null) {
	        return "\"" + object.toString() + "\"";
	    } else {
	        return "null";
	    }
	}
	
    public static String getArrayDescription(List objects) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(Object object : objects) {
            sb.append(InputHelper.getStackDescription(object)).append(", ");
        }
        sb.setLength(sb.length() - 2);
        sb.append(']');
        
        return sb.toString();
    }

	public static IItemStack[] toStacks(IIngredient[] iIngredient) {
		ArrayList<IItemStack> stacks = new ArrayList<IItemStack>();
		for (IIngredient ing : iIngredient) {
			for (IItemStack stack : ing.getItems()) {
				stacks.add(stack);
			}
		}
		IItemStack[] returnArray = new IItemStack[stacks.size()];
			for(int i = 0; i < returnArray.length;i++){
				returnArray[i] = stacks.get(i);
			}
		return returnArray;
	}

	public static boolean isABlock(ItemStack block) {
		return block.getItem() instanceof ItemBlock;
	}

	public static Entity toEntity(IEntity iEntity) {
		return null;
	}

	public static ItemStack toStack(IItemStack iStack) {
		if (iStack == null)
			return null;
		else {
			Object internal = iStack.getInternal();
			if (internal == null || !(internal instanceof ItemStack)) {
				LogHelper.logError("Not a valid item stack: " + iStack);
			}

			return (ItemStack) internal;
		}
	}
	
	public static IIngredient toIngredient(ItemStack stack) {
        return toIItemStack(stack);
	}
	
	public static IIngredient toIngredient(FluidStack stack) {
	    if(stack == null)
	        return null;
	    
	    return new MCLiquidStack(stack);
	}
	
	public static IItemStack toIItemStack(ItemStack stack) {
	    if(stack == null) {
	        return null;
	    }
	    
	    return new MCItemStack(stack);
	}
	
	public static ILiquidStack toILiquidStack(FluidStack stack) {
	    if(stack == null) {
	        return null;
	    }
	    
	    return new MCLiquidStack(stack);
	}

	public static ItemStack[] toStacks(IItemStack[] iStack) {
		if (iStack == null)
			return null;
		else {
			ItemStack[] output = new ItemStack[iStack.length];
			for (int i = 0; i < iStack.length; i++) {
				output[i] = toStack(iStack[i]);
			}

			return output;
		}
	}

	public static Object toObject(IIngredient iStack) {
		if (iStack == null)
			return null;
		else {
			if (iStack instanceof IOreDictEntry) {
				return toString((IOreDictEntry) iStack);
			} else if (iStack instanceof IItemStack) {
				return toStack((IItemStack) iStack);
			} else
				return null;
		}
	}

	public static Object[] toObjects(IIngredient[] ingredient) {
		if (ingredient == null)
			return null;
		else {
			Object[] output = new Object[ingredient.length];
			for (int i = 0; i < ingredient.length; i++) {
				if (ingredient[i] != null) {
					output[i] = toObject(ingredient[i]);
				} else
					output[i] = "";
			}

			return output;
		}
	}

	public static Object[] toShapedObjects(IIngredient[][] ingredients) {
		if (ingredients == null)
			return null;
		else {
			ArrayList prep = new ArrayList();
			prep.add("abc");
			prep.add("def");
			prep.add("ghi");
			char[][] map = new char[][]{{'a', 'b', 'c'}, {'d', 'e', 'f'}, {'g', 'h', 'i'}};
			for (int x = 0; x < ingredients.length; x++) {
				if (ingredients[x] != null) {
					for (int y = 0; y < ingredients[x].length; y++) {
						if (ingredients[x][y] != null && x < map.length && y < map[x].length) {
							prep.add(map[x][y]);
							prep.add(toObject(ingredients[x][y]));
						}
					}
				}
			}
			return prep.toArray();
		}
	}

	public static String toString(IOreDictEntry entry) {
		return ((IOreDictEntry) entry).getName();
	}

	public static FluidStack toFluid(ILiquidStack iStack) {
		if (iStack == null) {
			return null;
		} else
			return FluidRegistry.getFluidStack(iStack.getName(), iStack.getAmount());
	}

	public static Fluid getFluid(ILiquidStack iStack) {
		if (iStack == null) {
			return null;
		} else
			return FluidRegistry.getFluid(iStack.getName());

	}

	public static FluidStack[] toFluids(IIngredient[] input) {
		return toFluids((IItemStack[]) input);
	}

	public static FluidStack[] toFluids(ILiquidStack[] iStack) {
		FluidStack[] stack = new FluidStack[iStack.length];
		for (int i = 0; i < stack.length; i++)
			stack[i] = toFluid(iStack[i]);
		return stack;
	}
}

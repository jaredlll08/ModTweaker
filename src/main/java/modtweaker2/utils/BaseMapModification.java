package modtweaker2.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class BaseMapModification<K, V> extends BaseUndoable {
    protected Map<K, V> map;
    protected final HashMap<K, V> recipes;
    protected final HashMap<K, V> successful;
    
    protected BaseMapModification(String name, Map<K, V> map) {
        super(name);
        this.map = map;
        this.recipes = new HashMap<K, V>();
        this.successful = new HashMap<K, V>();
    }

    @Override
    public boolean canUndo() {
        return !recipes.isEmpty();
    }
    
    @Override
    protected String getRecipeInfo() {
        if(!recipes.isEmpty())
        {
            StringBuilder sb = new StringBuilder();
            for(Entry<K, V> recipe : recipes.entrySet()) {
                if(recipe != null) {
                    sb.append(getRecipeInfo(recipe)).append(", ");
                }
            }
            
            if(sb.length() > 0) {
                sb.setLength(sb.length() - 2);
            }
            
            return sb.toString();
        }
        
        return super.getRecipeInfo();
    }
    
    /**
     * This method must be overwritten by the extending classes. It should return
     * the name of the key item, for which the recipe is for. For example for machines
     * which produce new items, it should return the name of the ouput. For machines
     * which are processing items (like a pulverizer) it should return the name of the
     * the input. Another example would be the name of the enchantmant for a thaumcraft
     * infusion recipe.
     */
    protected abstract String getRecipeInfo(Entry<K, V> recipe);
}

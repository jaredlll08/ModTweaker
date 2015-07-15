package modtweaker2.utils;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import modtweaker2.helpers.LogHelper;

public abstract class BaseMapAddition<K, V> extends BaseMapModification<K, V> {
    
    protected final HashMap<K, V> overwritten;
    
    protected BaseMapAddition(String name, Map<K, V> map) {
        super(name, map);
        this.overwritten = new HashMap<K, V>();
    }
    
    protected BaseMapAddition(String name, Map<K, V> map, Map<K, V> recipes) {
        this(name, map);
        this.recipes.putAll(recipes);
    }

    @Override
    public void apply() {
        if(recipes.isEmpty())
            return;
        
        for(Entry<K, V> entry : recipes.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            V oldValue = map.put(key, value);
            
            if(oldValue != null) {
                LogHelper.logWarning(String.format("Overwritten %s Recipe for %s", name, getRecipeInfo( new AbstractMap.SimpleEntry<K, V>(entry.getKey(), value))));
                overwritten.put(key, oldValue);
            }
            
            successful.put(key, value);
        }
    }

    @Override
    public void undo() {
        if(successful.isEmpty() && overwritten.isEmpty())
            return;

        for(Entry<K, V> entry : successful.entrySet()) {
            K key = entry.getKey();
            V value = map.remove(key);

            if(value == null) {
                LogHelper.logError(String.format("Error removing %s Recipe: null object", name));
            }
        }
        
        for(Entry<K, V> entry : overwritten.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            V oldValue = map.put(key, value);
            
            if(oldValue != null) {
                LogHelper.logWarning(String.format("Overwritten %s Recipe which should not exist for %s", name, getRecipeInfo(new AbstractMap.SimpleEntry<K, V>(entry.getKey(), value))));
            }
        }
    }

    @Override
    public String describe() {
        return String.format("[ModTweaker2] Adding %d %s Recipe(s) for %s", recipes.size(), name, getRecipeInfo());
    }

    @Override
    public String describeUndo() {
        return String.format("[ModTweaker2] Removing %d %s Recipe(s) for %s", recipes.size(), name, getRecipeInfo());
    }
}

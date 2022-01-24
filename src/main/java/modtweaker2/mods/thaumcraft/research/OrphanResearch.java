package modtweaker2.mods.thaumcraft.research;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import minetweaker.IUndoableAction;
import modtweaker2.helpers.LogHelper;
import org.apache.commons.lang3.ArrayUtils;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;
import thaumcraft.api.research.ResearchItem;

public class OrphanResearch implements IUndoableAction {
    String key;
    private final Set<String> children = new HashSet<>();
    private final Set<String> secretChildren = new HashSet<>();
    private final Set<String> siblings = new HashSet<>();

    public OrphanResearch(String victim) {
        key = victim;
    }

    private void remove(String cur, String[] refs, Set<String> set, Consumer<String[]> consumer) {
        if (refs == null) return;
        for (int i = 0; i < refs.length; i++) {
            if (refs[i] != null && refs[i].equals(key)) {
                set.add(cur);
                consumer.accept(ArrayUtils.remove(refs, i));
                return;
            }
        }
    }

    private void add(Set<String> set, Function<ResearchItem, String[]> function, BiConsumer<ResearchItem, String[]> consumer) {
        for (String research : set) {
            ResearchItem researchItem = ResearchCategories.getResearch(research);
            if (researchItem == null)
                LogHelper.logWarning("Missing research: " + research);
            else
                consumer.accept(researchItem, ArrayUtils.add(function.apply(researchItem), key));
        }
    }

    @Override
    public void apply() {
        for (ResearchCategoryList category : ResearchCategories.researchCategories.values()) {
            for (Entry<String, ResearchItem> entry : category.research.entrySet()) {
                String research = entry.getKey();
                ResearchItem researchItem = entry.getValue();
                remove(research, researchItem.parents, children, researchItem::setParents);
                remove(research, researchItem.parentsHidden, secretChildren, researchItem::setParentsHidden);
                remove(research, researchItem.siblings, siblings, researchItem::setSiblings);
            }
        }
    }

    @Override
    public String describe() {
        return "Orphaning Research: " + key;
    }

    @Override
    public boolean canUndo() {
        return children.size() > 0 || secretChildren.size() > 0 || siblings.size() > 0;
    }

    @Override
    public void undo() {
        add(children, r -> r.parents, ResearchItem::setParents);
        add(secretChildren, r -> r.parentsHidden, ResearchItem::setParentsHidden);
        add(siblings, r -> r.siblings, ResearchItem::setSiblings);
    }

    @Override
    public String describeUndo() {
        return "Reattaching Research: " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}
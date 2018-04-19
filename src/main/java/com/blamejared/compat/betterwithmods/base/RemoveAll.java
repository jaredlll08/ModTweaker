package com.blamejared.compat.betterwithmods.base;

import com.blamejared.mtlib.utils.BaseAction;

import java.util.List;

public class RemoveAll<T> extends BaseAction {
    private List<T> list;

    public RemoveAll(String name, List<T> list) {
        super(name);
        this.list = list;
    }

    @Override
    public void apply() {
        list.clear();
    }

    @Override
    protected String getRecipeInfo() {
        return String.format("Removing all recipes for %s", name);
    }
}

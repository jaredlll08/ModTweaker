package modtweaker2.mods.mekanism.gas;

import java.util.List;

import mekanism.api.gas.GasStack;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemCondition;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.IItemTransformer;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.player.IPlayer;


public class MCGasStack implements IGasStack {
    private final GasStack stack;

    public MCGasStack(GasStack stack) {
        this.stack = stack;
    }

    @Override
    public IGasDefinition getDefinition() {
        return new MCGasDefinition(stack.getGas());
    }

    @Override
    public String getName() {
        return stack.getGas().getName();
    }

    @Override
    public String getDisplayName() {
        return stack.getGas().getLocalizedName();
    }

    @Override
    public int getAmount() {
        return stack.amount;
    }

    @Override
    public IGasStack withAmount(int amount) {
        GasStack result = new GasStack(stack.getGas(), amount);
        return new MCGasStack(result);
    }

    @Override
    public Object getInternal() {
        return stack;
    }
    
    @Override
    public String toString() {
        return "<gas:" + stack.getGas().getName() + ">";
    }

    @Override
    public IIngredient amount(int amount) {
        return withAmount(amount);
    }

    @Override
    public IItemStack applyTransform(IItemStack arg0, IPlayer arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean contains(IIngredient arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<IItemStack> getItems() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ILiquidStack> getLiquids() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getMark() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasTransformers() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public IIngredient marked(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean matches(IItemStack arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean matches(ILiquidStack arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public IIngredient only(IItemCondition arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IIngredient or(IIngredient arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IIngredient transform(IItemTransformer arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
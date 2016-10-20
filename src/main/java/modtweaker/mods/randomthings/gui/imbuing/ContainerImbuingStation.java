package modtweaker.mods.randomthings.gui.imbuing;

import com.blamejared.ctgui.api.ContainerBase;
import com.blamejared.ctgui.api.SlotRecipe;
import com.blamejared.ctgui.api.SlotRecipeOutput;
import com.blamejared.ctgui.client.InventoryFake;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Created by Jared.
 */
public class ContainerImbuingStation extends ContainerBase {

    public IInventory inventory = new InventoryFake(5);

    public ContainerImbuingStation(InventoryPlayer invPlayer) {
        this.addSlotToContainer(new SlotRecipe(this.inventory, 0, 80, 9));
        this.addSlotToContainer(new SlotRecipe(this.inventory, 1, 35, 54));
        this.addSlotToContainer(new SlotRecipe(this.inventory, 2, 80, 99));
        this.addSlotToContainer(new SlotRecipe(this.inventory, 3, 80, 54));
        this.addSlotToContainer(new SlotRecipeOutput(this.inventory, 4, 125, 54));

        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 126 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(invPlayer, i, 8 + i * 18, 184));
        }

    }

    protected Slot addSlotToContainer(Slot slotIn) {
        if (slotIn instanceof SlotRecipe) {
            this.getRecipeSlots().add((SlotRecipe) slotIn);
        }

        return super.addSlotToContainer(slotIn);
    }

}

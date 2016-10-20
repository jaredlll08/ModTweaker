package modtweaker.mods.randomthings.gui.imbuing;

import com.blamejared.ctgui.api.ContainerBase;
import com.blamejared.ctgui.api.GuiBase;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Jared.
 */
public class GuiImbuingStation extends GuiBase {

    public GuiImbuingStation(ContainerBase container) {
        super(container, 176, 208, false);
    }

    @Override
    public void initGui() {
        super.initGui();
        add.xPosition = add.xPosition + 20;
        remove.xPosition = remove.xPosition + 20;
    }

    @Override
    public String getOutputAdd() {
        return String.format("mods.randomthings.ImbuingStation.add(%s,%s,%s,%s,%s);", container.getRecipeSlots().get(4).getItemString(), container.getRecipeSlots().get(0).getItemString(), container.getRecipeSlots().get(1).getItemString(), container.getRecipeSlots().get(2).getItemString(), container.getRecipeSlots().get(3).getItemString());
    }

    @Override
    public String getOutputRemove() {
        return String.format("mods.randomthings.ImbuingStation.remove(%s);", container.getRecipeSlots().get(4).getItemString());
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation("randomthings:textures/gui/imbuingStation.png");
    }
}

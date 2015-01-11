package modtweaker.mods.tconstruct.handlers;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import modtweaker.helpers.ReflectionHelper;
import modtweaker.mods.tconstruct.TConstructHelper;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ArrowMaterial;
import tconstruct.library.tools.BowMaterial;
import tconstruct.library.tools.ToolMaterial;

@ZenClass("mods.tconstruct.ToolStats")
public class ToolStats {
    @ZenMethod
    public static void set(String material, @Optional String name, int level, int durability, int speed, int damage, double handle, int reinforced, double stonebound, String style, String ability) {
        if (name == null) name = material + " ";
        MineTweakerAPI.apply(new SetToolStats(material, "", new ToolMaterial(material, name, level, durability, speed, damage, (float) handle, reinforced, (float) stonebound, style, ability)));
    }

    @ZenMethod
    public static void setDisplayName(String material, String name) {
        MineTweakerAPI.apply(new SetToolStats(material, "displayName", name));
    }

    @ZenMethod
    public static void setHarvestLevel(String material, int value) {
        MineTweakerAPI.apply(new SetToolStats(material, "harvestLevel", value));
    }

    @ZenMethod
    public static void setDurability(String material, int value) {
        MineTweakerAPI.apply(new SetToolStats(material, "durability", value));
    }

    @ZenMethod
    public static void setSpeed(String material, int value) {
        MineTweakerAPI.apply(new SetToolStats(material, "miningspeed", value));
    }

    @ZenMethod
    public static void setDamage(String material, int value) {
        MineTweakerAPI.apply(new SetToolStats(material, "attack", value));
    }

    @ZenMethod
    public static void setHandleModifier(String material, double value) {
        MineTweakerAPI.apply(new SetToolStats(material, "handleModifier", (float) value));
    }

    @ZenMethod
    public static void setReinforcedLevel(String material, int value) {
        MineTweakerAPI.apply(new SetToolStats(material, "reinforced", value));
    }

    @ZenMethod
    public static void setStoneboundLevel(String material, double value) {
        MineTweakerAPI.apply(new SetToolStats(material, "stonebound", (float) value));
    }

    @ZenMethod
    public static void setStyle(String material, String name) {
        MineTweakerAPI.apply(new SetToolStats(material, "tipStyle", name));
    }

    @ZenMethod
    public static void setAbility(String material, String name) {
        MineTweakerAPI.apply(new SetToolStats(material, "ability", name));
    }

    //Sets various variables with reflection, making my life partially easier :D
    private static class SetToolStats implements IUndoableAction {
        protected int id;
        protected Object old;
        protected Object fresh;
        protected final String material;
        protected final String field;
        protected final Object value;

        public SetToolStats(String material, String field, Object value) {
            this.material = material;
            this.field = field;
            this.value = value;
        }

        @Override
        public void apply() {
            old = TConstructRegistry.toolMaterialStrings.get(material);
            id = TConstructHelper.getIDFromString(material);
            if (id != -1 && old != null) {
                if (value instanceof ToolMaterial) {
                    fresh = (ToolMaterial) value;
                } else {
                    ToolMaterial t = (ToolMaterial) old;
                    fresh = new ToolMaterial(t.materialName, t.displayName, t.harvestLevel, t.durability, t.miningspeed, t.attack, t.handleModifier, t.reinforced, t.stonebound, t.tipStyle, t.ability);
                    ReflectionHelper.setPrivateValue(ToolMaterial.class, fresh, field, value);
                }

                TConstructRegistry.toolMaterials.put(id, (ToolMaterial) fresh);
                TConstructRegistry.toolMaterialStrings.put(material, (ToolMaterial) fresh);
            }
        }

        @Override
        public boolean canUndo() {
            return TConstructRegistry.toolMaterialStrings != null && id != -1 && old != null;
        }

        @Override
        public void undo() {
            TConstructRegistry.toolMaterials.put(id, (ToolMaterial) old);
            TConstructRegistry.toolMaterialStrings.put(material, (ToolMaterial) old);
        }

        @Override
        public String describe() {
            return "Changing material stats field : + " + field + " for " + material;
        }

        @Override
        public String describeUndo() {
            return "Undoing change of material stats field : + " + field + " for " + material;
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    //Bow Stats
    @ZenMethod
    public static void setBowStats(String material, int durability, int drawspeed, double flightspeed) {
        MineTweakerAPI.apply(new SetBowStats(material, "", new BowMaterial(durability, drawspeed, (float) flightspeed)));
    }

    @ZenMethod
    public static void setBowDurability(String material, int value) {
        MineTweakerAPI.apply(new SetBowStats(material, "durability", value));
    }

    @ZenMethod
    public static void setBowDrawspeed(String material, int value) {
        MineTweakerAPI.apply(new SetBowStats(material, "drawspeed", value));
    }

    @ZenMethod
    public static void setBowFlightSpeed(String material, double value) {
        MineTweakerAPI.apply(new SetBowStats(material, "flightSpeedMax", (float) value));
    }

    // Bow Stats
    private static class SetBowStats extends SetToolStats {
        public SetBowStats(String material, String field, Object value) {
            super(material, field, value);
        }

        @Override
        public void apply() {
            id = TConstructHelper.getIDFromString(material);
            old = TConstructRegistry.bowMaterials.get(id);
            if (id != -1 && old != null) {
                if (value instanceof BowMaterial) {
                    fresh = (BowMaterial) value;
                } else {
                    BowMaterial b = (BowMaterial) old;
                    fresh = new BowMaterial(b.durability, b.drawspeed, b.flightSpeedMax);
                    ReflectionHelper.setPrivateValue(BowMaterial.class, fresh, field, value);
                }

                TConstructRegistry.bowMaterials.put(id, (BowMaterial) fresh);
            }
        }

        @Override
        public boolean canUndo() {
            return TConstructRegistry.bowMaterials != null && id != -1 && old != null;
        }

        @Override
        public void undo() {
            TConstructRegistry.bowMaterials.put(id, (BowMaterial) old);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    //Arrow Stats
    @ZenMethod
    public static void setArrowStats(String material, double mass, double breakChance, double accuracy) {
        MineTweakerAPI.apply(new SetArrowStats(material, "", new ArrowMaterial((float) mass, (float) breakChance, (float) accuracy)));
    }

    @ZenMethod
    public static void setArrowMass(String material, double value) {
        MineTweakerAPI.apply(new SetArrowStats(material, "mass", (float) value));
    }

    @ZenMethod
    public static void setArrowBreakChance(String material, double value) {
        MineTweakerAPI.apply(new SetArrowStats(material, "breakChance", (float) value));
    }

    @ZenMethod
    public static void setArrowAccuracy(String material, double value) {
        MineTweakerAPI.apply(new SetArrowStats(material, "accuracy", (float) value));
    }

    // Bow Stats
    private static class SetArrowStats extends SetToolStats {
        public SetArrowStats(String material, String field, Object value) {
            super(material, field, value);
        }

        @Override
        public void apply() {
            id = TConstructHelper.getIDFromString(material);
            old = TConstructRegistry.arrowMaterials.get(id);
            if (id != -1 && old != null) {
                if (value instanceof ArrowMaterial) {
                    fresh = (ArrowMaterial) value;
                } else {
                    ArrowMaterial a = (ArrowMaterial) old;
                    fresh = new ArrowMaterial(a.mass, a.breakChance, a.accuracy);
                    ReflectionHelper.setPrivateValue(ArrowMaterial.class, fresh, field, value);
                }

                TConstructRegistry.arrowMaterials.put(id, (ArrowMaterial) fresh);
            }
        }

        @Override
        public boolean canUndo() {
            return TConstructRegistry.arrowMaterials != null && id != -1 && old != null;
        }

        @Override
        public void undo() {
            TConstructRegistry.arrowMaterials.put(id, (ArrowMaterial) old);
        }
    }
}

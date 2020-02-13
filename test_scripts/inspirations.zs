// brewing recipes
mods.inspirations.Cauldron.addBrewingRecipe("minecraft:invisibility", "minecraft:thick", <minecraft:diamond>);
mods.inspirations.Cauldron.addBrewingRecipe("minecraft:healing", "minecraft:thick", <ore:gemEmerald>);
mods.inspirations.Cauldron.removeBrewingRecipe("inspirations:haste");
mods.inspirations.Cauldron.removeBrewingRecipe("minecraft:awkward", "minecraft:water", <minecraft:nether_wart>);

// dye recipes
mods.inspirations.Cauldron.addDyeRecipe(<minecraft:diamond>, <minecraft:emerald>, "blue");
mods.inspirations.Cauldron.addDyeRecipe(<minecraft:emerald>, <minecraft:diamond>, "lime");
mods.inspirations.Cauldron.addDyeRecipe(<minecraft:nether_star>, <minecraft:diamond>, "red", 2);
mods.inspirations.Cauldron.removeDyeRecipe(<*>, <*>, "blue");
mods.inspirations.Cauldron.removeDyeRecipe(<inspirations:carpeted_trapdoor_white>);

// fluid recipes
mods.inspirations.Cauldron.addFluidRecipe(<minecraft:blaze_rod>, <minecraft:blaze_powder> * 2, <liquid:lava>);
mods.inspirations.Cauldron.addFluidRecipe(<minecraft:grass>, <ore:treeLeaves> * 4, <liquid:water>);
mods.inspirations.Cauldron.addFluidRecipe(<minecraft:water_bucket>, <minecraft:ice>, <liquid:lava>, 1, true);
mods.inspirations.Cauldron.addFluidRecipe(<minecraft:nether_star>, <minecraft:stick>.withTag({RepairCost: 0, display: {Name: "True Stick"}}), <liquid:lava>);
mods.inspirations.Cauldron.addFluidTransform(<liquid:lava>, <minecraft:blaze_powder>, <liquid:water>, 2, false);
mods.inspirations.Cauldron.removeFluidRecipe(<minecraft:beetroot_soup>);
mods.inspirations.Cauldron.removeFluidTransform(<liquid:beetroot_soup>, <minecraft:beetroot>, <liquid:water>);

// cauldron filling
mods.inspirations.Cauldron.addFillRecipe(<ore:gemDiamond>, <liquid:water>, 2, <minecraft:emerald>);
mods.inspirations.Cauldron.addFillRecipe(<minecraft:emerald>, <liquid:lava>);
mods.inspirations.Cauldron.addFillRecipe(<minecraft:cobblestone>, <liquid:lava>, 1, null, true);
mods.inspirations.Cauldron.addFillRecipe(<minecraft:stone>, <liquid:lava>, 1, <minecraft:glass_bottle>, true);
mods.inspirations.Cauldron.removeFillRecipe(<minecraft:beetroot_soup>);
mods.inspirations.Cauldron.removeFillRecipe(<*>, <liquid:mushroom_stew>);

// potions
mods.inspirations.Cauldron.addPotionRecipe(<minecraft:golden_apple>, <minecraft:apple>, "minecraft:regeneration", 2);

// fluid mix
mods.inspirations.Cauldron.addFluidMix(<minecraft:golden_apple>, <liquid:lava>, <liquid:iron>);
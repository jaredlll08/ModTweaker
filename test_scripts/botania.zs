mods.botania.Apothecary.addRecipe(<minecraft:apple>, [<minecraft:stone>, <minecraft:nether_star>]);
mods.botania.Apothecary.addRecipe("hydroangeas", [<minecraft:apple>, <minecraft:cobblestone>]);
mods.botania.Apothecary.removeRecipe(<botania:specialflower>.withTag({type: "fallenKanade"}));
mods.botania.Apothecary.removeRecipe("thermalily");

mods.botania.Brew.addRecipe([<minecraft:apple>, <minecraft:nether_star>], "healing");
mods.botania.Brew.removeRecipe("resistance");

mods.botania.ElvenTrade.addRecipe([<minecraft:apple>], [<minecraft:nether_star>]);
mods.botania.ElvenTrade.removeRecipe(<botania:quartz:5>);

mods.botania.ManaInfusion.addInfusion(<minecraft:apple>, <minecraft:nether_star>, 140);
mods.botania.ManaInfusion.addAlchemy(<minecraft:stone>, <minecraft:bookshelf>, 40);
mods.botania.ManaInfusion.addConjuration(<minecraft:bookshelf>, <minecraft:book>, 500);
mods.botania.ManaInfusion.removeRecipe(<minecraft:ice>);

mods.botania.Orechid.addOre(<ore:blockIron>, 40000);
mods.botania.Orechid.addOre("blockQuartz", 99999);
mods.botania.Orechid.removeOre(<ore:oreCopper>);
mods.botania.Orechid.removeOre("oreRedstone");

mods.botania.PureDaisy.addRecipe(<minecraft:leaves>, <minecraft:red_flower>, 4);
mods.botania.PureDaisy.addRecipe(<minecraft:beacon>, <minecraft:torch>);
mods.botania.PureDaisy.removeRecipe(<botania:livingwood>);

mods.botania.RuneAltar.addRecipe(<minecraft:apple>, [<minecraft:nether_star>, <minecraft:beacon>, <minecraft:stone>], 40);
mods.botania.RuneAltar.removeRecipe(<botania:rune>);

mods.botania.Lexicon.addCategory("mtdev");
mods.botania.Lexicon.addEntry("deventry", "mtdev", <minecraft:nether_star>);
mods.botania.Lexicon.setEntryKnowledgeType("deventry", "deventry");
mods.botania.Lexicon.addTextPage("devpage", "deventry", 0);
mods.botania.Lexicon.addRunePage("devpage2", "deventry", 1, [<minecraft:apple>], [[<minecraft:nether_star>, <minecraft:beacon>, <minecraft:stone>]], [40]);
mods.botania.Lexicon.addPetalPage("devpage3", "deventry", 2, [<minecraft:apple>], [[<minecraft:stone>, <minecraft:nether_star>]]);

mods.botania.Lexicon.removeCategory("botania.category.generationFlowers");
mods.botania.Lexicon.removeEntry("botania.entry.relics");

mods.botania.Knowledge.registerKnowledgeType("deventry", "DARK_AQUA", true);
mods.botania.Knowledge.registerKnowledgeType("jared", "GOLD", false);
recipes.addShapeless(<botania:lexicon>.withTag({"knowledge.jared" : true}), [[<botania:lexicon>, <minecraft:diamond>]]);

var lex = <botania:lexicon>;
lex.addBotaniaKnowledge("jared");
lex.addBotaniaKnowledge("deventry");

recipes.addShapeless("botaniaKnowledgeAddition", lex, [<botania:lexicon>, <minecraft:diamond>]);

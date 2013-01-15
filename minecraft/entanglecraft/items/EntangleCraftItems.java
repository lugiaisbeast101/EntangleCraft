package entanglecraft.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import entanglecraft.blocks.EntangleCraftBlocks;

public class EntangleCraftItems {
	public static final int NetherEssence_ID = 8600;
	public static final int DeviceG_ID = 8601;
	public static final int DeviceR_ID = 8602; 
	public static final int DeviceY_ID = 8603;
	public static final int DeviceB_ID = 8604;
	public static final int NethermonicDiamond_ID = 8605;
	public static final int LambdaCore_ID = 8606;
	public static final int Transformer_ID = 8607;
	public static final int ReverseTransformer_ID = 8608;
	public static final int Transmitter_ID = 8609;
	public static final int FrShard_ID = 8610;
	public static final int BlueShard_ID = 8611;
	public static final int RedShard_ID = 8612;
	public static final int YelShard_ID = 8613;
	public static final int ImbuedShard_ID = 8614;
	public static final int InductionCircuit_ID = 8615;
	public static final int Circuit_ID = 8616;
	public static final int InclusiveFilter_ID = 8617;
	public static final int ExclusiveFilter_ID = 8618;
	public static final int DestroyFilter_ID = 8625;     // Sorry if this hurts your OCD.
	public static final int DontDestroyFilter_ID = 8626; //
	public static final int SuperInductionCircuit_ID = 8619;
	public static final int TPScroll_ID = 8620;
	public static final int ShardPickG_ID = 8621;
	public static final int ShardPickR_ID = 8622;
	public static final int ShardPickY_ID = 8623;
	public static final int ShardPickB_ID = 8624;
		
	public static final Item ItemNetherEssence = new ItemLambda(NetherEssence_ID).setIconIndex(87).setItemName("ItemNetherEssence");
	public static final Item ItemDeviceG = new ItemDevice(DeviceG_ID).setIconIndex(90).setItemName("ItemDeviceG");
	public static final Item ItemDeviceR = new ItemDevice(DeviceR_ID).setIconIndex(91).setItemName("ItemDeviceR");
	public static final Item ItemDeviceY = new ItemDevice(DeviceY_ID).setIconIndex(92).setItemName("ItemDeviceY");
	public static final Item ItemDeviceB = new ItemDevice(DeviceB_ID).setIconIndex(93).setItemName("ItemDeviceB");
	public static final Item ItemNethermonicDiamond = new ItemLambda(NethermonicDiamond_ID).setIconIndex(88).setItemName("ItemNethermonicDiamond");
	public static final Item ItemLambdaCore = new ItemLambda(LambdaCore_ID).setIconIndex(89).setItemName("ItemLambdaCore");
	public static final Item ItemTransformer = new ItemLambda(Transformer_ID).setIconIndex(80).setItemName("ItemTransformer");
	public static final Item ItemReverseTransformer = new ItemLambda(ReverseTransformer_ID).setIconIndex(81).setItemName("ItemReverseTransformer");
	public static final Item ItemTransmitter = new ItemLambda(Transmitter_ID).setIconIndex(82).setItemName("ItemTransmitter");
	public static final Item ItemFrShard = new ItemLambda(FrShard_ID).setIconIndex(86).setItemName("ItemFrShard");
	public static final Item ItemBlueShard = new ItemShard(BlueShard_ID,0).setIconIndex(83).setItemName("ItemBlueShard").setMaxDamage(256);
	public static final Item ItemRedShard = new ItemShard(RedShard_ID,1).setIconIndex(84).setItemName("ItemRedShard").setMaxDamage(256);
	public static final Item ItemYelShard = new ItemShard(YelShard_ID,2).setIconIndex(85).setItemName("ItemYelShard").setMaxDamage(256);
	public static final Item ItemImbuedShard = new ItemShard(ImbuedShard_ID,4).setIconIndex(94).setItemName("ItemImbuedShard");
	public static final Item ItemInductionCircuit = new ItemLambda(InductionCircuit_ID).setIconIndex(95).setItemName("ItemInductionCircuit").setMaxStackSize(1);
	public static final Item ItemCircuit = new ItemLambda(Circuit_ID).setIconIndex(96).setItemName("ItemCircuit");
	public static final Item ItemInclusiveFilter = new ItemLambda(InclusiveFilter_ID).setIconIndex(97).setItemName("ItemInclusiveFilter").setMaxStackSize(1);
	public static final Item ItemExclusiveFilter = new ItemLambda(ExclusiveFilter_ID).setIconIndex(98).setItemName("ItemExclusiveFilter").setMaxStackSize(1);
	public static final Item ItemDestroyFilter = new ItemLambda(DestroyFilter_ID).setIconIndex(97+16).setItemName("ItemDestroyFilter").setMaxStackSize(1);
	public static final Item ItemDontDestroyFilter = new ItemLambda(DontDestroyFilter_ID).setIconIndex(97+17).setItemName("ItemDontDestroyFilter").setMaxStackSize(1);
	public static final Item ItemSuperInductionCircuit = new ItemLambda(SuperInductionCircuit_ID).setIconIndex(99).setItemName("ItemSuperInductionCircuit").setMaxStackSize(1);
	public static final Item ItemTPScroll = new ItemShard(TPScroll_ID,3).setIconIndex(101).setItemName("ItemTPScroll").setMaxDamage(1);
	public static final Item ItemShardPickG = new ItemShardPick(ShardPickG_ID).setIconIndex(102).setItemName("ItemShardPickG").setMaxDamage(0);
	public static final Item ItemShardPickR = new ItemShardPick(ShardPickR_ID).setIconIndex(103).setItemName("ItemShardPickR").setMaxDamage(0);
	public static final Item ItemShardPickY = new ItemShardPick(ShardPickY_ID).setIconIndex(104).setItemName("ItemShardPickY").setMaxDamage(0);
	public static final Item ItemShardPickB = new ItemShardPick(ShardPickB_ID).setIconIndex(105).setItemName("ItemShardPickB").setMaxDamage(0);
	
	public static final Item[] itemList = new Item[] {
		ItemNetherEssence, ItemDeviceG, ItemDeviceR, ItemDeviceY, ItemDeviceB, ItemNethermonicDiamond,
		ItemLambdaCore, ItemTransformer, ItemReverseTransformer, ItemTransmitter, ItemFrShard,
		ItemBlueShard, ItemRedShard, ItemYelShard, ItemImbuedShard, ItemInductionCircuit, ItemCircuit,
		ItemInclusiveFilter, ItemExclusiveFilter, ItemDestroyFilter, ItemDontDestroyFilter, ItemSuperInductionCircuit,
		ItemTPScroll, ItemShardPickG, ItemShardPickR, ItemShardPickY, ItemShardPickB
	};
	
	public static int[] defaultIdsList = new int[] {
		NetherEssence_ID, DeviceG_ID, DeviceR_ID, DeviceY_ID, DeviceB_ID, NethermonicDiamond_ID,
		LambdaCore_ID, Transformer_ID, ReverseTransformer_ID, Transmitter_ID, FrShard_ID, BlueShard_ID,
		RedShard_ID, YelShard_ID, ImbuedShard_ID, InductionCircuit_ID, Circuit_ID, InclusiveFilter_ID,
		ExclusiveFilter_ID, DestroyFilter_ID, DontDestroyFilter_ID, SuperInductionCircuit_ID, TPScroll_ID,
		ShardPickG_ID, ShardPickR_ID, ShardPickY_ID, ShardPickB_ID
	};
	
public static void addItems(){
	itemInitializing();
	
    LanguageRegistry.addName(ItemDeviceG, "Lambda Device : G");
    LanguageRegistry.addName(ItemDeviceR, "Lambda Device : R");
    LanguageRegistry.addName(ItemDeviceY, "Lambda Device : Y");
    LanguageRegistry.addName(ItemDeviceB, "Lambda Device : B");
    LanguageRegistry.addName(ItemLambdaCore, "Lambda Core");
    LanguageRegistry.addName(ItemNetherEssence, "Nethermonic Essence");
    LanguageRegistry.addName(ItemNethermonicDiamond, "Nethermonic Diamond");
    LanguageRegistry.addName(ItemTransformer, "Displacement Flat-rate Transformer");
    LanguageRegistry.addName(ItemReverseTransformer, "Entanglement Flat-rate Transformer");
    LanguageRegistry.addName(ItemTransmitter,"Transmitter");
    LanguageRegistry.addName(ItemInductionCircuit, "Induction Circuit");
    LanguageRegistry.addName(ItemSuperInductionCircuit, "Forerunner Induction Circuit");
    LanguageRegistry.addName(ItemFrShard, "Forerunner Obsidian Shard");
    LanguageRegistry.addName(ItemBlueShard, "Mysterious Blue Shard");
    LanguageRegistry.addName(ItemRedShard, "Mysterious Red Shard");
    LanguageRegistry.addName(ItemYelShard, "Mysterious Yellow Shard");
    LanguageRegistry.addName(ItemImbuedShard, "Legendary Tri-Shard");
    LanguageRegistry.addName(ItemCircuit,"Circuit");
    LanguageRegistry.addName(ItemInclusiveFilter,"'Mine only x' Filter Device");
    LanguageRegistry.addName(ItemExclusiveFilter, "Exclusive Filter Device");
    LanguageRegistry.addName(ItemDestroyFilter, "'Destroy x' Filter Device");
    LanguageRegistry.addName(ItemDontDestroyFilter, "'Do not destroy x' Filter Device");
    LanguageRegistry.addName(ItemTPScroll, "TP Scroll");
    LanguageRegistry.addName(ItemShardPickG, "Displacement Pick : G");
    LanguageRegistry.addName(ItemShardPickR, "Displacement Pick : R");
    LanguageRegistry.addName(ItemShardPickY, "Displacement Pick : Y");
    LanguageRegistry.addName(ItemShardPickB, "Displacement Pick : B");
    GameRegistry.addSmelting(Block.netherrack.blockID, new ItemStack(ItemNetherEssence, 1),1F);
    GameRegistry.addRecipe(new ItemStack(EntangleCraftBlocks.BlockFObsidian,8), new Object[] {"OOO","OSO","OOO", Character.valueOf('O'),Block.obsidian, Character.valueOf('S'),ItemFrShard});
    GameRegistry.addRecipe(new ItemStack(ItemDeviceG, 1), new Object[] { "SOO", "OLO", "OXO", Character.valueOf('O'), Block.obsidian, Character.valueOf('L'), ItemLambdaCore, Character.valueOf('S'), Item.lightStoneDust, Character.valueOf('X'), Item.emptyMap});
    GameRegistry.addRecipe(new ItemStack(EntangleCraftBlocks.BlockGLD, 1), new Object[] { "FGF", "FLF", "FFF", Character.valueOf('G'), Block.glass, Character.valueOf('F'), Block.obsidian, Character.valueOf('L'), ItemLambdaCore });
    //GameRegistry.addRecipe(new ItemStack(EntangleCraftBlocks.BlockGenericDestination, 1), new Object[] { "  D","   ", "   ", Character.valueOf('D'), Block.dirt});
    GameRegistry.addRecipe(new ItemStack(ItemNethermonicDiamond, 1), new Object[] { "NNN", "NDN", "NNN", Character.valueOf('N'), ItemNetherEssence, Character.valueOf('D'), Item.diamond });
    GameRegistry.addRecipe(new ItemStack(ItemNethermonicDiamond,12), new Object[] {"DDD","DID","DDD", Character.valueOf('D'), ItemNethermonicDiamond, Character.valueOf('I'), ItemImbuedShard});
    GameRegistry.addRecipe(new ItemStack(ItemLambdaCore, 1), new Object[] { "FBF", "SNS", "FBF", Character.valueOf('S'), Item.lightStoneDust, Character.valueOf('F'), Block.obsidian, Character.valueOf('N'), ItemNethermonicDiamond, Character.valueOf('B'), new ItemStack(Item.dyePowder, 1, 4) });
    GameRegistry.addRecipe(new ItemStack(ItemTransformer, 1), new Object[] { "RNL", "NIN", "GNR", Character.valueOf('N'), ItemNetherEssence, Character.valueOf('R'), Item.redstone, Character.valueOf('L'), new ItemStack(Item.dyePowder, 1, 4), Character.valueOf('G'), Item.lightStoneDust, Character.valueOf('I'), Item.ingotIron });
    GameRegistry.addRecipe(new ItemStack(ItemReverseTransformer, 1), new Object[] { "NLN", "RIR", "NGN", Character.valueOf('N'), ItemNetherEssence, Character.valueOf('R'), Item.redstone, Character.valueOf('L'), new ItemStack(Item.dyePowder, 1, 4), Character.valueOf('G'), Item.lightStoneDust, Character.valueOf('I'), Item.ingotIron });
    GameRegistry.addRecipe(new ItemStack(ItemTransmitter,1), new Object[] {"IBI","ICI","INI", Character.valueOf('I'), Item.ingotIron,Character.valueOf('B'), new ItemStack(Item.dyePowder,1,4), Character.valueOf('N'),Item.compass, Character.valueOf('C'),ItemCircuit});
    GameRegistry.addRecipe(new ItemStack(ItemCircuit,1), new Object[] {"FIF","ISI","FIF", Character.valueOf('F'), ItemFrShard,Character.valueOf('I'), Item.ingotIron, Character.valueOf('S'), ItemImbuedShard});
    GameRegistry.addRecipe(new ItemStack(ItemInductionCircuit,1), new Object[] {"IRI","ISI"," C ", Character.valueOf('C'), ItemCircuit, Character.valueOf('I'), Item.ingotIron, Character.valueOf('R'), Item.redstone, Character.valueOf('S'),ItemFrShard});
    GameRegistry.addRecipe(new ItemStack(ItemSuperInductionCircuit,1), new Object[] {"FDF","FDF"," I ", Character.valueOf('F'), EntangleCraftBlocks.BlockFObsidian, Character.valueOf('D'), Item.diamond, Character.valueOf('I'), ItemInductionCircuit});
    GameRegistry.addRecipe(new ItemStack(EntangleCraftBlocks.BlockGLM,1), new Object[] {"FCF","TLT","FDF",Character.valueOf('D'), ItemShardPickG, Character.valueOf('F'), EntangleCraftBlocks.BlockFObsidian,Character.valueOf('L'), ItemLambdaCore, Character.valueOf('C'), ItemCircuit, Character.valueOf('T'), ItemTransmitter});
    GameRegistry.addRecipe(new ItemStack(EntangleCraftBlocks.BlockRLM,1), new Object[] {"FCF","TLT","FDF",Character.valueOf('D'), ItemShardPickR, Character.valueOf('F'), EntangleCraftBlocks.BlockFObsidian,Character.valueOf('L'), ItemLambdaCore, Character.valueOf('C'), ItemCircuit, Character.valueOf('T'), ItemTransmitter});
    GameRegistry.addRecipe(new ItemStack(EntangleCraftBlocks.BlockYLM,1), new Object[] {"FCF","TLT","FDF",Character.valueOf('D'), ItemShardPickY, Character.valueOf('F'), EntangleCraftBlocks.BlockFObsidian,Character.valueOf('L'), ItemLambdaCore, Character.valueOf('C'), ItemCircuit, Character.valueOf('T'), ItemTransmitter});
    GameRegistry.addRecipe(new ItemStack(EntangleCraftBlocks.BlockBLM,1), new Object[] {"FCF","TLT","FDF",Character.valueOf('D'), ItemShardPickB, Character.valueOf('F'), EntangleCraftBlocks.BlockFObsidian,Character.valueOf('L'), ItemLambdaCore, Character.valueOf('C'), ItemCircuit, Character.valueOf('T'), ItemTransmitter});
    GameRegistry.addRecipe(new ItemStack(ItemInclusiveFilter,1), new Object[] {"GTG","GCG","GTG", Character.valueOf('G'), Block.glass, Character.valueOf('C'),ItemCircuit,Character.valueOf('T'), ItemTransmitter});
    GameRegistry.addRecipe(new ItemStack(ItemExclusiveFilter,1), new Object[] {"GGG","TCT","GGG", Character.valueOf('G'), Block.glass, Character.valueOf('C'),ItemCircuit,Character.valueOf('T'), ItemTransmitter});
    GameRegistry.addRecipe(new ItemStack(ItemDestroyFilter,1), new Object[] {"GTG","GCG","GRG", Character.valueOf('G'), Block.glass, Character.valueOf('C'), ItemCircuit,Character.valueOf('T'), ItemTransmitter, Character.valueOf('R'), new ItemStack(Block.torchRedstoneActive,1)});
    GameRegistry.addRecipe(new ItemStack(ItemDontDestroyFilter,1), new Object[] {"GRG","GCG","GTG", Character.valueOf('G'), Block.glass, Character.valueOf('C'), ItemCircuit,Character.valueOf('T'), ItemTransmitter, Character.valueOf('R'), new ItemStack(Block.torchRedstoneActive,1)});
    GameRegistry.addShapelessRecipe(new ItemStack(ItemFrShard,1), new Object[]{Block.obsidian}); // THIS RECIPE IS TEMPORARY, SHOULD BE BlockFObsidian AFTER WORLD GENEREATION RE-IMPLEMENTED AND SHOULD GRANT 8 INSTEAD OF 1
    GameRegistry.addShapelessRecipe(new ItemStack(ItemRedShard,1), new Object[]{ItemFrShard,Item.redstone});
    GameRegistry.addShapelessRecipe(new ItemStack(ItemYelShard,1), new Object[]{ItemFrShard,Item.lightStoneDust});
    GameRegistry.addShapelessRecipe(new ItemStack(ItemBlueShard,1), new Object[]{ItemFrShard,new ItemStack(Item.dyePowder,1,4)});
    GameRegistry.addShapelessRecipe(new ItemStack(ItemImbuedShard), new Object[]{ItemYelShard,ItemBlueShard,ItemRedShard});
    GameRegistry.addShapelessRecipe(new ItemStack(ItemTPScroll,8), new Object[]{Item.enderPearl,Item.spiderEye,Item.gunpowder,Item.bone,Item.paper});
    GameRegistry.addShapelessRecipe(new ItemStack(ItemShardPickG), new Object[] {Item.pickaxeDiamond, ItemDeviceG});
    GameRegistry.addShapelessRecipe(new ItemStack(ItemShardPickR), new Object[] {Item.pickaxeDiamond, ItemDeviceR});
    GameRegistry.addShapelessRecipe(new ItemStack(ItemShardPickY), new Object[] {Item.pickaxeDiamond, ItemDeviceY});
    GameRegistry.addShapelessRecipe(new ItemStack(ItemShardPickB), new Object[] {Item.pickaxeDiamond, ItemDeviceB});
    GameRegistry.addShapelessRecipe(new ItemStack(EntangleCraftBlocks.BlockRLD, 1), new Object[] {EntangleCraftBlocks.BlockGLD, EntangleCraftItems.ItemRedShard});
    GameRegistry.addShapelessRecipe(new ItemStack(EntangleCraftBlocks.BlockYLD, 1), new Object[] {EntangleCraftBlocks.BlockGLD, EntangleCraftItems.ItemYelShard});
    GameRegistry.addShapelessRecipe(new ItemStack(EntangleCraftBlocks.BlockBLD, 1), new Object[] {EntangleCraftBlocks.BlockGLD,  EntangleCraftItems.ItemBlueShard});

}

private static void itemInitializing()
{
    Item[] availableChannelsDevices = new Item[] {
			EntangleCraftItems.ItemDeviceG, 
			EntangleCraftItems.ItemDeviceR, 
			EntangleCraftItems.ItemDeviceY, 
			EntangleCraftItems.ItemDeviceB};
    
    Item[] availableChannelsPicks = new Item[] {EntangleCraftItems.ItemShardPickG, 
    		EntangleCraftItems.ItemShardPickR, 
    		EntangleCraftItems.ItemShardPickY, 
    		EntangleCraftItems.ItemShardPickB};
    
    Item[][] classOfChannel = new Item[][] {availableChannelsDevices, availableChannelsPicks};
    
    for(Object obj : classOfChannel)
    {
    	Item[] thisClassOfChannels = (Item[])obj;
    	
	    int i = 0;
	    for(Object o : thisClassOfChannels)
	    {
	    	IChanneled thisItemChanneled = (IChanneled)o;
	    	thisItemChanneled.setAvailableChannels(thisClassOfChannels);
	    	thisItemChanneled.setChannel(i);
	    	i += 1;
	    }
    }
}

public static void configureIDs(Configuration config) {
	int i = 0;
	for (Item thisItem : itemList)
	{
		config.getItem(thisItem.getItemName(), defaultIdsList[i]);
		i++;
	}
}

}


package net.minecraft.src.lambdaMod.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.src.Block;
import net.minecraft.src.BlockOre;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.MinecraftForge;

public class EntangleCraftBlocks {
	public static final Block BlockGenericDestination = new BlockGenericDestination(2600, 0).setHardness(5.0F).setResistance(20.0F).setBlockName("BlockGenericDestination");
	public static final Block BlockRLD = new BlockGenericDestination(2601, 1).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockRLD");
	public static final Block BlockYLD = new BlockGenericDestination(2602, 2).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockYLD");
	public static final Block BlockBLD = new BlockGenericDestination(2603, 3).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockBLD");
	public static final Block BlockFObsidian = new BlockFObsidian(2604, 0).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockFObsidian");
	public static final Block BlockLambdaMiner = new BlockLambdaMiner(2605,0).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockLambdaMiner");
	public static final Block BlockRLM = new BlockLambdaMiner(2606,1).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockRLambdaMiner");
	public static final Block BlockYLM = new BlockLambdaMiner(2607,2).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockYLambdaMiner");
	public static final Block BlockBLM = new BlockLambdaMiner(2608,3).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockBLambdaMiner");
	public static final Block BlockGlowTorch = new BlockGlowTorch(2609,100).setLightValue(1F).setLightOpacity(0).setBlockName("GlowTorch");
	public static final Block BlockLitWater = new BlockLitWaterStill(2610).setLightValue(1F).setLightOpacity(0).setBlockName("LitWater");
	public static final Block BlockYelShard = new BlockLambdaOre(2611,0).setHardness(2F).setResistance(2F).setBlockName("BlockYelShard");
	public static final Block BlockRedShard = new BlockLambdaOre(2612,1).setHardness(2F).setResistance(2F).setBlockName("BlockRedShard");
	public static final Block BlockBluShard = new BlockLambdaOre(2613,2).setHardness(2F).setResistance(2F).setBlockName("BlockBluShard");
	
	public static void addBlocks(){
	    BlockRLD.setLightValue(0.8F);
	    BlockYLD.setLightValue(0.8F);
	    BlockBLD.setLightValue(0.8F);
	    BlockGenericDestination.setLightValue(0.8F);
		
		GameRegistry.registerBlock(BlockGenericDestination);
		GameRegistry.registerBlock(BlockRLD);
		GameRegistry.registerBlock(BlockYLD);
		GameRegistry.registerBlock(BlockBLD);
		GameRegistry.registerBlock(BlockFObsidian);
		GameRegistry.registerBlock(BlockLambdaMiner);
		GameRegistry.registerBlock(BlockRLM);
		GameRegistry.registerBlock(BlockYLM);
		GameRegistry.registerBlock(BlockBLM);
		GameRegistry.registerBlock(BlockGlowTorch);
		GameRegistry.registerBlock(BlockLitWater);
		GameRegistry.registerBlock(BlockYelShard);
		GameRegistry.registerBlock(BlockRedShard);
		GameRegistry.registerBlock(BlockBluShard);
		
	    GameRegistry.registerTileEntity(TileEntityGenericDestination.class, "tileEntityGD");
	    GameRegistry.registerTileEntity(TileEntityLambdaMiner.class, "tileEntitylM");
		
	    LanguageRegistry.addName(BlockGenericDestination, "G.L.D");
	    LanguageRegistry.addName(BlockRLD, "R.L.D");
	    LanguageRegistry.addName(BlockYLD, "Y.L.D");
	    LanguageRegistry.addName(BlockBLD, "B.L.D");
	    LanguageRegistry.addName(BlockFObsidian, "Forerunner Obsidian");
	    LanguageRegistry.addName(BlockLambdaMiner, "Entanglement Miner");
	    LanguageRegistry.addName(BlockRLM, "Redstone Entanglement Miner");
	    LanguageRegistry.addName(BlockYLM, "Yellow Shard Entanglement Miner");
	    LanguageRegistry.addName(BlockBLM, "Blue Shard Entanglement Miner");
	    LanguageRegistry.addName(BlockGlowTorch, "Magic light placed by Yellow Shards");
	    LanguageRegistry.addName(BlockYelShard,"Mysterious Yellow Shard Fragments");
	    LanguageRegistry.addName(BlockRedShard,"Mysterious Red Shard Fragments");
	    LanguageRegistry.addName(BlockBluShard,"Mysterious Blue Shard Fragments");
	}
}



package entanglecraft.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.MinecraftForge;

public class EntangleCraftBlocks {
	public static final int entangleCraftBaseBlockID = 2600;
	
	public static final Block BlockGLD = new BlockGenericDestination(2600, 0).setHardness(5.0F).setResistance(20.0F).setBlockName("BlockGenericDestination");
	public static final Block BlockRLD = new BlockGenericDestination(2601, 1).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockRLD");
	public static final Block BlockYLD = new BlockGenericDestination(2602, 2).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockYLD");
	public static final Block BlockBLD = new BlockGenericDestination(2603, 3).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockBLD");
	public static final Block BlockFObsidian = new BlockFObsidian(2604, 0).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockFObsidian");
	public static final Block BlockGLM = new BlockLambdaMiner(2605,0);
	public static final Block BlockRLM = new BlockLambdaMiner(2606,1);
	public static final Block BlockYLM = new BlockLambdaMiner(2607,2);
	public static final Block BlockBLM = new BlockLambdaMiner(2608,3);
	public static final Block BlockGlowTorch = new BlockGlowTorch(2609,100).setLightValue(1F).setLightOpacity(0).setBlockName("GlowTorch");
	public static final Block BlockLitWater = new BlockLitWaterStill(2610).setLightValue(1F).setLightOpacity(0).setBlockName("LitWater");
	public static final Block BlockYelShard = new BlockLambdaOre(2611,0).setHardness(2F).setResistance(2F).setBlockName("BlockYelShard");
	public static final Block BlockRedShard = new BlockLambdaOre(2612,1).setHardness(2F).setResistance(2F).setBlockName("BlockRedShard");
	public static final Block BlockBluShard = new BlockLambdaOre(2613,2).setHardness(2F).setResistance(2F).setBlockName("BlockBluShard");
	
	public static final Block[] blocksList = new Block[] {BlockGLD, BlockRLD, BlockYLD, BlockBLD,
								BlockFObsidian, BlockGLM, BlockRLM, BlockYLM, BlockBLM,
								BlockGlowTorch, BlockLitWater, BlockYelShard, BlockRedShard, BlockBluShard
	};
	
	public static void addBlocks(){
	    BlockRLD.setLightValue(0.8F);
	    BlockYLD.setLightValue(0.8F);
	    BlockBLD.setLightValue(0.8F);
	    BlockGLD.setLightValue(0.8F);
		
	    for (Object blockObj : blocksList)
	    {
	    	Block thisBlock = (Block)blockObj;
	    	
			GameRegistry.registerBlock(thisBlock, thisBlock.getBlockName());
	    }
	    
	    GameRegistry.registerTileEntity(TileEntityGenericDestination.class, "tileEntityGD");
	    GameRegistry.registerTileEntity(TileEntityLambdaMiner.class, "tileEntitylM");
		
	    LanguageRegistry.addName(BlockGLD, "G.L.D");
	    LanguageRegistry.addName(BlockRLD, "R.L.D");
	    LanguageRegistry.addName(BlockYLD, "Y.L.D");
	    LanguageRegistry.addName(BlockBLD, "B.L.D");
	    LanguageRegistry.addName(BlockFObsidian, "Forerunner Obsidian");
	    LanguageRegistry.addName(BlockGLM, "Entanglement Miner");
	    LanguageRegistry.addName(BlockRLM, "Redstone Entanglement Miner");
	    LanguageRegistry.addName(BlockYLM, "Yellow Shard Entanglement Miner");
	    LanguageRegistry.addName(BlockBLM, "Blue Shard Entanglement Miner");
	    LanguageRegistry.addName(BlockGlowTorch, "Magic light placed by Yellow Shards");
	    LanguageRegistry.addName(BlockYelShard,"Mysterious Yellow Shard Fragments");
	    LanguageRegistry.addName(BlockRedShard,"Mysterious Red Shard Fragments");
	    LanguageRegistry.addName(BlockBluShard,"Mysterious Blue Shard Fragments");
	}
}



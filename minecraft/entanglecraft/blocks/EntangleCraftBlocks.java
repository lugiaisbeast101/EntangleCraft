package entanglecraft.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

public class EntangleCraftBlocks {
	public static final int entangleCraftBaseBlockID = 2600;
	
	public static final int GLDBlockID = 2600;
	public static final int RLDBlockID = 2601;
	public static final int YLDBlockID = 2602;
	public static final int BLDBlockID = 2603;
	public static final int FObsidianBlockID = 2604;
	public static final int GLMBlockID = 2605;
	public static final int RLMBlockID = 2606;
	public static final int YLMBlockID = 2607;
	public static final int BLMBlockID = 2608;
	public static final int GlowTorchBlockID = 2609;
	public static final int LitWaterBlockID = 2610;
	public static final int YelShardBlockID = 2611;
	public static final int RedShardBlockID = 2612;
	public static final int BluShardBlockID = 2613;
	
	public static final Block BlockGLD = new BlockGenericDestination(GLDBlockID, 0).setHardness(5.0F).setResistance(20.0F).setBlockName("BlockGLD");
	public static final Block BlockRLD = new BlockGenericDestination(RLDBlockID, 1).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockRLD");
	public static final Block BlockYLD = new BlockGenericDestination(YLDBlockID, 2).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockYLD");
	public static final Block BlockBLD = new BlockGenericDestination(BLDBlockID, 3).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockBLD");
	public static final Block BlockFObsidian = new BlockFObsidian(FObsidianBlockID, 0).setHardness(10.0F).setResistance(20.0F).setBlockName("BlockFObsidian");
	public static final Block BlockGLM = new BlockLambdaMiner(GLMBlockID,0);
	public static final Block BlockRLM = new BlockLambdaMiner(RLMBlockID,1);
	public static final Block BlockYLM = new BlockLambdaMiner(YLMBlockID,2);
	public static final Block BlockBLM = new BlockLambdaMiner(BLMBlockID,3);
	public static final Block BlockGlowTorch = new BlockGlowTorch(GlowTorchBlockID,100).setLightValue(1F).setLightOpacity(0).setBlockName("BlockGlowTorch");
	public static final Block BlockLitWater = new BlockLitWaterStill(LitWaterBlockID).setLightValue(1F).setLightOpacity(0).setBlockName("BlockLitWater");
	public static final Block BlockYelShard = new BlockLambdaOre(YelShardBlockID,0).setHardness(2F).setResistance(2F).setBlockName("BlockYelShard");
	public static final Block BlockRedShard = new BlockLambdaOre(RedShardBlockID,1).setHardness(2F).setResistance(2F).setBlockName("BlockRedShard");
	public static final Block BlockBluShard = new BlockLambdaOre(BluShardBlockID,2).setHardness(2F).setResistance(2F).setBlockName("BlockBluShard");
	
	public static final Block[] blocksList = new Block[] {BlockGLD, BlockRLD, BlockYLD, BlockBLD,
								BlockFObsidian, BlockGLM, BlockRLM, BlockYLM, BlockBLM,
								BlockGlowTorch, BlockLitWater, BlockYelShard, BlockRedShard, BlockBluShard
	};
	
	public static int[] defaultIdsList = new int[] 
	{
		GLDBlockID, RLDBlockID, YLDBlockID, BLDBlockID,
		FObsidianBlockID, GLMBlockID, RLMBlockID, YLMBlockID, BLMBlockID,
		GlowTorchBlockID, LitWaterBlockID, YelShardBlockID, RedShardBlockID, BluShardBlockID
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
	
	public static void configureIDs(Configuration config) {
		int i = 0;
		for (Block thisBlock : blocksList)
		{
			config.getBlock(thisBlock.getBlockName(), defaultIdsList[i]);
			i++;
		}
	}
}



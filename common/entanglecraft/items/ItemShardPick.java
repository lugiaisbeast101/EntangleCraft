package entanglecraft.items;

import entanglecraft.Destination;
import entanglecraft.EntangleCraft;
import entanglecraft.InventoryController;
import entanglecraft.SoundHandling.LambdaSoundHandler;
import entanglecraft.blocks.TileEntityGenericDestination;
import net.minecraft.src.Block;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class ItemShardPick extends ItemShardTool{

	public static final Block[] blocksEffectiveAgainst = new Block[] {Block.cobblestone, Block.stoneDoubleSlab, Block.stoneSingleSlab, Block.stone, Block.sandStone, Block.cobblestoneMossy, Block.oreIron, Block.blockSteel, Block.oreCoal, Block.blockGold, Block.oreGold, Block.oreDiamond, Block.blockDiamond, Block.ice, Block.netherrack, Block.oreLapis, Block.blockLapis, Block.oreRedstone, Block.oreRedstoneGlowing, Block.rail, Block.railDetector, Block.railPowered};
	
	public ItemShardPick(int itemID) {
		super(itemID, 2, EnumToolMaterial.STONE, blocksEffectiveAgainst);

	}
	
	@Override
	public boolean onItemUseFirst(ItemStack par1ItemStack, EntityPlayer thePlayer, World world, int x, int y, int z, int side) 
	{
		/*
		int blockID = world.getBlockId(x, y, z);
		world.setBlockWithNotify(x, y, z, 0);
		
		double[] coords = new double[] {x + 0.5D, y + 0.5D, z + 0.5D};
		LambdaSoundHandler.playSound(world, "minerSound", coords, world.rand.nextFloat() * 0.1F + 0.6F, world.rand.nextFloat() * 0.1F + 0.9F);
		
		Destination closestDest = EntangleCraft.closestDestToCoord(coords, EntangleCraft.channelDests[channel]);
		TileEntityGenericDestination teGD = (TileEntityGenericDestination)world.getBlockTileEntity(closestDest.blockCoords[0], closestDest.blockCoords[1], closestDest.blockCoords[2]);
		ItemStack itemStack = InventoryController.getItemStackFromID(blockID);
		
		EntityItem e = new EntityItem(teGD.worldObj, (double) teGD.blockCoords[0] + 0.5, (double) teGD.blockCoords[1] + 1.5,
				(double) teGD.blockCoords[2] + 0.5, itemStack);
		e.dropItem(itemStack.itemID, itemStack.stackSize);
		*/

		return true;		
	}
	
    /**
     * Returns if the item (tool) can harvest results from the block type.
     */
    public boolean canHarvestBlock(Block par1Block)
    {
        return par1Block == Block.obsidian ? this.toolMaterial.getHarvestLevel() == 3 : (par1Block != Block.blockDiamond && par1Block != Block.oreDiamond ? (par1Block != Block.oreEmerald && par1Block != Block.blockEmerald ? (par1Block != Block.blockGold && par1Block != Block.oreGold ? (par1Block != Block.blockSteel && par1Block != Block.oreIron ? (par1Block != Block.blockLapis && par1Block != Block.oreLapis ? (par1Block != Block.oreRedstone && par1Block != Block.oreRedstoneGlowing ? (par1Block.blockMaterial == Material.rock ? true : (par1Block.blockMaterial == Material.iron ? true : par1Block.blockMaterial == Material.anvil)) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2);
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block)
    {
        return par2Block != null && (par2Block.blockMaterial == Material.iron || par2Block.blockMaterial == Material.anvil || par2Block.blockMaterial == Material.rock) ? this.efficiencyOnProperMaterial : super.getStrVsBlock(par1ItemStack, par2Block);
    }

}

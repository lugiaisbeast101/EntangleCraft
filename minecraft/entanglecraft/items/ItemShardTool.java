package entanglecraft.items;

import net.minecraft.block.Block;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemTool;

public class ItemShardTool extends ItemTool implements IChanneled{
	
	public int channel = 0;
	private Item[] availableChannels;
	
	public ItemShardTool(int itemID, int baseDamage, EnumToolMaterial par3EnumToolMaterial, Block[] blocksEffectiveAgainst)
	{
		super(itemID, baseDamage, par3EnumToolMaterial, blocksEffectiveAgainst);	
	}

	@Override
	public void setChannel(int i){
		channel = i;
	}
	
	@Override
	public int getChannel(){
		return channel;
	}
	
	@Override
	public void setAvailableChannels(Item[] availableChannels)
	{
		this.availableChannels = availableChannels;
	}
	
	@Override
	public Item incrementChannel(){
		Item is = null;
		
		int x = channel + 1;
		if (x == availableChannels.length) x = 0;
		is = availableChannels[x];

		return is;
	}
	
	public String getTextureFile(){
		return "/lambdaTextures.png";
	}
	
}

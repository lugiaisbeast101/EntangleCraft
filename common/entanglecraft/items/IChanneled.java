package entanglecraft.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public interface IChanneled {
	
	public int channel = 0;
	public Item[] availableChannels = null;

	public void setChannel(int i);
	public int getChannel();
	public void setAvailableChannels(Item[] availableChannels);
	public Item incrementChannel();
}

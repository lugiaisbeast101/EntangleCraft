package entanglecraft.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class ItemLambda extends Item{

	public ItemLambda(int par1) {
		super(par1);
		setCreativeTab(CreativeTabs.tabMisc);
	}
	
	public String getTextureFile(){
		return "/lambdaTextures.png";
	}
}

package net.minecraft.entanglecraft.gui;

public enum EnumGui {
	GenericDestination(0), LambdaMiner(1);
	
	
	private int index;
	private EnumGui(int i){
		index = i;
	}
	
	public int getIndex(){
		return index;
	}
}

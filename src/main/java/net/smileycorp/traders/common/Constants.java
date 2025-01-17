package net.smileycorp.traders.common;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class Constants {
	
	public static final String NAME = "Traders";
	public static final String MODID = "raids";
	public static final String VERSION = "1.0.0";
	public static final String DEPENDENCIES = "";
	public static final String PATH = "net.smileycorp.traders.";
	public static final String CLIENT = PATH + "client.ClientProxy";
	public static final String SERVER = PATH + "common.CommonProxy";
    
    public static String name(String name) {
		return MODID + "." + name.replace("_", "");
	}
	
	public static ResourceLocation loc(String name) {
		return new ResourceLocation(MODID, name.toLowerCase());
	}

	public static SoundEvent registerSound(String name) {
		SoundEvent newSound = new SoundEvent(new ResourceLocation(MODID, name));
		newSound.setRegistryName(name);
		return newSound;
	}
}

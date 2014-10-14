package yoan.game.gf2d.util;

import yoan.game.framework.util.DimensionConverter;
import yoan.game.gf2d.screens.world.GameScreen;

/**
 * Helper pour convertir des dimmensions réelles en dimension in game
 * @author yoan
 */
public class DimensionConverterHelper {
	private static DimensionConverter dimConverter = new DimensionConverter(GameScreen.BASIC_ELEMENT_SIZE);
	
	private DimensionConverterHelper() { }
	
	public static float pixelsToGameSize(float sizeInPixel) {
		return dimConverter.pixelsToGameSize(sizeInPixel);
	}
	
	public static int gameSizeToPixels(float inGameSize) {
		return dimConverter.gameSizeToPixels(inGameSize);
	}
	
}

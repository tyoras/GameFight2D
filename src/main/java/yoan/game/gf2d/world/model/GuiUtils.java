package yoan.game.gf2d.world.model;

import yoan.game.gf2d.util.Assets;
import yoan.game.gf2d.util.Settings;


/**
 * Utilitaire pour l'UI du jeu
 * @author yoan
 */
public final class GuiUtils {
	/** Instanciation interdite */
	private GuiUtils() { }
	
	/**
	 * Change l'état courant du son (ON/OFF)
	 */
	public static void switchSound() {
		Settings.soundEnabled= !Settings.soundEnabled;
		if(Settings.soundEnabled) 
			Assets.music.play();
		else 
			Assets.music.pause();
	}
}
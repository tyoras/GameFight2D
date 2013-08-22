package yoan.game.gf2d.screens.help;

import yoan.game.framework.modules.game.Game;
import yoan.game.gf2d.screens.MainMenuScreen;

/**
 * Ecran d'aide N°3
 * @author yoan
 */
public final class HelpScreen3 extends HelpScreen {
	/**
	 * Constructeur contenant les données à afficher sur l'écran d'aide
	 * @param game : instance du jeu
	 */
	public HelpScreen3(Game game){
		super(game);
		helpImageName= "pixmaps/help3.png";
		nextScreen = new MainMenuScreen(game);
	}
}
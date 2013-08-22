package yoan.game.gf2d.screens.help;

import yoan.game.framework.modules.game.Game;

/**
 * Ecran d'aide N°2
 * @author yoan
 */
public final class HelpScreen2 extends HelpScreen {
	/**
	 * Constructeur contenant les données à afficher sur l'écran d'aide
	 * @param game : instance du jeu
	 */
	public HelpScreen2(Game game){
		super(game);
		helpImageName= "pixmaps/help2.png";
		nextScreen = new HelpScreen3(game);
	}
}
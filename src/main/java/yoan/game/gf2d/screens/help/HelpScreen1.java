package yoan.game.gf2d.screens.help;

import yoan.game.framework.modules.game.Game;

/**
 * Ecran d'aide N°1
 * @author yoan
 */
public final class HelpScreen1 extends HelpScreen {
	/**
	 * Constructeur contenant les données à afficher sur l'écran d'aide
	 * @param game : instance du jeu
	 */
	public HelpScreen1(Game game){
		super(game);
		helpImageName= "pixmaps/help1.png";
		nextScreen = new HelpScreen2(game);
	}
}
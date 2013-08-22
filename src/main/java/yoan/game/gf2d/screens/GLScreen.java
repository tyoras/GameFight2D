package yoan.game.gf2d.screens;

import yoan.game.framework.modules.game.Game;
import yoan.game.framework.modules.game.gl.GLGame;
import yoan.game.framework.modules.graphics.gl.GLGraphics;
import yoan.game.framework.modules.screen.Screen;

/**
 * Classe de base des écrans affiché par openGL ES
 * @author yoan
 */
public abstract class GLScreen extends Screen {
	//Constants
	/** Largeur de l'écran */
	public static final int SCREEN_WIDTH = 480;
	/** Longueur de l'écran */
	public static final int SCREEN_HEIGHT = 320;
	
	/** Taille d'un élément basic du jeu */
	public static final int BASIC_ELEMENT_SIZE = 32;
	/** Taille des boutons */
	public static final int BUTTON_SIZE = 2*BASIC_ELEMENT_SIZE;
	/** Hauteur d'une plateforme */
	public static final int PLATFORM_HEIGHT = BASIC_ELEMENT_SIZE/2;
	/** Largeur d'une plateforme */
	public static final int PLATFORM_WIDTH = 2*BASIC_ELEMENT_SIZE;
	/** Hauteur d'une glyphe */
	public static final int GLYPH_HEIGHT = 20;
	/** Largeur d'une glyphe */
	public static final int GLYPH_WIDTH = 16;
	/** Hauteur du logo */
	public static final int LOGO_HEIGHT = 142;
	
	
	/** Accès à la gestion des graphismes */
	protected final GLGraphics glGraphics;
	/** Instance du jeu typé en GLGame */
	protected final GLGame glGame;
	
	/**
	 * Constrcuteur permettant de caster l'instance du jeu en GLGame
	 * @param game : instance du jeu
	 */
	public GLScreen(Game game){
		super(game);
		glGame= (GLGame) game;
		glGraphics= glGame.getGLGraphics();
	}
}
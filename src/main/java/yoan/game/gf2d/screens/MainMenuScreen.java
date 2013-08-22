package yoan.game.gf2d.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import yoan.game.framework.modules.game.Game;
import yoan.game.framework.modules.game.gl.GLGame;
import yoan.game.framework.modules.graphics.gl.Camera2D;
import yoan.game.framework.modules.graphics.gl.SpriteBatcher;
import yoan.game.framework.modules.input.Input.TouchEvent;
import yoan.game.framework.util.math.OverlapTester;
import yoan.game.framework.util.math.Rectangle;
import yoan.game.framework.util.math.Vector2;
import yoan.game.gf2d.screens.help.HelpScreen1;
import yoan.game.gf2d.screens.multiplayer.ConnexionScreen;
import yoan.game.gf2d.screens.world.GameScreen;
import yoan.game.gf2d.util.Assets;
import yoan.game.gf2d.util.Settings;
import yoan.game.gf2d.world.model.GuiUtils;

/**
 * 
 * @author yoan
 */
public class MainMenuScreen extends GLScreen {
	/** Camera pour l'interface */
	Camera2D guiCam;
	/** Batcher d'affichage de sprite */
	SpriteBatcher batcher;
	/** Limites du bouton son */
	Rectangle soundBounds;
	/** Limites du bouton play */
	Rectangle playBounds;
	/** Limites du bouton highscores */
	Rectangle highscoresBounds;
	/** Limites du bouton multiplayer */
	Rectangle multiplayerBounds;
	/** Limites du bouton help */
	Rectangle helpBounds;
	/** Point touché à l'écran (sert à convertir les coordonnées de l'écran coordonnées du monde */
	Vector2 touchPoint;
	
	/**
	 * Constructeur hérité de GLScreen
	 * @param game : instance du jeu
	 */
	public MainMenuScreen(Game game) {
		super(game);
		guiCam = new Camera2D(glGraphics, SCREEN_WIDTH, SCREEN_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 100);
		soundBounds = new Rectangle(0, 0, BUTTON_SIZE, BUTTON_SIZE);
		//TODO [1280x800] constantes à modifier
		playBounds = new Rectangle(SCREEN_WIDTH/2 - 150, SCREEN_HEIGHT - 10 - 71 - LOGO_HEIGHT - 10 + 18, 300, 36);
		highscoresBounds = new Rectangle(SCREEN_WIDTH/2 - 150, SCREEN_HEIGHT - 10 - 71 - LOGO_HEIGHT - 10 - 18, 300, 36);
		multiplayerBounds = new Rectangle(SCREEN_WIDTH/2 - 150, SCREEN_HEIGHT - 10 - 71 - LOGO_HEIGHT - 10 - 18 - 36, 300, 36);
		helpBounds = new Rectangle(SCREEN_WIDTH - BUTTON_SIZE, 0, BUTTON_SIZE, BUTTON_SIZE);
		touchPoint = new Vector2();
	}
	
	/**
	 * Surchage de update pour le menu principal
	 * Gère les touch events sur les éléments du menu
	 * @param deltaTime : temps écoulé depuis le dernier update
	 */
	@Override
	public void update(float deltaTime){
		//récupération des derniers touch events
		List<TouchEvent> touchEvents= game.getInput().getTouchEvents();
		//récupération des derniers key events pour vider le buffer interne
		game.getInput().getKeyEvents();
		
		//pour chacun des touch events
		int len= touchEvents.size();
		for(int i= 0; i < len; i++){
			TouchEvent event= touchEvents.get(i);
			//si c'est un click
			if(event.type == TouchEvent.TOUCH_UP){
				//transforme les coordonées vers la résolution cible
				touchPoint.set(event.x, event.y);
				guiCam.touchToWorld(touchPoint);
				//sur le bouton "Jouer"
				if(OverlapTester.pointInRectangle(playBounds, touchPoint)){
					Assets.playSound(Assets.clickSound);
					//transition vers l'écran de jeu
					game.setScreen(new GameScreen(game));
					return;
				}
				//sur le bouton "Highscores"
				if(OverlapTester.pointInRectangle(highscoresBounds, touchPoint)){
					Assets.playSound(Assets.clickSound);
					//transition vers l'écran des highscores
					game.setScreen(new HighscoreScreen(game));
					return;
				}
				//sur le bouton "Multiplayer"
				if(OverlapTester.pointInRectangle(multiplayerBounds, touchPoint)){
					Assets.playSound(Assets.clickSound);
					//transition vers l'écran des highscores
					game.setScreen(new ConnexionScreen((GLGame) game));
					return;
				}
				//sur le bouton "Aide"
				if(OverlapTester.pointInRectangle(helpBounds, touchPoint)){
					Assets.playSound(Assets.clickSound);
					//transition vers l'écran d'aide
					game.setScreen(new HelpScreen1(game));
					return;
				}
				//sur le bouton son
				if(OverlapTester.pointInRectangle(soundBounds, touchPoint)){
					Assets.playSound(Assets.clickSound);
					//on active / désactive le son
					GuiUtils.switchSound();
				}
			}
		}
	}
	
	/**
	 * Transmet les données de l'écran à l'affichage
	 * @param deltaTime : temps écoulé depuis le dernier affichage
	 */
	@Override
	public void present(float deltaTime){
		GL10 gl= glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.setViewportAndMatrices();

		gl.glEnable(GL10.GL_TEXTURE_2D);

		batcher.beginBatch(Assets.background);
		batcher.drawSprite(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, SCREEN_WIDTH, SCREEN_HEIGHT, Assets.backgroundRegion);
		batcher.endBatch();

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		batcher.beginBatch(Assets.items);
		//TODO [1280x800] constantes à modifier
		batcher.drawSprite(SCREEN_WIDTH/2, SCREEN_HEIGHT - 10 - 71, 274, LOGO_HEIGHT, Assets.logo);
		batcher.drawSprite(SCREEN_WIDTH/2, SCREEN_HEIGHT - 10 - 71 - LOGO_HEIGHT - 10, 300, 110, Assets.mainMenu);
		batcher.drawSprite(BUTTON_SIZE/2, BUTTON_SIZE/2, BUTTON_SIZE, BUTTON_SIZE, Settings.soundEnabled ? Assets.soundOn : Assets.soundOff);
		batcher.drawSprite(SCREEN_WIDTH - BUTTON_SIZE/2, BUTTON_SIZE/2, BUTTON_SIZE, BUTTON_SIZE, Assets.help);
		batcher.endBatch();

		gl.glDisable(GL10.GL_BLEND);
	}
	
	/**
	 * Met l'écran en pause
	 */
	@Override
	public void pause(){
		//sauvegarde des options
		Settings.save(game.getFileIO());
	}
	
	/** inutilisé pour cet écran */
	@Override
	public void resume(){}
	
	/** inutilisé pour cet écran */
	@Override
	public void dispose(){}
}

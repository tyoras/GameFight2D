package yoan.game.gf2d.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import yoan.game.framework.modules.game.Game;
import yoan.game.framework.modules.graphics.gl.Camera2D;
import yoan.game.framework.modules.graphics.gl.SpriteBatcher;
import yoan.game.framework.modules.input.Input.TouchEvent;
import yoan.game.framework.util.math.OverlapTester;
import yoan.game.framework.util.math.Rectangle;
import yoan.game.framework.util.math.Vector2;
import yoan.game.gf2d.util.Assets;
import yoan.game.gf2d.util.Settings;

/**
 * 
 * @author yoan
 */
public class HighscoreScreen extends GLScreen {
	/** Camera pour l'interface */
	Camera2D guiCam;
	/** Batcher d'affichage de sprite */
	SpriteBatcher batcher;
	/** Limites du bouton Back */
	Rectangle backBounds;
	/** Point touché à l'écran (sert à convertir les coordonnées de l'écran coordonnées du monde */
	Vector2 touchPoint;
	/** Tableau des scores */
	String[] highScores;
	/** Position pour centrer les lignes */
	float xOffset = 0;
	
	/**
	 * Constructeur hérité de GLScreen
	 * @param game : instance du jeu
	 */
	public HighscoreScreen(Game game){
		super(game);
		guiCam= new Camera2D(glGraphics, SCREEN_WIDTH, SCREEN_HEIGHT);
		backBounds= new Rectangle(0, 0, BUTTON_SIZE, BUTTON_SIZE);
		touchPoint= new Vector2();
		batcher= new SpriteBatcher(glGraphics, 100);
		highScores= new String[5];
		//génération des lignes
		for(int i= 0; i < 5; i++){
			highScores[i]= (i + 1) + ". " + Settings.highscores[i];
			//on garde la largeur maximale des lignes à afficher
			xOffset= Math.max(highScores[i].length() * Assets.font.glyphWidth, xOffset);
		}
		//calcul de l'offset
		xOffset= (SCREEN_WIDTH - xOffset) / 2;
	}

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
				//sur le bouton Back
				if(OverlapTester.pointInRectangle(backBounds, touchPoint)){
					Assets.playSound(Assets.clickSound);
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
		}
	}

	@Override
	public void present(float deltaTime){
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.setViewportAndMatrices();
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		//dessin du fond
		batcher.beginBatch(Assets.background);
		batcher.drawSprite(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, SCREEN_WIDTH, SCREEN_HEIGHT, Assets.backgroundRegion);
		batcher.endBatch();
		 
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		 
		//dessin du texte
		batcher.beginBatch(Assets.items);
		//TODO [1280x800] constantes à modifier
		batcher.drawSprite(SCREEN_WIDTH/2, 260, 300, 33, Assets.highScoresRegion);
		 
		float y= 120;
		for(int i= 4; i >= 0; i--){
			Assets.font.drawText(batcher, highScores[i], xOffset, y);
			y+= Assets.font.glyphHeight;
		}
		 
		batcher.drawSprite(BASIC_ELEMENT_SIZE, BASIC_ELEMENT_SIZE, BUTTON_SIZE, BUTTON_SIZE, Assets.arrow);
		batcher.endBatch();
		 
		gl.glDisable(GL10.GL_BLEND);
	}

	/** inutilisé pour cet écran */
	@Override
	public void pause(){}
	
	/** inutilisé pour cet écran */
	@Override
	public void resume(){}
	
	/** inutilisé pour cet écran */
	@Override
	public void dispose(){}
}
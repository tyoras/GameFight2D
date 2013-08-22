package yoan.game.gf2d.screens.help;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import yoan.game.framework.modules.game.Game;
import yoan.game.framework.modules.graphics.gl.Camera2D;
import yoan.game.framework.modules.graphics.gl.SpriteBatcher;
import yoan.game.framework.modules.graphics.gl.Texture;
import yoan.game.framework.modules.graphics.gl.TextureRegion;
import yoan.game.framework.modules.input.Input.TouchEvent;
import yoan.game.framework.modules.screen.Screen;
import yoan.game.framework.util.math.OverlapTester;
import yoan.game.framework.util.math.Rectangle;
import yoan.game.framework.util.math.Vector2;
import yoan.game.gf2d.screens.GLScreen;
import yoan.game.gf2d.util.Assets;

/**
 * Ecran d'aide
 * @author yoan
 */
public abstract class HelpScreen extends GLScreen {
	/** Camera pour l'interface */
	private Camera2D guiCam;
	/** Batcher d'affichage de sprite */
	private SpriteBatcher batcher;
	/** Limites du bouton Next */
	private Rectangle nextBounds;
	/** Point touché à l'écran (sert à convertir les coordonnées de l'écran coordonnées du monde */
	private Vector2 touchPoint;
	/** Texture des images d'aide */
	private Texture helpImage;
	/** Image à afficher dans l'atlas d'aide */
	private TextureRegion helpRegion;
	
	/** l'écran suivant (alimenté dans l'implémentation) */
	protected Screen nextScreen;
	/** le nom du fichier image de la texture */
	protected String helpImageName;
	
	/**
	 * Constructeur hérité de GLScreen
	 * @param game : instance du jeu
	 */
	public HelpScreen(Game game) {
		super(game);
		guiCam = new Camera2D(glGraphics, SCREEN_WIDTH, SCREEN_HEIGHT);
		nextBounds = new Rectangle(SCREEN_WIDTH - BUTTON_SIZE, 0, BUTTON_SIZE, BUTTON_SIZE);
		touchPoint = new Vector2();
		batcher = new SpriteBatcher(glGraphics, 1);
	}
	
	/**
	 * Retire la pause
	 */
	@Override
	public void resume() {
		//(re)chargement l'image à afficher
    	helpImage = new Texture(glGame, helpImageName);
    	helpRegion = new TextureRegion(helpImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
	}
	 
	/**
	 * Met l'écran en pause
	 */
	@Override
	public void pause() {
		//libération de l'image en mémoire 
		helpImage.dispose();
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
				//sur le bouton Next
				if(OverlapTester.pointInRectangle(nextBounds, touchPoint)){
					Assets.playSound(Assets.clickSound);
					game.setScreen(nextScreen);
					return;
				}
			}
		}
	}
	
	@Override
	public void present(float deltaTime){
		GL10 gl= glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.setViewportAndMatrices();
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		//affichage du fond
		batcher.beginBatch(helpImage);
		batcher.drawSprite(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, SCREEN_WIDTH, SCREEN_HEIGHT, helpRegion);
		batcher.endBatch();
		
		//affichage du bouton
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		batcher.beginBatch(Assets.items);
		batcher.drawSprite(SCREEN_WIDTH - BASIC_ELEMENT_SIZE, BASIC_ELEMENT_SIZE, -BUTTON_SIZE, BUTTON_SIZE, Assets.arrow);
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}

	/** inutilisé pour cet écran */
	@Override
	public void dispose(){}
}

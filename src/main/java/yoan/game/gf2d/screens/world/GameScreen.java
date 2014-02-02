package yoan.game.gf2d.screens.world;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import yoan.game.framework.modules.game.Game;
import yoan.game.framework.modules.graphics.gl.Camera2D;
import yoan.game.framework.modules.graphics.gl.SpriteBatcher;
import yoan.game.framework.modules.input.Input.TouchEvent;
import yoan.game.framework.util.math.OverlapTester;
import yoan.game.framework.util.math.Rectangle;
import yoan.game.framework.util.math.Vector2;
import yoan.game.gf2d.screens.GLScreen;
import yoan.game.gf2d.screens.MainMenuScreen;
import yoan.game.gf2d.util.Assets;
import yoan.game.gf2d.util.Settings;
import yoan.game.gf2d.world.model.GuiUtils;
import yoan.game.gf2d.world.model.World;
import yoan.game.gf2d.world.model.World.WorldListener;

/**
 * 
 * @author yoan
 */
public class GameScreen extends GLScreen {
	/** Enum des états de l'écran de jeu */
	enum State {
		/** Pret à démarrer */
		READY,
		/** En cours de partie */
		RUNNING,
		/** En pause */
		PAUSED,
		/** Niveau fini */
		LEVEL_END,
		/** Perdu */
		GAME_OVER
	}
	
	/** Etat courant du jeu */
	State state;
	/** Camera pour l'interface */
	Camera2D guiCam;
	/** Point touché à l'écran (sert à convertir les coordonnées de l'écran coordonnées du monde */
	Vector2 touchPoint;
	/** Batcher d'affichage de sprite */
	SpriteBatcher batcher;
	/** Modélisation du monde */
	World world;
	/** Consommateur d'évenement du monde */
	WorldListener worldListener;
	/** Moteur de rendu du monde */
	WorldRenderer renderer;
	/** Limites du bouton pause */
	Rectangle pauseBounds;
	/** Limites du bouton continue */
	Rectangle resumeBounds;
	/** Limites du bouton quitter */
	Rectangle quitBounds;
	/** Limites du bouton son */
	Rectangle soundBounds;
	
	/**
	 * Constructeur hérité de GLScreen
	 * @param game : instance du jeu
	 */
	public GameScreen(Game game) {
        super(game);
        state = State.READY;
        guiCam = new Camera2D(glGraphics, SCREEN_WIDTH, SCREEN_HEIGHT);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 1000);
        //implémentation anonyme du consommateur d'événement du monde
        worldListener = new WorldListener() {
            @Override
            public void jump() {            
                Assets.playSound(Assets.jumpSound);
            }

            @Override
            public void hit() {
                Assets.playSound(Assets.hitSound);
            }
        };
        world = new World(worldListener);
        renderer = new WorldRenderer(glGraphics, batcher, world);
        pauseBounds = new Rectangle(0, SCREEN_HEIGHT- BUTTON_SIZE, BUTTON_SIZE, BUTTON_SIZE);
        soundBounds = new Rectangle(0, 0, BUTTON_SIZE, BUTTON_SIZE);
        //TODO [1280x800] constantes à modifier
        resumeBounds = new Rectangle(SCREEN_WIDTH/2 - 3*BASIC_ELEMENT_SIZE, SCREEN_HEIGHT/2, 6*BASIC_ELEMENT_SIZE, 36);
        quitBounds = new Rectangle(SCREEN_WIDTH/2 - 3*BASIC_ELEMENT_SIZE, SCREEN_HEIGHT/2 - 36, 6*BASIC_ELEMENT_SIZE, 36);
    }

	/**
	 * Mise à jour de l'affichage en fonction de l'état courant
	 * @param deltaTime : temps écoulé depuis le dernier update
	 */
	@Override
	public void update(float deltaTime) {
		//compensation du GC
		if(deltaTime > 0.1f) deltaTime= 0.1f;

		switch(state){
			case READY:
				updateReady();
				break;
			case RUNNING:
				updateRunning(deltaTime);
				break;
			case PAUSED:
				updatePaused();
				break;
			case LEVEL_END:
				updateLevelEnd();
				break;
			case GAME_OVER:
				updateGameOver();
				break;
		}
	}
	
	/**
	 * Surchage de update pour l'écran de démarrage de partie
	 * Gère les touch events sur l'écran
	 */
	private void updateReady(){
		//si touché de l'écran
		if(game.getInput().getTouchEvents().size() > 0){
			//démarrage de la partie
			state= State.RUNNING;
		}
	}
	
	/**
	 * Surchage de update pour l'écran de jeu
	 * Gère les touch events et l'orientation de l'écran
	 */
	private void updateRunning(float deltaTime){
		//gestion du bouton pause
		List<TouchEvent> touchEvents= game.getInput().getTouchEvents();
		int len= touchEvents.size();
		for(int i= 0; i < len; i++){
			TouchEvent event= touchEvents.get(i);
			if(event.type != TouchEvent.TOUCH_UP) continue;

			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);
			//si click sur le bouton pause
			if(OverlapTester.pointInRectangle(pauseBounds, touchPoint)){
				Assets.playSound(Assets.clickSound);
				//mise en pause du jeu
				state= State.PAUSED;
				return;
			}
		}

		//mise à jour du mond en fonction de l'accéleromètre
		world.update(deltaTime, game.getInput().getAccelY());
		
		//si niveau terminé
		if(world.state == World.State.NEXT_LEVEL){
			state= State.LEVEL_END;
		//si partie perdue
		} else if(world.state == World.State.GAME_OVER){
			state= State.GAME_OVER;
		}
	}
	
	/**
	 * Surchage de update pour l'écran de pause
	 * Gère les touch events sur l'écran
	 */
	private void updatePaused(){
		List<TouchEvent> touchEvents= game.getInput().getTouchEvents();
		int len= touchEvents.size();
		for(int i= 0; i < len; i++){
			TouchEvent event= touchEvents.get(i);
			if(event.type != TouchEvent.TOUCH_UP) continue;

			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);

			//si click sur le bouton continue
			if(OverlapTester.pointInRectangle(resumeBounds, touchPoint)){
				Assets.playSound(Assets.clickSound);
				//on reprend la partie
				state= State.RUNNING;
				return;
			}

			//si click sur le bouton quitter
			if(OverlapTester.pointInRectangle(quitBounds, touchPoint)){
				Assets.playSound(Assets.clickSound);
				//on retourne au menu principal
				game.setScreen(new MainMenuScreen(game));
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
	
	/**
	 * Surchage de update pour l'écran de fin de niveau
	 * Gère les touch events sur l'écran
	 */
	private void updateLevelEnd(){
		List<TouchEvent> touchEvents= game.getInput().getTouchEvents();
		int len= touchEvents.size();
		for(int i= 0; i < len; i++){
			TouchEvent event= touchEvents.get(i);
			if(event.type != TouchEvent.TOUCH_UP) continue;
			//si écran touché
			//réinitialisation du monde
			world= new World(worldListener);
			//réinitialisation du rendu
			renderer= new WorldRenderer(glGraphics, batcher, world);
			state= State.READY;
		}
	}
	
	/**
	 * Surchage de update pour l'écran de fin de partie
	 * Gère les touch events sur l'écran
	 */
	private void updateGameOver(){
		List<TouchEvent> touchEvents= game.getInput().getTouchEvents();
		int len= touchEvents.size();
		for(int i= 0; i < len; i++){
			TouchEvent event= touchEvents.get(i);
			if(event.type != TouchEvent.TOUCH_UP) continue;
			//si écran touché
			//on retourne au menu principal
			game.setScreen(new MainMenuScreen(game));
		}
	}
	
	/**
	 * Gestion de l'affichage de l'écran de jeu
	 * @param deltaTime : temps écoulé depuis le dernier affichage
	 */
	@Override
	public void present(float deltaTime) {
		GL10 gl= glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		//rendu du monde
		renderer.render();

		//affichage de l'interface par dessus le monde
		guiCam.setViewportAndMatrices();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		//un seul batch dépendant de l'état du jeu
		batcher.beginBatch(Assets.items);
		switch(state){
			case READY:
				presentReady();
				break;
			case RUNNING:
				presentRunning();
				break;
			case PAUSED:
				presentPaused();
				break;
			case LEVEL_END:
				presentLevelEnd();
				break;
			case GAME_OVER:
				presentGameOver();
				break;
		}
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}
	
	/**
	 * Gestion de l'affichage de l'UI de l'écran de démarrage de partie
	 */
	private void presentReady() {
		//dessin du message "Ready?"
		batcher.drawSprite(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, 6*BASIC_ELEMENT_SIZE, BASIC_ELEMENT_SIZE, Assets.ready);
	}
	
	/**
	 * Gestion de l'affichage de l'UI en cours de partie
	 */
	private void presentRunning(){
		//dessin du bouton pause
		batcher.drawSprite(BASIC_ELEMENT_SIZE, SCREEN_HEIGHT - BASIC_ELEMENT_SIZE, BUTTON_SIZE, BUTTON_SIZE, Assets.pause);
	}
	
	/**
	 * Gestion de l'affichage de l'UI de l'écran de pause
	 */
	private void presentPaused() {
		//dessin du menu de pause
		batcher.drawSprite(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, 6*BASIC_ELEMENT_SIZE, 3*BASIC_ELEMENT_SIZE, Assets.pauseMenu);
		batcher.drawSprite(BASIC_ELEMENT_SIZE, BASIC_ELEMENT_SIZE, BUTTON_SIZE, BUTTON_SIZE, Settings.soundEnabled ? Assets.soundOn : Assets.soundOff);
	}
	
	/**
	 * Gestion de l'affichage de l'UI de l'écran de fin de niveau
	 */
	private void presentLevelEnd(){
		String topText= "the princess is ...";
		String bottomText= "in another castle!";
		float topWidth= Assets.font.glyphWidth * topText.length();
		float bottomWidth= Assets.font.glyphWidth * bottomText.length();
		//affichage du texte
		//TODO [1280x800] constantes à modifier
		Assets.font.drawText(batcher, topText, SCREEN_WIDTH/2 - topWidth / 2, SCREEN_HEIGHT - 40);
		Assets.font.drawText(batcher, bottomText, SCREEN_WIDTH/2 - bottomWidth / 2, 40);
	}
	

	/**
	 * Gestion de l'affichage de l'UI de l'écran de fin de partie
	 */
	private void presentGameOver() {
		//dessin du message "GAME OVER"
		batcher.drawSprite(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, 5*BASIC_ELEMENT_SIZE, 3*BASIC_ELEMENT_SIZE, Assets.gameOver);
	}
	
	/**
	 * Mise en pause du jeu
	 */
	@Override
	public void pause(){
		if(state == State.RUNNING) state= State.PAUSED;
	}

	/** inutilisé pour cet écran */
	@Override
	public void resume(){}

	/** inutilisé pour cet écran */
	@Override
	public void dispose(){}

}
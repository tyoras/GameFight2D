package yoan.game.gf2d.screens.world;

import javax.microedition.khronos.opengles.GL10;

import yoan.game.framework.modules.graphics.gl.Animation;
import yoan.game.framework.modules.graphics.gl.Camera2D;
import yoan.game.framework.modules.graphics.gl.GLGraphics;
import yoan.game.framework.modules.graphics.gl.SpriteBatcher;
import yoan.game.framework.modules.graphics.gl.TextureRegion;
import yoan.game.gf2d.util.Assets;
import yoan.game.gf2d.world.model.Platform;
import yoan.game.gf2d.world.model.World;

/**
 * Moteur de rendu du monde
 * @author yoan
 */
public class WorldRenderer {
	/** Largeur de l'écran en unité du monde */
	static final float FRUSTUM_WIDTH = 15;
	/** Hauteur de l'écran en unité du monde */
	static final float FRUSTUM_HEIGHT = 10;
	
	/** Accès à la gestion des graphismes openGL ES */
	GLGraphics glGraphics;
	/** Modélisation du monde */
	World world;
	/** Caméra centrée sur la plus grande hauteur atteinte par le personnage */
	Camera2D cam;
	/** Batcher d'affichage de sprite */
	SpriteBatcher batcher;
	
	/**
	 * Constructeur du moteur de rendu du monde
	 * @param glGraphics : accès à la gestion des graphismes openGL ES
	 * @param batcher : instance du batcher à utilisé pour faire le rendu
	 * @param world : modélisation du monde à rendre
	 */
	public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world){
		this.glGraphics = glGraphics;
		this.world = world;
		this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.batcher = batcher;
	}
	
	/**
	 * Effectue le rendu de l'état actuel du modèle du monde
	 */
	public void render(){
		//la caméra se positionne à la plus grande hauteur atteinte
		if(world.champ.position.y > cam.position.y) cam.position.y= world.champ.position.y;
		cam.setViewportAndMatrices();
		renderBackground();
		renderObjects();
	}

	/**
	 * Effectue le rendu du fond
	 */
	private void renderBackground(){
		batcher.beginBatch(Assets.background);
		//dessin du fond centré sur la caméra
		batcher.drawSprite(cam.position.x, cam.position.y, FRUSTUM_WIDTH, FRUSTUM_HEIGHT, Assets.backgroundRegion);
		batcher.endBatch();
	}
	
	/**
	 * Effectue le rendu des éléments
	 */
	private void renderObjects(){
		GL10 gl = glGraphics.getGL();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		//un seul batch pour tous les éléments
		batcher.beginBatch(Assets.items);
		renderChampion();
		renderPlatforms();
		renderItems();
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}

	/**
	 * Effectue le rendu du personnage en fonction de son état
	 */
	private void renderChampion(){
		TextureRegion keyFrame;
		switch(world.champ.state){
			case FALL:
				//recupération de la frame de chute
				keyFrame= Assets.fakeChamp1Fall.getKeyFrame(world.champ.stateTime, Animation.ANIMATION_LOOPING);
				break;
			case JUMP:
				//récupération de la frame de saut
				keyFrame= Assets.fakeChamp1Jump.getKeyFrame(world.champ.stateTime, Animation.ANIMATION_LOOPING);
				break;
			case HIT:
			default:
				keyFrame= Assets.fakeChamp1Hit;
		}
		//on détermine de quel côté il est tourné
		float side= world.champ.velocity.x < 0 ? -1 : 1;
		batcher.drawSprite(world.champ.position.x, world.champ.position.y, side * 1, 1, keyFrame);
	}
	
	/**
	 * Effectue le rendu des plateformes en fonction de leur état
	 */
	private void renderPlatforms(){
		int len= world.platforms.size();
		for(int i= 0; i < len; i++){
			Platform platform= world.platforms.get(i);
			TextureRegion keyFrame= Assets.platform;
			if(platform.state == Platform.State.PULVERIZING){
				//récupération de la frame de destruction
				keyFrame= Assets.breakingPlatform.getKeyFrame(platform.stateTime, Animation.ANIMATION_NONLOOPING);
			}
			batcher.drawSprite(platform.position.x, platform.position.y, 2, 0.5f, keyFrame);
		}
	}
	
	/**
	 * Effectue le rendu des ressorts et des pièces en fonction de leur état
	 */
	private void renderItems(){
	}
	
}
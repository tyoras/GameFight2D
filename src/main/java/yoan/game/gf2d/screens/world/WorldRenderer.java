package yoan.game.gf2d.screens.world;

import javax.microedition.khronos.opengles.GL10;

import yoan.game.framework.modules.graphics.gl.Animation;
import yoan.game.framework.modules.graphics.gl.Camera2D;
import yoan.game.framework.modules.graphics.gl.GLGraphics;
import yoan.game.framework.modules.graphics.gl.SpriteBatcher;
import yoan.game.framework.modules.graphics.gl.TextureRegion;
import yoan.game.framework.modules.graphics.gl.Vertices;
import yoan.game.gf2d.util.Assets;
import yoan.game.gf2d.world.model.Platform;
import yoan.game.gf2d.world.model.World;
import yoan.game.gf2d.world.model.champions.Champion;

/**
 * Moteur de rendu du monde
 * @author yoan
 */
public class WorldRenderer {
	private static final boolean DEBUG = true;
	
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
		//if(world.champ.position.y > cam.position.y) cam.position.y= world.champ.position.y;
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
		
		batcher.beginBatch(Assets.robot);
		renderChampion();
		batcher.endBatch();
		batcher.beginBatch(Assets.items);
		renderEnemy();
		renderPlatforms();
		renderItems();
		batcher.endBatch();
		
		if (DEBUG)
			renderChampionBound(world.champ);
			renderChampionBound(world.enemy);
		
		gl.glDisable(GL10.GL_BLEND);
	}

	/**
	 * Effectue le rendu du personnage en fonction de son état
	 */
	private void renderChampion(){
		TextureRegion keyFrame;
		Champion champ = world.champ;
		switch(world.champ.state){
			case FALL:
				//recupération de la frame de chute
				keyFrame= Assets.robotStand.getKeyFrame(champ.stateTime, Animation.ANIMATION_LOOPING);
				break;
			case JUMP:
				//récupération de la frame de saut
				keyFrame= Assets.robotStand.getKeyFrame(champ.stateTime, Animation.ANIMATION_LOOPING);
				break;
			case STAND:
				keyFrame= Assets.robotStand.getKeyFrame(champ.stateTime, Animation.ANIMATION_LOOPING);
				break;
			case WALK:
				keyFrame= Assets.robotWalk.getKeyFrame(champ.stateTime, Animation.ANIMATION_LOOPING);
				break;
			case HIT:
			default:
				keyFrame= Assets.robotHit;
		}
		//on détermine de quel côté il est tourné
		float side= world.champ.velocity.x < 0 ? -1 : 1;
		batcher.drawSprite(world.champ.position.x, world.champ.position.y, side *2, 4, keyFrame);
	}
	
	/**
	 * Débug uniquement : affiche les limites physique d'un champion
	 * @param champ : le champion dont on veut afficher les limites
	 */
	private void renderChampionBound(Champion champ){
		GL10 gl = glGraphics.getGL();
		gl.glDisable(GL10.GL_TEXTURE_2D);
		Vertices vertices = new Vertices(glGraphics, 4, 0, true, false);
		
		//pré-calcul
		float halfWidth = champ.bounds.width / 2;
		float halfHeight = champ.bounds.height / 2;
		//calcul du coin inférieur gauche
		float x1 = champ.position.x - halfWidth;
		float y1 = champ.position.y - halfHeight;
		//calcul du coin supérieur droit
		float x2 = champ.position.x + halfWidth;
		float y2 = champ.position.y + halfHeight;
		
		int bufferIndex = 0;
		float[] verticesBuffer = new float[4*6];
		//coin inférieur gauche
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = 1.0f;
		verticesBuffer[bufferIndex++] = 0.0f;
		verticesBuffer[bufferIndex++] = 0.0f;
		verticesBuffer[bufferIndex++] = 1.0f;
		//coin inférieur droit
		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = 1.0f;
		verticesBuffer[bufferIndex++] = 0.0f;
		verticesBuffer[bufferIndex++] = 0.0f;
		verticesBuffer[bufferIndex++] = 1.0f;
		//coin supérieur droit
		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = 1.0f;
		verticesBuffer[bufferIndex++] = 0.0f;
		verticesBuffer[bufferIndex++] = 0.0f;
		verticesBuffer[bufferIndex++] = 1.0f;
		//coin supérieur gauche
		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = 1.0f;
		verticesBuffer[bufferIndex++] = 0.0f;
		verticesBuffer[bufferIndex++] = 0.0f;
		verticesBuffer[bufferIndex++] = 1.0f;
		vertices.setVertices(verticesBuffer, 0, bufferIndex);
		vertices.bind();
		vertices.draw(GL10.GL_LINE_LOOP, 0, 4);
		vertices.unbind();
		gl.glEnable(GL10.GL_TEXTURE_2D);
	}
	
	
	/**
	 * Effectue le rendu de l'adversaire en fonction de son état
	 */
	private void renderEnemy(){
		TextureRegion keyFrame;
		switch(world.enemy.state){
			case FALL:
				//recupération de la frame de chute
				keyFrame= Assets.fakeChamp1Fall.getKeyFrame(world.enemy.stateTime, Animation.ANIMATION_LOOPING);
				break;
			case JUMP:
				//récupération de la frame de saut
				keyFrame= Assets.fakeChamp1Jump.getKeyFrame(world.enemy.stateTime, Animation.ANIMATION_LOOPING);
				break;
			case HIT:
			default:
				keyFrame= Assets.fakeChamp1Hit;
		}
		//on détermine de quel côté il est tourné
		float side= world.enemy.velocity.x < 0 ? -1 : 1;
		batcher.drawSprite(world.enemy.position.x, world.enemy.position.y, side * 1, 1, keyFrame);
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
package yoan.game.gf2d.world.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import yoan.game.framework.util.math.OverlapTester;
import yoan.game.framework.util.math.Vector2;
import yoan.game.gf2d.world.model.champions.Champion;
import yoan.game.gf2d.world.model.champions.FakeChampion1;
import yoan.game.gf2d.world.model.champions.Robot;

/**
 * Modélisation du monde
 * @author yoan
 */
public class World {
	/**
	 * Interface d'écoute des évenements du monde
	 */
	public interface WorldListener {
		/** Un saut a été effectué */
		public void jump();
		/** Personnage touché */
		public void hit();
	}

	/** Largeur du monde */
	public static final float WORLD_WIDTH = 15;
	/** Hauteur du monde */
	public static final float WORLD_HEIGHT = 10;
	/** Gravité */
	public static final Vector2 gravity = new Vector2(0, -12);
	
	/** Enum des etats du monde */
	public enum State {
		/** En cours de jeu */
		RUNNING,
		/** Passage au niveau suivant */
		NEXT_LEVEL,
		/** Perdu */
		GAME_OVER
	}
	
	/** Instance du personnage */
	public final Champion champ;
	
	/** Instance du personnage */
	public final Champion enemy;
	
	/** Liste des plateformes */
	public final List<Platform> platforms;
    /** listener d'évenement du monde */
    public final WorldListener listener;
    /** Générateur d'aléatoire */
    public final Random rand;
    /** Etat courant du monde */
    public State state;
    
    /**
     * Constructeur du monde
     * @param listener : consommateur des évenements du monde
     */
    public World(WorldListener listener) {
    	champ = new Robot(WORLD_WIDTH/2, 4);
    	enemy = new FakeChampion1(WORLD_WIDTH/3, 1);
    	platforms = new ArrayList<Platform>();
    	this.listener = listener;
    	rand = new Random();
    	generateLevel();
    	 
    	state = State.RUNNING;
    }

    /**
     * Génére un nouveau niveau
     */
    private void generateLevel() {
        float y = Platform.PLATFORM_HEIGHT / 2;
        //calul du saut maximum = v*v/2G
        float maxJumpHeight = champ.JUMP_VELOCITY * champ.JUMP_VELOCITY / (2 * -gravity.y);
        //tant qu'on a pas atteint le haut de la map
        while (y < WORLD_HEIGHT - WORLD_WIDTH / 2) {
            //création d'une platforme
        	Platform.Type type = rand.nextFloat() > 0.8f ? Platform.Type.MOVING : Platform.Type.STATIC;
            float x = rand.nextFloat() * (WORLD_WIDTH - Platform.PLATFORM_WIDTH) + Platform.PLATFORM_WIDTH / 2;
            Platform platform = new Platform(type, x, y);
            platforms.add(platform);
   
            //itértion sur un peu moins que la hauteur max de saut
            y += (maxJumpHeight - 0.5f);
            y -= rand.nextFloat() * (maxJumpHeight / 3);
        }
    }

    /**
     * Mise à jour des éléments du jeu
     * @param deltaTime : temps écoulé
     * @param accelX : accéleration sur l'axe X
     */
	public void update(float deltaTime, float accelX) {
		updateChampion(deltaTime, accelX);
		updateEnemy(deltaTime, accelX);
		updatePlatforms(deltaTime);
		if(champ.state != Champion.State.HIT) checkCollisions();
		checkGameOver();
	}

	/**
	 * Mise à jour du personnage
	 * @param deltaTime : temps écoulé
	 * @param accelX : accéleration sur l'axe X
	 */
	private void updateChampion(float deltaTime, float accelX) {
		//le champion s'arrête de tomber au niveau du sol
	    //if (champ.state != Champion.State.HIT && champ.position.y <= 2.0f)
    	if (champ.position.y <= 2.0f) {
	    	champ.velocity.y = 0;
    		champ.position.y = 2.0f;
    	}
	    //modification de la vitesse horizontale en fonction de l'inclinaison du téléphone
	    if (champ.state != Champion.State.HIT)
	    	// /10 pour la normalisation [-10;10] => [-1;1]
	        champ.velocity.x = accelX / 10 * champ.MOVE_VELOCITY;
	    //rebond sur les côtés autorisé
	    if (champ.state != Champion.State.HIT && (champ.getMostLeftPosition() < 0 || champ.getMostRightPosition() > World.WORLD_WIDTH))
	        champ.hitSide();
	    if (champ.state != Champion.State.HIT && (champ.getBotPosition() <= 0))
	        champ.hitGround();
	    champ.update(deltaTime);
	}
	
	/**
	 * Mise à jour de l'adversaire
	 * @param deltaTime : temps écoulé
	 * @param accelX : accéleration sur l'axe X
	 */
	private void updateEnemy(float deltaTime, float accelX) {
		//rebond sur le sol autorisé
	    if (enemy.state != Champion.State.HIT && enemy.position.y <= 0.5f)
	    	enemy.hitPlatform();
	    //modification de la vitesse horizontale en fonction de l'inclinaison du téléphone
	    if (enemy.state != Champion.State.HIT)
	    	// /10 pour la normalisation [-10;10] => [-1;1]
	    	enemy.velocity.x = accelX / 100 * champ.MOVE_VELOCITY;
	    enemy.update(deltaTime);
	}

	/**
	 * Mise à jour des plateformes
	 * @param deltaTime : temps écoulé
	 */
	private void updatePlatforms(float deltaTime) {
		//pour chaque plateforme
	    int len = platforms.size();
	    for (int i = 0; i < len; i++) {
	        Platform platform = platforms.get(i);
	        platform.update(deltaTime);
	        //on retire celles qui sont entierement détruites
	        if (platform.state == Platform.State.PULVERIZING && platform.stateTime > Platform.PLATFORM_PULVERIZE_TIME) {
	            platforms.remove(platform);
	            len = platforms.size();
	        }
	    }
	}

	/**
	 * Détection des colisions
	 */
	private void checkCollisions(){
		checkPlatformCollisions();
		checkItemCollisions();
	}

	/**
	 * Détection des colisions avec les plateformes
	 */
	private void checkPlatformCollisions(){
		//pas de colision par le bas
		if(champ.velocity.y > 0) return;

		int len= platforms.size();
		for(int i= 0; i < len; i++){
			Platform platform= platforms.get(i);
			//si personnage au dessus de la plateforme
			if(champ.position.y > platform.position.y){
				//si chevauchement
				if(OverlapTester.overlapRectangles(champ.bounds, platform.bounds)){
					//on indique au personnage qu'il l'a touché
					champ.hitPlatform();
					//et on envoi un évenement saut au consommateur
					listener.jump();
					// 1 chance sur 2 de détruire la plateforme
					if(rand.nextFloat() > 0.5f){
						platform.pulverize();
					}
					break;
				}
			}
		}
	}

	/**
	 * Détection des colisions avec les autres éléments
	 */
	private void checkItemCollisions(){
		//si personnage au dessus de la plateforme
		if(champ.position.y > enemy.position.y){
			//si chevauchement
			if(OverlapTester.overlapRectangles(champ.bounds, enemy.bounds)){
				//on indique au personnage qu'il l'a touché
				champ.hitPlatform();
				//et on envoi un évenement saut au consommateur
				listener.jump();
				enemy.hit();
			}
		}
	}

	/**
	 * Détection de la mort du personnage
	 */
	private void checkGameOver(){
//		if(...){
//			state= State.GAME_OVER;
//		}
	}
}
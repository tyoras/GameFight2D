package yoan.game.gf2d.world.model.champions;

import yoan.game.framework.modules.game.model.DynamicGameObject;
import yoan.game.gf2d.world.model.World;

/**
 * 
 * @author yoan
 */
public abstract class Champion extends DynamicGameObject {
	/** Vitesse de saut */
	public final float JUMP_VELOCITY = 11;
	/** Vitesse de déplacement */
	public final float MOVE_VELOCITY = 20;
	
	/** Etat du personnage */
	public enum State {
		/** En train de sauter */
		JUMP,
		/** En train de tomber */
		FALL,
		/** Touché par un objet */
		HIT, 
		/** Debout sans bouger */
		STAND,
		/** En train de marcher */
		WALK
	}
	
	/** Etat courant du personnage */
	public State state;
	/** Durée depuis la création */
	public float stateTime= 0;
	
	/**
	 * Constructeur de positionnement du centre du personnage
	 */
	public Champion(float x, float y, float width, float heigth) {
		super(x, y, width, heigth);
		state = State.FALL;
		stateTime = 0;
	}
	
	/**
	 * Déplacement et mise à jour de l'état
	 * @param deltaTime : temps écoulé
	 */
	public void update(float deltaTime){
		//mise à jour de la vitesse en fonction de la gravité
		velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
		//déplacement
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		//décalage de la hitbox
		bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);

		//si déplacement vers le haut 
		if(velocity.y > 0 && state != State.HIT){
			//saut
			if(state != State.JUMP){
				state= State.JUMP;
				stateTime= 0;
			}
		//si déplacement vers le bas
		} else if(velocity.y < 0 && state != State.HIT){
			//chute
			if(state != State.FALL){
				state= State.FALL;
				stateTime= 0;
			}
		}
		//déplacements sur les bords opposés du monde
		if(position.x < 0) position.x= World.WORLD_WIDTH;
		if(position.x > World.WORLD_WIDTH) position.x= 0;

		stateTime+= deltaTime;
	}
	
	/**
	 * Gestion du touché d'écureuil
	 */
	public void hit(){
		//arret du déplacement
		velocity.set(0, 0);
		state= State.HIT;
		stateTime= 0;
	}

	/**
	 * Gestion du touché de plateforme
	 */
	public void hitPlatform(){
		//rebond => réinitialistion de la vitesse de saut vers le haut
		velocity.y= JUMP_VELOCITY;
		state= State.JUMP;
		stateTime= 0;
	}
}
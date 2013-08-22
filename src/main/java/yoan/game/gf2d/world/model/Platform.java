package yoan.game.gf2d.world.model;

import yoan.game.framework.modules.game.model.DynamicGameObject;

/**
 * 
 * @author yoan
 */
public class Platform extends DynamicGameObject {
	/** Largeur d'une plateforme dans le monde */
	public static final float PLATFORM_WIDTH = 2;
	/** Hauteur d'une plateforme dans le monde */
	public static final float PLATFORM_HEIGHT = 0.5f;
	/** Temps de destruction d'une plateforme */
	public static final float PLATFORM_PULVERIZE_TIME = 0.2f * 4;
	/** Vitesse des plateformes */
	public static final float PLATFORM_VELOCITY = 2;
	
	/** Types des plateformes */
	public enum Type {
		/** Non mouvante */
		STATIC,
		/** Mouvante */
		MOVING
	}
	
	/** Etats des plateformes */
	public enum State {
		/** Normale */
		NORMAL,
		/** En train de se détruire */
		PULVERIZING
	}
	
	/** Type de la plateforme */
	Type type;
	/** Etat courant de la plateforme */
	public State state;
	/** Durée depuis la création */
	public float stateTime= 0;
	
	/**
	 * Constructeur de positionnement du centre de la plateforme
	 * @param type : type de plateforme
	 */
	public Platform(Type type, float x, float y) {
		super(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
		this.type= type;
		this.state= State.NORMAL;
		this.stateTime= 0;
		//si la plateforme peut bouger
		if(type == Type.MOVING){
			//elle a une vitesse
			velocity.x= PLATFORM_VELOCITY;
		}
	}
	
	/**
	 * Déplacement 
	 * @param deltaTime : temps écoulé
	 */
	public void update(float deltaTime){
		//si la plateforme peut bouger
		if(type == Type.MOVING){
			//déplacement latéral
			position.add(velocity.x * deltaTime, 0);
			//décalage de la hitbox
			bounds.lowerLeft.set(position).sub(PLATFORM_WIDTH / 2,PLATFORM_HEIGHT / 2);

			//si sortie du monde sur la gauche
			if(position.x < PLATFORM_WIDTH / 2){
				velocity.x= -velocity.x;
				position.x= PLATFORM_WIDTH / 2;
			}
			//si sortie du monde sur la droite
			if(position.x > World.WORLD_WIDTH - PLATFORM_WIDTH / 2){
				velocity.x= -velocity.x;
				position.x= World.WORLD_WIDTH - PLATFORM_WIDTH / 2;
			}
		}

		stateTime+= deltaTime;
	}
	
	/**
	 * Destruction de la plateforme
	 */
	public void pulverize() {
		state = State.PULVERIZING;
		stateTime = 0;
		//plus de déplacement
		velocity.x = 0;
	}
}
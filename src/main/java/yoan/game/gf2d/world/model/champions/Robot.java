package yoan.game.gf2d.world.model.champions;

import yoan.game.gf2d.world.model.World;
import static yoan.game.gf2d.world.model.champions.Champion.State.*;


/**
 * Implémentation du champion : "Robot"
 * @author yoan
 */
public class Robot extends Champion {
	/** Largeur du personnage dans le monde */
	public static final float ROBOT_WIDTH = 2.0f;
	/** Hauteur du personnage dans le monde */
	public static final float ROBOT_HEIGHT = 4.0f;
	
	public Robot(float x, float y){
		super(x, y, ROBOT_WIDTH, ROBOT_HEIGHT);
		state = STAND;
	}
	
	/**
	 * Surcharge du comportement du robot
	 * @param deltaTime : temps écoulé
	 */
	@Override
	public void update(float deltaTime){
		//mise à jour de la vitesse en fonction de la gravité
		velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
		//déplacement
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		//décalage de la hitbox
		bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);

		//si déplacement vers le haut 
		if(velocity.y > 0 && state != HIT){
			//saut
			changeState(JUMP);
		//si déplacement vers le bas
		} else if(velocity.y < 0 && position.y > 0 && state != HIT){
			//chute
			changeState(FALL);
		} else if(velocity.y <= 0 && position.y <= 0&& state != HIT) {
			changeState(STAND);
		}

		stateTime+= deltaTime;
	}
	
	@Override
	public void hitSide() {
		//if (state == STAND) {
			velocity.x= -velocity.x;
		//}
	}
}

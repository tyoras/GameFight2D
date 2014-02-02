package yoan.game.gf2d.world.model.champions;


/**
 * Implémentation temporaire d'un champion
 * @author yoan
 */
public class FakeChampion1 extends Champion {
	/** Largeur du personnage dans le monde */
	public static final float FAKECHAMP_1_WIDTH = 0.8f;
	/** Hauteur du personnage dans le monde */
	public static final float FAKECHAMP_1_HEIGHT = 0.8f;
	
	public FakeChampion1(float x, float y){
		super(x, y, FAKECHAMP_1_WIDTH, FAKECHAMP_1_HEIGHT);
	}
}

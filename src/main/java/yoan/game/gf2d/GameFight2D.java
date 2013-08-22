package yoan.game.gf2d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import yoan.game.framework.modules.game.gl.GLGame;
import yoan.game.framework.modules.screen.Screen;
import yoan.game.gf2d.screens.MainMenuScreen;
import yoan.game.gf2d.util.Assets;
import yoan.game.gf2d.util.Settings;

/**
 * Activité principale du jeu
 * @author yoan
 */
public class GameFight2D extends GLGame {
	/** Indique si c'est la première fois qu'on créer l'activité */
	boolean firstTimeCreate = true;
	 
	/**
	 * Récupère l'écran de démarrage
	 * @return Screen
	 */
	public Screen getStartScreen() {
		return new MainMenuScreen(this);
	}
	 
	/**
	 * Appelé à la création de la surface GL
	 * @param gl : l'instance courante de GL10
	 * @param config : la config actuelle d'OpenGL ES
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config){
		super.onSurfaceCreated(gl, config);
		//au premier démarrage on charge entièrement les assets
		if(firstTimeCreate){
			Settings.load(getFileIO());
			Assets.load(this);
			firstTimeCreate= false;
		}else{
			//sinon, on les recharge
			Assets.reload();
		}
	}

	/**
	 * 
	 */
	@Override
	public void onPause(){
		super.onPause();
		//on arrête la music si l'application est en pause
		if(Settings.soundEnabled) Assets.music.pause();
	}
}

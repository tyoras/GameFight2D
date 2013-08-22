package yoan.game.gf2d.util;

import static yoan.game.gf2d.screens.GLScreen.BASIC_ELEMENT_SIZE;
import static yoan.game.gf2d.screens.GLScreen.BUTTON_SIZE;
import static yoan.game.gf2d.screens.GLScreen.GLYPH_HEIGHT;
import static yoan.game.gf2d.screens.GLScreen.GLYPH_WIDTH;
import static yoan.game.gf2d.screens.GLScreen.LOGO_HEIGHT;
import static yoan.game.gf2d.screens.GLScreen.PLATFORM_HEIGHT;
import static yoan.game.gf2d.screens.GLScreen.PLATFORM_WIDTH;
import static yoan.game.gf2d.screens.GLScreen.SCREEN_HEIGHT;
import static yoan.game.gf2d.screens.GLScreen.SCREEN_WIDTH;
import yoan.game.framework.modules.audio.Music;
import yoan.game.framework.modules.audio.Sound;
import yoan.game.framework.modules.game.gl.GLGame;
import yoan.game.framework.modules.graphics.gl.Animation;
import yoan.game.framework.modules.graphics.gl.Font;
import yoan.game.framework.modules.graphics.gl.Texture;
import yoan.game.framework.modules.graphics.gl.TextureRegion;

/**
 * Conteneur des assets (graphic, audio,...) du jeu
 * @author yoan
 */
public class Assets {
	/* Graphics */
	//Textures
	/** Fond du jeu */
	public static Texture background;
	public static TextureRegion backgroundRegion;
	/** Fond de l'acran de connexion */
	public static Texture connectScreen;
	public static TextureRegion connectScreenRegion;
	/** Atlas des éléments du jeu */
	public static Texture items; 
	//Textures dans l'atlas "items"
    /** Menu principal */
    public static TextureRegion mainMenu;
    /** Menu pause */
    public static TextureRegion pauseMenu;
    /** Menu "Ready?" */
    public static TextureRegion ready;
    /** Menu game over */
    public static TextureRegion gameOver;
    /** Menu Highscore */
    public static TextureRegion highScoresRegion;
    /** Menu Multiplayer */
    public static TextureRegion multiplayerRegion;
    /** Logo du jeu */
    public static TextureRegion logo;
    /** Bouton son activé */
    public static TextureRegion soundOn;
    /** Bouton son désactivé */
    public static TextureRegion soundOff;
    /** Bouton help */
    public static TextureRegion help;
    /** Flèche vers la gauche */
    public static TextureRegion arrow;
    /** Bouton pause */
    public static TextureRegion pause; 
    /** Personnage touché par un ennemi */
    public static TextureRegion fakeChamp1Hit;
    /** Plateformes */
    public static TextureRegion platform;
    //Animations
    /** Saut du personnage */
    public static Animation fakeChamp1Jump;
    /** Chute du personnage */
    public static Animation fakeChamp1Fall;
    /** Destruction de plateforme */
    public static Animation breakingPlatform;
    //Fonts
    public static Font font;
    
    /* Audio */
    //Musics
    public static Music music;
    //Sounds
    public static Sound clickSound;
    public static Sound jumpSound;
    public static Sound hitSound;
    
    /**
     * Charge les différents éléments du jeu
     * @param game : instance du jeu
     */
    public static void load(GLGame game) {
        background = new Texture(game, "pixmaps/background.png");
        backgroundRegion = new TextureRegion(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        connectScreen = new Texture(game, "pixmaps/connecting.png");
        connectScreenRegion = new TextureRegion(connectScreen, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        items = new Texture(game, "pixmaps/items.png");
        //TODO [1280x800] constantes à modifier
        mainMenu = new TextureRegion(items, 0, 7*BASIC_ELEMENT_SIZE, 300, 110);
        pauseMenu = new TextureRegion(items, 7*BASIC_ELEMENT_SIZE, 4*BASIC_ELEMENT_SIZE, 6*BASIC_ELEMENT_SIZE, 3*BASIC_ELEMENT_SIZE);
        ready = new TextureRegion(items, 10*BASIC_ELEMENT_SIZE, 7*BASIC_ELEMENT_SIZE, 6*BASIC_ELEMENT_SIZE, BASIC_ELEMENT_SIZE);
        gameOver = new TextureRegion(items, 11*BASIC_ELEMENT_SIZE, 8*BASIC_ELEMENT_SIZE, 5*BASIC_ELEMENT_SIZE, 3*BASIC_ELEMENT_SIZE);
        highScoresRegion = new TextureRegion(Assets.items, 0, 8*BASIC_ELEMENT_SIZE, 10*BASIC_ELEMENT_SIZE, 110 / 3);
        
        logo = new TextureRegion(items, 0, 11*BASIC_ELEMENT_SIZE, 274, LOGO_HEIGHT);
        //fin TODO
        soundOff = new TextureRegion(items, 0, 0, BUTTON_SIZE, BUTTON_SIZE);
        soundOn = new TextureRegion(items, BUTTON_SIZE, 0, BUTTON_SIZE, BUTTON_SIZE);
        help = new TextureRegion(items, 2*BUTTON_SIZE, 0, BUTTON_SIZE, BUTTON_SIZE);
        arrow = new TextureRegion(items, 0, BUTTON_SIZE, BUTTON_SIZE, BUTTON_SIZE);
        pause = new TextureRegion(items, BUTTON_SIZE, BUTTON_SIZE, BUTTON_SIZE, BUTTON_SIZE);
        
        fakeChamp1Jump = new Animation(0.2f,
                                new TextureRegion(items, 0, 2*BUTTON_SIZE, BASIC_ELEMENT_SIZE, BASIC_ELEMENT_SIZE),
                                new TextureRegion(items, BASIC_ELEMENT_SIZE, 2*BUTTON_SIZE, BASIC_ELEMENT_SIZE, BASIC_ELEMENT_SIZE));
        fakeChamp1Fall = new Animation(0.2f,
                                new TextureRegion(items, 2*BASIC_ELEMENT_SIZE, 2*BUTTON_SIZE, BASIC_ELEMENT_SIZE, BASIC_ELEMENT_SIZE),
                                new TextureRegion(items, 3*BASIC_ELEMENT_SIZE, 2*BUTTON_SIZE, BASIC_ELEMENT_SIZE, BASIC_ELEMENT_SIZE));
        fakeChamp1Hit = new TextureRegion(items, 4*BASIC_ELEMENT_SIZE, 2*BUTTON_SIZE, BASIC_ELEMENT_SIZE, BASIC_ELEMENT_SIZE);
        platform = new TextureRegion(items, 2*BASIC_ELEMENT_SIZE, 2*BUTTON_SIZE + BASIC_ELEMENT_SIZE, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        breakingPlatform = new Animation(0.2f,
                                     new TextureRegion(items, 2*BASIC_ELEMENT_SIZE, 2*BUTTON_SIZE + BASIC_ELEMENT_SIZE, PLATFORM_WIDTH, PLATFORM_HEIGHT),
                                     new TextureRegion(items, 2*BASIC_ELEMENT_SIZE, 2*BUTTON_SIZE + BASIC_ELEMENT_SIZE + PLATFORM_HEIGHT, PLATFORM_WIDTH, PLATFORM_HEIGHT),
                                     new TextureRegion(items, 2*BASIC_ELEMENT_SIZE, 2*BUTTON_SIZE + BASIC_ELEMENT_SIZE + 2*PLATFORM_HEIGHT, PLATFORM_WIDTH, PLATFORM_HEIGHT),
                                     new TextureRegion(items, 2*BASIC_ELEMENT_SIZE, 2*BUTTON_SIZE + BASIC_ELEMENT_SIZE + 3*PLATFORM_HEIGHT, PLATFORM_WIDTH, PLATFORM_HEIGHT));
        
        font = new Font(items, 2*BUTTON_SIZE + 3*BASIC_ELEMENT_SIZE, 0, 16, GLYPH_WIDTH, GLYPH_HEIGHT);
        
        //la music est jouée en boucle si le son est activé
        music = game.getAudio().newMusic("audio/musics/music.mp3");
        music.setLooping(true);
        music.setVolume(0.5f);
        if(Settings.soundEnabled)
            music.play();
        clickSound = game.getAudio().newSound("audio/sounds/click.ogg");    
        jumpSound = game.getAudio().newSound("audio/sounds/jump.ogg");
        hitSound = game.getAudio().newSound("audio/sounds/hit.ogg");
    }       
    
    /**
     * Recharge les éléments du jeu
     */
    public static void reload() {
        background.reload();
        items.reload();
        if(Settings.soundEnabled)
            music.play();
    }
    
    /**
     * Joue un son si le son du jeu est activé
     * @param sound : son à jouer
     */
    public static void playSound(Sound sound) {
        if(Settings.soundEnabled)
            sound.play(1);
    }
}

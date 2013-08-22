package yoan.game.gf2d.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import yoan.game.framework.modules.fileio.FileIO;

/**
 * Gestion des options du jeu
 * @author yoan
 */
public class Settings {
	/** Indique si le son est activé */
	public static boolean soundEnabled = true;
	/** Tableau des meilleurs scores */
	public static int[] highscores = new int[] { 0, 0, 0, 0, 0 };
	/** Nom du fichier de score */
    public final static String file = ".gamefight2D";

    /**
	 * Charge les options à partir du fichier
	 * @param files : gestionnaire de fichier
	 */
    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
        	//buffer de lecture sur le fichier
            in = new BufferedReader(new InputStreamReader(files.readFile(file)));
            //le son est stocké sur la première ligne
            soundEnabled = Boolean.parseBoolean(in.readLine());
            //les suivantes sont les scores
            for(int i = 0; i < 5; i++) {
                highscores[i] = Integer.parseInt(in.readLine());
            }
        } catch (IOException e) {
            // :( It's ok we have defaults
        } catch (NumberFormatException e) {
            // :/ It's ok, defaults save our day
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }

    /**
	 * Enregistre les options dans un fichier
	 * @param files : gestionnaire de fichier
	 */
    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
        	//buffer d'écriture sur le fichier de score
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(file)));
            out.write(Boolean.toString(soundEnabled));
            out.write("\n");
            for(int i = 0; i < 5; i++) {
                out.write(Integer.toString(highscores[i]));
                out.write("\n");
            }

        } catch (IOException e) {
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }
    }

    /**
	 * Ajout d'un score dans le tableau
	 * Insère le score à la bonne place s'il fait partie des 5 meilleurs
	 * @param score
	 */
    public static void addScore(int score) {
        for(int i=0; i < 5; i++) {
            if(highscores[i] < score) {
                for(int j= 4; j > i; j--)
                    highscores[j] = highscores[j-1];
                highscores[i] = score;
                break;
            }
        }
    }
}

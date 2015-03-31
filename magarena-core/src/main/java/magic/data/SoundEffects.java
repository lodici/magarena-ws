package magic.data;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import magic.model.MagicGame;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

public class SoundEffects {

    public static final String WIN_SOUND="win.au";
    public static final String LOSE_SOUND="lose.au";
    public static final String TURN_SOUND="turn.au";
    public static final String RESOLVE_SOUND="resolve.au";
    public static final String COMBAT_SOUND="combat.au";

    private static final File SOUNDS_PATH = MagicFileSystem.getDataPath(DataPath.SOUNDS).toFile();
    private static Clip clip;

    private SoundEffects() {}
    
    public static void playGameSound(final MagicGame game, final String name) {
        if (game.isReal() && GeneralConfig.getInstance().isSound()) {
            playSound(name);
        }
    }
    
    public static void playUISound(final String name) { 
        if (GeneralConfig.getInstance().isUiSound()) {
            playSound(name);
        }
    }

    private static void playSound(final String name) {
        if (clip != null) {
            if (clip.isRunning() || clip.isActive()) {
                clip.stop();
                clip.close();
            }
        }
        try (final AudioInputStream ins = AudioSystem.getAudioInputStream(new File(SOUNDS_PATH, name))) {
            clip = AudioSystem.getClip();
            clip.open(ins);
            clip.start();
        } catch (Exception ex) {
            System.err.println("WARNING. Unable to play clip " + name + ", " + ex.getMessage());
            // switch off sound for current session but restore on restart.
            GeneralConfig.getInstance().setIsUiSound(false);
            GeneralConfig.getInstance().setSound(false);
        }
    }

}

package magfx.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javafx.scene.image.Image;
import magic.data.MagicPredefinedFormat;
import magic.data.MagicSets;
import magic.utility.FileIO;

public final class MagicResources {
    private MagicResources() { }

    // Used as reference class for accessing JAR resources.
    private static final MagicResources instance = new MagicResources();

    public static String getFileContent(final MagicSets magicSet) {
        return getResourceFileContent("/magic/data/sets/" + magicSet.toString().replace("_", "") + ".txt");
    }

    public static String getFileContent(final MagicPredefinedFormat magicFormat) {
        return getResourceFileContent("/magic/data/formats/" + magicFormat.getFilename() + ".fmt");
    }

    public static InputStream getJarResourceStream(String filename) {
        return instance.getClass().getResourceAsStream(filename);
    }

    public static URL getCssFileUrl(String filename) {
        return instance.getClass().getResource("/styles/" + filename);
    }

    public static URL getMusicFileUrl(String filename) {
        return instance.getClass().getResource("/mp3/" + filename);
    }

    private static String getResourceFileContent(final String filename) {
        try (final InputStream inputStream = getJarResourceStream(filename)) {
            return inputStream != null ? FileIO.toStr(inputStream) : "";
        } catch (final IOException ex) {
            System.err.println(filename + " : " + ex.getMessage());
            return "";
        }
    }

    public static Image getIconImage(final String filename) {
        return new Image(MagicResources.getJarResourceStream("/magic/data/icons/" + filename));
    }

    public static Image getTextureImage(final String imagefilename) {
        return new Image(MagicResources.getJarResourceStream("/magic/data/textures/" + imagefilename));
    }

}

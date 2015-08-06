package magic.ui.utility;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import magic.exception.DesktopNotSupportedException;
import magic.translate.UiString;
import magic.utility.MagicFileSystem;
import static magic.utility.MagicFileSystem.getDataPath;
import magic.utility.MagicSystem;


public final class DesktopUtils {

    // translatable strings
    private static final String _S1 = "Sorry, opening this file with the default application is not supported on this operating system.";

    private DesktopUtils() {}

    public static void openMagicDirectory(final MagicFileSystem.DataPath directory) throws IOException {
        openDirectory(getDataPath(directory).toString());
    }

    /**
     * Opens specified directory in OS file explorer.
     */
    public static void openDirectory(final String path) throws IOException {
        final File imagesPath = new File(path);
        if (MagicSystem.IS_WINDOWS_OS) {
            // Specific fix for Windows.
            // If running Windows and path is the default "Magarena" directory
            // then Desktop.getDesktop() will start a new instance of Magarena
            // instead of opening the directory! This is because the "Magarena"
            // directory and "Magarena.exe" are both at the same level and
            // Windows incorrectly assumes you mean "Magarena.exe".
            new ProcessBuilder("explorer.exe", imagesPath.getPath()).start();
        } else {
            Desktop.getDesktop().open(imagesPath);
        }
    }

    public static void openFileInDefaultOsEditor(final File file) throws IOException, DesktopNotSupportedException {
        if (Desktop.isDesktopSupported()) {
            if (MagicSystem.IS_WINDOWS_OS) {
                // There is an issue in Windows where the open() method of getDesktop()
                // fails silently. The recommended solution is to use getRuntime().
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + file.toString());
            } else {
                Desktop.getDesktop().open(file);
            }
        } else {
            throw new DesktopNotSupportedException(UiString.get(_S1));
        }
    }


}

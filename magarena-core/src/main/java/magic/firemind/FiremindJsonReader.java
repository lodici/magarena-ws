package magic.firemind;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import magic.utility.DeckUtils;
import magic.data.GeneralConfig;
import magic.data.json.DownloadableJsonFile;
import magic.model.MagicDeck;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import org.apache.commons.io.FileUtils;

public final class FiremindJsonReader {
    private FiremindJsonReader() {}

    private static boolean isJsonFileUpToDate(final File jsonFile) {
        final Calendar calToday = Calendar.getInstance();
        final Calendar calFile = Calendar.getInstance();
        calFile.setTimeInMillis(jsonFile.lastModified());
        return calToday.get(Calendar.YEAR) == calFile.get(Calendar.YEAR)
                && calToday.get(Calendar.MONTH) == calFile.get(Calendar.MONTH)
                && calToday.get(Calendar.DAY_OF_MONTH) == calFile.get(Calendar.DAY_OF_MONTH);
    }

    private static void downloadLatestJsonFile(final File jsonFile) {
        try {
            final DownloadableJsonFile downloadFile =
                    new DownloadableJsonFile("https://www.firemind.ch/decks/top.json", jsonFile);
            downloadFile.download(GeneralConfig.getInstance().getProxy());
        } catch (IOException ex) {
            System.err.println("Download of json file failed : " + ex.getMessage());
        }
        if (jsonFile.exists()) {
            if (!isJsonFileUpToDate(jsonFile)) {
                // only attempt download once per day even if download fails.
                final Calendar cal = Calendar.getInstance();                
                jsonFile.setLastModified(cal.getTimeInMillis());
            }
        } else {
            try {
                // a problem occurred so create empty json file so that for
                // the rest of the day does not keep trying to download.
                jsonFile.createNewFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static void clearFiremindDecksDirectory(final Path firemindDecksPath) throws IOException {
        FileUtils.deleteDirectory(firemindDecksPath.toFile());
        MagicFileSystem.verifyDirectoryPath(firemindDecksPath);
    }

    public static void refreshTopDecks() {

        final File jsonFile = MagicFileSystem.getDataPath(DataPath.FIREMIND).resolve("topdecks.json").toFile();

        if (jsonFile.exists() && isJsonFileUpToDate(jsonFile)) {
            // JSON file has already been downloaded/updated today.
            return;
        }

        // Ensure Firemind decks directory exists.
        final Path firemindDecksPath = MagicFileSystem.getDataPath(DataPath.DECKS).resolve("firemind");
        MagicFileSystem.verifyDirectoryPath(firemindDecksPath);

        downloadLatestJsonFile(jsonFile);
        if (jsonFile.length() == 0) {
            // a problem occurred, use existing decks.
            return;
        }

        final List<MagicDeck> decks = new ArrayList<MagicDeck>();
        try {
            decks.addAll(JsonOrgParser.parse(jsonFile));
        } catch (IOException ex) {
            System.err.println("Invalid top.json : " + ex.getMessage());
            return;
        }

        try {
            clearFiremindDecksDirectory(firemindDecksPath);
        } catch (IOException ex) {
            System.err.println("Unable to clear " + firemindDecksPath);
            return;
        }

        for (MagicDeck deck : decks) {
            try {
                final String filename = firemindDecksPath.resolve(deck.getFilename() + ".dec").toString();
                DeckUtils.saveDeck(filename, deck);
            } catch (Exception ex) {
                System.err.println("Invalid deck : " + deck.getFilename() + " - " + ex.getMessage());
            }
        }
    }
}

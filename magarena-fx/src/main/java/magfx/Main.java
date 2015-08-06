package magfx;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import magfx.ui.ScreenController;
import magfx.ui.StartupProgressReporter;
import magfx.utility.MagicResources;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.data.UnimplementedParser;
import magic.exception.handler.ConsoleExceptionHandler;
import magic.utility.DeckUtils;
import magic.utility.MagicSystem;
import magic.utility.ProgressReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    // see configuration in /resources/simplelogger.properties.
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static MediaPlayer mediaPlayer;
    private static ProgressReporter reporter = new ProgressReporter();
        
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) {

        assert Platform.isFxApplicationThread();

        System.setProperty("data.dir", System.getProperty("data.dir", "data"));
        System.out.printf(":: data.dir set to '%s'\n", System.getProperty("data.dir"));

        reporter = new StartupProgressReporter();

        // display black full-screen as quickly as possible.
        ScreenController.setStage(stage);
        
        // entertain user while waiting for main menu to appear.
        playMusic();

        // initialize model / load data on background (non-ui) thread. It may take a while...
        doInitModel();

    }

    private void doInitModel() {
        final StartupService startupService = new StartupService();
        startupService.setOnSucceeded(e -> {
            ScreenController.showMainMenuScreen();
        });
        startupService.setOnFailed(e -> {
            LOGGER.error("Startup failed!\n" + e.toString());
            Platform.exit();
        });
        startupService.setOnCancelled(e -> {
            LOGGER.info("Startup cancelled!");
            Platform.exit();
        });
        startupService.start();
    }

    private class StartupService extends Service<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    assert !Platform.isFxApplicationThread();
                    if (!MagicSystem.isDevMode()) {
                        ScreenController.showStartupMessage("");
                        Thread.sleep(3000);
                    }
                    doMainInit(); 
//                    ScreenController.showStartupMessage("Pausing for 5 seconds...");
//                    Thread.sleep(5000);
                    return null;
                }
            };
        }
    }

    private static void playMusic() {
        if (Boolean.getBoolean("noMusic") == false) {
            final Media media = new Media(MagicResources.getMusicFileUrl("Evening Melodrama.mp3").toString());
            try {
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
            } catch (MediaException ex) {
                System.err.println("ERROR [playMusic] " + ex.getMessage());
            }
        }
    }

    private static void doMainInit() {

        Thread.setDefaultUncaughtExceptionHandler(new ConsoleExceptionHandler());

        LOGGER.trace("VM arguments:\n{}", getVmArguments());

//        parseCommandline(args);

        final long start_time = System.currentTimeMillis();

        // must load config here otherwise annotated card image theme-specifc
        // icons are not loaded before the AbilityIcon class is initialized
        // and you end up with the default icons instead.
        GeneralConfig.getInstance().load();

        initialize();
        
        LOGGER.trace("Initalization of engine took {} seconds", (double)(System.currentTimeMillis() - start_time) / 1000);

    }

    private static void initialize() {

        final File gamePathFile = magic.utility.MagicFileSystem.getDataPath().toFile();
        if (!gamePathFile.exists() && !gamePathFile.mkdir()) {
            throw new RuntimeException("Unable to create directory " + gamePathFile.toString());
        }

        final File modsPathFile = magic.utility.MagicFileSystem.getDataPath(magic.utility.MagicFileSystem.DataPath.MODS).toFile();
        if (!modsPathFile.exists() && !modsPathFile.mkdir()) {
            throw new RuntimeException("Unable to create directory " + modsPathFile.toString());
        }

        DeckUtils.createDeckFolder();

        initializeEngine();

    }

    static void initializeEngine() {
        if (Boolean.getBoolean("parseMissing")) {
            UnimplementedParser.parseScriptsMissing(reporter);
            reporter.setMessage("Parsing card abilities...");
            UnimplementedParser.parseCardAbilities();
        }
        CardDefinitions.loadCardDefinitions(reporter);
//        if (Boolean.getBoolean("debug")) {
//            reporter.setMessage("Loading card abilities...");
//            CardDefinitions.loadCardAbilities();
//        }
//        reporter.setMessage("Loading cube definitions...");
//        CubeDefinitions.loadCubeDefinitions();
//        reporter.setMessage("Loading keyword definitions...");
//        KeywordDefinitions.getInstance().loadKeywordDefinitions();
//        reporter.setMessage("Loading deck generators...");
//        DeckGenerators.getInstance().loadDeckGenerators();
    }

    private static String getVmArguments() {
        final List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();
        return args.stream()
                .map((arg) -> arg.concat(System.lineSeparator()))
                .reduce(String::concat)
                .orElse("");
    }



}

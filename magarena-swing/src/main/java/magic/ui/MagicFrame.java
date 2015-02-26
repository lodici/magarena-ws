package magic.ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import magic.data.CardDefinitions;
import magic.data.DuelConfig;
import magic.data.GeneralConfig;
import magic.data.MagicIcon;
import magic.data.OSXAdapter;
import magic.exception.DesktopNotSupportedException;
import magic.exception.InvalidDeckException;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.model.MagicDuel;
import magic.model.MagicGameLog;
import magic.ui.screen.AbstractScreen;
import magic.ui.theme.ThemeFactory;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;

@SuppressWarnings("serial")
public class MagicFrame extends JFrame implements IImageDragDropListener {

    private boolean ignoreWindowDeactivate;
    private boolean confirmQuitToDesktop = true;

    private static final Dimension MIN_SIZE = new Dimension(GeneralConfig.DEFAULT_WIDTH, GeneralConfig.DEFAULT_HEIGHT);

    // Check if we are on Mac OS X.  This is crucial to loading and using the OSXAdapter class.
    public static final boolean MAC_OS_X = System.getProperty("os.name").toLowerCase().startsWith("mac os x");

    private final GeneralConfig config;
    private final JPanel contentPanel;
    private MagicDuel duel;

    public MagicFrame(final String frameTitle) {

        ToolTipManager.sharedInstance().setInitialDelay(400);
        
        config = GeneralConfig.getInstance();

        // Setup frame.
        this.setTitle(frameTitle + "  [F11 : full screen]");
        this.setIconImage(IconImages.getIcon(MagicIcon.ARENA).getImage());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListeners();
        registerForMacOSXEvents();
        setSizeAndPosition();

        // Setup content container with a painted background based on theme.
        contentPanel = new BackgroundPanel(new MigLayout("insets 0, gap 0"));
        contentPanel.setOpaque(true);
        setContentPane(contentPanel);
        setF10KeyInputMap();
        setF11KeyInputMap();
        setF12KeyInputMap();

        // Enable drag and drop of background image file.
        new DropTarget(this, new ImageDropTargetListener(this));

        setVisible(true);
    }

    private void addWindowListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                onClose();
            }
            @Override
            public void windowDeactivated(final WindowEvent e) {
                if (isFullScreen() && e.getOppositeWindow() == null && !ignoreWindowDeactivate) {
                    setState(Frame.ICONIFIED);
                }
                ignoreWindowDeactivate = false;
            }
        });
    }

    public void showDuel() throws InvalidDeckException {
        if (duel!=null) {
            ScreenController.showDuelDecksScreen(duel);
            if (Boolean.getBoolean("selfMode")) {
                if (!duel.isFinished()) {
                    nextGame();
                } else {
                    newDuel(DuelConfig.getInstance());
                }
            }
        }
    }

    public void newDuel(final DuelConfig configuration) throws InvalidDeckException {
        duel = new MagicDuel(configuration);
        duel.initialize();
        showDuel();
    }

    public void loadDuel() throws InvalidDeckException {
        final File duelFile=MagicDuel.getDuelFile();
        if (duelFile.exists()) {
            duel=new MagicDuel();
            duel.load(duelFile);
            showDuel();
        } else {
            ScreenController.showWarningMessage("No saved duel found.");
        }
    }

    public void restartDuel() throws InvalidDeckException {
        if (duel!=null) {
            duel.restart();
            showDuel();
        }
    }

    public boolean isLegalDeckAndShowErrors(final MagicDeck deck, final String playerName) {
        final String brokenRulesText =
                MagicDeckConstructionRule.getRulesText(MagicDeckConstructionRule.checkDeck(deck));

        if (brokenRulesText.length() > 0) {
            ScreenController.showWarningMessage(playerName + "'s deck is illegal.\n\n" + brokenRulesText);
            return false;
        }

        return true;
    }

    public void nextGame() {
        ScreenController.showDuelGameScreen(duel);
    }

    /**
     * Set up our application to respond to the Mac OS X application menu
     */
    private void registerForMacOSXEvents() {
        if (MAC_OS_X) {
            try {
                // Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
                // use as delegates for various com.apple.eawt.ApplicationListener methods
                OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("onClose"));
                //OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod("about", (Class[])null));
                //OSXAdapter.setPreferencesHandler(this, getClass().getDeclaredMethod("preferences", (Class[])null));
                //OSXAdapter.setFileHandler(this, getClass().getDeclaredMethod("loadImageFile", new Class[] { String.class }));
            } catch (Exception e) {
                System.err.println("Error while loading the OSXAdapter:");
                e.printStackTrace();
            }
        }
    }

    public boolean onClose() {
        if (!confirmQuitToDesktop) {
            doShutdownMagarena();
        } else {
            final String message = "Are you sure you want to quit Magarena?\n";
            final Object[] params = {message};
            final int n = JOptionPane.showConfirmDialog(
                    contentPanel,
                    params,
                    "Confirm Quit to Desktop",
                    JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                doShutdownMagarena();
            }
        }
        // set the ApplicationEvent as handled (for OS X)
        return false;
    }

    private void doShutdownMagarena() {
        if (isFullScreen()) {
            config.setFullScreen(true);
        } else {
            final boolean maximized = (MagicFrame.this.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
            if (maximized) {
                config.setMaximized(true);
            } else {
                config.setLeft(getX());
                config.setTop(getY());
                config.setWidth(getWidth());
                config.setHeight(getHeight());
                config.setMaximized(false);
            }
        }

        MagicGameLog.close();

        /*
        if (gamePanel != null) {
            gamePanel.getController().haltGame();
        }
        */
        System.exit(0);
    }

    public void quitToDesktop(final boolean confirmQuit) {
        this.confirmQuitToDesktop = confirmQuit;
        processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void setSizeAndPosition() {
        setMinimumSize(MIN_SIZE);
        if (config.isFullScreen()) {
            setFullScreenMode(true);
        } else {
            this.setSize(config.getWidth(),config.getHeight());
            if (config.getLeft() != -1) {
                this.setLocation(config.getLeft(),config.getTop());
            } else {
                this.setLocationRelativeTo(null);
            }
            if (config.isMaximized()) {
                this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        }
    }

    /**
     *
     */
    public void closeDuelScreen() throws InvalidDeckException {
        ScreenController.closeActiveScreen(false);
        showDuel();
    }

    public void toggleFullScreenMode() {
        setFullScreenMode(!config.isFullScreen());
    }

    private void setFullScreenMode(final boolean isFullScreen) {
        this.dispose();
        if (isFullScreen) {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setUndecorated(true);
            final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            this.setSize(screenSize.width, screenSize.height);
            config.setFullScreen(true);
        } else {
            this.setExtendedState(JFrame.NORMAL);
            this.setUndecorated(false);
            config.setFullScreen(false);
            setSizeAndPosition();
        }
        setVisible(true);
        ignoreWindowDeactivate = true;
        config.save();
    }

    private boolean isFullScreen() {
        return isMaximized() && this.isUndecorated();
    }

    private boolean isMaximized() {
        return this.getExtendedState() == JFrame.MAXIMIZED_BOTH;
    }

    /**
     * F10 take a screen shot.
     */
    private void setF10KeyInputMap() {
        contentPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0), "Screenshot");
        contentPanel.getActionMap().put("Screenshot", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                GraphicsUtilities.setBusyMouseCursor(true);
                doScreenshot();
                GraphicsUtilities.setBusyMouseCursor(false);
            }
        });
    }

    /**
     * F11 key toggles full screen mode.
     */
    private void setF11KeyInputMap() {
        contentPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "FullScreen");
        contentPanel.getActionMap().put("FullScreen", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                toggleFullScreenMode();
            }
        });
    }

    private void setF12KeyInputMap() {
        contentPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0), "HideMenu");
        contentPanel.getActionMap().put("HideMenu", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final AbstractScreen activeScreen = ScreenController.getActiveScreen();
                activeScreen.setVisible(!activeScreen.isVisible());
            }
        });
    }

    private void doScreenshot() {
        try {
            final Path filePath = MagicFileSystem.getDataPath(DataPath.LOGS).resolve("screenshot.png");
            final File imageFile = GraphicsUtilities.doScreenshotToFile(this.getContentPane(), filePath);
            DesktopUtils.openFileInDefaultOsEditor(imageFile);
        } catch (IOException | DesktopNotSupportedException e) {
            e.printStackTrace();
            ScreenController.showWarningMessage(e.toString());
        }
    }

    @Override
    public void setDroppedImageFile(File imageFile) {
        final Path path = MagicFileSystem.getDataPath(DataPath.MODS).resolve("background.image");
        try {
            FileUtils.copyFile(imageFile, path.toFile());
        } catch (IOException ex) {
            ScreenController.showWarningMessage("Invalid action!\n\n" + ex.getMessage());
        }
        refreshBackground();
        config.setCustomBackground(true);
        config.save();
    }

    private void refreshBackground() {
        ((BackgroundPanel)contentPanel).refreshBackground();
    }


    public void refreshLookAndFeel() {
        ScreenController.refreshStyle();
        refreshBackground();
    }

    public void refreshUI() {
        config.setIsMissingFiles(false);
        CardDefinitions.checkForMissingFiles();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ThemeFactory.getInstance().loadThemes();
                refreshLookAndFeel();
            }
        });
    }

}

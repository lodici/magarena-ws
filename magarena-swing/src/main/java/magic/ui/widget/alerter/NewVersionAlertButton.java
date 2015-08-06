package magic.ui.widget.alerter;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import magic.data.GeneralConfig;
import magic.ui.URLUtils;
import magic.data.json.NewVersionJsonParser;
import magic.ui.ScreenController;
import magic.translate.UiString;

@SuppressWarnings("serial")
public class NewVersionAlertButton extends AlertButton {

    // translatable strings
    private static final String _S1 = "Version %s has been released.";
    private static final String _S2 = "Open download page";
    private static final String _S3 = "Don't remind me again";
    private static final String _S4 = "Cancel";
    private static final String _S5 = "New version alert";
    private static final String _S6 = "New version released (%s)";

    private String newVersion = "";
    private static boolean hasChecked = false;

    @Override
    protected AbstractAction getAlertAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                String[] buttons = {UiString.get(_S2), UiString.get(_S3), UiString.get(_S4)};
                int rc = JOptionPane.showOptionDialog(
                        ScreenController.getMainFrame(),
                        UiString.get(_S1, newVersion),
                        UiString.get(_S5),
                        0,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        buttons, buttons[0]);
                if (rc == 0) {
                    URLUtils.openURL(URLUtils.URL_HOMEPAGE);
                    // don't display alert again until next restart.
                    newVersion = "";
                } else if (rc == 1) {
                    // suppress alert for this release.
                    final GeneralConfig config = GeneralConfig.getInstance();
                    config.setIgnoredVersionAlert(newVersion);
                    config.save();
                    setVisible(false);
                } else {
                    setVisible(true);
                }
            }
        };
    }

    @Override
    protected String getAlertCaption() {

        assert !SwingUtilities.isEventDispatchThread();

        // Only download json once at startup.
        if (!hasChecked) {
            newVersion = NewVersionJsonParser.getLatestVersion();
            hasChecked = true;
        }
        if (isNewVersionAvailable()) {
            return UiString.get(_S6, newVersion);
        } else {
            return "";
        }
    }


    private boolean isNewVersionAvailable() {
        final String ignoredVersion = GeneralConfig.getInstance().getIgnoredVersionAlert();
        return !newVersion.isEmpty() && !ignoredVersion.equals(newVersion);
    }

}

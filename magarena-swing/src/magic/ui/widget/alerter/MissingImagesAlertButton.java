package magic.ui.widget.alerter;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.ui.ScreenController;
import magic.ui.dialog.DownloadImagesDialog;

@SuppressWarnings("serial")
public class MissingImagesAlertButton extends AlertButton {
    
    private static DownloadImagesDialog downloadDialog;
    private static boolean isSoundEffectPlayed = false;
    private static boolean hasChecked = false;
    private boolean isMissingImages = false;

    @Override
    protected AbstractAction getAlertAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                if (downloadDialog == null || !downloadDialog.isDisplayable()) {
                    downloadDialog = new DownloadImagesDialog(ScreenController.getMainFrame());
                } else {
                    downloadDialog.setVisible(true);
                }
                hasChecked = false;
                doAlertCheck();
            }
        };
    }

    @Override
    protected void playNewAlertSoundEffect() {
        if (!isSoundEffectPlayed) {
            super.playNewAlertSoundEffect();
            isSoundEffectPlayed = true;
        }
    }

    @Override
    protected String getAlertCaption() {
        
        assert !SwingUtilities.isEventDispatchThread();

        if (!hasChecked || isVisible()) {
            isMissingImages = CardDefinitions.isMissingImages();
            GeneralConfig.getInstance().setIsMissingFiles(isMissingImages);
            hasChecked = true;
        }
        if (isMissingImages) {
            return "Download missing card images";
        } else {
            return "";
        }

    }

}

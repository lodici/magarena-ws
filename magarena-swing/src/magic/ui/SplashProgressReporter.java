package magic.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import magic.utility.ProgressReporter;
import magic.data.GeneralConfig;

public class SplashProgressReporter extends ProgressReporter {

    private final SplashScreen splash;

    // CTR
    public SplashProgressReporter(final SplashScreen splash) {
        this.splash = splash;
    }

    @Override
    public void setMessage(String message) {
        setSplashStatusMessage(message);
    }

    private void setSplashStatusMessage(final String message) {
        try {
            final Graphics2D g2d = splash.createGraphics();
            // clear what we don't need from previous state
            g2d.setComposite(AlphaComposite.Clear);
            g2d.fillRect(0, 0, splash.getSize().width, splash.getSize().height);
            // draw new state
            g2d.setPaintMode();
            g2d.setColor(MagicStyle.getTranslucentColor(Color.BLACK, 100));
            g2d.fillRect(0, 0, splash.getSize().width, 22);
            g2d.fillRect(0, splash.getSize().height - 22, splash.getSize().width, 22);
            // draw message
            g2d.setColor(Color.WHITE);
            final Font f = new Font("Monospaced", Font.PLAIN, 16);
            g2d.setFont(f);
            // version
            final String version = "Version " + GeneralConfig.VERSION;
            int w = g2d.getFontMetrics(f).stringWidth(version);
            int x = (splash.getSize().width / 2) - (w / 2);
            g2d.drawString(version, x, 15);
            // status
            w = g2d.getFontMetrics(f).stringWidth(message);
            x = (splash.getSize().width / 2) - (w / 2);
            g2d.drawString(message, x, 274);
            splash.update();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}

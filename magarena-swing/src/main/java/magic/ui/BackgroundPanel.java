package magic.ui;

import magic.ui.utility.MagicStyle;
import magic.ui.utility.GraphicsUtils;
import magic.data.GeneralConfig;
import magic.ui.theme.Theme;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class BackgroundPanel extends JPanel {

    private Theme activeTheme;
    private BufferedImage image;
    private boolean stretchTexture;

    public BackgroundPanel(final LayoutManager layout) {
        super(layout);
        setBackgroundImage();
    }

    @Override
    protected void paintComponent(final Graphics g) {
        setBackgroundImage();
        final Dimension size = getSize();
        final Rectangle rect = new Rectangle(0, 0, size.width, size.height);
        if (stretchTexture) {
            paintZoneStretch(g, image, rect);
        } else {
            paintZoneTile(g, image, rect);
        }
    }

    public void refreshBackground() {
        activeTheme = null;
        repaint();
    }

    private void setBackgroundImage() {
        if (activeTheme != MagicStyle.getTheme()) {
            activeTheme = MagicStyle.getTheme();
            image = getBackgroundImage();
            stretchTexture =
                    activeTheme.getValue(Theme.VALUE_BACKGROUND_STRETCH) == 1 ||
                    GeneralConfig.getInstance().isCustomBackground();
        }
    }

    private BufferedImage getBackgroundImage() {
        if (GeneralConfig.getInstance().isCustomBackground()) {
            return GraphicsUtils.getCustomBackgroundImage();
        } else {
            return activeTheme.getTexture(Theme.TEXTURE_BACKGROUND);
        }
    }

    private void paintZoneStretch(final Graphics g,final BufferedImage aImage,final Rectangle rect) {
        final int iw=aImage.getWidth();
        final int ih=aImage.getHeight();
        final int iw2=ih*rect.width/rect.height;
        final Rectangle imageRect;
        if (iw2<=iw) {
            imageRect=new Rectangle((iw-iw2)/2,0,iw2,ih);
        } else {
            final int ih2=iw*rect.height/rect.width;
            imageRect=new Rectangle(0,(ih-ih2)/2,iw,ih2);
        }
        g.drawImage(aImage,rect.x,rect.y,rect.x+rect.width,rect.y+rect.height,
                imageRect.x,imageRect.y,imageRect.x+imageRect.width,imageRect.y+imageRect.height,this);
    }

    private void paintZoneTile(final Graphics g,final BufferedImage aImage,final Rectangle rect) {
        final int imageWidth=aImage.getWidth();
        final int imageHeight=aImage.getHeight();
        final int x2=rect.x+rect.width;
        final int y2=rect.y+rect.height;
        for (int y=rect.y;y<y2;y+=imageHeight) {
            for (int x=rect.x;x<x2;x+=imageWidth) {
                g.drawImage(aImage,x,y,this);
            }
        }
    }

}

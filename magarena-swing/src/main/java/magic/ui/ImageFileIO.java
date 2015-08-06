package magic.ui;

import magic.ui.utility.GraphicsUtils;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public final class ImageFileIO {
    private ImageFileIO() { }

    private static BufferedImage loadImage(final File input) {
        try {
            return javax.imageio.ImageIO.read(input);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to read from " + input);
            return null;
        } catch (final IllegalArgumentException ex) {
            System.err.println("ERROR! Unable to read from null");
            return null;
        }
    }

    public static BufferedImage toImg(final File input, final BufferedImage def) {
        final BufferedImage img = loadImage(input);
        if (img == null) {
            // no registered ImageReader able to read the file, likely file is corrupted
            input.delete();
            return def;
        } else {
            final BufferedImage optimizedImage =
                    GraphicsUtils.getCompatibleBufferedImage(img.getWidth(), img.getHeight(), img.getTransparency());
            optimizedImage.getGraphics().drawImage(img, 0, 0 , null);
            return optimizedImage;
        }
    }

    public static BufferedImage toImg(final URL input, final BufferedImage def) {
        BufferedImage img = def;
        try {
            img = javax.imageio.ImageIO.read(input);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to read from " + input);
        } catch (final IllegalArgumentException ex) {
            System.err.println("ERROR! Unable to read from null");
        }
        return img;
    }

    public static BufferedImage toImg(final InputStream input, final BufferedImage def) {
        BufferedImage img = def;
        try {
            img = javax.imageio.ImageIO.read(input);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to read from input stream");
        } catch (final IllegalArgumentException ex) {
            System.err.println("ERROR! Unable to read from null");
        } finally {
            close(input);
        }
        return img;
    }

    private static void close(final Closeable resource) {
        if (resource == null) {
            return;
        }
        boolean closed = false;
        while (!closed) {
            try {
                resource.close();
                closed = true;
            } catch (final Exception ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

}

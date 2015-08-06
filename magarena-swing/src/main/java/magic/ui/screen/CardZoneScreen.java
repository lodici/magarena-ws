package magic.ui.screen;

import magic.model.MagicCardList;
import magic.ui.canvas.cards.CardsCanvas;
import magic.ui.canvas.cards.CardsCanvas.LayoutMode;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.MenuButton;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.util.Collections;
import java.util.List;
import magic.ui.CardImagesProvider;
import magic.translate.UiString;

@SuppressWarnings("serial")
public class CardZoneScreen
        extends AbstractScreen
        implements IStatusBar, IActionBar {

    // translatable strings
    private static final String _S1 = "Close";

    private final static Dimension cardSize = CardImagesProvider.PREFERRED_CARD_SIZE;

    private final CardsCanvas content;
    private final String screenCaption;

    public CardZoneScreen(final MagicCardList cards, final String zoneName, final boolean animateCards) {
        this.screenCaption = zoneName;
        this.content = new CardsCanvas(cardSize);
        this.content.setAnimationEnabled(animateCards);
        this.content.setLayoutMode(LayoutMode.SCALE_TO_FIT);
        Collections.sort(cards);
        content.refresh(cards, cardSize);
        setContent(content);
    }

    @Override
    public String getScreenCaption() {
        return screenCaption;
    }

    @Override
    public MenuButton getLeftAction() {
        return MenuButton.getCloseScreenButton(UiString.get(_S1));
    }

    @Override
    public MenuButton getRightAction() {
        return null;
    }

    @Override
    public List<MenuButton> getMiddleActions() {
        return null;
    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

    @Override
    public JPanel getStatusPanel() {
        return null;
    }

}

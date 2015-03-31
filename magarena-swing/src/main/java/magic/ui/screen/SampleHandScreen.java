package magic.ui.screen;

import magic.ui.IconImages;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.model.MagicDeck;
import magic.model.MagicRandom;
import magic.ui.canvas.cards.CardsCanvas;
import magic.ui.canvas.cards.CardsCanvas.LayoutMode;
import magic.ui.screen.interfaces.IActionBar;
import magic.ui.screen.interfaces.IStatusBar;
import magic.ui.screen.widget.ActionBarButton;
import magic.ui.screen.widget.MenuButton;
import magic.ui.widget.deck.DeckStatusPanel;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import magic.data.MagicIcon;
import magic.ui.CardImagesProvider;

@SuppressWarnings("serial")
public class SampleHandScreen
    extends AbstractScreen
    implements IStatusBar, IActionBar {

    private final static Dimension cardSize = CardImagesProvider.PREFERRED_CARD_SIZE;

    private final CardsCanvas content;
    private final MagicDeck deck;
    private final DeckStatusPanel deckStatusPanel = new DeckStatusPanel();

    public SampleHandScreen(final MagicDeck deck) {
        this.deck = deck;
        this.content = new CardsCanvas(cardSize);
        content.setAnimationDelay(50, 20);
        this.content.setLayoutMode(LayoutMode.SCALE_TO_FIT);
        this.content.refresh(getRandomHand(deck), cardSize);
        setContent(this.content);
    }

    private List<MagicCard> getRandomHand(final MagicDeck deck) {
        final MagicCardList library = new MagicCardList();
        for (MagicCardDefinition magicCardDef : deck) {
            library.add(new MagicCard(magicCardDef, null, 0));
        }
        library.shuffle(MagicRandom.nextRNGInt());
        if (library.size() >= 7) {
            final List<MagicCard> hand = library.subList(0, 7);
            Collections.sort(hand);
            return hand;
        } else {
            return null;
        }
    }

    @Override
    public String getScreenCaption() {
        return "Sample Hand";
    }

    @Override
    public MenuButton getLeftAction() {
        return MenuButton.getCloseScreenButton("Close");
    }

    @Override
    public MenuButton getRightAction() {
        return null;
    }

    @Override
    public List<MenuButton> getMiddleActions() {
        final List<MenuButton> buttons = new ArrayList<>();
        buttons.add(
                new ActionBarButton(
                        IconImages.getIcon(MagicIcon.REFRESH_ICON),
                        "Refresh", "Deal a new sample hand.",
                        new AbstractAction() {
                            @Override
                            public void actionPerformed(final ActionEvent e) {
                                if (!content.isBusy()) {
                                    content.refresh(getRandomHand(deck), cardSize);
                                }
                            }
                        })
                );
        return buttons;
    }

    @Override
    public boolean isScreenReadyToClose(final AbstractScreen nextScreen) {
        return true;
    }

    @Override
    public JPanel getStatusPanel() {
        deckStatusPanel.setDeck(deck, false);
        return deckStatusPanel;
    }

}

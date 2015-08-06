package magic.ui.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.cardtable.CardTablePanel;
import magic.ui.deck.editor.DeckEditorSideBarPanel;
import magic.ui.utility.GraphicsUtils;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckViewPanel extends JPanel {

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private MagicDeck deck;
    private final MigLayout migLayout = new MigLayout();
    private final DeckEditorSideBarPanel sideBarPanel;
    private final CardTablePanel deckTable;

    public DeckViewPanel(final MagicDeck aDeck, final MagicCardDefinition selectedCard) {

        this.deck = aDeck;

        sideBarPanel = new DeckEditorSideBarPanel();
        sideBarPanel.getStatsViewer().setDeck(deck);

        deckTable = new CardTablePanel(this.deck, "  " + this.deck.getName());
        deckTable.setDeckEditorSelectionMode();
        deckTable.setHeaderVisible(false);
        deckTable.showCardCount(true);
        deckTable.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.DARK_GRAY));
        setDeckTablePropChangeListeners();

        setLookAndFeel();
        refreshLayout();

        if (selectedCard != null) {
            deckTable.setSelectedCard(selectedCard);
        } else {
            deckTable.setSelectedCard(null);
        }

    }

    public DeckViewPanel(final MagicDeck aDeck) {
        this(aDeck, null);
    }

    private void setDeckTablePropChangeListeners() {
        deckTable.addPropertyChangeListener(
                CardTablePanel.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        setCard(getSelectedCard());
                    }
                });
    }

    private MagicCardDefinition getSelectedCard() {
        if (deckTable.getSelectedCards().size() > 0) {
            return deckTable.getSelectedCards().get(0);
        } else {
            return MagicCardDefinition.UNKNOWN;
        }
    }

    private void setCard(final MagicCardDefinition card) {
        final int cardCount = deck.getCardCount(card);
        sideBarPanel.setCard(card);
        sideBarPanel.setCardCount(cardCount);
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(migLayout);
    }

    private void refreshLayout() {
        final Dimension imageSize = GraphicsUtils.getMaxCardImageSize();
        removeAll();
        migLayout.setLayoutConstraints("insets 0, gap 0");
        if (CONFIG.isHighQuality()) {
            migLayout.setColumnConstraints("[][grow]");
            add(sideBarPanel, "h 100%, w 0:" + imageSize.width + ":" + imageSize.width);
            add(deckTable, "h 100%, growx");
        } else {
            migLayout.setColumnConstraints("[" + imageSize.width + "!][100%]");
            add(sideBarPanel, "h 100%");
            add(deckTable, "w 100%, h 100%");
        }
    }

    public MagicDeck getDeck() {
        return this.deck;
    }

    void setDeck(MagicDeck aDeck) {
        this.deck = aDeck;
    }

}

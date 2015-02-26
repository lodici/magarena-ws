package magic.ui.explorer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import magic.data.DeckGenerator;
import magic.data.MagicIcon;
import magic.ui.IconImages;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.ui.ScreenController;
import magic.ui.dialog.RandomDeckGeneratorDialog;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckEditorTabbedPanel extends JPanel {

    // fired when contents of deck list are updated.
    public static final String CP_DECKLIST= DeckTabPanel.CP_DECKLIST;
    // fired when card selection changes
    public static final String CP_CARD_SELECTED = CardPoolTabPanel.CP_CARD_SELECTED;
    // fired when deck is cleared.
    public static final String CP_DECK_CLEARED = DeckTabPanel.CP_DECK_CLEARED;

    private final MigLayout miglayout = new MigLayout();
    //
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final CardPoolTabPanel cardPoolTabPanel;
    private final DeckTabPanel deckTabPanel;
    private final HistoryTabPanel historyTabPanel;

    DeckEditorTabbedPanel(final MagicDeck deck) {
        deckTabPanel = new DeckTabPanel(deck);
        cardPoolTabPanel = new CardPoolTabPanel();
        historyTabPanel = new HistoryTabPanel();
        setLookAndFeel();
        refreshLayout();
        updateDeckTabCaption();
        updateCardPoolTabCaption();
        addPropertyChangeListeners();
        setTabbedPanelToFocus();
    }

    private void addPropertyChangeListeners() {
        // CardPoolTabPanel
        cardPoolTabPanel.addPropertyChangeListener(
                CardPoolTabPanel.CP_RANDOM_DECK,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        generateRandomDeck();
                    }
                });
        cardPoolTabPanel.addPropertyChangeListener(
                CardPoolTabPanel.CP_REMOVE_FROM_DECK,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        doRemoveCardFromDeck(cardPoolTabPanel.getSelectedCard());
                    }
                });
        cardPoolTabPanel.addPropertyChangeListener(
                CardPoolTabPanel.CP_ADD_TO_DECK,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        doAddCardToDeck(cardPoolTabPanel.getSelectedCard());
                    }
                });
        cardPoolTabPanel.addPropertyChangeListener(
                CardPoolTabPanel.CP_CARDPOOL,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        updateCardPoolTabCaption();
                    }
                });
        cardPoolTabPanel.addPropertyChangeListener(
                CardPoolTabPanel.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_CARD_SELECTED, false, true);
                    }
                });
        // DeckTabPanel
        deckTabPanel.addPropertyChangeListener(
                DeckTabPanel.CP_DECK_CLEARED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_DECK_CLEARED, false, true);
                    }
                });
        deckTabPanel.addPropertyChangeListener(
                DeckTabPanel.CP_REMOVE_FROM_DECK,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        doRemoveCardFromDeck(deckTabPanel.getSelectedCard());
                    }
                });
        deckTabPanel.addPropertyChangeListener(
                DeckTabPanel.CP_ADD_TO_DECK,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        doAddCardToDeck(deckTabPanel.getSelectedCard());
                    }
                });
        deckTabPanel.addPropertyChangeListener(
                DeckTabPanel.CP_DECKLIST,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        updateDeckTabCaption();
                        firePropertyChange(CP_DECKLIST, false, true);
                    }
                });
        deckTabPanel.addPropertyChangeListener(
                DeckTabPanel.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_CARD_SELECTED, false, true);
                    }
                });
        // HistoryTabPanel
        historyTabPanel.addPropertyChangeListener(
                HistoryTabPanel.CP_REMOVE_FROM_DECK,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        doRemoveCardFromDeck(historyTabPanel.getSelectedCard());
                    }
                });
        historyTabPanel.addPropertyChangeListener(
                HistoryTabPanel.CP_ADD_TO_DECK,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        doAddCardToDeck(historyTabPanel.getSelectedCard());
                    }
                });
        historyTabPanel.addPropertyChangeListener(
                HistoryTabPanel.CP_CARD_SELECTED,
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        firePropertyChange(CP_CARD_SELECTED, false, true);
                    }
                });
    }

    private boolean generateRandomDeck() {
        final RandomDeckGeneratorDialog dialog = new RandomDeckGeneratorDialog(ScreenController.getMainFrame(), cardPoolTabPanel.getCardPoolSize());
        dialog.setVisible(true);
        if (!dialog.isCancelled()) {
            final DeckGenerator deckGenerator = dialog.getDeckGenerator();
            setDeck(deckGenerator.getRandomDeck(cardPoolTabPanel.getCardPool()));
            return true;
        } else {
            return false;
        }
    }

    private void doAddCardToDeck(final MagicCardDefinition card) {
        if (card != null && card != MagicCardDefinition.UNKNOWN) {
            deckTabPanel.addCardToDeck(card);
            historyTabPanel.addCardToHistory(card);
            firePropertyChange(CP_CARD_SELECTED, false, true);
        }
    }

    private void doRemoveCardFromDeck(final MagicCardDefinition card) {
        if (card != null && card != MagicCardDefinition.UNKNOWN) {
            deckTabPanel.removeCardFromDeck(card);
            historyTabPanel.addCardToHistory(card);
            firePropertyChange(CP_CARD_SELECTED, false, true);
        }
    }
    
    private void refreshLayout() {
        removeAll();
        add(tabbedPane, "w 100%, h 100%");
    }

    private void setLookAndFeel() {
        setOpaque(false);
        setLayout(miglayout);
        miglayout.setLayoutConstraints("insets 0, gap 0");
        //
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));
        tabbedPane.add(deckTabPanel);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, getTabLabel("Deck : 0 cards"));
        tabbedPane.add(cardPoolTabPanel);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, getTabLabel("Card Pool"));
        tabbedPane.add(historyTabPanel);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, getTabLabel("Card Recall"));
        tabbedPane.addChangeListener(new TabbedPaneChangeListener());
        setSelectedTabStyle();
    }

    private JLabel getTabLabel(final String text) {
        final JLabel lbl = new JLabel(text);
        lbl.setPreferredSize(new Dimension(180, lbl.getFont().getSize()+4));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setVerticalAlignment(SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        return lbl;
    }
    
    private void setSelectedTabStyle() {
        final int index = tabbedPane.getSelectedIndex();
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            final JLabel tabLabel = (JLabel) tabbedPane.getTabComponentAt(i);
            if (i == index) {
                tabLabel.setFont(tabLabel.getFont().deriveFont(Font.BOLD));
                tabLabel.setIcon(IconImages.getIcon(MagicIcon.CHARGE));
            } else {
                tabLabel.setFont(tabLabel.getFont().deriveFont(Font.PLAIN));
                tabLabel.setIcon(null);
            }
        }
    }

    private void setTabbedPanelToFocus() {
        final int index = tabbedPane.getSelectedIndex();
        final Component component = tabbedPane.getComponentAt(index);
        component.requestFocusInWindow();
    }

    public MagicCardDefinition getSelectedCard() {
        if (tabbedPane.getSelectedIndex() == 0) {
            return deckTabPanel.getSelectedCard();
        } else if (tabbedPane.getSelectedIndex() == 1) {
            return cardPoolTabPanel.getSelectedCard();
        } else {
            return historyTabPanel.getSelectedCard();
        }
    }

    public void setDeck(final MagicDeck originalDeck) {
        deckTabPanel.setDeck(originalDeck);
    }

    MagicDeck getDeck() {
        return deckTabPanel.getDeck();
    }

    void updateOriginalDeck() {
        deckTabPanel.updateOriginalDeck();
    }

    boolean isUpdatingExistingDeck() {
        return deckTabPanel.isUpdatingExistingDeck();
    }

    private class TabbedPaneChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setSelectedTabStyle();
                    setTabbedPanelToFocus();
                    firePropertyChange(CP_CARD_SELECTED, false, true);
                }
            });
        }
    }

    private void updateDeckTabCaption() {
        ((JLabel)tabbedPane.getTabComponentAt(0)).setText("Deck : " + deckTabPanel.getDeck().size() + " cards");
    }

    private void updateCardPoolTabCaption() {
        ((JLabel)tabbedPane.getTabComponentAt(1)).setText("Card Pool : " + NumberFormat.getInstance().format(cardPoolTabPanel.getCardPoolSize()));
    }

}

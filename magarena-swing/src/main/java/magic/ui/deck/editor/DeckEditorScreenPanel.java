package magic.ui.deck.editor;

import java.awt.Dimension;
import javax.swing.JPanel;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.model.MagicDeckConstructionRule;
import magic.ui.ScreenController;
import magic.translate.UiString;
import magic.ui.utility.GraphicsUtils;
import magic.utility.MagicSystem;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class DeckEditorScreenPanel extends JPanel implements IDeckEditorListener {

    // translatable strings
    private static final String _S1 = "This deck is illegal.\n\n%s";

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    private final MigLayout migLayout = new MigLayout();
    private final DeckEditorSideBarPanel sideBarPanel; // LHS
    private final MainViewsPanel viewsPanel; // RHS
    private final IDeckEditorListener listener;
    private boolean isStandalone = true;

    public DeckEditorScreenPanel(final MagicDeck deck, final IDeckEditorListener aListener) {
        
        MagicSystem.waitForAllCards();

        this.listener = aListener;

        // lhs
        sideBarPanel = new DeckEditorSideBarPanel();
        // rhs
        viewsPanel = new MainViewsPanel(deck, this);
        setSelectedCard();
        //
        setLookAndFeel();
        refreshLayout();
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
            add(viewsPanel, "h 100%, growx");
        } else {
            migLayout.setColumnConstraints("[" + imageSize.width + "!][100%]");
            add(sideBarPanel, "h 100%");
            add(viewsPanel, "w 100%, h 100%");
        }
    }

    public void setCard(final MagicCardDefinition card) {
        final int cardCount = viewsPanel.getDeck().getCardCount(card);
        sideBarPanel.setCard(card);
        sideBarPanel.setCardCount(cardCount);
    }

    private void setSelectedCard() {
        setCard(viewsPanel.getSelectedCard());
        sideBarPanel.getStatsViewer().setDeck(viewsPanel.getDeck());
    }

    public void setDeck(final MagicDeck deck) {
        viewsPanel.setDeck(deck);
    }

    public MagicDeck getDeck() {
        return viewsPanel.getDeck();
    }

    public boolean applyDeckUpdates() {
        boolean updatesApplied = true;
        if (isUpdatingExistingDeck()) {
            if (validateDeck(false)) {
                viewsPanel.updateOriginalDeck();
            } else {
                updatesApplied = false;
            }
        }
        return updatesApplied;
    }

    private String getBrokenRules(final MagicDeck deck) {
        return MagicDeckConstructionRule.getRulesText(MagicDeckConstructionRule.checkDeck(deck));
    }

    private void notifyUser(final String brokenRules) {
        ScreenController.showWarningMessage(UiString.get(_S1, brokenRules));
    }

    public boolean validateDeck(final boolean notifyUser) {
        final String brokenRules = getBrokenRules(viewsPanel.getDeck());
        if (brokenRules.length() > 0) {
            if (notifyUser) {
                notifyUser(brokenRules);
            }
            return false;
        }
        return true;
    }

    public boolean isStandaloneDeckEditor() {
        return !isUpdatingExistingDeck();
    }

    private boolean isUpdatingExistingDeck() {
        return viewsPanel.isUpdatingExistingDeck();
    }

    public void setIsStandalone(final boolean b) {
        this.isStandalone = b;
    }

    @Override
    public void deckUpdated(MagicDeck deck) {
        if (deck.isEmpty() && isStandalone) {
            CONFIG.setMostRecentDeckFilename("");
            CONFIG.save();
        }
        listener.deckUpdated(deck);
    }

    @Override
    public void cardSelected(MagicCardDefinition card) {
        setCard(card);
        sideBarPanel.getStatsViewer().setDeck(getDeck());
    }

    @Override
    public void addCardToRecall(MagicCardDefinition card) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

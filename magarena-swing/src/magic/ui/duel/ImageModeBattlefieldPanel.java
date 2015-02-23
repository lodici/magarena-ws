package magic.ui.duel;

import magic.ui.duel.animation.PlayCardAnimation;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.ui.SwingGameController;
import magic.ui.duel.resolution.ResolutionProfileResult;
import magic.ui.duel.resolution.ResolutionProfileType;
import magic.ui.duel.viewer.ImageBattlefieldViewer;
import magic.ui.duel.viewer.ImageCardListViewer;
import magic.ui.duel.viewer.ImageCombatViewer;
import magic.ui.duel.viewer.ImageHandGraveyardExileViewer;
import magic.ui.duel.viewer.StackViewer;

@SuppressWarnings("serial")
public class ImageModeBattlefieldPanel extends BattlefieldPanel {

    private PlayCardAnimation animationEvent = null;

    private final ImageHandGraveyardExileViewer imageHandGraveyardViewer;
    private final ImageBattlefieldViewer imagePlayerPermanentViewer;
    private final ImageBattlefieldViewer imageOpponentPermanentViewer;
    private final ImageCombatViewer imageCombatViewer;
    private final StackViewer imageStackViewer;

    public ImageModeBattlefieldPanel(final SwingGameController controller) {
        setOpaque(false);
        //
        imageHandGraveyardViewer = new ImageHandGraveyardExileViewer(controller);
        imagePlayerPermanentViewer = new ImageBattlefieldViewer(controller, false);
        imageOpponentPermanentViewer = new ImageBattlefieldViewer(controller, true);
        imageCombatViewer = new ImageCombatViewer(controller);
        imageStackViewer = new StackViewer(controller, true);
        //
        setLayout(null);
        add(imageStackViewer);
        add(imageHandGraveyardViewer);
        add(imagePlayerPermanentViewer);
        add(imageOpponentPermanentViewer);
        add(imageCombatViewer);
    }

    @Override
    public void doUpdate() {
        imageHandGraveyardViewer.update();
        imagePlayerPermanentViewer.update();
        imageOpponentPermanentViewer.update();
        imageCombatViewer.update();
        imageStackViewer.update();
    }

    @Override
    public void showCards(final MagicCardList cards) {
        imageHandGraveyardViewer.showCards(cards);
        imageHandGraveyardViewer.setSelectedTab(5);
    }

    @Override
    public void focusViewers(int handGraveyard) {
        imageHandGraveyardViewer.setSelectedTab(handGraveyard);
    }

    @Override
    public void resizeComponents(ResolutionProfileResult result) {
        imageHandGraveyardViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImageHandGraveyardViewer));
        imagePlayerPermanentViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImagePlayerPermanentViewer));
        imageOpponentPermanentViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImageOpponentPermanentViewer));
        imageCombatViewer.setBounds(result.getBoundary(ResolutionProfileType.GameImageCombatViewer));
    }

    /**
     * If player Hand is visible then start animation from position of the card
     * in the Hand otherwise start animation from the Hand icon next to player portrait.
     */
    private void setAnimationStartPoint(final MagicPlayer player, final MagicCardDefinition card) {
        if (isPlayerHandVisible(player)) {
            final ImageCardListViewer handViewer = imageHandGraveyardViewer.getCardListViewer();
            final Point startPoint = handViewer.getCardPosition(card);
            animationEvent.setStartSize(handViewer.getCardSize());
            animationEvent.setStartPoint(startPoint);
        }
    }

    /**
     * TODO: checking the player index to determine if the Hand is visible is
     * not really desirable but it works. The index is a bad code smell. It is
     * used to position the player on screen and determine whether a player
     * is human or AI (except for an AI v AI game). It seems very arbitrary.
     */
    private boolean isPlayerHandVisible(final MagicPlayer player) {
        return player.getIndex() == 0;
    }

    /**
     * Produces an animation of a card being played from a player's hand
     * to the battlefield or stack the next time GamePanel is refreshed.
     */
    @Override
    public void setAnimationEvent(final MagicEvent event, final DuelPanel gamePanel) {
        final MagicCardDefinition card = event.getSource().getCardDefinition();
        final MagicPlayer player = event.getPlayer();
        animationEvent = new PlayCardAnimation(player, card, gamePanel);
        setAnimationStartPoint(player, card);
        if (card.usesStack()) {
            animationEvent.setEndPoint(new Point(150, imageStackViewer.getLocation().y));
        } else {
            if (player.getIndex() == 0) {
                animationEvent.setEndPoint(getLocationOnDuelPanel(imagePlayerPermanentViewer));
            } else {
                animationEvent.setEndPoint(getLocationOnDuelPanel(imageOpponentPermanentViewer));
            }
        }
    }

    private Point getLocationOnDuelPanel(final JComponent component) {
        final DuelPanel duelPanel = (DuelPanel)component.getParent().getParent();
        return SwingUtilities.convertPoint(component.getParent(), component.getLocation(), duelPanel);
    }

    @Override
    public StackViewer getStackViewer() {
        return imageStackViewer;
    }

    @Override
    public PlayCardAnimation getPlayCardFromHandAnimation() {
        return animationEvent;
    }

    @Override
    public void setPlayCardFromHandAnimation(PlayCardAnimation event) {
        animationEvent = event;
    }

}


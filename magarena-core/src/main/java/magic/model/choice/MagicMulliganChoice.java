package magic.model.choice;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCostManaType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicType;
import magic.model.condition.MagicCondition;
import magic.model.MagicPayedCost;
import magic.model.action.PlayCardAction;
import magic.model.event.MagicEvent;
import magic.model.phase.MagicMainPhase;
import magic.exception.UndoClickedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import magic.model.IUIGameController;

public class MagicMulliganChoice extends MagicChoice {

    private static final List<Object[]> YES_CHOICE_LIST =
            Collections.singletonList(new Object[]{YES_CHOICE});
    private static final List<Object[]> NO_CHOICE_LIST =
            Collections.singletonList(new Object[]{NO_CHOICE});

    public MagicMulliganChoice() {
        super("");
    }

    @Override
    Collection<Object> getArtificialOptions(final MagicGame game, final MagicEvent event) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Object[]> getArtificialChoiceResults(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        int costSum = 0;
        for (final MagicCard card: player.getLibrary()){
            costSum += card.getConvertedCost();
        }
        for (final MagicCard card: player.getHand()){
            costSum += card.getConvertedCost();
            //System.err.println("MULLIGAN: card=" + card);
        }
        
        // There is more fine tuning to be done here
        int minLands = 2;
        int maxLands = 3;
        if (costSum > 90) {
            minLands = 3;
            maxLands = 4;
        } else if(costSum > 70) {
            minLands = 2;
            maxLands = 4;
        }

        final int hand = player.getHandSize();
        
        if (hand <= 4) {
            return NO_CHOICE_LIST;
        }

        final MagicGame assumedGame = new MagicGame(game, player);
        final MagicPlayer assumedPlayer = assumedGame.getPlayer(player.getIndex());
        assumedGame.setPhase(MagicMainPhase.getFirstInstance());

        int numLands = 0;
        for (final MagicCard card : assumedPlayer.getHand()) {
            if (card.hasType(MagicType.Land)) {
                numLands++;
                assumedGame.doAction(new PlayCardAction(card, assumedPlayer));
            }
        }

        int playable = 0;
        for (final MagicCard card : assumedPlayer.getHand()) {
            if (card.hasType(MagicType.Land) == false && 
                card.getCost().getCondition().accept(card)) {
                playable++;
            }
        }
          
        //System.err.println("MULLIGAN: hand=" + hand + " lands=" + numLands + " playable=" + playable);

        if ((hand >  6 && playable > 1) ||
            (hand <= 6 && playable > 0)) { 
            return NO_CHOICE_LIST;
        } else if (numLands < minLands || numLands > maxLands) {
            return YES_CHOICE_LIST;
        } else {
            return NO_CHOICE_LIST;
        }
    }

    @Override
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        if (player.getHandSize() <= 1) {
            return new Object[]{NO_CHOICE};
        }
        controller.disableActionButton(false);
        if (controller.getTakeMulliganChoice(source, player)) {
            return new Object[]{YES_CHOICE};
        }
        return new Object[]{NO_CHOICE};
    }

}

package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicCardList;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTriggerType;

public class MagicClashEvent extends MagicEvent {
    
    private static MagicEventAction clashAction;
    
    public MagicClashEvent(final MagicEvent event, final MagicEventAction aClashAction) {
        this(event.getSource(), event.getPlayer(), aClashAction);
    }

    public MagicClashEvent(final MagicSource source, final MagicPlayer player, final MagicEventAction aClashAction) {
        super(
            source,
            player,
            EventAction,
            "Clash with an opponent."
        );
        clashAction = aClashAction;
    }
    
    private static final MagicEventAction EventAction = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer winner = executeClash(game, event);
            if (winner == event.getPlayer()) {
                clashAction.executeEvent(game, event);
            };
            game.executeTrigger(MagicTriggerType.WhenClash, winner);
        }
    };
    
    public static MagicPlayer executeClash(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicPlayer opponent = player.getOpponent();
        final MagicCardList clashCards = player.getLibrary().getCardsFromTop(1);
        clashCards.addAll(opponent.getLibrary().getCardsFromTop(1));

        // 701.20c A player wins a clash if that player revealed a card with a
        // higher converted mana cost than all other cards revealed in that clash.
        MagicPlayer winner = MagicPlayer.NONE;
        int maxCMC = -1;
        for (final MagicCard card : clashCards) {
            if (card.getConvertedCost() > maxCMC) {
                maxCMC = card.getConvertedCost();
                winner = card.getOwner();
            } else if (card.getConvertedCost() == maxCMC) {
                winner = MagicPlayer.NONE;
            }
        }
        
        if (winner == MagicPlayer.NONE) {
            game.logAppendMessage(player, "It is a tie.");
        } else if (winner == player) {
            game.logAppendMessage(player, player + " won the clash.");
        } else {
            game.logAppendMessage(player, player + " lost the clash.");
        }
            
        game.addFirstEvent(new MagicScryEvent(event.getSource(), opponent));
        game.addFirstEvent(new MagicScryEvent(event.getSource(), player));
                
        return winner;
    }
}

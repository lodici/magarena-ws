package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicObject;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.stack.MagicCardOnStack;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPutCardOnStackEvent;

public class CastCardAction extends MagicAction {

    private final MagicPlayer player;
    private final MagicCard card;
    private final boolean withoutManaCost;
    private final MagicLocationType from;
    private final MagicLocationType to;

    public CastCardAction(final MagicPlayer aPlayer, final MagicCard aCard, final MagicLocationType aFrom, final MagicLocationType aTo) {
        this(aPlayer, aCard, false, aFrom, aTo);
    }

    public static CastCardAction WithoutManaCost(final MagicPlayer aPlayer, final MagicCard aCard, final MagicLocationType aFrom, final MagicLocationType aTo) {
        return new CastCardAction(aPlayer, aCard, true, aFrom, aTo);
    }

    private CastCardAction(final MagicPlayer aPlayer, final MagicCard aCard, final boolean aWithoutManaCost, final MagicLocationType aFrom, final MagicLocationType aTo) {
        player = aPlayer;
        card = aCard;
        withoutManaCost = aWithoutManaCost;
        from = aFrom;
        to = aTo;
    }
    
    @Override
    public void doAction(final MagicGame game) {
        for (final MagicEvent event : card.getAdditionalCostEvent()) {
            if (event.isSatisfied() == false) {
                game.logAppendMessage(player, "Casting failed as " + player + " is unable to pay additional casting costs.");
                return;
            }
        }
        for (final MagicEvent event : withoutManaCost ? card.getAdditionalCostEvent() : card.getCostEvent()) {
            game.addEvent(event);
        }
        game.addEvent(new MagicPutCardOnStackEvent(card, player, MagicLocationType.Exile, MagicLocationType.Graveyard));
    }

    @Override
    public void undoAction(final MagicGame game) {}

    @Override
    public String toString() {
        return getClass().getSimpleName()+" (" +player + ',' + card +')';
    }
}

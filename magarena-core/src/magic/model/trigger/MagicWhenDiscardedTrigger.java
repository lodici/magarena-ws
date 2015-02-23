package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicMoveCardAction;
import magic.model.event.MagicEvent;

public abstract class MagicWhenDiscardedTrigger extends MagicWhenOtherPutIntoGraveyardTrigger {
    public MagicWhenDiscardedTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenDiscardedTrigger() {}

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicMoveCardAction act) {
        return (act.fromLocation == MagicLocationType.OwnersHand) ?
            getEvent(permanent, act.card) : MagicEvent.NONE;
    }

    protected abstract MagicEvent getEvent(final MagicPermanent source, final MagicCard card);
}

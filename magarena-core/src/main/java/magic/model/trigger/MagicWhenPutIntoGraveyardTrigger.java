package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.event.MagicEvent;

public abstract class MagicWhenPutIntoGraveyardTrigger extends MagicTrigger<MagicMoveCardAction> {
    public MagicWhenPutIntoGraveyardTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenPutIntoGraveyardTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenPutIntoGraveyard;
    }
    
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicMoveCardAction act) {
        return act.getToLocation() == MagicLocationType.Graveyard;
    }
    
    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }
    
    public static final MagicWhenPutIntoGraveyardTrigger LibraryInsteadOfGraveyard = new MagicWhenPutIntoGraveyardTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction act) {
            act.setToLocation(MagicLocationType.OwnersLibrary);
            return MagicEvent.NONE;
        }
    };
    
    public static final MagicWhenPutIntoGraveyardTrigger OpponentDiscardOntoBattlefield = new MagicWhenPutIntoGraveyardTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction act) {
            final MagicCard card = act.card;
            if (card.isEnemy(game.getActiveSource()) && 
                act.from(MagicLocationType.OwnersHand) &&
                act.to(MagicLocationType.Graveyard)) {
                act.setToLocation(MagicLocationType.Play);
                game.doAction(new MagicPlayCardAction(card));
            }
            return MagicEvent.NONE;
        }
    };

    public static final MagicWhenPutIntoGraveyardTrigger RecoverGraveyard = new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction act) {
            final MagicPlayer owner = act.card.getOwner();
            return new MagicEvent(
                //source may be permanent if on battlefield or card (exile, hand)
                permanent.isValid() ? permanent : act.card,
                owner,
                this,
                "PN shuffles his or her graveyard into his or her library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicCardList graveyard = new MagicCardList(player.getGraveyard());
            for (final MagicCard card : graveyard) {
                game.doAction(new MagicRemoveCardAction(
                    card,
                    MagicLocationType.Graveyard
                ));
                game.doAction(new MagicMoveCardAction(
                    card,
                    MagicLocationType.Graveyard,
                    MagicLocationType.OwnersLibrary
                ));
            }
        }
    };
}

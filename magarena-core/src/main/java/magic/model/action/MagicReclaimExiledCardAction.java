package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;

public class MagicReclaimExiledCardAction extends MagicAction {
    private final MagicPermanent source;
    private final MagicCard card;

    public MagicReclaimExiledCardAction(final MagicPermanent source,final MagicCard card){
        this.source = source;
        this.card = card;
    }
    
    public void doAction(final MagicGame game) {
        game.doAction(new MagicRemoveCardAction(card, MagicLocationType.Exile));
        game.doAction(new MagicMoveCardAction(card, MagicLocationType.Exile, MagicLocationType.OwnersHand));
        source.removeExiledCard(card);
    }
    
    public void undoAction(final MagicGame game) {
        source.addExiledCard(card);
    }
}

package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicObject;
import magic.model.stack.MagicCardOnStack;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

public class MagicEnterAsCopyAction extends MagicAction {

    private final MagicCardOnStack cardOnStack;
    private final MagicObject obj;
    private MagicLocationType oldLocation;
    private List<MagicPermanentAction> modifications;

    public MagicEnterAsCopyAction(final MagicCardOnStack aCardOnStack, final MagicObject aObj, final List<? extends MagicPermanentAction> aModifications) {
        cardOnStack = aCardOnStack;
        obj = aObj;
        modifications = new LinkedList<>(aModifications);
        modifications.addAll(cardOnStack.getModifications());
    }
    
    public MagicEnterAsCopyAction(final MagicCardOnStack aCardOnStack, final MagicObject aObj, final MagicPermanentAction... aModifications) {
        this(aCardOnStack, aObj, Arrays.asList(aModifications));
    }
    
    @Override
    public void doAction(final MagicGame game) {
        oldLocation = cardOnStack.getMoveLocation();
        cardOnStack.setMoveLocation(MagicLocationType.Play);

        final MagicCardOnStack replacement = new MagicCardOnStack(
            cardOnStack.getCard(),
            obj,
            cardOnStack.getController(),
            obj.getCardDefinition().getCardEvent(),
            cardOnStack.getPayedCost(),
            modifications
        );
        game.doAction(new MagicPutItemOnStackAction(replacement));
    }

    @Override
    public void undoAction(final MagicGame game) {
        cardOnStack.setMoveLocation(oldLocation);
    }
}

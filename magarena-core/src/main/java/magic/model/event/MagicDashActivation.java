package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.stack.MagicCardOnStack;
import magic.model.action.MagicPlayMod;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.condition.MagicCondition;

import java.util.Arrays;

public class MagicDashActivation extends MagicCardActivation {

    final MagicManaCost cost;

    public MagicDashActivation(final MagicManaCost aCost) {
        super(
            new MagicCondition[]{
                MagicCondition.CARD_CONDITION,
            },
            new MagicActivationHints(MagicTiming.FirstMain,true),
            "Dash"
        );
        cost = aCost;
    }

    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(
                source,
                cost
            )
        );
    }
    
    @Override
    public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
        return new MagicEvent(
            cardOnStack,
            this,
            "Put SN onto the battlefield, " +
            "it gains haste and it's returned from the battlefield to its owner's hand at the beginning of the next end step."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicPlayCardFromStackAction(
            event.getCardOnStack(),
            MagicPlayMod.HASTE, MagicPlayMod.RETURN_AT_END_OF_TURN
        ));
    }
}

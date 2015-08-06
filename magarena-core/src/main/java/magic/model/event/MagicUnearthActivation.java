package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicSource;
import magic.model.MagicCardDefinition;
import magic.model.action.MagicPlayMod;
import magic.model.action.ReanimateAction;
import magic.model.choice.MagicChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicAbilityOnStack;
import java.util.Arrays;

public class MagicUnearthActivation extends MagicCardAbilityActivation {

    private static final MagicCondition[] COND = new MagicCondition[]{ MagicCondition.SORCERY_CONDITION };
    private static final MagicActivationHints HINT = new MagicActivationHints(MagicTiming.Pump,true);
    final MagicManaCost cost;

    public MagicUnearthActivation(final MagicManaCost aCost) {
        super(
            COND,
            HINT,
            "Unearth"
        );
        cost = aCost;
    }
    
    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addGraveyardAct(this);
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(source, cost)
        );
    }

    @Override
    public MagicEvent getCardEvent(final MagicCard source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "Return SN from PN's graveyard to the battlefield. It gains haste. Exile it at the beginning of the next end step or if it would leave the battlefield."
        );
    }
    
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new ReanimateAction(
            event.getCard(),
            event.getPlayer(),
            MagicPlayMod.HASTE,
            MagicPlayMod.EXILE_AT_END_OF_TURN,
            MagicPlayMod.EXILE_WHEN_LEAVES
        ));
    }
}

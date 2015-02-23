package magic.model.event;

import magic.model.MagicSource;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicRegenerationConditionsEvent extends MagicEvent {

    private final MagicCondition[] conds;

    public MagicRegenerationConditionsEvent(final MagicSource source, final MagicPermanentActivation act) {
        super(
            source,
            MagicEvent.NO_ACTION,
            ""
        );
        conds = new MagicCondition[]{
            MagicCondition.CAN_REGENERATE_CONDITION,
            MagicConditionFactory.SingleActivation(act)
        };
    }

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}

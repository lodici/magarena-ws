package magic.model.event;

import magic.model.MagicSource;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicUntapConditionsEvent extends MagicEvent {

    private final MagicCondition[] conds;

    public MagicUntapConditionsEvent(final MagicSource source, final MagicPermanentActivation act) {
        super(
            source,
            MagicEvent.NO_ACTION,
            ""
        );
        conds = new MagicCondition[]{
            MagicCondition.TAPPED_CONDITION,
            MagicConditionFactory.SingleActivation(act)
        };
    }

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}

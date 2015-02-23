package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicAddCounterChosenEvent extends MagicEvent {

    private final MagicCondition[] conds;

    public MagicAddCounterChosenEvent(final MagicSource source, final MagicCounterType counterType) {
        super(
            source,
            MagicTargetChoice.CREATURE_YOU_CONTROL,
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game, new MagicPermanentAction() {
                        public void doAction(final MagicPermanent perm) {
                            game.doAction(new MagicChangeCountersAction(
                                perm,
                                counterType,
                                1
                            ));
                        }
                    });
                }
            },
            "Put a " + counterType.getName() + " counter on a creature$ you control."
        );
        conds = new MagicCondition[]{
            MagicConditionFactory.HasOptions(source.getController(), getTargetChoice())
        };
    }

    @Override
    public final MagicCondition[] getConditions() {
        return conds;
    }
}

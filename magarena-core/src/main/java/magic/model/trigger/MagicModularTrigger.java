package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.ChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicPumpTargetPicker;

public class MagicModularTrigger extends MagicWhenDiesTrigger {

    private static final MagicModularTrigger INSTANCE = new MagicModularTrigger();

    private MagicModularTrigger() {}

    public static MagicModularTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
        final int amount = permanent.getCounters(MagicCounterType.PlusOne);
        return new MagicEvent(
            permanent,
            new MagicMayChoice(
                MagicTargetChoice.POS_TARGET_ARTIFACT_CREATURE
            ),
            MagicPumpTargetPicker.create(),
            amount,
            this,
            "PN may$ put RN +1/+1 counters on target artifact creature$."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new ChangeCountersAction(
                        creature,
                        MagicCounterType.PlusOne,
                        event.getRefInt()
                    ));
                }
            });
        }
    }
}

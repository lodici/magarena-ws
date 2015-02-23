package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.target.MagicBounceTargetPicker;

public class MagicBounceChosenPermanentEvent extends MagicEvent {

    private final MagicCondition[] conds;

    public MagicBounceChosenPermanentEvent(
            final MagicSource source,
            final MagicTargetChoice targetChoice) {
        this(source, source.getController(), targetChoice);
    }

    public MagicBounceChosenPermanentEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicTargetChoice targetChoice) {
        super(
            source,
            player,
            targetChoice,
            MagicBounceTargetPicker.create(),
            EVENT_ACTION,
            "Return "+targetChoice.getTargetDescription()+"$ to its owner's hand."
        );
        conds = new MagicCondition[]{MagicConditionFactory.HasOptions(player, targetChoice)};
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicRemoveFromPlayAction(
                        permanent,
                        MagicLocationType.OwnersHand
                    ));
                }
            });
        }
    };

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}

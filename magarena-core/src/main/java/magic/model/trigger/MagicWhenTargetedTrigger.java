package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.SacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.stack.MagicItemOnStack;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTarget;

public abstract class MagicWhenTargetedTrigger extends MagicTrigger<MagicItemOnStack> {
    public MagicWhenTargetedTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenTargetedTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenTargeted;
    }
    
    public static MagicWhenTargetedTrigger create(final MagicTargetFilter<MagicPermanent> pfilter, final MagicTargetFilter<MagicItemOnStack> ifilter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenTargetedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
                final MagicTarget target = itemOnStack.getTarget();
                return target.isPermanent() &&
                       pfilter.accept(permanent, permanent.getController(), (MagicPermanent)target) &&
                       ifilter.accept(permanent, permanent.getController(), itemOnStack);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
                return sourceEvent.getEvent(permanent, (MagicPermanent)itemOnStack.getTarget());
            }
        };
    }
}

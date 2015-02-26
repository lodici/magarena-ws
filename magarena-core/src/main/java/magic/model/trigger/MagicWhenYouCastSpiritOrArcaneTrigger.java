package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.stack.MagicCardOnStack;

public abstract class MagicWhenYouCastSpiritOrArcaneTrigger extends MagicWhenOtherSpellIsCastTrigger {
    public MagicWhenYouCastSpiritOrArcaneTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenYouCastSpiritOrArcaneTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicCardOnStack spell) {
        return (permanent.isFriend(spell) &&
                (spell.getCardDefinition().hasSubType(MagicSubType.Spirit) ||
                 spell.getCardDefinition().hasSubType(MagicSubType.Arcane)));
    }
    
    public static final MagicWhenYouCastSpiritOrArcaneTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenYouCastSpiritOrArcaneTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCardOnStack spell) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}

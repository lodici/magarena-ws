package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.ScryAction;
import magic.model.choice.MagicScryChoice;
import magic.model.trigger.MagicTriggerType;

public class MagicScryEvent extends MagicEvent {
    
    public MagicScryEvent(final MagicEvent event) {
        this(event.getSource(), event.getPlayer(), true);
    }
    
    public MagicScryEvent(final MagicSource source, final MagicPlayer player) {
        this(source, player, true);
    }
    
    public static MagicScryEvent Pseudo(final MagicEvent event) {
        return new MagicScryEvent(event.getSource(), event.getPlayer(), false);
    }
    
    public static MagicScryEvent Pseudo(final MagicSource source, final MagicPlayer player) {
        return new MagicScryEvent(source, player, false);
    }

    private MagicScryEvent(final MagicSource source, final MagicPlayer player, final boolean trigger) {
        super(
            source,
            player,
            new MagicScryChoice(),
            EventAction(trigger),
            ""
        );
    }
    
    private static final MagicEventAction EventAction(final boolean trigger) {
        return new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                final MagicPlayer p = event.getPlayer();
                if (event.isYes()) {
                    game.logAppendMessage(p, p + " looks at the card on the top of his or her library and moves it to the bottom.");
                    game.doAction(new ScryAction(p));
                } else {
                    game.logAppendMessage(p, p + " looks at the card on the top of his or her library and puts it back on top.");
                }
                //Scry triggers even if the card is not moved. Only once regardless of amount of cards scryed
                if (trigger) {
                    game.executeTrigger(MagicTriggerType.WhenScry,p);
                }
            }
        };
    };
}

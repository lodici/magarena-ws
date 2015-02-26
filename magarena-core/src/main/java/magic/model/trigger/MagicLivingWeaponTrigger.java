package magic.model.trigger;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicAttachAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicPermanentAction;
import magic.model.event.MagicEvent;

/**
 * Trigger that occurs when a card with the living weapon mechanic comes into play
 */
public class MagicLivingWeaponTrigger extends MagicWhenComesIntoPlayTrigger {

    private static final MagicWhenComesIntoPlayTrigger INSTANCE = new MagicLivingWeaponTrigger();

    private MagicLivingWeaponTrigger() {}

    public static MagicWhenComesIntoPlayTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
        return new MagicEvent(
            permanent,
            this,
            "PN puts a 0/0 black Germ creature token onto the battlefield, then attaches SN to it."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicPermanent sn = event.getPermanent();
        game.doAction(new MagicPlayTokenAction(
            event.getPlayer(),
            TokenCardDefinitions.get("0/0 black Germ creature token"),
            new MagicPermanentAction() {
                @Override
                public void doAction(final MagicPermanent perm) {
                    final MagicGame G = perm.getGame();
                    G.doAction(new MagicAttachAction(
                        sn.map(G),
                        perm
                    ));
                }
            }
        ));
    }
}

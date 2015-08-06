package magic.model.choice;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicRandom;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.exception.UndoClickedException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import magic.model.IUIGameController;

public class MagicDeclareAttackersChoice extends MagicChoice {

    private static final MagicDeclareAttackersChoice INSTANCE = new MagicDeclareAttackersChoice();

    private static final String MESSAGE =
        "Click on a creature to declare as attacker or remove it from combat.|Press {f} to continue.";

    private MagicDeclareAttackersChoice() {
        super("Declare attackers.");
    }

    @Override
    Collection<Object> getArtificialOptions(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();
        return MagicDeclareAttackersResultBuilder.buildResults(game,player);
    }

    @Override
    public Object[] getSimulationChoiceResult(final MagicGame game, final MagicEvent event) {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final MagicDeclareAttackersResult result = new MagicDeclareAttackersResult();
        final MagicCombatCreatureBuilder builder = new MagicCombatCreatureBuilder(game,player,player.getOpponent());
        builder.buildBlockers();

        if (builder.buildAttackers()) {
            for (final MagicCombatCreature attacker : builder.getAttackers()) {
                if (attacker.hasAbility(MagicAbility.AttacksEachTurnIfAble) ||
                    MagicRandom.nextRNGInt(2) == 1) {
                    //creatures must attack OR
                    //creature has 50% chance of attacking
                    result.add(attacker.permanent);
                }
            }
        }

        return new Object[]{result};
    }

    @Override
    public Object[] getPlayerChoiceResults(final IUIGameController controller, final MagicGame game, final MagicEvent event) throws UndoClickedException {
        final MagicPlayer player = event.getPlayer();
        final MagicSource source = event.getSource();

        final MagicDeclareAttackersResult result=new MagicDeclareAttackersResult();
        final MagicCombatCreatureBuilder builder=new MagicCombatCreatureBuilder(game,player,player.getOpponent());
        builder.buildBlockers();

        final Set<Object> validChoices=new HashSet<Object>();
        if (builder.buildAttackers()) {
            for (final MagicCombatCreature attacker : builder.getAttackers()) {
                if (attacker.hasAbility(MagicAbility.AttacksEachTurnIfAble)) {
                    attacker.permanent.setState(MagicPermanentState.Attacking);
                    result.add(attacker.permanent);
                } else {
                    validChoices.add(attacker.permanent);
                }
            }
        }

        if (validChoices.isEmpty() && MagicGame.canSkipSingleChoice()) {
            return new Object[]{result};
        }

        controller.focusViewers(-1);
        controller.showMessage(source,MESSAGE);
        controller.setValidChoices(validChoices,true);
        controller.enableForwardButton();
        controller.updateGameView();

        try {
            while (true) {
                controller.waitForInput();
                if (controller.isActionClicked()) {
                    break;
                }
                final MagicPermanent attacker = controller.getChoiceClicked();
                if (attacker.isAttacking()) {
                    attacker.clearState(MagicPermanentState.Attacking);
                    result.remove(attacker);
                } else {
                    attacker.setState(MagicPermanentState.Attacking);
                    result.add(attacker);
                }
                controller.updateGameView();
            }
        } finally {
            // Cleanup
            for (final MagicCombatCreature creature : builder.getAttackers()) {
                creature.permanent.clearState(MagicPermanentState.Attacking);
            }
        }
        game.snapshot();
        return new Object[]{result};
    }

    public static MagicDeclareAttackersChoice getInstance() {
        return INSTANCE;
    }

}

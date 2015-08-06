package magic.model.phase;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.phase.MagicPhaseType;
import magic.model.action.ChangeExtraTurnsAction;
import magic.model.action.CleanupPlayerAction;
import magic.model.action.CleanupTurnStaticsAction;
import magic.model.action.CleanupTurnTriggersAction;
import magic.model.action.PayDelayedCostsAction;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;

public class MagicCleanupPhase extends MagicPhase {

    private static final MagicPhase INSTANCE=new MagicCleanupPhase();

    private MagicCleanupPhase() {
        super(MagicPhaseType.Cleanup);
    }

    public static MagicPhase getInstance() {
        return INSTANCE;
    }

    private static void cleanup(final MagicGame game) {
        final MagicPlayer turnPlayer=game.getTurnPlayer();
        // discard excess cards
        if (turnPlayer.getNumExcessCards() > 0) {
            final int amount = turnPlayer.getNumExcessCards();
            game.addEvent(new MagicDiscardEvent(MagicSource.NONE,turnPlayer,amount));
        }
        // remove until EOT triggers/static, clean up player and permanents
        game.doAction(new CleanupTurnTriggersAction());
        for (final MagicPlayer player : game.getPlayers()) {
            game.doAction(new CleanupPlayerAction(player));
        }
        game.doAction(new CleanupTurnStaticsAction());
        game.update();
        game.checkStatePutTriggers();
    }

    private static void nextTurn(final MagicGame game) {
        final MagicPlayer turnPlayer=game.getTurnPlayer();
        final MagicPlayer opponentPlayer=game.getTurnPlayer().getOpponent();
        if (!turnPlayer.getBuilderCost().isEmpty()) {
            game.doAction(new PayDelayedCostsAction(turnPlayer));
        }
        if (turnPlayer.getExtraTurns()>0) {
            game.doAction(new ChangeExtraTurnsAction(turnPlayer,-1));
            final String playerName = turnPlayer.getName();
            game.logMessage(turnPlayer,playerName + " takes an extra turn.");
        } else {
            game.setTurnPlayer(turnPlayer.getOpponent());
        }
        game.setTurn(game.getTurn()+1);
        game.resetLandsPlayed();
        game.resetMaxLands();
        game.setCreatureDiedThisTurn(false);
        game.clearSkipTurnTill();
        for (final MagicPlayer player : game.getPlayers()) {
            player.setSpellsCastLastTurn(player.getSpellsCast());
            player.setSpellsCast(0);
        }
    }

    @Override
    public void executeBeginStep(final MagicGame game) {
        cleanup(game);
        if (game.getStack().isEmpty()) {
            game.setStep(MagicStep.NextPhase);
        } else {
            game.setStep(MagicStep.ActivePlayer);
        }
    }
    
    @Override
    public void executeEndOfPhase(final MagicGame game) {
        nextTurn(game);
    }
}

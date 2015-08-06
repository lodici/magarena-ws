package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestBairn extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS,8);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Wall of Denial", 5);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Island", 20);
        createPermanent(P, "Island", 10);
        createPermanent(P, "Gilder Bairn");
        createPermanent(P, "Paradise Mantle", 1);
        addToHand(P, "Simic Initiate", 4);

        return game;
    }
}

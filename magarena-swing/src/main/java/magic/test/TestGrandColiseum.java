
package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestGrandColiseum extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Grand Coliseum",false,8);
        addToHand(P,"Grizzly Bears",1);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Grand Coliseum",false,8);
        addToHand(P,"Grizzly Bears",1);

        return game;
    }
}

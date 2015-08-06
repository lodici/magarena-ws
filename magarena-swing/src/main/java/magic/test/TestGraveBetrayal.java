package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestGraveBetrayal extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Mountain", 20);
        createPermanent(P,"Mountain",false,8);
        createPermanent(P,"Grave Betrayal",false,1);
        addToHand(P, "Scavenging Ooze", 1);
        addToHand(P, "Lightning Bolt", 1);
        addToHand(P, "Rise of the Hobgoblins", 3);

        P = opponent;

        P.setLife(2);
        addToLibrary(P, "Mountain", 20);
        createPermanent(P,"Mountain",false,9);
        addToHand(P, "Rise of the Hobgoblins", 3);

        return game;
    }
}

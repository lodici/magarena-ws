package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestEntersWithX extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(1);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Thieving Magpie",false,1);
        addToHand(P,"Primordial Hydra",1);
        addToHand(P,"Mikaeus, the Lunarch",1);
        addToHand(P,"Chimeric Mass",1);
        addToHand(P,"Apocalypse Hydra",1);
        addToHand(P,"Ivy Elemental",1);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Thieving Magpie",false,1);
        addToHand(P, "Plains", 7);

        return game;
    }
}

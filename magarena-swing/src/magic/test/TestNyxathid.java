package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;

class TestNyxathid extends TestGameBuilder {

    public MagicGame getGame() {
        final MagicDuel duel=new MagicDuel();
        duel.setDifficulty(6);

        final MagicDeckProfile profile=new MagicDeckProfile("bgruw");
        final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile);
        final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile);
        duel.setPlayers(new MagicPlayerDefinition[]{player1,player2});
        duel.setStartPlayer(0);

        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Mountain", 10);
        createPermanent(game,P,"Rupture Spire",false,6);
        createPermanent(game,P,"Nyxathid",false,1);
        addToHand(P,"Eager Cadet",1);


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Mountain", 10);
        createPermanent(game,P,"Rupture Spire",false,6);
        createPermanent(game,P,"Eager Cadet",false,1);
        addToHand(P,"Sift",1);
        addToHand(P,"Mountain",3);

        return game;
    }
}

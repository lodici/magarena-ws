package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;

class TestPlaneswalker extends TestGameBuilder {
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
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Rupture Spire",false,10);
        createPermanent(game,P,"Grizzly Bears",false,1);
        addToHand(P, "Ajani, Caller of the Pride", 1);
        addToHand(P, "Sarkhan Vol", 1);
        addToHand(P, "Tamiyo, the Moon Sage", 1);
        addToHand(P, "Burst Lightning", 1);
        addToHand(P, "Elspeth Tirel", 1);
        addToHand(P, "Venser, the Sojourner", 1);
        addToHand(P, "Vraska the Unseen", 1);
        addToHand(P, "Gideon, Champion of Justice", 1);
        addToHand(P, "Elspeth, Knight-Errant", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Rupture Spire",false,8);
        //createPermanent(game,P,"Grizzly Bears",false,1);
        addToHand(P, "Elspeth, Knight-Errant", 1);
        addToHand(P, "Ajani Goldmane", 1);
        addToHand(P, "Garruk, Primal Hunter", 1);
        addToHand(P, "Jace Beleren", 1);
        addToHand(P, "Tibalt, the Fiend-Blooded", 1);
        addToHand(P, "Ajani Vengeant", 1);

        return game;
    }
}

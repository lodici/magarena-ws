package magic.model.action;

import magic.model.MagicGame;
import magic.model.target.MagicTarget;

public class PreventDamageAction extends MagicAction {

    private final MagicTarget target;
    private final int amount;
    private int oldAmount;

    public PreventDamageAction(final MagicTarget aTarget, final int aAmount) {
        target = aTarget;
        amount = aAmount;
    }

    @Override
    public void doAction(final MagicGame game) {
        oldAmount=target.getPreventDamage();
        target.setPreventDamage(oldAmount+amount);
    }

    @Override
    public void undoAction(final MagicGame game) {
        target.setPreventDamage(oldAmount);
    }
}

package magic.model.target;

import magic.model.MagicCopyable;
import magic.model.MagicObject;
import magic.model.MagicPlayer;
import magic.model.MagicSource;

public interface MagicTarget extends MagicCopyable, MagicObject {
    boolean isLegalTarget(final MagicPlayer player, final MagicTargetFilter<? extends MagicTarget> targetFilter);
    boolean isValidTarget(final MagicSource source);
    int     getPreventDamage();
    void    setPreventDamage(int amount);
}

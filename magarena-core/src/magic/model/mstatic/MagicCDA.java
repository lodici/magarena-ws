package magic.model.mstatic;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTarget;

import java.util.Set;

/*
604.3a A static ability is a characteristic-defining ability if it meets
the following criteria: (1) It defines an object's colors, subtypes,
power, or toughness; (2) it is printed on the card it affects, it was
granted to the token it affects by the effect that created the token,
or it was acquired by the object it affects as the result of a copy
effect or text-changing effect; (3) it does not directly affect the
characteristics of any other objects; (4) it is not an ability that
an object grants to itself; and (5) it does not set the values of such
characteristics only if certain conditions are met.
*/

public abstract class MagicCDA implements MagicChangeCardDefinition {

    public static final MagicCDA Changeling = new MagicCDA() {
        public void getSubTypeFlags(
                final MagicGame game,
                final MagicPlayer player,
                final Set<MagicSubType> flags) {
            flags.addAll(MagicSubType.ALL_CREATURES);
        }
    };

    public static MagicCDA setPT(final int base, final MagicTargetFilter<MagicTarget> filter) {
        return new MagicCDA() {
            @Override
            public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
                final int amount = game.filterTargets(player, filter).size();
                pt.set(base + amount, base + amount);
            }
        };
    }
    
    public static MagicCDA setPower(final int base, final MagicTargetFilter<MagicTarget> filter) {
        return new MagicCDA() {
            @Override
            public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
                final int amount = game.filterTargets(player, filter).size();
                pt.setPower(base + amount);
            }
        };
    }
    
    public static MagicCDA setToughness(final int base, final MagicTargetFilter<MagicTarget> filter) {
        return new MagicCDA() {
            @Override
            public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
                final int amount = game.filterTargets(player, filter).size();
                pt.setToughness(base + amount);
            }
        };
    }

    public int getColorFlags(final MagicGame game, final MagicPlayer player,final int flags) {
        return flags;
    }

    public void getSubTypeFlags(
            final MagicGame game,
            final MagicPlayer player,
            final Set<MagicSubType> flags) {
    }

    public void modPowerToughness(
            final MagicGame game,
            final MagicPlayer player,
            final MagicPermanent permanent,
            final MagicPowerToughness pt) {
        modPowerToughness(game, player, pt);
    }

    public void modPowerToughness(
            final MagicGame game,
            final MagicPlayer player,
            final MagicPowerToughness pt) {
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addCDA(this);
    }
}

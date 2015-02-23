package magic.model;

public enum MagicPlayerState {

    Exhausted("creatures and lands don't untap during your next untap step"),
    WasDealtDamage(""),
    CantCastSpells("can't cast spells this turn"),
    CantActivateAbilities("can't activate abilities this turn"),
    HasLostLife(""),
    HasGainedLife(""),
    ;

    private final String description;
    private final int mask;

    private MagicPlayerState(final String description) {
        this.description=description;
        this.mask=1<<ordinal();
    }

    public String getDescription() {
        return description;
    }

    public int getMask() {
        return mask;
    }

    public boolean hasState(final int flags) {
        return (flags&mask)!=0;
    }
}

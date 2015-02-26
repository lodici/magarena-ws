package magic.model;

import java.util.TreeSet;

public class MagicPermanentSet extends TreeSet<MagicPermanent> {

    private static final long serialVersionUID = 1L;

    MagicPermanentSet() {}

    MagicPermanentSet(final MagicCopyMap copyMap,final MagicPermanentSet source) {
        for (final MagicPermanent permanent : source) {
            add(copyMap.copy(permanent));
        }
    }

    MagicPermanent getPermanent(final long id) {
        for (final MagicPermanent permanent : this) {
            if (permanent.getId() == id) {
                return permanent;
            }
        }
        return MagicPermanent.NONE;
    }

    long getStateId() {
        final long[] keys = new long[size()];
        int idx = 0;
        for (final MagicPermanent permanent : this) {
            keys[idx] = permanent.getStateId();
            idx++;
        }
        return magic.model.MurmurHash3.hash(keys);
    }
}

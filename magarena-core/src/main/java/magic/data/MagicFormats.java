package magic.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import magic.model.MagicCardDefinition;
import magic.utility.MagicResources;

public enum MagicFormats {

    // add new formats here...
    // @name: display name in UI.
    // @filename: case-sensitive name of file (without extension) in magic/data/formats.

    STANDARD ("Standard", "standard"),
    MODERN ("Modern", "modern"),
    LEGACY("Legacy", "legacy"),
    VINTAGE("Vintage", "vintage"),
    ICE_AGE_BLOCK("Ice Age Block", "ice_age_block"),
    MIRAGE_BLOCK("Mirage block", "mirage_block"),
    TEMPEST_BLOCK("Tempest block", "tempest_block"),
    URZA_BLOCK("Urza block", "urza_block"),
    MASQUES_BLOCK("Masques block", "masques_block"),
    INVASION_BLOCK("Invasion block", "invasion_block"),
    ODYSSEY_BLOCK("Odyssey block", "odyssey_block"),
    ONSLAUGHT_BLOCK("Onslaught block", "onslaught_block"),
    MIRRODIN_BLOCK("Mirrodin block", "mirrodin_block"),
    KAMIGAWA_BLOCK("Kamigawa block", "kamigawa_block"),
    RAVNICA_BLOCK("Ravnica block", "ravnica_block"),
    TIME_SPIRAL_BLOCK("Time Spiral block", "time_spiral_block"),
    LORWYN_SHADOWMOOR_BLOCK("Lorwyn-Shadowmoor block", "lorwyn_shadowmoor_block"),
    SHARDS_OF_ALARA_BLOCK("Shards of Alara block", "shards_of_alara_block"),
    ZENDIKAR_RISE_OF_THE_ELDRAZI_BLOCK("Zendikar-Rise of the Eldrazi block", "zendikar_rise_of_the_eldrazi_block"),
    SCARS_OF_MIRRODIN_BLOCK("Scars of Mirrodin block", "scars_of_mirrodin_block"),
    INNISTRAD_AVACYN_RESTORED_BLOCK("Innistrad-Avacyn Restored block", "innistrad_avacyn_restored_block"),
    RETURN_TO_RAVNICA_BLOCK("Return to Ravnica block", "return_to_ravnica_block"),
    THEROS_BLOCK("Theros block", "theros_block"),
    KHANS_OF_TARKIR_BLOCK("Khans of Tarkir block", "khans_of_tarkir_block")
    ;

    private final String name;
    private final String filename;

    private static final HashMap<MagicFormats, MagicFormatDefinition> loadedFormats = new HashMap<>();

    private MagicFormats(final String name, final String filename) {
        this.name = name;
        this.filename = filename;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public static String[] getFilterValues() {
        final List<String> values = new ArrayList<>();
        for (MagicFormats f : MagicFormats.values()) {
            values.add(f.getName());
        }
        return values.toArray(new String[values.size()]);
    }

    public static boolean isCardLegal(MagicCardDefinition card, MagicFormats magicFormatType) {
        if (!loadedFormats.containsKey(magicFormatType)) {
            loadedFormats.put(magicFormatType, loadMagicFormatFile(magicFormatType));
        }
        final MagicFormatDefinition magicFormat = loadedFormats.get(magicFormatType);
        return magicFormat.contains(card);
    }

    private static MagicFormatDefinition loadMagicFormatFile(final MagicFormats magicFormatType) {

        final MagicFormatDefinition magicFormat = new MagicFormatDefinition();

        try (final Scanner sc = new Scanner(MagicResources.getFileContent(magicFormatType))) {
            while (sc.hasNextLine()) {
                final String line = sc.nextLine().trim();
                final boolean skipLine = (line.startsWith("#") || line.isEmpty());
                if (!skipLine) {
                    switch (line.substring(0, 1)) {
                    case "!":
                        magicFormat.addBannedCardName(line.substring(1));
                        break;
                    case "*":
                        magicFormat.addRestrictedCardName(line.substring(1));
                        break;
                    default:
                        magicFormat.addSetCode(line);
                    }
                }
            }
        }

        return magicFormat;
    }

}

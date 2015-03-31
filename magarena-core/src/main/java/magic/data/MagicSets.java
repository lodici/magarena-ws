package magic.data;

public enum MagicSets {

    DTK("Dragons of Tarkir"), // 2015-3-27
    FRF("Fate Reforged"), // 2015-1-23
    C14("Commander 2014"), // 2014-11-7
    KTK("Khans of Tarkir"), // 2014-9-26
    M15("Magic 2015 Core Set"), // 2014-7-18
    CNS("Conspiracy"), // 2014-6-6
    JOU("Journey into Nyx"), // 2014-5-2
    BNG("Born of the Gods"), // 2014-2-7
    C13("Commander (2013 Edition)"), // 2013-11-1
    THS("Theros"), // 2013-9-27
    M14("Magic 2014 Core Set"), // 2013,7,19
    DGM("Dragon's Maze"), // 2013-5-3
    GTC("Gatecrash"), // 2013-2-1
    RTR("Return to Ravnica"), // 2012-10-5
    M13("Magic 2013"), // 2012-7-7
    PC2("Planechase 2012 Edition"), // 2012-6-1
    AVR("Avacyn Restored"), // 2012-5-4
    DKA("Dark Ascension"), // 2012-2-3
    ISD("Innistrad"), // 2011-11-30
    M12("Magic 2012"), // 2011-7-9
    CMD("Commander"), // 2011-6-17
    NPH("New Phyrexia"), // 2011-5-13
    MBS("Mirrodin Besieged"), // 2011-2-4
    SOM("Scars of Mirrodin"), // 2010-10-1
    M11("Magic 2011"), // 2010-7-16
    ROE("Rise of the Eldrazi"), // 2010-4-23
    WWK("Worldwake"), // 2010-2-5
    ZEN("Zendikar"), // 2009-10-2
    M10("Magic 2010"), // 2009-7-17
    ARB("Alara Reborn"), // 2009-4-30
    CFX("Conflux"), // 2009-2-6
    ALA("Shards of Alara"), // 2008-10-3
    EVE("Eventide"), // 2008-7-25
    SHM("Shadowmoor"), // 2008-5-2
    MOR("Morningtide"), // 2008-2-1
    LRW("Lorwyn"), // 2007-10-12
    _10E("Core Set - Tenth Edition"), // 2007-7-13
    FUT("Future Sight"), // 2007-5-4
    PLC("Planar Chaos"), // 2007-2-2
    TSP("Time Spiral"), // 2006-10-6
    TSB("Time Spiral \"Timeshifted\""), // 2006-10-6
    CSP("Coldsnap"), // 2006-7-21
    DIS("Dissension"), // 2006-5-6
    GPT("Guildpact"), // 2006-2-3
    RAV("Ravnica: City of Guilds"), // 2005-10-7
    _9ED("Core Set - Ninth Edition"), // 2005-7-29
    SOK("Saviors of Kamigawa"), // 2005-6-3
    BOK("Betrayers of Kamigawa"), // 2005-2-4
    CHK("Champions of Kamigawa"), // 2004-10-1
    _5DN("Fifth Dawn"), // 2004-5-22
    DST("Darksteel"), // 2004-2-6
    MRD("Mirrodin"), // 2003-10-3
    _8ED("Core Set - Eighth Edition"), // 2003-7-28
    SCG("Scourge"), // 2003-5-26
    LGN("Legions"), // 2003-2-3
    ONS("Onslaught"), // 2002-10-7
    JUD("Judgment"), // 2002-5-27
    TOR("Torment"), // 2002-2-4
    ODY("Odyssey"), // 2001-10-1
    APC("Apocalypse"), // 2001-6-4
    _7ED("Seventh Edition"), // 2001-4-11
    PLS("Planeshift"), // 2001-2-5
    INV("Invasion"), // 2000-10-2
    S00("Starter 2000"), // 2007-7-?
    PCY("Prophecy"), // 2000-6-5
    NEM("Nemesis"), // 2000-2-14
    MMQ("Mercadian Masques"), // 1999-10-4
    S99("Starter 1999"), // 1999-7-?
    UDS("Urza's Destiny"), // 1999-6-7
    PTK("Portal Three Kingdoms"), // 1999-5-?
    _6ED("Classic (Sixth Edition)"), // 1999-4-28
    ULG("Urza's Legacy"), // 1999-2-15
    USG("Urza's Saga"), // 1998-10-12
    EXO("Exodus"), // 1998-6-15
    PO2("Portal Second Age"), // 1998-6-?
    STH("Stronghold"), // 1998-3-2
    TMP("Tempest"), // 1997-10-13
    WTH("Weatherlight"), // 1997-6-9
    POR("Portal"), // 1997-6-?
    _5ED("Fifth Edition"), // 1997-3-27
    VIS("Visions"), // 1997-2-3
    MIR("Mirage"), // 1996-10-7
    ALL("Alliances"), // 1996-7-10
    HML("Homelands"), // 1995-10-?
    CHR("Chronicles"), // 1995-7-?
    ICE("Ice Age"), // 1995-6-?
    _4ED("Fourth Edition"), // 1995-3-?
    FEM("Fallen Empires"), // 1994-11
    DRK("The Dark"), // 1994-8
    LEG("Legends"), // 1994-6-?
    ATQ("Antiquities"), // 1994-3-?
    _3ED("Revised Edition"), // 1994-3-?
    ARN("Arabian Nights"), // 1993-12-?
    _2ED("Unlimited Edition"), // 1993-12-1
    LEB("Limited Edition Beta"), // 1993-10-?
    LEA("Limited Edition Alpha"), // 1993-8-5
    ;

    private final String setName;

    private MagicSets(final String name) {
        this.setName = name;
    }

    public String getSetName() {
        return setName;
    }

}


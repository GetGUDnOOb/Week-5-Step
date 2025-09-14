import java.util.*;

public class KingdomManager {
    private final List<Object> structures = new ArrayList<>();
    private final KingdomConfig config;

    public KingdomManager(KingdomConfig config) {
        this.config = config;
    }

    public void addStructure(Object structure) {
        structures.add(structure);
    }

    public List<Object> getStructures() {
        return Collections.unmodifiableList(structures);
    }

    public KingdomConfig getConfig() {
        return config;
    }

    public static boolean canStructuresInteract(Object s1, Object s2) {
        // Example logic: Only WizardTower can interact with EnchantedCastle or DragonLair
        if ((s1 instanceof WizardTower && (s2 instanceof EnchantedCastle || s2 instanceof DragonLair)) ||
            (s2 instanceof WizardTower && (s1 instanceof EnchantedCastle || s1 instanceof DragonLair))) {
            return true;
        }
        return false;
    }

    public static String performMagicBattle(Object attacker, Object defender) {
        if (!(attacker instanceof MagicalStructure) || !(defender instanceof MagicalStructure)) {
            return "Invalid combatants";
        }
        MagicalStructure att = (MagicalStructure) attacker;
        MagicalStructure def = (MagicalStructure) defender;

        int attPower = att.getMagicPower();
        int defPower = def.getMagicPower();

        if (attPower > defPower) {
            return att.getStructureName() + " wins!";
        } else if (defPower > attPower) {
            return def.getStructureName() + " wins!";
        } else {
            return "It's a tie!";
        }
    }

    public static int calculateKingdomPower(Object[] structures) {
        int totalPower = 0;
        for (Object s : structures) {
            if (s instanceof MagicalStructure) {
                totalPower += ((MagicalStructure) s).getMagicPower();
            }
        }
        return totalPower;
    }

    private String determineStructureCategory(Object structure) {
        if (structure instanceof WizardTower) return "WizardTower";
        if (structure instanceof EnchantedCastle) return "EnchantedCastle";
        if (structure instanceof MysticLibrary) return "MysticLibrary";
        if (structure instanceof DragonLair) return "DragonLair";
        if (structure instanceof MagicalStructure) return "MagicalStructure";
        return "Unknown";
    }

    public static void main(String[] args) {
        KingdomConfig defaultConfig = KingdomConfig.createDefaultKingdom();
        KingdomManager manager = new KingdomManager(defaultConfig);

        WizardTower tower = new WizardTower("Tower One", "North Hill", 500, true,
                300, Arrays.asList("Fireball", "Shield"), "Gandalf");
        EnchantedCastle castle = new EnchantedCastle("Castle Black", "East Valley", 700, true,
                "Royal", 80, true);
        MysticLibrary library = new MysticLibrary("Library of Arcana", "Central City", 300, true,
                Map.of("Book1", "Magic Spells", "Book2", "Alchemy"), 90);
        DragonLair lair = new DragonLair("Dragon's Nest", "Volcano", 900, true,
                "Fire Drake", 1000000L, 50);

        manager.addStructure(tower);
        manager.addStructure(castle);
        manager.addStructure(library);
        manager.addStructure(lair);

        System.out.println("Can Tower and Castle interact? " + canStructuresInteract(tower, castle));
        System.out.println("Battle between Tower and Lair: " + performMagicBattle(tower, lair));
        System.out.println("Total kingdom power: " + calculateKingdomPower(manager.getStructures().toArray()));

        System.out.println("Structure Categories:");
        for (Object s : manager.getStructures()) {
            System.out.println(manager.determineStructureCategory(s) + ": " + s);
        }
    }
}

final class KingdomConfig {
    private final String kingdomName;
    private final int foundingYear;
    private final String[] allowedStructureTypes;
    private final Map<String, Integer> resourceLimits;

    public KingdomConfig(String kingdomName, int foundingYear, String[] allowedStructureTypes, Map<String, Integer> resourceLimits) {
        if (kingdomName == null || kingdomName.isBlank())
            throw new IllegalArgumentException("Kingdom name cannot be null or blank");
        if (foundingYear < 0)
            throw new IllegalArgumentException("Founding year cannot be negative");
        if (allowedStructureTypes == null || allowedStructureTypes.length == 0)
            throw new IllegalArgumentException("Allowed structure types cannot be null or empty");
        if (resourceLimits == null)
            throw new IllegalArgumentException("Resource limits cannot be null");

        this.kingdomName = kingdomName;
        this.foundingYear = foundingYear;
        this.allowedStructureTypes = allowedStructureTypes.clone();
        this.resourceLimits = new HashMap<>(resourceLimits);
    }

    public String getKingdomName() {
        return kingdomName;
    }

    public int getFoundingYear() {
        return foundingYear;
    }

    public String[] getAllowedStructureTypes() {
        return allowedStructureTypes.clone();
    }

    public Map<String, Integer> getResourceLimits() {
        return new HashMap<>(resourceLimits);
    }

    public static KingdomConfig createDefaultKingdom() {
        return new KingdomConfig("Default Kingdom", 1000,
                new String[]{"WizardTower", "EnchantedCastle", "MysticLibrary", "DragonLair"},
                Map.of("Gold", 10000, "Wood", 5000, "Stone", 8000));
    }

    public static KingdomConfig createFromTemplate(String type) {
        switch (type.toLowerCase()) {
            case "fortress":
                return new KingdomConfig("Fortress Kingdom", 1200,
                        new String[]{"EnchantedCastle", "WizardTower"},
                        Map.of("Gold", 20000, "Wood", 10000, "Stone", 15000));
            case "arcane":
                return new KingdomConfig("Arcane Kingdom", 900,
                        new String[]{"WizardTower", "MysticLibrary"},
                        Map.of("Gold", 15000, "Wood", 7000, "Stone", 5000));
            default:
                return createDefaultKingdom();
        }
    }

    @Override
    public String toString() {
        return "KingdomConfig{" +
                "kingdomName='" + kingdomName + '\'' +
                ", foundingYear=" + foundingYear +
                ", allowedStructureTypes=" + Arrays.toString(allowedStructureTypes) +
                ", resourceLimits=" + resourceLimits +
                '}';
    }
}

class MagicalStructure {
    private final String structureId;
    private final long constructionTimestamp;
    private final String structureName;
    private final String location;

    private int magicPower;
    private boolean isActive;
    private String currentMaintainer;

    static final int MIN_MAGIC_POWER = 0;
    static final int MAX_MAGIC_POWER = 1000;

    public static final String MAGIC_SYSTEM_VERSION = "3.0";

    public MagicalStructure(String structureName, String location) {
        this(structureName, location, MIN_MAGIC_POWER, false);
    }

    public MagicalStructure(String structureName, String location, int magicPower) {
        this(structureName, location, magicPower, false);
    }

    public MagicalStructure(String structureName, String location, int magicPower, boolean isActive) {
        if (structureName == null || structureName.isBlank())
            throw new IllegalArgumentException("Structure name cannot be null or blank");
        if (location == null || location.isBlank())
            throw new IllegalArgumentException("Location cannot be null or blank");
        if (magicPower < MIN_MAGIC_POWER || magicPower > MAX_MAGIC_POWER)
            throw new IllegalArgumentException("Magic power must be between " + MIN_MAGIC_POWER + " and " + MAX_MAGIC_POWER);

        this.structureId = UUID.randomUUID().toString();
        this.constructionTimestamp = System.currentTimeMillis();
        this.structureName = structureName;
        this.location = location;
        this.magicPower = magicPower;
        this.isActive = isActive;
        this.currentMaintainer = "";
    }

    // JavaBean getters/setters

    public String getStructureId() {
        return structureId;
    }

    public long getConstructionTimestamp() {
        return constructionTimestamp;
    }

    public String getStructureName() {
        return structureName;
    }

    public String getLocation() {
        return location;
    }

    public int getMagicPower() {
        return magicPower;
    }

    public void setMagicPower(int magicPower) {
        if (magicPower < MIN_MAGIC_POWER || magicPower > MAX_MAGIC_POWER)
            throw new IllegalArgumentException("Magic power must be between " + MIN_MAGIC_POWER + " and " + MAX_MAGIC_POWER);
        this.magicPower = magicPower;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCurrentMaintainer() {
        return currentMaintainer;
    }

    public void setCurrentMaintainer(String currentMaintainer) {
        if (currentMaintainer == null)
            throw new IllegalArgumentException("Maintainer cannot be null");
        this.currentMaintainer = currentMaintainer;
    }

    @Override
    public String toString() {
        return "MagicalStructure{" +
                "structureId='" + structureId + '\'' +
                ", constructionTimestamp=" + new Date(constructionTimestamp) +
                ", structureName='" + structureName + '\'' +
                ", location='" + location + '\'' +
                ", magicPower=" + magicPower +
                ", isActive=" + isActive +
                ", currentMaintainer='" + currentMaintainer + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MagicalStructure)) return false;
        MagicalStructure other = (MagicalStructure) obj;
        return structureId.equals(other.structureId);
    }

    @Override
    public int hashCode() {
        return structureId.hashCode();
    }
}

class WizardTower extends MagicalStructure {
    private final int maxSpellCapacity;
    private final List<String> knownSpells;
    private String currentWizard;

    public WizardTower(String structureName, String location, int magicPower, boolean isActive,
                       int maxSpellCapacity, List<String> knownSpells, String currentWizard) {
        super(structureName, location, magicPower, isActive);
        if (maxSpellCapacity < 0)
            throw new IllegalArgumentException("maxSpellCapacity cannot be negative");
        if (knownSpells == null)
            throw new IllegalArgumentException("knownSpells cannot be null");
        if (currentWizard == null || currentWizard.isBlank())
            throw new IllegalArgumentException("currentWizard cannot be null or blank");

        this.maxSpellCapacity = maxSpellCapacity;
        this.knownSpells = new ArrayList<>(knownSpells);
        this.currentWizard = currentWizard;
    }

    public int getMaxSpellCapacity() {
        return maxSpellCapacity;
    }

    public List<String> getKnownSpells() {
        return Collections.unmodifiableList(knownSpells);
    }

    public String getCurrentWizard() {
        return currentWizard;
    }

    public void setCurrentWizard(String currentWizard) {
        if (currentWizard == null || currentWizard.isBlank())
            throw new IllegalArgumentException("currentWizard cannot be null or blank");
        this.currentWizard = currentWizard;
    }

    @Override
    public String toString() {
        return "WizardTower{" + super.toString() +
                ", maxSpellCapacity=" + maxSpellCapacity +
                ", knownSpells=" + knownSpells +
                ", currentWizard='" + currentWizard + '\'' +
                '}';
    }
}

class EnchantedCastle extends MagicalStructure {
    private final String castleType;
    private int defenseRating;
    private boolean hasDrawbridge;

    public EnchantedCastle(String structureName, String location, int magicPower, boolean isActive,
                           String castleType, int defenseRating, boolean hasDrawbridge) {
        super(structureName, location, magicPower, isActive);
        if (castleType == null || castleType.isBlank())
            throw new IllegalArgumentException("castleType cannot be null or blank");
        if (defenseRating < 0)
            throw new IllegalArgumentException("defenseRating cannot be negative");

        this.castleType = castleType;
        this.defenseRating = defenseRating;
        this.hasDrawbridge = hasDrawbridge;
    }

    public String getCastleType() {
        return castleType;
    }

    public int getDefenseRating() {
        return defenseRating;
    }

    public void setDefenseRating(int defenseRating) {
        if (defenseRating < 0)
            throw new IllegalArgumentException("defenseRating cannot be negative");
        this.defenseRating = defenseRating;
    }

    public boolean hasDrawbridge() {
        return hasDrawbridge;
    }

    public void setHasDrawbridge(boolean hasDrawbridge) {
        this.hasDrawbridge = hasDrawbridge;
    }

    @Override
    public String toString() {
        return "EnchantedCastle{" + super.toString() +
                ", castleType='" + castleType + '\'' +
                ", defenseRating=" + defenseRating +
                ", hasDrawbridge=" + hasDrawbridge +
                '}';
    }
}

class MysticLibrary extends MagicalStructure {
    private final Map<String, String> bookCollection;
    private int knowledgeLevel;

    public MysticLibrary(String structureName, String location, int magicPower, boolean isActive,
                         Map<String, String> bookCollection, int knowledgeLevel) {
        super(structureName, location, magicPower, isActive);
        if (bookCollection == null)
            throw new IllegalArgumentException("bookCollection cannot be null");
        if (knowledgeLevel < 0)
            throw new IllegalArgumentException("knowledgeLevel cannot be negative");

        this.bookCollection = new HashMap<>(bookCollection);
        this.knowledgeLevel = knowledgeLevel;
    }

    public Map<String, String> getBookCollection() {
        return Collections.unmodifiableMap(bookCollection);
    }

    public int getKnowledgeLevel() {
        return knowledgeLevel;
    }

    public void setKnowledgeLevel(int knowledgeLevel) {
        if (knowledgeLevel < 0)
            throw new IllegalArgumentException("knowledgeLevel cannot be negative");
        this.knowledgeLevel = knowledgeLevel;
    }

    @Override
    public String toString() {
        return "MysticLibrary{" + super.toString() +
                ", bookCollection=" + bookCollection +
                ", knowledgeLevel=" + knowledgeLevel +
                '}';
    }
}

class DragonLair extends MagicalStructure {
    private final String dragonType;
    private final long treasureValue;
    private int territorialRadius;

    public DragonLair(String structureName, String location, int magicPower, boolean isActive,
                      String dragonType, long treasureValue, int territorialRadius) {
        super(structureName, location, magicPower, isActive);
        if (dragonType == null || dragonType.isBlank())
            throw new IllegalArgumentException("dragonType cannot be null or blank");
        if (treasureValue < 0)
            throw new IllegalArgumentException("treasureValue cannot be negative");
        if (territorialRadius < 0)
            throw new IllegalArgumentException("territorialRadius cannot be negative");

        this.dragonType = dragonType;
        this.treasureValue = treasureValue;
        this.territorialRadius = territorialRadius;
    }

    public String getDragonType() {
        return dragonType;
    }

    public long getTreasureValue() {
        return treasureValue;
    }

    public int getTerritorialRadius() {
        return territorialRadius;
    }

    public void setTerritorialRadius(int territorialRadius) {
        if (territorialRadius < 0)
            throw new IllegalArgumentException("territorialRadius cannot be negative");
        this.territorialRadius = territorialRadius;
    }

    @Override
    public String toString() {
        return "DragonLair{" + super.toString() +
                ", dragonType='" + dragonType + '\'' +
                ", treasureValue=" + treasureValue +
                ", territorialRadius=" + territorialRadius +
                '}';
    }
}

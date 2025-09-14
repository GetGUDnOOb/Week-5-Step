import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

// --- Immutable PetSpecies class ---
final class PetSpecies {
    private final String speciesName;
    private final String[] evolutionStages;
    private final int maxLifespan;
    private final String habitat;

    public PetSpecies(String speciesName, String[] evolutionStages, int maxLifespan, String habitat) {
        if (speciesName == null || speciesName.isBlank()) {
            throw new IllegalArgumentException("Species name cannot be null/empty");
        }
        if (evolutionStages == null || evolutionStages.length == 0) {
            throw new IllegalArgumentException("Evolution stages must have at least one stage");
        }
        for (String stage : evolutionStages) {
            if (stage == null || stage.isBlank()) {
                throw new IllegalArgumentException("Evolution stage cannot be null/empty");
            }
        }
        if (maxLifespan <= 0) {
            throw new IllegalArgumentException("Max lifespan must be positive");
        }
        if (habitat == null || habitat.isBlank()) {
            throw new IllegalArgumentException("Habitat cannot be null/empty");
        }

        this.speciesName = speciesName;
        this.evolutionStages = evolutionStages.clone(); // defensive copy
        this.maxLifespan = maxLifespan;
        this.habitat = habitat;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public String[] getEvolutionStages() {
        return evolutionStages.clone(); // defensive copy
    }

    public int getMaxLifespan() {
        return maxLifespan;
    }

    public String getHabitat() {
        return habitat;
    }

    @Override
    public String toString() {
        return "PetSpecies{" +
                "speciesName='" + speciesName + '\'' +
                ", evolutionStages=" + Arrays.toString(evolutionStages) +
                ", maxLifespan=" + maxLifespan +
                ", habitat='" + habitat + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetSpecies)) return false;
        PetSpecies that = (PetSpecies) o;
        return maxLifespan == that.maxLifespan &&
                speciesName.equals(that.speciesName) &&
                Arrays.equals(evolutionStages, that.evolutionStages) &&
                habitat.equals(that.habitat);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(speciesName, maxLifespan, habitat);
        result = 31 * result + Arrays.hashCode(evolutionStages);
        return result;
    }
}

// --- Main VirtualPet class ---
public class VirtualPet {
    // Core immutable fields - private final
    private final String petId;
    private final PetSpecies species;
    private final long birthTimestamp;

    // Controlled mutable state - private
    private String petName;
    private int age;
    private int happiness;
    private int health;

    // Protected static final, package-accessible
    protected static final String[] DEFAULT_EVOLUTION_STAGES = {"Egg", "Baby", "Adult", "Elder"};

    // Package-private constants
    static final int MAX_HAPPINESS = 100;
    static final int MAX_HEALTH = 100;

    // Public static final
    public static final String PET_SYSTEM_VERSION = "2.0";

    // --- Constructors with chaining ---

    // 1. Default constructor - random pet with default species
    public VirtualPet() {
        this("Fluffy", new PetSpecies("DefaultSpecies", DEFAULT_EVOLUTION_STAGES, 10, "Forest"), 0, 50, 50);
    }

    // 2. Name only constructor - default species, moderate stats
    public VirtualPet(String petName) {
        this(petName, new PetSpecies("DefaultSpecies", DEFAULT_EVOLUTION_STAGES, 10, "Forest"), 0, 50, 50);
    }

    // 3. Name + species constructor - moderate stats
    public VirtualPet(String petName, PetSpecies species) {
        this(petName, species, 0, 50, 50);
    }

    // 4. Main constructor (full parameters with validation)
    public VirtualPet(String petName, PetSpecies species, int age, int happiness, int health) {
        this.petId = generatePetId();
        if (species == null) throw new IllegalArgumentException("Species cannot be null");
        this.species = species;
        this.birthTimestamp = System.currentTimeMillis();

        setPetName(petName);
        setAge(age);
        setHappiness(happiness);
        setHealth(health);
    }

    // --- Private helper methods ---
    private static String generatePetId() {
        return UUID.randomUUID().toString();
    }

    private void validateStat(int value, String statName) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException(statName + " must be between 0 and 100");
        }
    }

    private void modifyHappiness(int delta) {
        happiness = Math.min(MAX_HAPPINESS, Math.max(0, happiness + delta));
        updateEvolutionStage();
    }

    private void modifyHealth(int delta) {
        health = Math.min(MAX_HEALTH, Math.max(0, health + delta));
        updateEvolutionStage();
    }

    private void updateEvolutionStage() {
        // Simple example: increase age => evolve
        if (age >= species.getEvolutionStages().length) {
            age = species.getEvolutionStages().length - 1; // cap age evolution
        }
    }

    // --- Public interface methods ---
    public void feedPet(String foodType) {
        int bonus = calculateFoodBonus(foodType);
        modifyHappiness(bonus);
        modifyHealth(bonus / 2);
        System.out.println(petName + " was fed with " + foodType + ". Happiness +" + bonus);
    }

    public void playWithPet(String gameType) {
        int effect = calculateGameEffect(gameType);
        modifyHappiness(effect);
        modifyHealth(-effect / 3);
        System.out.println(petName + " played " + gameType + ". Happiness +" + effect);
    }

    // --- Protected internal methods ---
    protected int calculateFoodBonus(String foodType) {
        if (foodType == null) return 5;
        return switch (foodType.toLowerCase()) {
            case "fruit" -> 10;
            case "meat" -> 15;
            case "candy" -> 5;
            default -> 7;
        };
    }

    protected int calculateGameEffect(String gameType) {
        if (gameType == null) return 10;
        return switch (gameType.toLowerCase()) {
            case "fetch" -> 20;
            case "hideandseek" -> 15;
            case "puzzle" -> 10;
            default -> 5;
        };
    }

    // Package-private debugging method
    String getInternalState() {
        return "VirtualPet{" +
                "petId='" + petId + '\'' +
                ", species=" + species.getSpeciesName() +
                ", petName='" + petName + '\'' +
                ", age=" + age +
                ", happiness=" + happiness +
                ", health=" + health +
                '}';
    }

    // --- JavaBean compliant getters/setters ---
    public String getPetId() {
        return petId;
    }

    public PetSpecies getSpecies() {
        return species;
    }

    public long getBirthTimestamp() {
        return birthTimestamp;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        if (petName == null || petName.isBlank()) {
            throw new IllegalArgumentException("Pet name cannot be empty");
        }
        this.petName = petName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 0 || age > species.getEvolutionStages().length - 1) {
            throw new IllegalArgumentException("Age must be within evolution stages range");
        }
        this.age = age;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        validateStat(happiness, "Happiness");
        this.happiness = happiness;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        validateStat(health, "Health");
        this.health = health;
    }

    @Override
    public String toString() {
        return "VirtualPet{" +
                "petId='" + petId + '\'' +
                ", species=" + species.getSpeciesName() +
                ", petName='" + petName + '\'' +
                ", age=" + age + " (" + species.getEvolutionStages()[age] + ")" +
                ", happiness=" + happiness +
                ", health=" + health +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VirtualPet)) return false;
        VirtualPet that = (VirtualPet) o;
        return petId.equals(that.petId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(petId);
    }
}

// --- DragonPet specialized class ---
class DragonPet {
    private final String dragonType;
    private final String breathWeapon;

    // Composition - reference to VirtualPet
    private final VirtualPet virtualPet;

    public DragonPet(String dragonType, String breathWeapon, VirtualPet virtualPet) {
        if (dragonType == null || dragonType.isBlank()) {
            throw new IllegalArgumentException("Dragon type required");
        }
        if (breathWeapon == null || breathWeapon.isBlank()) {
            throw new IllegalArgumentException("Breath weapon required");
        }
        if (virtualPet == null) {
            throw new IllegalArgumentException("VirtualPet instance required");
        }
        this.dragonType = dragonType;
        this.breathWeapon = breathWeapon;
        this.virtualPet = virtualPet;
    }

    // JavaBean style getters (no setters for final fields)
    public String getDragonType() {
        return dragonType;
    }

    public String getBreathWeapon() {
        return breathWeapon;
    }

    public VirtualPet getVirtualPet() {
        return virtualPet;
    }

    @Override
    public String toString() {
        return "DragonPet{" +
                "dragonType='" + dragonType + '\'' +
                ", breathWeapon='" + breathWeapon + '\'' +
                ", virtualPet=" + virtualPet +
                '}';
    }
}

// --- RobotPet specialized class ---
class RobotPet {
    private boolean needsCharging;
    private int batteryLevel; // 0 - 100

    // Composition
    private final VirtualPet virtualPet;

    public RobotPet(boolean needsCharging, int batteryLevel, VirtualPet virtualPet) {
        if (virtualPet == null) {
            throw new IllegalArgumentException("VirtualPet instance required");
        }
        this.virtualPet = virtualPet;
        this.needsCharging = needsCharging;
        setBatteryLevel(batteryLevel);
    }

    // Getters and setters with validation
    public boolean isNeedsCharging() {
        return needsCharging;
    }

    public void setNeedsCharging(boolean needsCharging) {
        this.needsCharging = needsCharging;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        if (batteryLevel < 0 || batteryLevel > 100) {
            throw new IllegalArgumentException("Battery level must be 0-100");
        }
        this.batteryLevel = batteryLevel;
    }

    public VirtualPet getVirtualPet() {
        return virtualPet;
    }

    @Override
    public String toString() {
        return "RobotPet{" +
                "needsCharging=" + needsCharging +
                ", batteryLevel=" + batteryLevel +
                ", virtualPet=" + virtualPet +
                '}';
    }
}

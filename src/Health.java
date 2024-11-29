public class Health {
    private int currentHealth;
    private int maxHealth;
    private HealthChangeListener healthChangeListener;

    // Constructor to initialize health with a given max health
    public Health(int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth; // Start with full health
    }

    // Getter for current health
    public int getCurrentHealth() {
        return currentHealth;
    }

    // Getter for max health
    public int getMaxHealth() {
        return maxHealth;
    }

    // Method to change health (e.g., after taking damage)
    public void changeHealth(int amount) {
        currentHealth += amount;
        // Ensure health is within bounds (0 to maxHealth)
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        } else if (currentHealth < 0) {
            currentHealth = 0;
        }

        // Notify listener if there is one
        if (healthChangeListener != null) {
            healthChangeListener.onHealthChanged(currentHealth);
        }
    }

    // Set a listener for health changes
    public void setHealthChangeListener(HealthChangeListener listener) {
        this.healthChangeListener = listener;
    }

    // Health change listener interface
    public interface HealthChangeListener {
        void onHealthChanged(int newHealth);
    }
}

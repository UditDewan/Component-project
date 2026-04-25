import java.util.HashMap;
import java.util.Map;

/**
 * Proof-of-Concept for an RPG GameInventory component.
 *
 */
public class GameInventoryMVP {

    // Representation: item name -> quantity
    private Map<String, Integer> items;

    /**
     * Constructor initializes empty inventory.
     */
    public GameInventoryMVP() {
        this.items = new HashMap<>();
    }

    /**
     * Adds a quantity of an item to the inventory.
     */
    public void addItem(String item, int quantity) {
        if (quantity <= 0) {
            return;
        }
        this.items.put(item, this.getQuantity(item) + quantity);
    }

    /**
     * Removes a quantity of an item.
     */
    public void removeItem(String item, int quantity) {
        if (!this.items.containsKey(item) || quantity <= 0) {
            return;
        }

        int current = this.items.get(item);
        if (quantity >= current) {
            this.items.remove(item);
        } else {
            this.items.put(item, current - quantity);
        }
    }

    /**
     * Returns quantity of an item.
     */
    public int getQuantity(String item) {
        return this.items.getOrDefault(item, 0);
    }

    /**
     * Returns whether inventory contains the item.
     */
    public boolean hasItem(String item) {
        return this.getQuantity(item) > 0;
    }

    /**
     * Transfers items to another inventory.
     */
    public void transferItem(GameInventoryMVP other, String item, int quantity) {
        if (this.getQuantity(item) >= quantity) {
            this.removeItem(item, quantity);
            other.addItem(item, quantity);
        }
    }

    /**
     * Returns total number of all items combined.
     */
    public int totalItems() {
        int total = 0;
        for (int qty : this.items.values()) {
            total += qty;
        }
        return total;
    }

    /**
     * Displays inventory contents.
     */
    public void displayInventory() {
        System.out.println("Inventory Contents:");
        for (Map.Entry<String, Integer> entry : this.items.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("Total Items: " + this.totalItems());
        System.out.println();
    }

    /**
     * Main method demonstrating proof-of-concept.
     */
    public static void main(String[] args) {

        GameInventoryMVP player = new GameInventoryMVP();
        GameInventoryMVP chest = new GameInventoryMVP();

        // Add items
        player.addItem("Health Potion", 5);
        player.addItem("Iron Sword", 1);
        player.addItem("Gold Coin", 100);

        System.out.println("Player inventory after adding items:");
        player.displayInventory();

        // Remove items
        player.removeItem("Gold Coin", 25);
        System.out.println("Player inventory after spending gold:");
        player.displayInventory();

        // Transfer items
        player.transferItem(chest, "Health Potion", 2);

        System.out.println("Player inventory after transferring potions:");
        player.displayInventory();

        System.out.println("Chest inventory after receiving potions:");
        chest.displayInventory();

        // Demonstrate hasItem
        System.out.println("Does player have Iron Sword? " + player.hasItem("Iron Sword"));
        System.out.println("Does player have Diamond? " + player.hasItem("Diamond"));
    }
}

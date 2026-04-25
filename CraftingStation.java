package components.gameinventory;

/**
 * Use case #2: {@code CraftingStation} — a small component whose
 * representation <em>contains</em> a {@link GameInventory}.
 *
 * <p>
 * A {@code CraftingStation} models a single recipe: a set of required
 * ingredients (an item name mapped to the quantity needed) plus an output
 * item and output quantity. Given a player's inventory, the station can
 * report whether the player has enough of every ingredient, and if so,
 * consume those ingredients and deposit the crafted output.
 * </p>
 *
 * <p>
 * This demonstrates the "component as a building block" pattern mentioned
 * in the portfolio assignment: the crafting station does not reinvent
 * item-quantity bookkeeping — it reuses {@code GameInventory} for both its
 * internal recipe representation and the player's inventory parameter.
 * </p>
 */
public final class CraftingStation {

    /**
     * Required ingredients, stored as a GameInventory whose quantities mean
     * "you must have at least this many of this item".
     */
    private final GameInventory recipe;

    /** Item produced by one successful craft. */
    private final String outputItem;

    /** Quantity of {@link #outputItem} produced per successful craft. */
    private final int outputQuantity;

    /**
     * Creates a new crafting station for a recipe with no ingredients yet.
     *
     * @param outputItem
     *            the item produced by this station
     * @param outputQuantity
     *            how many of {@code outputItem} are produced per craft
     */
    public CraftingStation(String outputItem, int outputQuantity) {
        assert outputItem != null : "Violation of: outputItem is not null";
        assert outputItem.length() > 0 : "Violation of: |outputItem| > 0";
        assert outputQuantity > 0 : "Violation of: outputQuantity > 0";
        this.outputItem = outputItem;
        this.outputQuantity = outputQuantity;
        this.recipe = new GameInventory1L();
    }

    /**
     * Adds (or increases) an ingredient requirement for this recipe.
     *
     * @param ingredient
     *            the ingredient item name
     * @param quantity
     *            how much of {@code ingredient} the recipe requires
     */
    public void addIngredient(String ingredient, int quantity) {
        assert ingredient != null && ingredient.length() > 0
                : "Violation of: |ingredient| > 0";
        assert quantity > 0 : "Violation of: quantity > 0";
        this.recipe.addItem(ingredient, quantity);
    }

    /**
     * Reports whether {@code playerInventory} has enough of every required
     * ingredient to craft once.
     *
     * @param playerInventory
     *            the inventory to check
     * @return true iff every ingredient required by this recipe is present in
     *         {@code playerInventory} with at least the required quantity
     */
    public boolean canCraft(GameInventory playerInventory) {
        assert playerInventory != null
                : "Violation of: playerInventory is not null";
        for (String ingredient : this.recipe) {
            int needed = this.recipe.getQuantity(ingredient);
            if (playerInventory.getQuantity(ingredient) < needed) {
                return false;
            }
        }
        return true;
    }

    /**
     * Attempts one craft. If {@code playerInventory} contains every required
     * ingredient, consumes exactly the required quantities and deposits the
     * output; otherwise leaves {@code playerInventory} unchanged.
     *
     * @param playerInventory
     *            the inventory to craft against
     * @return true iff the craft succeeded
     */
    public boolean craft(GameInventory playerInventory) {
        assert playerInventory != null
                : "Violation of: playerInventory is not null";
        if (!this.canCraft(playerInventory)) {
            return false;
        }
        for (String ingredient : this.recipe) {
            int needed = this.recipe.getQuantity(ingredient);
            playerInventory.removeItem(ingredient, needed);
        }
        playerInventory.addItem(this.outputItem, this.outputQuantity);
        return true;
    }

    /**
     * Reports the output item name.
     *
     * @return the output item
     */
    public String getOutputItem() {
        return this.outputItem;
    }

    /*
     * Demo -------------------------------------------------------------------
     */

    /**
     * Runs a small crafting scenario.
     *
     * @param args
     *            command-line arguments (ignored)
     */
    public static void main(String[] args) {
        // Recipe: 2 Iron Ingot + 1 Wood Plank -> 1 Iron Sword
        CraftingStation forge = new CraftingStation("Iron Sword", 1);
        forge.addIngredient("Iron Ingot", 2);
        forge.addIngredient("Wood Plank", 1);

        GameInventory player = new GameInventory1L();
        player.addItem("Iron Ingot", 5);
        player.addItem("Wood Plank", 3);
        player.addItem("Gold", 20);

        System.out.println("Before:           " + player);
        System.out.println("Can craft?        " + forge.canCraft(player));

        System.out.println("Craft #1 success: " + forge.craft(player));
        System.out.println("After craft #1:   " + player);

        System.out.println("Craft #2 success: " + forge.craft(player));
        System.out.println("After craft #2:   " + player);

        // After two crafts: 5-4 = 1 Iron Ingot, 3-2 = 1 Wood Plank, 2 Swords.
        // The recipe needs 2 Iron Ingots, so the third attempt should fail.
        System.out.println("Craft #3 success: " + forge.craft(player));
        System.out.println("Final:            " + player);
    }
}

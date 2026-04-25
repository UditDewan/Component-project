package components.gameinventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Use case #1: a scripted RPG trading scene.
 *
 * <p>
 * This demo exercises the full {@link GameInventory} API — kernel mutators,
 * secondary queries, transfers, and merges — by narrating a short sequence:
 * a player loots a chest, visits a shopkeeper to sell potions for gold, and
 * collects a quest reward. Output is printed to {@code System.out} so the
 * before/after state of each inventory is visible.
 * </p>
 *
 * <p>
 * This is a command-line demo: no GUI, no input — just
 * {@code public static void main}.
 * </p>
 */
public final class InventoryTradingDemo {

    /** No instances; utility class. */
    private InventoryTradingDemo() {
    }

    /**
     * Prints a labeled snapshot of an inventory.
     */
    private static void show(String label, GameInventory inv) {
        System.out.println("  " + label + "  ("
                + inv.size() + " distinct, "
                + inv.totalItems() + " total)");
        System.out.println("    " + inv);
    }

    /**
     * Snapshots the keys currently in {@code src} into a list so we can
     * iterate over them while mutating {@code src}.
     */
    private static List<String> keySnapshot(GameInventory src) {
        List<String> names = new ArrayList<>();
        for (String name : src) {
            names.add(name);
        }
        return names;
    }

    /**
     * Entry point for the trading demo.
     *
     * @param args
     *            command-line arguments (ignored)
     */
    public static void main(String[] args) {
        GameInventory player = new GameInventory1L();
        GameInventory chest = new GameInventory1L();
        GameInventory shopkeeper = new GameInventory1L();
        GameInventory questReward = new GameInventory1L();

        // --- Scene 1: starting state -----------------------------------
        player.addItem("Gold", 50);
        player.addItem("Health Potion", 2);
        player.addItem("Iron Sword", 1);

        chest.addItem("Gold", 120);
        chest.addItem("Health Potion", 6);
        chest.addItem("Diamond", 1);

        System.out.println("== Scene 1: starting out ==");
        show("Player", player);
        show("Chest",  chest);
        System.out.println();

        // --- Scene 2: loot the chest -----------------------------------
        System.out.println("== Scene 2: looting the chest ==");
        for (String name : keySnapshot(chest)) {
            int qty = chest.getQuantity(name);
            chest.transferItem(player, name, qty);
        }
        show("Player", player);
        show("Chest",  chest);
        System.out.println();

        // --- Scene 3: sell potions to the shopkeeper -------------------
        System.out.println("== Scene 3: trading with shopkeeper ==");
        shopkeeper.addItem("Gold", 500);
        shopkeeper.addItem("Steel Shield", 1);

        int sellQty = 4;
        int pricePerPotion = 10;
        if (player.getQuantity("Health Potion") >= sellQty
                && shopkeeper.getQuantity("Gold") >= sellQty * pricePerPotion) {
            player.transferItem(shopkeeper, "Health Potion", sellQty);
            shopkeeper.transferItem(player, "Gold", sellQty * pricePerPotion);
            System.out.println("  Sold " + sellQty + " Health Potions for "
                    + sellQty * pricePerPotion + " Gold.");
        }
        show("Player",     player);
        show("Shopkeeper", shopkeeper);
        System.out.println();

        // --- Scene 4: claim the quest reward ---------------------------
        System.out.println("== Scene 4: claiming the quest reward ==");
        questReward.addItem("Gold", 75);
        questReward.addItem("Elixir", 2);
        player.mergeFrom(questReward);
        show("Player",        player);
        show("Quest reward",  questReward);
        System.out.println();

        // --- Epilogue --------------------------------------------------
        System.out.println("== Epilogue ==");
        System.out.println("  Carrying " + player.totalItems()
                + " items across " + player.size() + " stacks.");
        if (player.size() > 0) {
            String top = player.mostAbundantItem();
            System.out.println("  Most abundant stack: "
                    + top + " (" + player.getQuantity(top) + ")");
        }
        if (player.hasItem("Diamond")) {
            System.out.println("  Still carrying that Diamond from the chest!");
        }
    }
}

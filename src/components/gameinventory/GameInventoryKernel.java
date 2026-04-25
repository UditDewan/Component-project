package components.gameinventory;

import components.standard.Standard;

/**
 * {@code GameInventoryKernel} kernel interface for the {@code GameInventory}
 * component type.
 *
 * <p>
 * A {@code GameInventory} models a finite collection of named items, each
 * associated with a positive integer quantity. An item is considered present in
 * the inventory only when its quantity is greater than zero.
 * </p>
 *
 * @mathmodel type GameInventory is modeled by
 *            finite partial function (String -> int)
 *            where for every key k in domain(this), this[k] > 0
 * @initially |this| = 0
 *
 * @iterator provides a {@code String} iterator over the names of all items
 *            currently in this inventory in no particular order; each item name
 *            appears exactly once
 */
public interface GameInventoryKernel
        extends Standard<GameInventory>, Iterable<String> {

    /**
     * Adds {@code quantity} of {@code item} to {@code this}.
     *
     * <p>
     * If {@code item} is already present, its existing quantity is increased by
     * {@code quantity}. Otherwise, {@code item} is added as a new entry with
     * the given quantity.
     * </p>
     *
     * @param item
     *            the name of the item to add
     * @param quantity
     *            the number of {@code item} to add
     * @updates this
     * @requires |item| > 0 and quantity > 0
     * @ensures this[item] = #this[item] + quantity
     *          and for all k /= item in domain(#this), this[k] = #this[k]
     */
    void addItem(String item, int quantity);

    /**
     * Removes {@code quantity} of {@code item} from {@code this}.
     *
     * <p>
     * If removing exactly {@code quantity} reduces the item's total to zero,
     * the entry is removed entirely from the inventory.
     * </p>
     *
     * @param item
     *            the name of the item to remove
     * @param quantity
     *            the number of {@code item} to remove
     * @updates this
     * @requires |item| > 0 and quantity > 0 and quantity <= this[item]
     * @ensures if #this[item] = quantity then item is not in domain(this)
     *          else this[item] = #this[item] - quantity
     *          and for all k /= item in domain(#this), this[k] = #this[k]
     */
    void removeItem(String item, int quantity);

    /**
     * Reports the quantity of {@code item} in {@code this}.
     *
     * @param item
     *            the name of the item to look up
     * @return the quantity of {@code item} in {@code this}, or 0 if
     *         {@code item} is not present
     * @restores this
     * @requires |item| > 0
     * @ensures getQuantity =
     *              (item is in domain(this) ?  this[item] : 0)
     */
    int getQuantity(String item);

    /**
     * Reports the number of distinct item types in {@code this}.
     *
     * @return the number of distinct item names currently in {@code this}
     * @restores this
     * @ensures size = |domain(this)|
     */
    int size();

}

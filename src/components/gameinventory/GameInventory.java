package components.gameinventory;

/**
 * {@code GameInventory} enhanced interface, layering secondary methods on top
 * of {@link GameInventoryKernel}.
 *
 * <p>
 * All secondary methods in this interface can be implemented solely using the
 * kernel methods {@code addItem}, {@code removeItem}, {@code getQuantity},
 * {@code size}, and the {@code Standard} methods inherited from
 * {@link GameInventoryKernel}. No direct access to the underlying
 * representation is needed or permitted.
 * </p>
 */
public interface GameInventory extends GameInventoryKernel {

    /**
     * Reports whether {@code this} contains at least one of {@code item}.
     *
     * @param item
     *            the name of the item to check
     * @return true iff {@code item} is in {@code domain(this)}
     * @restores this
     * @requires |item| > 0
     * @ensures hasItem = (item is in domain(this))
     */
    boolean hasItem(String item);

    /**
     * Reports the total quantity of all items combined in {@code this}.
     *
     * @return the sum of quantities for every item in {@code this}
     * @restores this
     * @ensures totalItems = sum of this[k] for all k in domain(this)
     */
    int totalItems();

    /**
     * Reports the name of the item with the highest quantity in {@code this}.
     *
     * <p>
     * If multiple items share the maximum quantity, any one of their names may
     * be returned.
     * </p>
     *
     * @return the name of the item whose quantity is the maximum in
     *         {@code this}
     * @restores this
     * @requires |domain(this)| > 0
     * @ensures mostAbundantItem is in domain(this) and
     *          for all k in domain(this),
     *              this[mostAbundantItem] >= this[k]
     */
    String mostAbundantItem();

    /**
     * Transfers {@code quantity} of {@code item} from {@code this} to
     * {@code other}.
     *
     * <p>
     * If transferring the full quantity reduces {@code this[item]} to zero,
     * that entry is removed from {@code this}. The corresponding quantity in
     * {@code other} is increased (or the item is added if not yet present).
     * </p>
     *
     * @param other
     *            the destination inventory to receive the items
     * @param item
     *            the name of the item to transfer
     * @param quantity
     *            the number of {@code item} to transfer
     * @updates this, other
     * @requires other /= this and |item| > 0 and quantity > 0
     *           and quantity <= this[item]
     * @ensures this[item] = #this[item] - quantity
     *          (or item is removed from this if #this[item] = quantity)
     *          and other[item] = #other[item] + quantity
     *          and for all k /= item in domain(#this), this[k] = #this[k]
     *          and for all k /= item in domain(#other), other[k] = #other[k]
     */
    void transferItem(GameInventory other, String item, int quantity);

    /**
     * Merges all items from {@code source} into {@code this}.
     *
     * <p>
     * For every item in {@code source}, its quantity is added to the
     * corresponding quantity in {@code this}. After the merge, {@code source}
     * is empty.
     * </p>
     *
     * @param source
     *            the inventory whose items are merged into {@code this}
     * @updates this
     * @clears source
     * @requires source /= this
     * @ensures for all k in domain(#source),
     *              this[k] = #this[k] + #source[k]
     *          and for all k in domain(#this) \ domain(#source),
     *              this[k] = #this[k]
     *          and |domain(source)| = 0
     */
    void mergeFrom(GameInventory source);

}

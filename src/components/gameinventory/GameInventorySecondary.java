package components.gameinventory;

import java.util.Iterator;

/**
 * {@code GameInventorySecondary} is a layered implementation of the secondary
 * methods of {@link GameInventory}.
 *
 * <p>
 * Every method in this class is implemented using only the kernel methods
 * ({@code addItem}, {@code removeItem}, {@code getQuantity}, {@code size}) and
 * the iteration provided by {@code Iterable<String>}. The abstract class does
 * not know or depend on the underlying representation, so a concrete
 * implementation (e.g. {@code GameInventory1L}) need only implement the kernel
 * methods and the {@code Standard} methods ({@code newInstance}, {@code clear},
 * {@code transferFrom}).
 * </p>
 *
 * @author (your name)
 */
public abstract class GameInventorySecondary implements GameInventory {

    /*
     * Common methods (from Object) --------------------------------------------
     */

    /**
     * Returns a string representation of this inventory in the form
     * {@code {item1=qty1, item2=qty2, ...}}. Items appear in iterator order.
     *
     * @return a string representation of this inventory
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (String item : this) {
            if (!first) {
                sb.append(", ");
            }
            // Iterator yields item names that are in domain(this), so by the
            // inventory invariant |item| > 0. getQuantity's precondition holds.
            sb.append(item).append("=").append(this.getQuantity(item));
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Two inventories are equal iff they contain exactly the same items with
     * exactly the same quantities.
     *
     * @param obj
     *            the object to compare with
     * @return true iff {@code obj} is a {@code GameInventory} with the same
     *         contents as this
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GameInventory)) {
            return false;
        }
        GameInventory other = (GameInventory) obj;
        // size() has no precondition, so this is always safe.
        if (this.size() != other.size()) {
            return false;
        }
        // Since both sizes are equal, if every item of this has a matching
        // quantity in other, the two are equal.
        for (String item : this) {
            // |item| > 0 by the inventory invariant (this's iterator). That
            // also satisfies other.getQuantity's precondition (|item| > 0).
            if (this.getQuantity(item) != other.getQuantity(item)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Hash code that is consistent with {@link #equals(Object)}: equal
     * inventories produce equal hash codes.
     *
     * @return a hash code for this inventory
     */
    @Override
    public int hashCode() {
        int hash = 0;
        for (String item : this) {
            // Order-independent: two equal inventories iterate in possibly
            // different orders but produce the same sum.
            // |item| > 0 by the iterator invariant, so getQuantity is safe.
            hash += item.hashCode() * this.getQuantity(item);
        }
        return hash;
    }

    /*
     * Secondary methods (from GameInventory) ----------------------------------
     */

    @Override
    public final boolean hasItem(String item) {
        assert item != null : "Violation of: item is not null";
        assert item.length() > 0 : "Violation of: |item| > 0";

        // getQuantity precondition |item| > 0 satisfied by assertion above.
        return this.getQuantity(item) > 0;
    }

    @Override
    public final int totalItems() {
        int total = 0;
        for (String item : this) {
            // |item| > 0 by inventory invariant (iterator only yields items
            // currently in this inventory).
            total += this.getQuantity(item);
        }
        return total;
    }

    @Override
    public final String mostAbundantItem() {
        assert this.size() > 0 : "Violation of: |domain(this)| > 0";

        String best = null;
        int bestQty = 0;
        for (String item : this) {
            // |item| > 0 by iterator invariant, so getQuantity is safe.
            int qty = this.getQuantity(item);
            if (best == null || qty > bestQty) {
                best = item;
                bestQty = qty;
            }
        }
        // Loop executed at least once because size() > 0, so best != null.
        return best;
    }

    @Override
    public final void transferItem(GameInventory other, String item,
            int quantity) {
        assert other != null : "Violation of: other is not null";
        assert other != this : "Violation of: other /= this";
        assert item != null : "Violation of: item is not null";
        assert item.length() > 0 : "Violation of: |item| > 0";
        assert quantity > 0 : "Violation of: quantity > 0";
        assert this.getQuantity(item) >= quantity
                : "Violation of: quantity <= this[item]";

        // removeItem preconditions: |item| > 0 (asserted), quantity > 0
        // (asserted), quantity <= this[item] (asserted). All satisfied.
        this.removeItem(item, quantity);

        // addItem preconditions: |item| > 0 (asserted) and quantity > 0
        // (asserted). Both satisfied.
        other.addItem(item, quantity);
    }

    @Override
    public final void mergeFrom(GameInventory source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source /= this";

        /*
         * Drain source one item at a time. We cannot iterate source while also
         * removing from it (the iterator would be invalidated), so we grab a
         * fresh iterator each pass, pull one name, then remove it entirely
         * from source and add it to this.
         */
        while (source.size() > 0) {
            Iterator<String> it = source.iterator();
            String item = it.next();
            // |item| > 0 by source's inventory invariant.
            int qty = source.getQuantity(item);

            // removeItem preconditions: |item| > 0 (invariant), qty > 0 (by
            // inventory invariant: every present item has quantity > 0), and
            // qty <= source[item] because qty IS source[item].
            source.removeItem(item, qty);

            // addItem preconditions: |item| > 0 (invariant) and qty > 0
            // (invariant). Both satisfied.
            this.addItem(item, qty);
        }
    }

}

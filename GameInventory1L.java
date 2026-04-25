package components.gameinventory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * {@code GameInventory1L} is a kernel implementation of {@link GameInventory},
 * layered thinly over {@link java.util.HashMap}.
 *
 * <p>
 * <b>Representation:</b> a {@code Map<String, Integer>} that holds one entry
 * per distinct item currently in the inventory.
 * </p>
 *
 * <p>
 * <b>Convention (representation invariant):</b>
 * <ul>
 * <li>{@code this.items != null}</li>
 * <li>for every key {@code k} in {@code this.items.keySet()},
 *     {@code k != null} and {@code k.length() > 0}</li>
 * <li>for every key {@code k} in {@code this.items.keySet()},
 *     {@code this.items.get(k) != null} and {@code this.items.get(k) > 0}</li>
 * </ul>
 * In short: all keys are non-empty strings and all values are strictly
 * positive integers. No zero- or negative-quantity entries are ever stored;
 * when a removal would drop an item's count to zero, the entry is deleted.
 * </p>
 *
 * <p>
 * <b>Correspondence (abstraction function):</b> the abstract inventory
 * modeled by {@code this} is the finite partial function whose domain is
 * {@code this.items.keySet()}, where for every key {@code k} in that domain,
 * {@code this[k] = this.items.get(k)}. Items whose names are not keys of
 * {@code this.items} are not in the abstract inventory.
 * </p>
 *
 * @author (your name)
 */
public class GameInventory1L extends GameInventorySecondary {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Representation of {@code this}.
     */
    private Map<String, Integer> items;

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {
        this.items = new HashMap<>();
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public GameInventory1L() {
        this.createNewRep();
    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @Override
    public final GameInventory newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(GameInventory source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof GameInventory1L
                : "Violation of: source is of dynamic type GameInventory1L";

        /*
         * The cast is safe because of the instanceof assertion above. We swap
         * representations: this takes ownership of source's map in O(1), and
         * source gets a fresh empty map.
         */
        GameInventory1L localSource = (GameInventory1L) source;
        this.items = localSource.items;
        localSource.createNewRep();
    }

    /*
     * Kernel methods ---------------------------------------------------------
     */

    @Override
    public final void addItem(String item, int quantity) {
        assert item != null : "Violation of: item is not null";
        assert item.length() > 0 : "Violation of: |item| > 0";
        assert quantity > 0 : "Violation of: quantity > 0";

        int current = this.items.getOrDefault(item, 0);
        this.items.put(item, current + quantity);
    }

    @Override
    public final void removeItem(String item, int quantity) {
        assert item != null : "Violation of: item is not null";
        assert item.length() > 0 : "Violation of: |item| > 0";
        assert quantity > 0 : "Violation of: quantity > 0";
        assert this.items.containsKey(item)
                : "Violation of: item is in domain(this)";
        assert quantity <= this.items.get(item)
                : "Violation of: quantity <= this[item]";

        int current = this.items.get(item);
        if (current == quantity) {
            /*
             * Removing all of this item preserves the convention: the entry
             * is deleted rather than left with value 0.
             */
            this.items.remove(item);
        } else {
            this.items.put(item, current - quantity);
        }
    }

    @Override
    public final int getQuantity(String item) {
        assert item != null : "Violation of: item is not null";
        assert item.length() > 0 : "Violation of: |item| > 0";

        return this.items.getOrDefault(item, 0);
    }

    @Override
    public final int size() {
        return this.items.size();
    }

    /*
     * Iterable<String> -------------------------------------------------------
     */

    @Override
    public Iterator<String> iterator() {
        /*
         * Wrap the keySet iterator so that callers cannot invoke remove() on
         * it. Allowing that would let a client delete an item without going
         * through removeItem, bypassing the kernel and breaking information
         * hiding. hasNext/next delegate to the underlying iterator.
         */
        return new Iterator<String>() {

            /**
             * The wrapped iterator over the map's keys.
             */
            private final Iterator<String> inner = GameInventory1L.this.items
                    .keySet().iterator();

            @Override
            public boolean hasNext() {
                return this.inner.hasNext();
            }

            @Override
            public String next() {
                return this.inner.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException(
                        "remove operation not supported");
            }
        };
    }

}

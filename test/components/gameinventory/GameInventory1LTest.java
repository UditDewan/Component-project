package components.gameinventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

/**
 * JUnit tests for {@link GameInventory1L}, focused on the constructor, kernel
 * methods, iteration, and Standard methods.
 *
 * <p>
 * Tests of non-mutating methods also verify that the inventory is left
 * unchanged, usually by comparing against a separately-built "expected"
 * inventory.
 * </p>
 */
public class GameInventory1LTest {

    /*
     * Constructor / initial state --------------------------------------------
     */

    @Test
    public void testConstructorIsEmpty() {
        GameInventory inv = new GameInventory1L();
        assertEquals(0, inv.size());
        assertEquals(0, inv.getQuantity("Anything"));
    }

    /*
     * addItem ----------------------------------------------------------------
     */

    @Test
    public void testAddItemNewEntry() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Potion", 3);
        assertEquals(1, inv.size());
        assertEquals(3, inv.getQuantity("Potion"));
    }

    @Test
    public void testAddItemExistingEntryAccumulates() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Potion", 3);
        inv.addItem("Potion", 5);
        assertEquals(1, inv.size());
        assertEquals(8, inv.getQuantity("Potion"));
    }

    @Test
    public void testAddItemMultipleDistinct() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Sword", 1);
        inv.addItem("Shield", 1);
        inv.addItem("Helmet", 1);
        assertEquals(3, inv.size());
        assertEquals(1, inv.getQuantity("Sword"));
        assertEquals(1, inv.getQuantity("Shield"));
        assertEquals(1, inv.getQuantity("Helmet"));
    }

    /*
     * removeItem -------------------------------------------------------------
     */

    @Test
    public void testRemoveItemPartial() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Gold", 100);
        inv.removeItem("Gold", 25);
        assertEquals(75, inv.getQuantity("Gold"));
        assertEquals(1, inv.size());
    }

    @Test
    public void testRemoveItemAllDeletesEntry() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Arrow", 12);
        inv.removeItem("Arrow", 12);
        assertEquals(0, inv.getQuantity("Arrow"));
        assertEquals(0, inv.size());
    }

    @Test
    public void testRemoveItemLastOneDeletesEntry() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Key", 1);
        inv.removeItem("Key", 1);
        assertEquals(0, inv.size());
    }

    @Test
    public void testRemoveItemDoesNotTouchOthers() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Gold", 10);
        inv.addItem("Arrow", 20);
        inv.removeItem("Gold", 5);
        assertEquals(5, inv.getQuantity("Gold"));
        assertEquals(20, inv.getQuantity("Arrow"));
        assertEquals(2, inv.size());
    }

    /*
     * getQuantity ------------------------------------------------------------
     */

    @Test
    public void testGetQuantityMissingReturnsZero() {
        GameInventory inv = new GameInventory1L();
        assertEquals(0, inv.getQuantity("Nothing"));
    }

    @Test
    public void testGetQuantityPresent() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Mushroom", 4);
        assertEquals(4, inv.getQuantity("Mushroom"));
    }

    @Test
    public void testGetQuantityRestoresState() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Mushroom", 4);
        GameInventory expected = new GameInventory1L();
        expected.addItem("Mushroom", 4);
        inv.getQuantity("Mushroom");
        inv.getQuantity("Nonexistent");
        assertEquals(expected, inv);
    }

    /*
     * size -------------------------------------------------------------------
     */

    @Test
    public void testSizeEmpty() {
        assertEquals(0, new GameInventory1L().size());
    }

    @Test
    public void testSizeOne() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Bread", 7);
        assertEquals(1, inv.size());
    }

    @Test
    public void testSizeCountsDistinctKeysNotTotalQuantity() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Bread", 100);
        inv.addItem("Water", 50);
        // 150 items total, but 2 distinct keys.
        assertEquals(2, inv.size());
    }

    @Test
    public void testSizeMany() {
        GameInventory inv = new GameInventory1L();
        for (int i = 0; i < 20; i++) {
            inv.addItem("item" + i, i + 1);
        }
        assertEquals(20, inv.size());
    }

    /*
     * iterator ---------------------------------------------------------------
     */

    @Test
    public void testIteratorEmpty() {
        assertFalse(new GameInventory1L().iterator().hasNext());
    }

    @Test
    public void testIteratorYieldsEveryKeyExactlyOnce() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("a", 1);
        inv.addItem("b", 2);
        inv.addItem("c", 3);
        Set<String> seen = new HashSet<>();
        for (String name : inv) {
            assertTrue("duplicate key: " + name, seen.add(name));
        }
        Set<String> expected = new HashSet<>();
        expected.add("a");
        expected.add("b");
        expected.add("c");
        assertEquals(expected, seen);
    }

    @Test
    public void testIteratorRemoveThrows() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("x", 1);
        Iterator<String> it = inv.iterator();
        it.next();
        try {
            it.remove();
            fail("iterator.remove() should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            // expected
        }
    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @Test
    public void testNewInstanceReturnsEmpty() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Torch", 2);
        GameInventory fresh = inv.newInstance();
        assertEquals(0, fresh.size());
    }

    @Test
    public void testNewInstanceDoesNotMutateReceiver() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Torch", 2);
        GameInventory expected = new GameInventory1L();
        expected.addItem("Torch", 2);
        inv.newInstance();
        assertEquals(expected, inv);
    }

    @Test
    public void testNewInstanceDynamicType() {
        GameInventory inv = new GameInventory1L();
        GameInventory fresh = inv.newInstance();
        assertTrue(fresh instanceof GameInventory1L);
    }

    @Test
    public void testClearEmptiesInventory() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("A", 1);
        inv.addItem("B", 2);
        inv.clear();
        assertEquals(0, inv.size());
        assertFalse(inv.hasItem("A"));
        assertFalse(inv.hasItem("B"));
    }

    @Test
    public void testClearOnAlreadyEmpty() {
        GameInventory inv = new GameInventory1L();
        inv.clear();
        assertEquals(0, inv.size());
    }

    @Test
    public void testTransferFromMovesContents() {
        GameInventory dest = new GameInventory1L();
        GameInventory src = new GameInventory1L();
        src.addItem("Coin", 10);
        src.addItem("Map", 1);
        dest.transferFrom(src);
        assertEquals(10, dest.getQuantity("Coin"));
        assertEquals(1, dest.getQuantity("Map"));
        assertEquals(2, dest.size());
        assertEquals(0, src.size());
    }

    @Test
    public void testTransferFromOverwritesDestinationContents() {
        GameInventory dest = new GameInventory1L();
        dest.addItem("Old", 99);
        GameInventory src = new GameInventory1L();
        src.addItem("New", 1);
        dest.transferFrom(src);
        assertEquals(0, dest.getQuantity("Old"));
        assertEquals(1, dest.getQuantity("New"));
        assertEquals(1, dest.size());
        assertEquals(0, src.size());
    }

    @Test
    public void testTransferFromIntoFreshlyClearedDest() {
        GameInventory dest = new GameInventory1L();
        GameInventory src = new GameInventory1L();
        src.addItem("X", 3);
        dest.clear();
        dest.transferFrom(src);
        assertEquals(3, dest.getQuantity("X"));
        assertEquals(0, src.size());
    }

    /*
     * Sanity: a fresh inventory equals a cleared-then-unused inventory. -----
     */

    @Test
    public void testClearMakesInventoryEqualToFresh() {
        GameInventory a = new GameInventory1L();
        a.addItem("stuff", 42);
        a.clear();
        GameInventory b = new GameInventory1L();
        assertEquals(b, a);
    }

    @Test
    public void testDistinctInventoriesNotEqualByDefault() {
        GameInventory a = new GameInventory1L();
        GameInventory b = new GameInventory1L();
        b.addItem("X", 1);
        assertNotEquals(a, b);
    }

}

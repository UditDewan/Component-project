package components.gameinventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * JUnit tests for the secondary methods on {@link GameInventory} and the
 * Object overrides defined on {@link GameInventorySecondary}, exercised
 * through {@link GameInventory1L}.
 */
public class GameInventoryTest {

    /*
     * hasItem ----------------------------------------------------------------
     */

    @Test
    public void testHasItemTrue() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Sword", 1);
        assertTrue(inv.hasItem("Sword"));
    }

    @Test
    public void testHasItemFalseOnEmpty() {
        GameInventory inv = new GameInventory1L();
        assertFalse(inv.hasItem("Sword"));
    }

    @Test
    public void testHasItemFalseAfterFullRemoval() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Torch", 3);
        inv.removeItem("Torch", 3);
        assertFalse(inv.hasItem("Torch"));
    }

    @Test
    public void testHasItemDoesNotMutate() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Torch", 3);
        GameInventory expected = new GameInventory1L();
        expected.addItem("Torch", 3);
        inv.hasItem("Torch");
        inv.hasItem("Nothing");
        assertEquals(expected, inv);
    }

    /*
     * totalItems -------------------------------------------------------------
     */

    @Test
    public void testTotalItemsEmpty() {
        assertEquals(0, new GameInventory1L().totalItems());
    }

    @Test
    public void testTotalItemsSingleEntry() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Gold", 42);
        assertEquals(42, inv.totalItems());
    }

    @Test
    public void testTotalItemsMultipleEntries() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Gold", 42);
        inv.addItem("Arrow", 8);
        inv.addItem("Sword", 1);
        assertEquals(51, inv.totalItems());
    }

    @Test
    public void testTotalItemsDoesNotMutate() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Gold", 42);
        GameInventory expected = new GameInventory1L();
        expected.addItem("Gold", 42);
        inv.totalItems();
        assertEquals(expected, inv);
    }

    /*
     * mostAbundantItem -------------------------------------------------------
     */

    @Test
    public void testMostAbundantSingleItem() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Gold", 100);
        assertEquals("Gold", inv.mostAbundantItem());
    }

    @Test
    public void testMostAbundantClearWinner() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Gold", 100);
        inv.addItem("Arrow", 8);
        inv.addItem("Sword", 1);
        assertEquals("Gold", inv.mostAbundantItem());
    }

    @Test
    public void testMostAbundantTieReturnsOneOfTheWinners() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("A", 5);
        inv.addItem("B", 5);
        inv.addItem("C", 2);
        String winner = inv.mostAbundantItem();
        // Contract allows any item with the maximum quantity. Verify that
        // whatever was returned, its quantity equals the max.
        assertNotNull(winner);
        assertEquals(5, inv.getQuantity(winner));
    }

    /*
     * transferItem -----------------------------------------------------------
     */

    @Test
    public void testTransferItemPartial() {
        GameInventory a = new GameInventory1L();
        GameInventory b = new GameInventory1L();
        a.addItem("Coin", 10);
        a.transferItem(b, "Coin", 4);
        assertEquals(6, a.getQuantity("Coin"));
        assertEquals(4, b.getQuantity("Coin"));
    }

    @Test
    public void testTransferItemAllRemovesFromSource() {
        GameInventory a = new GameInventory1L();
        GameInventory b = new GameInventory1L();
        a.addItem("Coin", 10);
        a.transferItem(b, "Coin", 10);
        assertFalse(a.hasItem("Coin"));
        assertEquals(0, a.size());
        assertEquals(10, b.getQuantity("Coin"));
        assertEquals(1, b.size());
    }

    @Test
    public void testTransferItemIntoExistingStack() {
        GameInventory a = new GameInventory1L();
        GameInventory b = new GameInventory1L();
        a.addItem("Coin", 10);
        b.addItem("Coin", 5);
        a.transferItem(b, "Coin", 3);
        assertEquals(7, a.getQuantity("Coin"));
        assertEquals(8, b.getQuantity("Coin"));
    }

    @Test
    public void testTransferItemLeavesOtherItemsAlone() {
        GameInventory a = new GameInventory1L();
        GameInventory b = new GameInventory1L();
        a.addItem("Coin", 10);
        a.addItem("Gem", 2);
        a.transferItem(b, "Coin", 4);
        assertEquals(2, a.getQuantity("Gem"));
        assertEquals(0, b.getQuantity("Gem"));
    }

    /*
     * mergeFrom --------------------------------------------------------------
     */

    @Test
    public void testMergeFromEmptySource() {
        GameInventory dest = new GameInventory1L();
        dest.addItem("Gold", 10);
        GameInventory expected = new GameInventory1L();
        expected.addItem("Gold", 10);
        GameInventory src = new GameInventory1L();
        dest.mergeFrom(src);
        assertEquals(expected, dest);
        assertEquals(0, src.size());
    }

    @Test
    public void testMergeFromDisjointKeys() {
        GameInventory dest = new GameInventory1L();
        GameInventory src = new GameInventory1L();
        dest.addItem("Gold", 10);
        src.addItem("Arrow", 4);
        dest.mergeFrom(src);
        assertEquals(10, dest.getQuantity("Gold"));
        assertEquals(4, dest.getQuantity("Arrow"));
        assertEquals(2, dest.size());
        assertEquals(0, src.size());
    }

    @Test
    public void testMergeFromOverlappingKeysSums() {
        GameInventory dest = new GameInventory1L();
        GameInventory src = new GameInventory1L();
        dest.addItem("Gold", 10);
        dest.addItem("Map", 1);
        src.addItem("Gold", 5);
        src.addItem("Key", 2);
        dest.mergeFrom(src);
        assertEquals(15, dest.getQuantity("Gold"));
        assertEquals(1, dest.getQuantity("Map"));
        assertEquals(2, dest.getQuantity("Key"));
        assertEquals(3, dest.size());
        assertEquals(0, src.size());
    }

    @Test
    public void testMergeFromIntoEmptyDest() {
        GameInventory dest = new GameInventory1L();
        GameInventory src = new GameInventory1L();
        src.addItem("A", 1);
        src.addItem("B", 2);
        dest.mergeFrom(src);
        assertEquals(1, dest.getQuantity("A"));
        assertEquals(2, dest.getQuantity("B"));
        assertEquals(2, dest.size());
        assertEquals(0, src.size());
    }

    /*
     * equals / hashCode / toString ------------------------------------------
     */

    @Test
    public void testEqualsSameContents() {
        GameInventory a = new GameInventory1L();
        GameInventory b = new GameInventory1L();
        a.addItem("X", 1);
        a.addItem("Y", 2);
        b.addItem("X", 1);
        b.addItem("Y", 2);
        assertEquals(a, b);
    }

    @Test
    public void testEqualsDifferentQuantities() {
        GameInventory a = new GameInventory1L();
        GameInventory b = new GameInventory1L();
        a.addItem("X", 1);
        b.addItem("X", 2);
        assertNotEquals(a, b);
    }

    @Test
    public void testEqualsDifferentSizes() {
        GameInventory a = new GameInventory1L();
        GameInventory b = new GameInventory1L();
        a.addItem("X", 1);
        b.addItem("X", 1);
        b.addItem("Y", 1);
        assertNotEquals(a, b);
    }

    @Test
    public void testEqualsReflexive() {
        GameInventory a = new GameInventory1L();
        a.addItem("X", 1);
        assertEquals(a, a);
    }

    @Test
    public void testEqualsNullIsFalse() {
        GameInventory a = new GameInventory1L();
        assertNotEquals(null, a);
    }

    @Test
    public void testEqualsDifferentTypeIsFalse() {
        GameInventory a = new GameInventory1L();
        assertNotEquals("not an inventory", a);
    }

    @Test
    public void testHashCodeConsistentWithEqualsRegardlessOfInsertionOrder() {
        GameInventory a = new GameInventory1L();
        GameInventory b = new GameInventory1L();
        a.addItem("X", 1);
        a.addItem("Y", 2);
        b.addItem("Y", 2);
        b.addItem("X", 1);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testToStringEmpty() {
        assertEquals("{}", new GameInventory1L().toString());
    }

    @Test
    public void testToStringContainsEachEntry() {
        GameInventory a = new GameInventory1L();
        a.addItem("Gold", 10);
        a.addItem("Sword", 1);
        String s = a.toString();
        assertTrue(s.startsWith("{"));
        assertTrue(s.endsWith("}"));
        assertTrue(s.contains("Gold=10"));
        assertTrue(s.contains("Sword=1"));
    }

    @Test
    public void testToStringDoesNotMutate() {
        GameInventory a = new GameInventory1L();
        a.addItem("Gold", 10);
        GameInventory expected = new GameInventory1L();
        expected.addItem("Gold", 10);
        a.toString();
        assertEquals(expected, a);
    }

}

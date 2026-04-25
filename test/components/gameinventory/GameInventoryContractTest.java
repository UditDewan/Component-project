package components.gameinventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * JUnit tests for precondition (design-by-contract) boundaries on
 * {@link GameInventory1L}.
 *
 * <p>
 * These tests verify that every method throws {@link AssertionError} when its
 * preconditions are violated. They complement {@link GameInventory1LTest} and
 * {@link GameInventoryTest}, which cover the happy-path behaviour.
 * </p>
 *
 * <p>
 * <strong>Important:</strong> assertions must be enabled when running this
 * suite. In VSCode the {@code java.debug.settings.vmArgs} setting already
 * includes {@code -ea}. From the command line, pass {@code -ea} to the JVM:
 * {@code java -ea -cp ... org.junit.runner.JUnitCore
 * components.gameinventory.GameInventoryContractTest}
 * </p>
 */
public class GameInventoryContractTest {

    // -----------------------------------------------------------------------
    // Helper
    // -----------------------------------------------------------------------

    /**
     * Returns an inventory pre-loaded with one entry: "Gold" → 10.
     */
    private static GameInventory oneEntry() {
        GameInventory inv = new GameInventory1L();
        inv.addItem("Gold", 10);
        return inv;
    }

    // -----------------------------------------------------------------------
    // addItem — quantity preconditions
    // -----------------------------------------------------------------------

    @Test
    public void testAddItemZeroQuantityThrows() {
        GameInventory inv = new GameInventory1L();
        try {
            inv.addItem("Gold", 0);
            fail("Expected AssertionError for qty == 0");
        } catch (AssertionError expected) {
            // correct
        }
    }

    @Test
    public void testAddItemNegativeQuantityThrows() {
        GameInventory inv = new GameInventory1L();
        try {
            inv.addItem("Gold", -1);
            fail("Expected AssertionError for qty < 0");
        } catch (AssertionError expected) {
            // correct
        }
    }

    @Test
    public void testAddItemLargeNegativeQuantityThrows() {
        GameInventory inv = new GameInventory1L();
        try {
            inv.addItem("Gold", Integer.MIN_VALUE);
            fail("Expected AssertionError for qty == Integer.MIN_VALUE");
        } catch (AssertionError expected) {
            // correct
        }
    }

    // -----------------------------------------------------------------------
    // addItem — item-name preconditions
    // -----------------------------------------------------------------------

    @Test
    public void testAddItemNullNameThrows() {
        GameInventory inv = new GameInventory1L();
        try {
            inv.addItem(null, 1);
            fail("Expected AssertionError for null item name");
        } catch (AssertionError expected) {
            // correct
        }
    }

    @Test
    public void testAddItemEmptyNameThrows() {
        GameInventory inv = new GameInventory1L();
        try {
            inv.addItem("", 1);
            fail("Expected AssertionError for empty item name");
        } catch (AssertionError expected) {
            // correct
        }
    }

    // -----------------------------------------------------------------------
    // removeItem — quantity preconditions
    // -----------------------------------------------------------------------

    @Test
    public void testRemoveItemZeroQuantityThrows() {
        GameInventory inv = oneEntry();
        try {
            inv.removeItem("Gold", 0);
            fail("Expected AssertionError for qty == 0");
        } catch (AssertionError expected) {
            // correct
        }
    }

    @Test
    public void testRemoveItemNegativeQuantityThrows() {
        GameInventory inv = oneEntry();
        try {
            inv.removeItem("Gold", -5);
            fail("Expected AssertionError for qty < 0");
        } catch (AssertionError expected) {
            // correct
        }
    }

    // -----------------------------------------------------------------------
    // removeItem — "not enough" preconditions
    // -----------------------------------------------------------------------

    @Test
    public void testRemoveItemMoreThanPresentThrows() {
        GameInventory inv = oneEntry(); // Gold=10
        try {
            inv.removeItem("Gold", 11);
            fail("Expected AssertionError for removing more than present");
        } catch (AssertionError expected) {
            // correct
        }
    }

    @Test
    public void testRemoveItemFromMissingKeyThrows() {
        GameInventory inv = new GameInventory1L();
        try {
            inv.removeItem("Arrow", 1);
            fail("Expected AssertionError for removing from absent key");
        } catch (AssertionError expected) {
            // correct
        }
    }

    @Test
    public void testRemoveItemExactlyPresentDoesNotThrow() {
        // Boundary: removing exactly what is there must succeed.
        GameInventory inv = oneEntry(); // Gold=10
        inv.removeItem("Gold", 10);    // should not throw
        assertEquals(0, inv.size());
    }

    @Test
    public void testRemoveItemAfterFullRemovalThrows() {
        // Once the entry is gone, a second remove must throw.
        GameInventory inv = oneEntry();
        inv.removeItem("Gold", 10);
        try {
            inv.removeItem("Gold", 1);
            fail("Expected AssertionError: entry was deleted");
        } catch (AssertionError expected) {
            // correct
        }
    }

    // -----------------------------------------------------------------------
    // removeItem — name preconditions
    // -----------------------------------------------------------------------

    @Test
    public void testRemoveItemNullNameThrows() {
        GameInventory inv = oneEntry();
        try {
            inv.removeItem(null, 1);
            fail("Expected AssertionError for null item name");
        } catch (AssertionError expected) {
            // correct
        }
    }

    @Test
    public void testRemoveItemEmptyNameThrows() {
        GameInventory inv = oneEntry();
        try {
            inv.removeItem("", 1);
            fail("Expected AssertionError for empty item name");
        } catch (AssertionError expected) {
            // correct
        }
    }

    // -----------------------------------------------------------------------
    // transferItem — secondary method preconditions
    // -----------------------------------------------------------------------

    @Test
    public void testTransferItemMoreThanPresentThrows() {
        GameInventory a = oneEntry(); // Gold=10
        GameInventory b = new GameInventory1L();
        try {
            a.transferItem(b, "Gold", 11);
            fail("Expected AssertionError: transferring more than present");
        } catch (AssertionError expected) {
            // correct
        }
    }

    @Test
    public void testTransferItemZeroQuantityThrows() {
        GameInventory a = oneEntry();
        GameInventory b = new GameInventory1L();
        try {
            a.transferItem(b, "Gold", 0);
            fail("Expected AssertionError for qty == 0");
        } catch (AssertionError expected) {
            // correct
        }
    }

    @Test
    public void testTransferItemNullOtherThrows() {
        GameInventory a = oneEntry();
        try {
            a.transferItem(null, "Gold", 1);
            fail("Expected AssertionError for null destination");
        } catch (AssertionError expected) {
            // correct
        }
    }

    // -----------------------------------------------------------------------
    // mostAbundantItem — empty inventory precondition
    // -----------------------------------------------------------------------

    @Test
    public void testMostAbundantItemOnEmptyThrows() {
        GameInventory inv = new GameInventory1L();
        try {
            inv.mostAbundantItem();
            fail("Expected AssertionError: inventory is empty");
        } catch (AssertionError expected) {
            // correct
        }
    }

    // -----------------------------------------------------------------------
    // mergeFrom — null source precondition
    // -----------------------------------------------------------------------

    @Test
    public void testMergeFromNullSourceThrows() {
        GameInventory dest = oneEntry();
        try {
            dest.mergeFrom(null);
            fail("Expected AssertionError for null source");
        } catch (AssertionError expected) {
            // correct
        }
    }

    // -----------------------------------------------------------------------
    // Verify state is unchanged after a caught precondition violation
    // -----------------------------------------------------------------------

    @Test
    public void testStateUnchangedAfterBadAdd() {
        GameInventory inv = oneEntry(); // Gold=10
        try {
            inv.addItem("Gold", -1);
        } catch (AssertionError ignored) {
            // expected; fall through
        }
        // The inventory must be exactly as it was.
        assertEquals(1, inv.size());
        assertEquals(10, inv.getQuantity("Gold"));
    }

    @Test
    public void testStateUnchangedAfterBadRemove() {
        GameInventory inv = oneEntry(); // Gold=10
        try {
            inv.removeItem("Gold", 999);
        } catch (AssertionError ignored) {
            // expected; fall through
        }
        assertEquals(1, inv.size());
        assertEquals(10, inv.getQuantity("Gold"));
    }
}

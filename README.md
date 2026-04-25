# GameInventory

An OSU-discipline software component that models a **video game inventory** —
a finite mapping from item names to positive integer quantities, with no
stack limit and no weight cap. Items are added, removed, transferred between
inventories, merged, and queried through a small, well-specified API.

This component was built as the portfolio project for CSE 2231. It follows
the software sequence discipline: a kernel interface, an enhanced interface
layered on top, an abstract class that implements all secondary methods
using only the kernel, and a thin kernel implementation over
`java.util.HashMap`.

## Why this component?

Games like Minecraft, Kingdom Come: Deliverance, Fallout, and Baldur's
Gate 3 all use some notion of an inventory — but they often add weight
limits and max stack sizes that slow down play without adding much
strategic depth. `GameInventory` deliberately strips both of those away, so
it can serve as a clean building block for any RPG-style project that just
needs to track *what* the player has and *how much*.

## Hierarchy

```
Standard<GameInventory>    Iterable<String>
        \                       /
         \                     /
         GameInventoryKernel
                  |
            GameInventory                  (enhanced interface)
                  |
         GameInventorySecondary            (secondary methods + Object overrides)
                  |
           GameInventory1L                 (HashMap-backed kernel implementation)
```

## Quick start

```java
import components.gameinventory.GameInventory;
import components.gameinventory.GameInventory1L;

GameInventory player = new GameInventory1L();
player.addItem("Gold", 50);
player.addItem("Health Potion", 3);

GameInventory chest = new GameInventory1L();
chest.addItem("Gold", 100);
chest.transferItem(player, "Gold", 100);  // player now has 150 gold

System.out.println(player);                // {Gold=150, Health Potion=3}
System.out.println(player.mostAbundantItem());  // Gold
```

## API overview

**Kernel methods** (in `GameInventoryKernel`):

| Method | What it does |
|---|---|
| `addItem(item, qty)` | add `qty` of `item` (creates the entry if new) |
| `removeItem(item, qty)` | remove `qty` of `item` (deletes the entry if it hits zero) |
| `getQuantity(item)` | returns the current count, or 0 if the item isn't present |
| `size()` | returns the number of distinct item names |
| `iterator()` | iterates over the names of items currently in the inventory |

**Secondary methods** (in `GameInventory`):

| Method | What it does |
|---|---|
| `hasItem(item)` | true iff `item` is present with quantity > 0 |
| `totalItems()` | sum of all quantities across all items |
| `mostAbundantItem()` | name of an item tied for the largest quantity |
| `transferItem(other, item, qty)` | move `qty` of `item` from this to `other` |
| `mergeFrom(source)` | combine everything from `source` into this; `source` ends empty |

**Standard methods** come from `Standard<GameInventory>`: `newInstance`,
`clear`, `transferFrom`.

## Project layout

```
src/
├── components/
│   └── gameinventory/
│       ├── GameInventoryKernel.java        kernel interface
│       ├── GameInventory.java              enhanced interface
│       ├── GameInventorySecondary.java     abstract class
│       └── GameInventory1L.java            kernel implementation
├── InventoryTradingDemo.java               use case #1 — scripted scene
└── CraftingStation.java                    use case #2 — layered component

test/
└── components/
    └── gameinventory/
        ├── GameInventory1LTest.java        kernel + Standard tests
        └── GameInventoryTest.java          secondary + Object-override tests

doc/
└── 01..06 assignment write-ups

CHANGELOG.md   Keep-a-Changelog style history, CalVer dated
```

## Use cases

Two worked examples live in `src/`:

- **`InventoryTradingDemo`** — a scripted RPG scene: the player loots a
  chest, sells potions to a shopkeeper for gold, and claims a quest reward.
  This exercises the full API end to end.
- **`CraftingStation`** — a small component that uses a `GameInventory` as
  part of its *own* representation. A station holds a recipe (also a
  `GameInventory`), checks a player's inventory for the required
  ingredients, and consumes them to produce an output item. This is the
  "component as building block" pattern.

Each use case has a `main` method you can run directly.

## Testing

Run the JUnit suites under `test/components/gameinventory/`:

- `GameInventory1LTest` — constructor, every kernel method, iterator
  semantics (including the wrapped `remove()` throwing
  `UnsupportedOperationException`), and every Standard method.
- `GameInventoryTest` — every secondary method and the `equals` /
  `hashCode` / `toString` overrides. Non-mutating tests also check that
  the inventory is unchanged by comparing against a separately-built
  "expected" inventory.

Tests that depend on iteration order (e.g. `toString`, `mostAbundantItem`
on a tie) verify contract-level properties rather than a specific literal
string or key, because iteration order on a `HashMap` is not guaranteed.

## Convention & correspondence

Documented at the top of `GameInventory1L`:

- **Convention:** the backing `Map<String, Integer>` is non-null, every key
  is a non-empty string, every value is strictly positive. Entries whose
  quantity would drop to zero are deleted rather than stored as zero.
- **Correspondence:** the abstract inventory is the partial function whose
  domain is the map's key set, where `this[k] = items.get(k)`.

## License

See `LICENSE` in the repo root.

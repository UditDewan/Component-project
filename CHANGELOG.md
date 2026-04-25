# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Calendar Versioning](https://calver.org/) of
the following form: YYYY.0M.0D.

## [Unreleased]

- Disabled an aspect of the coderunner extension
- Fixed broken links
- Disabled AI features globally

## 2026.04.24

### Added

- Added Junit test file `GameInventory1LTest` covering every test case not convered by the others

- Added JUnit test file `GameInventory1LTest` covering the constructor,
  every kernel method, iterator semantics (including that the wrapped
  `remove()` throws `UnsupportedOperationException`), and every Standard
  method
- Added JUnit test file `GameInventoryTest` covering every secondary
  method as well as the `equals`, `hashCode`, and `toString` overrides,
  including tie handling in `mostAbundantItem` and order-independent
  hash consistency
- Added use case `InventoryTradingDemo` — a scripted RPG scene that loots
  a chest, trades with a shopkeeper, and merges a quest reward
- Added use case `CraftingStation` — a component that holds a
  `GameInventory` in its own representation to model recipes and consume
  ingredients from a player's inventory
- Added polished project `README.md` documenting the hierarchy, API,
  project layout, convention and correspondence, and how to run the tests
  and demos
- Added reflection responses covering growth, knowledge gaps, skills
  gained, and next steps

### Updated

- No changes were made to the kernel or enhanced interfaces; all
  additions in this release are tests, sample clients, and documentation

## 2026.04.24

### Added

- Designed kernel implementation `GameInventory1L` for the GameInventory
  component, layered on top of `java.util.HashMap<String, Integer>`
- Documented convention (representation invariant) and correspondence
  (abstraction function) at the top of `GameInventory1L`
- Implemented all kernel methods (`addItem`, `removeItem`, `getQuantity`,
  `size`) according to the convention and correspondence
- Implemented the `Iterable<String>` iterator, wrapped so that `remove()`
  throws `UnsupportedOperationException` to prevent clients from bypassing
  the kernel
- Implemented all Standard methods (`newInstance`, `clear`, `transferFrom`),
  including a `createNewRep()` helper and an O(1) reference swap in
  `transferFrom`
- Added a no-argument constructor that initializes the inventory to empty
- Designed abstract class `GameInventorySecondary` for the GameInventory
  component
- Implemented all secondary methods (`hasItem`, `totalItems`,
  `mostAbundantItem`, `transferItem`, `mergeFrom`) using only kernel
  methods and iteration
- Implemented `toString()`, `equals(Object)`, and `hashCode()` using only
  kernel methods, so concrete kernel implementations do not need to
  override them
- Added precondition assertions to every secondary method to match the
  contracts on the enhanced interface

### Updated

- No changes were made to the kernel or enhanced interfaces; all
  secondary-method and kernel-method signatures were implemented as
  previously specified

## 2025.03.24

### Added

- Designed kernel and enhanced interfaces for GameInventory component
- Added hierarchy diagram for GameInventory component

### Updated

- Changed design to include `size()` kernel method to support secondary
  method implementations
- Changed design to include `Iterable<String>` in kernel to allow
  iteration in secondary methods
- Changed design to include `mostAbundantItem()` and `mergeFrom()` as
  secondary methods

## 2025.02.13

### Added

- Designed a proof of concept for GameInventory component

### Updated

- Changed design to include `transferItem()` between two inventories
- Changed design to include `displayInventory()` for printing inventory
  contents

## 2025.02.06

### Added

- Designed a ReadingTracker component
- Designed a GameInventory component
- Designed a EventPopularityAnalyzer component

## [2024.12.30]

- Added table-based rubrics to all 6 parts of the project
- Updated gitignore to exclude more files
- Fixed image markdown in the interfaces document

## [2024.08.07]

### Added

- Added `/bin` to `.gitignore`, so binaries are no longer committed
- Added the TODO tree extensions to `extensions.json`
- Added the `todo-tree.general.showActivityBarBadge` setting to `settings.json`
- Added the `todo-tree.tree.showCountsInTree` setting to `settings.json`
- Added the VSCode PDF extension to `extensions.json`
- Added `java.debug.settings.vmArgs` setting to enable assertions (i.e., `-ea`)
- Added information about making branches to all parts of the project
- Added information about how to update the CHANGELOG to every part of the
  project
- Added information about how to make a pull request to every part of the
  project

### Changed

- Updated `settings.json` to format document on save using `editor.formatOnSave`
  setting
- Updated `settings.json` to exclude certain files from markdown to PDF
  generation using `markdown-pdf.convertOnSaveExclude` setting
- Updated `settings.json` to use latest `java.cleanup.actions` setting
- Updated `settings.json` to automatically choose line endings using `files.eol`
  setting
- Updated `settings.json` to organize imports automatically on save using the
  `editor.codeActionsOnSave` and `source.organizeImports` settings
- Changed the component brainstorming assignment to ask a few clarifying
  questions
- Changed the component brainstorming example from `Point3D` to `NaturalNumber`
  to avoid the getter/setter trend
- Updated assignment feedback sections to include a link to a survey that
  I'll actually review
- Updated README to include step about using template repo
- Updated part 3 rubric to include a hierarchy diagram
- Updated part 6 rubric to account for overall polish

### Fixed

- Fixed issue where checkstyle paths would not work on MacOS

### Removed

- Removed `java.saveActions.organizeImports` setting from `settings.json`
- Removed references to `Point3D` completely

## [2024.01.07]

### Added

- Added a list of extensions to capture the ideal student experience
- Added PDFs to the `.gitignore`
- Added the OSU checkstyle config file
- Added the OSU formatter config file
- Added a `settings.json` file to customize the student experience
- Created a README at the root to explain how to use the template repo
- Created initial drafts of the six portfolio assessments
- Added READMEs to key folders like `test` and `lib` to explain their purpose

[unreleased]: https://github.com/jrg94/portfolio-project/compare/v2024.08.07...HEAD
[2024.08.07]: https://github.com/jrg94/portfolio-project/compare/v2024.01.07...v2024.08.07
[2024.01.07]: https://github.com/jrg94/portfolio-project/releases/tag/v2024.01.07

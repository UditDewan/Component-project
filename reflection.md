# Part 6 Reflection 


---

### How much better (or worse) do you understand software development now, and why?

The biggest shift for me wasn't learning new syntax — it was actually
living with a component across six assignments instead of writing
throwaway homework. I designed `GameInventory` in Part 1, built a
proof-of-concept in Part 2, and then had to keep untangling earlier
decisions in every part after that. In Part 3 I realized I couldn't
implement my secondary methods without a `size()` kernel method and
without making the kernel `Iterable<String>`, so I went back and changed
the interface. In Part 4 I hit an iterator-invalidation problem in
`mergeFrom` — I couldn't for-each over the source while removing from it —
and had to change the implementation strategy to a `while (source.size() >
0)` drain. None of that was hard in isolation, but feeling the cost of a
bad early decision in a later assignment is a real-world experience
textbook problems don't reproduce.

### Did the portfolio project surface any gaps in your own knowledge? If so, what were they and how did you address them?

Yeah 2:
- I didn't really get  why the OSU discipline separates kernel
  from secondary until I had to write `GameInventorySecondary` using only
  kernel methods. Being forbidden from touching the HashMap directly
  forced me to actually use the abstraction I'd designed, and that made
  me catch two missing kernel methods. I slightly forgive all of you for 
  forcing me to only use kernel methods on tests. 
- I didn't think about iterator safety at all until I realized
  `HashMap.keySet().iterator()` lets callers delete things, which would
  let them bypass the kernel. Wrapping the iterator to throw on `remove()`
  was a small change that tightened encapsulation a lot.

### Has your perspective on software development changed? Do you still enjoy it?

I enjoy it more than I did going in, but for different reasons. I used to
think "good code" mostly meant code that worked; now I think a lot more
about the contract someone else reads before calling my method. The
design-by-contract assertions in `addItem` and `removeItem` aren't there
to make the file longer — they catch bugs at the call site instead of ten
stack frames deep, which I've actually come to appreciate.

I also underestimated how much of software development is writing about
code: the convention, the correspondence, the Javadoc, the CHANGELOG, the
README. That part surprised me. It's not my favorite thing, but I can see
why a future collaborator (or future me) would need it.

### What skills did you pick up during this process?

- Using `git` and GitHub for branching and pull-request workflow instead
  of zip files
- Writing a CHANGELOG that a reader can actually use to see what changed
- Translating an informal idea ("inventory") into a formal interface
  (math model, contracts, pre/postconditions)
- Writing a representation invariant and abstraction function
- Reading and applying the software-sequence discipline on a component I
  actually designed, not one the instructor pre-built
- Markdown, basic repo hygiene, using CheckStyle and the formatter

### Rephrase those skills as resume bullets

- Designed and implemented a layered software component in Java from
  interface through kernel implementation, following design-by-contract
  and a written representation invariant.
- Wrote a JUnit test suite covering kernel, secondary, and standard
  methods, including iterator-safety and contract-edge-case tests.
- Authored technical documentation — Javadoc, a project README,
  convention and correspondence, and a Keep-a-Changelog history — aimed
  at external readers.
- Used Git and GitHub with a feature-branch / pull-request workflow
  across six iterative deliverables.

### How has this project affected your career trajectory?

It reinforced that I'm interested in game-adjacent software. The project
started because inventory systems in the RPGs I play (Minecraft, KCD,
Fallout, BG3) always frustrated me with artificial limits. Building the
component that I wanted reminded me that software is how you
get to fix the small things that bug you, not just ship features someone
else scoped. Whether that turns into gameplay programming, tools, or
something adjacent, I'm more interested in the path, not less.

### What's next, and who could mentor you?

Concrete next steps:
- Finish the component by wiring it into a small playable prototype (a
  text-adventure or a Minecraft-style crafting sandbox) so it actuall 
  does something and doesnt just rot like my projects.
- Pick up a game-focused framework — LibGDX or Unity — and port some of
  what I've built.
- Contribute to an open-source mod or tool for a game I already play,
  to get used to reading code I didn't write.

For mentors: my instructor and TAs this semester have already been
useful for design feedback. Beyond that, the CSE department runs office
hours for project-based advising, and OSU's ACM student chapter has
members with internship experience I can ask about the jump from
coursework to industry. If I'm serious about games, connecting with a
dev from a studio like Digital Extremes or a local indie via LinkedIn
alumni search would probably be the single highest-leverage thing I
could do.Im far more likely to just make a game with some friends
though. Game development dont make enough money for me.

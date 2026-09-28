# Joseph Ramer - Project 01 Retrospective

## My work

### Merged PRs

- [#16: feature/JTRBG123/Issue#4](https://github.com/BillyP2002/CST438-Project1-Team5/pull/16)
- [#23: Feature/jtrbg123/issue#4](https://github.com/BillyP2002/CST438-Project1-Team5/pull/23)
- [#25: Feature/jtrbg123/issue#7](https://github.com/BillyP2002/CST438-Project1-Team5/pull/25)
- [#32: Feature/jtrbg123/issue#27](https://github.com/BillyP2002/CST438-Project1-Team5/pull/32)
- [#48: fix/JTRBG123/Issue#42](https://github.com/BillyP2002/CST438-Project1-Team5/pull/48)
- [#54: Made some changes to fix the Room database](https://github.com/BillyP2002/CST438-Project1-Team5/pull/54)
- [#60: Feature/jtrbg123/issue#56](https://github.com/BillyP2002/CST438-Project1-Team5/pull/60)
- [#72: Fix: Add Detekt suppressions for merged code from main](https://github.com/BillyP2002/CST438-Project1-Team5/pull/72)

### My issues

- [#4](https://github.com/BillyP2002/CST438-Project1-Team5/issues/4), [#7](https://github.com/BillyP2002/CST438-Project1-Team5/issues/7), [#8](https://github.com/BillyP2002/CST438-Project1-Team5/issues/8), [#9](https://github.com/BillyP2002/CST438-Project1-Team5/issues/9)
- [#27](https://github.com/BillyP2002/CST438-Project1-Team5/issues/27), [#36](https://github.com/BillyP2002/CST438-Project1-Team5/issues/36), [#38](https://github.com/BillyP2002/CST438-Project1-Team5/issues/38), [#42](https://github.com/BillyP2002/CST438-Project1-Team5/issues/42)
- [#56](https://github.com/BillyP2002/CST438-Project1-Team5/issues/56), [#57](https://github.com/BillyP2002/CST438-Project1-Team5/issues/57), [#58](https://github.com/BillyP2002/CST438-Project1-Team5/issues/58), [#59](https://github.com/BillyP2002/CST438-Project1-Team5/issues/59), [#71](https://github.com/BillyP2002/CST438-Project1-Team5/issues/71)
- [#46: presentation](https://github.com/BillyP2002/CST438-Project1-Team5/issues/46) — still open at the time of inspection.

### What I built

I worked on the authentication and local data foundation. I created and tested sign-in/registration UI, connected authentication to the home screen and bottom navigation, removed the test email from the login flow, and added UI tests for the home and profile screens. I migrated the database from SQLiteOpenHelper to Room, implemented/fixed the Room entities and DAOs, added database tests, and worked on the player-name/database behavior. I also contributed shared theme/background changes, static-check fixes, and integration fixes after merging main.

## Biggest challenge

The hardest part was integrating the Room migration and authentication/UI work while the rest of the app was changing. The database types and screen structure were moving at the same time, so a change that was correct in isolation could fail after a merge or make an existing test stale. I handled this by breaking the work into smaller commits, merging current main into the feature branch, fixing the resulting compile/test/Detekt failures, and adding DAO and Compose UI tests. The process worked, but it also showed me that integration and tests need to happen continuously rather than in a final cleanup pass.

## Most valuable thing I learned

I learned that a working feature is not just its implementation: it also needs a clear issue, a reviewable pull request, tests that exercise the actual integration point, and a repeatable way to verify it after other branches merge. Room was valuable technically, but the larger lesson was how strongly data-model decisions affect every screen and test that consumes them.

## What I carry into Project 02

- I would've appreciated it if you had communicated your progress on the original database code a little earlier in the project.
- Some of Joseph's status updates on slack could be more specific, as some were vague or non-specific. A minor issue, but still a possible place to improve.

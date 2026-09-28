# Project 01 Post Mortem - CST438-Project1-Team5

## Context

We set out to build an Android game in which a player listens to anime theme-song clips and guesses the anime title. The shipped app is a Kotlin/Jetpack Compose application with local account registration and sign-in, Room-backed local data, progressive audio hints, scoring and results, a profile and cosmetic shop, and optional MyAnimeList linking/MAL Mode. The final repository also includes automated checks and tests. The MAL/API-dependent parts require network access, so behavior can still be affected when an external service is unavailable.

## By the numbers

- Issues opened: **33** ([all issues](https://github.com/BillyP2002/CST438-Project1-Team5/issues)) | closed: **30**
- Pull requests opened: **46** ([all pull requests](https://github.com/BillyP2002/CST438-Project1-Team5/pulls)) | merged: **33**
- Planned at kickoff: **9 stories** ([#1](https://github.com/BillyP2002/CST438-Project1-Team5/issues/1)–[#9](https://github.com/BillyP2002/CST438-Project1-Team5/issues/9)) | done: **9**

The counts above include the repository's full issue and pull-request history. The kickoff count is based on the original numbered feature issues #1 through #9; the team should adjust it if the kickoff board used a different story list.

## What went well

1. We moved the local data layer to Room and added DAO/database tests. That gave account, song, daily-challenge, and MAL-watchlist data a consistent persistence model instead of leaving each feature to manage storage independently.
2. We established CI checks with Detekt, lint, compilation, and tests early enough to catch integration problems before submission. The repository history shows repeated fixes for static checks and test/build failures rather than leaving verification until after the final merge.
3. The team delivered a usable vertical slice: authentication connects to the main navigation, the game can load and play theme clips, correct answers produce rewards, and the profile/shop area uses those rewards. This made the final app more than a collection of disconnected screens.

## What went wrong

1. Integration became concentrated near the end of the project. Many UI, API, game, and bug-fix pull requests were merged within the final few days.
   - Cause: We allowed several features to develop in parallel against rapidly changing screens and data models without a fixed integration cadence or an explicit freeze point. That increased merge conflicts and made late fixes affect already-completed work.
2. Some final debugging time was spent investigating behavior that was actually caused by an external API outage.
   - Cause: We did not begin troubleshooting by separating local-app failures from dependency failures with a known-good response, mock, or service-availability check. As a result, code changes were attempted before the external dependency had been ruled in or out.
3. Verification was not always reproducible in the development environment. The history includes emulator/CI availability issues, flaky Compose UI tests, and Detekt warnings after merges.
   - Cause: Our test environment assumptions were not documented and enforced consistently, and some tests depended too closely on timing, emulator state, or the latest merged code. Those conditions made a passing local result less predictive of CI.

## Advice to our next teams

1. Keep one small, explicit integration window each day. Merge and run the full check suite before starting another large feature, and establish a code freeze before the deadline.
2. Open or update an issue before coding, keep each pull request narrow, and link it to the issue. If a PR is abandoned or superseded, close it with a short explanation so the history remains understandable.
3. Treat external APIs as failure-prone dependencies from the beginning: add a service-health check or mock path, document the expected responses, and test the app's offline/error state separately from the happy path.

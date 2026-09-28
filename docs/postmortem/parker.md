# Kyle Parker - Project 01 Retrospective

## My work
- Merged PRs:
[#79 – Improve README setup instructions](https://github.com/BillyP2002/CST438-Project1-Team5/pull/79)
[#74 – Connected elements of the app](https://github.com/BillyP2002/CST438-Project1-Team5/pull/74)
[#73 – Game UI Fixes](https://github.com/BillyP2002/CST438-Project1-Team5/pull/73)
[#70 – Api Data Integration into Db](https://github.com/BillyP2002/CST438-Project1-Team5/pull/70)
[#69 – Quality of life changes for game](https://github.com/BillyP2002/CST438-Project1-Team5/pull/69)
[#67 – Game Finished & Audio Implemented](https://github.com/BillyP2002/CST438-Project1-Team5/pull/67)
[#65 – Fix for CI emulator test](https://github.com/BillyP2002/CST438-Project1-Team5/pull/65)
[#49 – Anime API connection](https://github.com/BillyP2002/CST438-Project1-Team5/pull/49)
[#40 – CI/CD checks](https://github.com/BillyP2002/CST438-Project1-Team5/pull/40)
[#29 – Connected AnimeThemeSong API](https://github.com/BillyP2002/CST438-Project1-Team5/pull/29)
[#22 – Updated gitignore](https://github.com/BillyP2002/CST438-Project1-Team5/pull/22)
[#17 – Issue 13 implementation](https://github.com/BillyP2002/CST438-Project1-Team5/pull/17)
- My issues:
[#68 – Create Game](https://github.com/BillyP2002/CST438-Project1-Team5/issues/68)
[#35 – Add Readme](https://github.com/BillyP2002/CST438-Project1-Team5/issues/35)
[#33 – Integrate CI Plugins](https://github.com/BillyP2002/CST438-Project1-Team5/issues/33)
[#24 – Toolbar](https://github.com/BillyP2002/CST438-Project1-Team5/issues/24)
[#21 – Update Shop UI to work with mobile](https://github.com/BillyP2002/CST438-Project1-Team5/issues/21)
[#15 – Create a home screen](https://github.com/BillyP2002/CST438-Project1-Team5/issues/15)
[#14 – Profile Page and Customization](https://github.com/BillyP2002/CST438-Project1-Team5/issues/14)
[#13 – Reward Currency and Prize Shop](https://github.com/BillyP2002/CST438-Project1-Team5/issues/13)
- What I built: 
    - Shop and Anime Coin System
    
    I worked on the shop and anime coin system. This mainly involved creating a shop composable screen, a data class to hold item information, using a vertical grid to ensure that multiple items could be shown on the screen at the same time, and using shared preferences to save purchased items and balance. I also used modal views to show the shopping cart, and purchase validation.

    - Anime Themes API integration

    This mostly involved creating data classes to store the correct information, using gson to correctly parse the information gotten, and testing the api endpoints to make sure that the information I was getting was the information that I wanted.

    - Game functionality
        - Points system

    I used a composable screen for the game, with boxes that are updated with color as the player progresses through the game. Each round selects a random anime theme song (Eashwar added the MAL connection to the game). Players can listen to progressively more song 0.5, 1, 3, 8, 15 seconds respectively. They're guessing by typing the anime title, which posts that cleaned title to the API and waits for a response. Correct answers will give the player points, incorrect doesn't really do anything. The player can play as many rounds as they want, but when they press finish their final guessed anime will pop onto a new screen, with the title and the video, and the number of points and converted anime coins will show up on the screen. 

    - Audio downloading and caching

    The audio is gotten from the AnimeThemes API, downloaded using OkHttp, and stored on the device cache. It's played using a Media3 ExoPlayer. Each hint reuses the cache but changes the amount of time clipped from the playback, allowing the song to progress with the hint time exposed by the player clicking next hint.

    - MyAnimeList/API-to-database integration

    This was very simple, I just had to get the MAL api data class information that Eashwar provided, and put it into a dropdown inside the profile page.

    - CI/CD integration

    This was also very simple, I just added Detekt to the project, added a ruleset and let it work. The issue that I ran into with this though, was that quite a lot of the time the emulator was crashing on GitHub so the tests were failing to run properly. I wasn't able to find a fix for this.

## Biggest challenge

My biggest challenge was pushing PRs on time. I was bad at managing my time, and this led to me pushing my PRs late on the night they were due. I never really got better at this throughout the project, and some of my team's feedback reflected this.

## Most valuable thing I learned

The most valuable thing I learned was the audio pipeline integration. I got to work on something very objectively difficult, and the result was very satisfying. Also, I learned a lot about myself and my limitations when coding something large and undocumented.

## What I carry into Project 02

1. I will commit to pushing PRs by Monday night and reviewing PRs throughout the week. - I will know it worked if my PRs consistently throughout project 2 are pushed on Monday night.
2. I will strive to communicate with my teammates about anything that might be holding me back from completing my work. - I will know it worked if my teammates give me good reviews in that section of project 2 reviews.

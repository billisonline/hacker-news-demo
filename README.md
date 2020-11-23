# Hacker News Demo
This is a demo app intended to demonstrate fluency with the Android platform. It displays the current top 10 stories on the news aggregator site [Hacker News](https://en.wikipedia.org/wiki/Hacker_News) and allows the user to open each story in the default browser by clicking the headline.

Here it is in action:

![Hacker News Demo GIF](https://i.imgur.com/DfVABVr.gif)

## Android APIs used
Because this app is for portfolio/demonstration purposes, the readme will describe how it is built instead of the functionality. It uses:

* Activities and Intents (start browser activity on headline click)
* RecyclerView
* ContentProvider
* CursorLoader to fetch content off the main thread
* [Dagger](https://dagger.dev/) and [Hilt](https://dagger.dev/hilt/) for dependency injection
* Multiple concurrent requests using the [Volley HTTP client](https://developer.android.com/training/volley) (this is needed for HN's [unusual API format](https://github.com/HackerNews/API))
* Unit tests to inject a fake HackerNewsRepository into the HackerNewsContentProvider and ensure it returns records in the correct format
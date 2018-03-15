# Android REDDIT propotype: 
## Display a list of the 50 top Reddit Posts

* Pagination support
* App state-preservation/restoration
* Indicator of unread/read post (white/gray background color)
* Dismiss Post Button (remove the cell from list. Animations required)
* Dismiss All Button (remove all posts. Animations required)
* Support split layout (left side: all posts / right side: detail post)

## Implementation details
* MVP pattern applied. 
* Model calls based on event bus triggering and response handling on the presenter side. UI decouple from model calls.
* DTOs response mapping with Gson and Retrofit2.
* Images caching and loading with Glide. Images can be saved it the device galery depending on the user permissions.
* Error handling on API call fail with event triggering.
* Event bus injected with Dagger2.
* Retrofit service Injected with Dagger2.

Library ussages: Retrofit, Gson, Glide, Dagger 2, Otto (Event bus)

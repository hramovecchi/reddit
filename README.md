# Android REDDIT propotype: 
## Display a list of the 50 top Reddit Posts

* MVP pattern applied. 
* Model calls based on event bus triggering and response handling on the presenter side. UI decouple from model calls.
* DTOs response mapping with Gson and Retrofit2.
* Images caching and loading with Glide. Images can be saved it the device galery depending on the user permissions.
* Error handling on API call fail with event triggering.
* Event bus injected with Dagger2.
* Retrofit service Injected with Dagger2.

Library ussages: Retrofit, Gson, Glide, Dagger 2, Otto (Event bus)

# Weather app for Android. 
This is a sample Weather app using various app componenents. Features are bare bone and is still work in progress. 

## Specifications of the app
### Func requirements 
1. Search by city or City and country
2. Display the important weather information - (current, hi/lo weather, icon) 
3. Should display weather icon
4. Auto-load the last city searched upon app launch
5. If permission to access the location, then retrive weather data by default. 


### Non-Func requirements: 
1. Use openweathermap.org  to get the api data.
1. Uses MVVM architecture
1. Network library - Retrofit
1. Unit testing - Mockio and Mockk
1. Network Concurrency - Coroutines 
1. Dependency injection - Hilt+Dagger
1. Glide - For Image caching 
1. Retrofit - Network library
1. SharedPreferences 
1. View BInding 


### UI Flow 
Ui flow can be seen [here](https://drive.google.com/file/d/1saO-INwhgZZR-nGf5ORGhQIrURg8R0eu/view?usp=drive_link)

### Component details 
* Model layer - WeatherRepository Implementation
    * Caching and persistence - Makes use of Retrofit to provide caching. Retrofit caches the response for 10 mins. To keep up with weather changes, responses are cached for 10 mins and after that, weather update is fetched
    * from server. More complicated caching can be provided if the data is persisted in database and validate if the data is still relevant based on current time.
    * Has 2 data sources - Current location data source and saved search datasource.
    * Current location returns the current location if permission is available
    * Saved search Datatsource - Returns a list of coordinates. Currently, the list consists of last search result. 
* WeatherViewModel
    * ViewModel to show the main weather screen.
    * It keeps track of the current view state by keeping track of user inputs like which location is being viewed, new search request
    * Gets the needed data for UI from repository
    * Keeps the data up to date based on the user events and ui state change
    * Maintains view state that can be consumed by View layer.
* SearchViewModel
    * ViewModel for search related activities


### WIP
* Multi city search result handling
* Unit test sample provided and project does not have 100% code coverage.


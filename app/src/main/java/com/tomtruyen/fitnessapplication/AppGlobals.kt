package com.tomtruyen.fitnessapplication

// This class is used to store global variables
data class AppGlobals(
    val fetchedData: FetchedData = FetchedData()
)


// We use this to check if we have already made a request to Firebase.
// If we have, we don't need to make another request --> Reduces the amount of Reads on Firebase
data class FetchedData(
    val data: MutableMap<Type, Boolean> = mutableMapOf()
) {
   fun hasFetched(type: Type): Boolean {
       return data[type] ?: false
   }

   fun setFetched(type: Type, fetched: Boolean) {
       data[type] = fetched
   }

   enum class Type {
       EXERCISES,
       USER_EXERCISES,
       SETTINGS
   }
}
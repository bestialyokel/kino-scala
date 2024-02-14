import { configureStore } from "@reduxjs/toolkit";
import { default as tasksReducer } from "./features/tasks/reducer";
import { tasksApi } from "./features/tasks/rtk";
import { setupListeners } from "@reduxjs/toolkit/query";

const store = configureStore({
	reducer: {
		// Add the generated reducer as a specific top-level slice
		[tasksApi.reducerPath]: tasksApi.reducer,
		tasks: tasksReducer
	},
	// Adding the api middleware enables caching, invalidation, polling,
	// and other useful features of `rtk-query`.
	middleware: (getDefaultMiddleware) =>
		getDefaultMiddleware().concat(tasksApi.middleware),
	
})

// optional, but required for refetchOnFocus/refetchOnReconnect behaviors
// see `setupListeners` docs - takes an optional callback as the 2nd arg for customization
setupListeners(store.dispatch)

export default store;
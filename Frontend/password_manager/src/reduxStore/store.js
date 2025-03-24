import { configureStore } from "@reduxjs/toolkit";
import passwordReducer from "./slices/passwordSlice";
import moreActionReducer from "./slices/moreActionSlice";
import otpReducer from "./slices/otpSlice";

const store = configureStore({
	reducer: {
		password: passwordReducer,
		moreAction: moreActionReducer,
		otp: otpReducer
	}
});

export default store;
 
// your validateOtp action was being dispatched, but there was no registered reducer to handle it.
// Since extraReducers in otpSlice.js is part of otpReducer, Redux did not know where to store the state updates.

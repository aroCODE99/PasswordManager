import { createAsyncThunk, createSlice }  from "@reduxjs/toolkit";
import axios from "axios";

export const validateThe2FaToken = createAsyncThunk(
	"moreAction/validateThe2FaToken",
	async (_, thunkApi) => {
		try {
			let res = await axios.post(
				"http://localhost:8080/api/validateThe2FaToken",
				{},
				{
					headers: {
						Authorization: `Bearer ${localStorage.getItem("2FA_TOKEN")}`
					},
				}
			);

			let data = res.data;
			console.log(data);
		} catch (e) {
			console.error(e);
		}
	});

export const testGenerateTheOtp = createAsyncThunk(
	"moreAction/testGenerateTheOtp",
	async () => {
		// so now i am not be able to do anything 
		return new Promise((resolve) => {
			setTimeout(() => {
				resolve("OTP Generated"); // Must resolve something
			}, 2000);
		});
	}
);

export const generateTheOtp = createAsyncThunk(
	"moreAction/generateTheOtp",
	async (_, thunkAPI) => {
		try {
			let res = await axios.post(
				"http://localhost:8080/api/generateOtp",
				{},
				{
					headers: {
						Authorization: `Bearer ${localStorage.getItem("token")}`
					}
				}
			);

			return res.data;
		} catch (error) {
			console.error("Error with generating the OTP API");
			return thunkAPI.rejectWithValue(error.response?.data || "Failed to generate OTP");
		}
	});

// actually we don't need the getTheOtp method because we are doing the validation in the backend
export const getTheOtp = createAsyncThunk(
	"moreAction/getTheOtp",
	async (_, thunkAPI) => {
		try {
			let res = await axios.get(
				"http://localhost:8080/api/getOtpByUserId",
				{
					headers: {
						Authorization: `Bearer ${localStorage.getItem("token")}`
					}
				}
			);

			return res.data;
		} catch (error) {
			console.error("Error getting the otp for the user");
			return thunkAPI.rejectWithValue(error.response?.data || "Failed to getting the OTP");
		}
	});

// so we are managing the otpForm state properly
const initialState = {
	is2FaValid: false,
	moreActionValidated: false,
	otpFormState: false,
	loadingPhase: false,
	error: null,
	otp: "",
	activeCard: null
}

const moreActionSlice = createSlice({
	name: "moreAction",
	initialState,
	reducers: {
		setOtpFormState: (state, action) => { 
			state.otpFormState = action.payload;
		},
		setIs2FaValid: (state, _action) => {
			state.is2FaValid = true;
		},
		testReducer: () => {
			console.log("Hello this is the testReducer");
		},
		setActiveCard: (state, action) => {
			state.activeCard = action.payload;
			localStorage.setItem("activeCard", action.payload);
		},
		loadActiveCard: (state, _action) => {
			let localActiveCard = localStorage.getItem("activeCard");
			if (localActiveCard) {
				state.activeCard = localActiveCard;
			}
		}
	},
	extraReducers: (builder) => {
		builder
			.addCase(generateTheOtp.pending, (state, _action) => {
				state.loadingPhase = true;
			})
			.addCase(generateTheOtp.fulfilled, (state, _action) => {
				state.loadingPhase = false;
				state.otpFormState = true;
			})
			.addCase(generateTheOtp.rejected, (state, action) => {
				state.loadingPhase = false;
				state.error = action.payload;
			})

			.addCase(getTheOtp.fulfilled, (state, action) => {
				state.otp = action.payload;
			})

			.addCase(testGenerateTheOtp.pending, (state, _action) => {
				state.loadingPhase = true;
			})
			.addCase(testGenerateTheOtp.fulfilled, (state, _action) => {
				state.loadingPhase = false;
				state.otpFormState = true;
			})
			.addCase(testGenerateTheOtp.rejected, (state, action) => {
				state.loadingPhase = false;
				state.error = action.payload;
			})

			.addCase(validateThe2FaToken.fulfilled, (state, action) => {
				state.error = null;
				state.is2FaValid = true;
			})
			.addCase(validateThe2FaToken.rejected, (state, action) => {
				state.error = action.payload;
			})
	}
});

export const { setOtpFormState, setActiveCard } = moreActionSlice.actions;
export default moreActionSlice.reducer;

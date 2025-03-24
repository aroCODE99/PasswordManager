import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";

export const validateOtp = createAsyncThunk(
	"otp/validateOtp",
	async(enteredOtp, thunkApi) => {
		try {
			let res = await axios.post(
				"http://localhost:8080/api/validateTheOtp",
				enteredOtp,
				{
					headers: {
						Authorization: `Bearer ${localStorage.getItem("token")}`,
					}
				}
			);

			return res.data; // now the action.payload is this
		} catch (e) {
			return thunkApi.rejectWithValue(e.response?.date.response?.data?.message || "Otp validation failed");
		}
	}
);

// shoudln't you think i should be keeping track if the user is 2fa_validated or not
const initialState = {
	token_2fa: "",
	otpState: false,
	otpError: null,
	is2FaValid: false,
	loading: false
}

const otpSlice = createSlice({
	name: "otp",
	initialState,
	reducers: {
		getToken: (state) => {
			state.token_2fa = localStorage.getItem("TOKEN_2FA");
		},
		clearToken: (state) => {
			state.token_2fa = "";
			state.is2FaValid = false;
			localStorage.removeItem("TOKEN_2FA");
		}
	},
	extraReducers: (builder) => {
		builder
			.addCase(validateOtp.pending, (state) => {
				state.otpState = true;
				state.loading = true;
			})
			.addCase(validateOtp.fulfilled, (state, action) => {
				state.otpState = false;
				state.token_2fa = action.payload;
				state.is2FaValid = true;
				state.loading = false;

				if (!localStorage.getItem("TOKEN_2FA")) {
					localStorage.setItem("TOKEN_2FA", action.payload);
				}
			})
			.addCase(validateOtp.rejected, (state, action) => {
				state.otpState = false;
				state.otpError = action.payload;
				state.loading = false;
			})
	}
});

export const { getToken, clearToken } = otpSlice.actions;
export default otpSlice.reducer;

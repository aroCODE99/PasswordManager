import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";

export const fetchPasswords = createAsyncThunk(
	"password/fetchPasswords", // this is just 
	// making the addPasswordApi work
	async (_, { rejectWithValue }) => {
		try {
			let res = await axios.post(
				"http://localhost:8080/api/findAll",
				{}, // Empty body (since it's a POST request)
				{
					headers: {
						Authorization: `Bearer ${localStorage.getItem("token")}`,
					},
				}
			);

			return res.data;
		} catch (error) {
			return rejectWithValue(error.message);
		}
	}
);


// now i will make work with the api of adding the password
export const addPasswordToDb = createAsyncThunk(
	"password/addPasswordToDb",
	async (passwordData, { rejectWithValue }) => {
		console.log("hitted the addNewPassword api");
		try {
			let res = await axios.post(
				"http://localhost:8080/api/addNewPassword",
				passwordData,
				{
					headers: {
						Authorization: `Bearer ${localStorage.getItem("token")}`,
					},
				},
			);

			console.log(res.data);
			return res.data;
		} catch (error) {
			rejectWithValue(error.message);
		}
	}
);

export const updatePassword = createAsyncThunk(
	"password/updatePassword",
	async (updatedData, { rejectWithValue }) => {
		try {
			let res = await axios.post(
				`http://localhost:8080/api/update/${updatedData.passwordId}`,
				updatedData,
				{
					headers: {
						Authorization: `Bearer ${localStorage.getItem("token")}`,
					},
				}
			);

			return res.data;
		} catch (e) {
			rejectWithValue(e.message);
		}
	},
);

export const deletePassword = createAsyncThunk(
	"password/deletePassword",
	async (passwordId, thunkApi) => {
		try {
			// so in delete Request we don't pass the body but in post it is required 
			let res = await axios.delete(
				`http://localhost:8080/api/deleteById/${passwordId}`,
				{
					headers: {
						Authorization: `Bearer ${localStorage.getItem("token")}`
					}
				}
			);

			return res.data;
		} catch (e) {
			thunkApi.rejectWithValue(e.message);
		}
	}
);

const initialState = {
	passwords: [],
	loading: false,
	error: null,
	searchQuery: ""
}

const passwordSlice = createSlice({
	name: "password",
	initialState,
	reducers: {
		testReduer: () => {
			console.log("helloo how are yall this is the test reducer");
		},
		setSearchQuery: (state, action) => {
			state.searchQuery = action.payload;
			
		}
	},
	extraReducers: (builder) => {
		builder
			.addCase(fetchPasswords.pending, (state) => {
				state.loading = true;
				state.error = null;
			})
			.addCase(fetchPasswords.fulfilled, (state, action) => {
				state.loading = false;
				state.passwords = action.payload;
			})
			.addCase(fetchPasswords.rejected, (state, action) => {
				state.loading = false;
				state.error = action.payload;
			})

			.addCase(addPasswordToDb.pending, (state) => {
				state.loading = true;
				state.error = null;
			})
			.addCase(addPasswordToDb.fulfilled, (state, action) => {
				state.loading = false;
				state.passwords = [...state.passwords, action.payload];
			})
			.addCase(addPasswordToDb.rejected, (state, action) => {
				state.loading = false;
				state.error = action.payload;
			})

			.addCase(updatePassword.fulfilled, (state, action) => {
				state.passwords = state.passwords.filter((p) => p.passwordId !== action.payload.passwordId);
				state.passwords = [action.payload, ...state.passwords];
			})

			.addCase(deletePassword.fulfilled, (state, action) => {
				state.loading = false;
				state.passwords = state.passwords.filter((p) => p.passwordId !== action.payload);

				if (action.payload === parseInt(localStorage.getItem("activeCard"))) {
					if (state.passwords.length > 0) {
						localStorage.setItem("activeCard", state.passwords[0].passwordId);
					} else {
						localStorage.removeItem("activeCard"); // Clear activeCard if no passwords are left
					}
				}
			})
			.addCase(deletePassword.rejected, (state, action) => {
				state.loading = false;
				state.error = action.payload;
			})
	},
});

export const { setSearchQuery } = passwordSlice.actions;
export default passwordSlice.reducer;

/*
✔  reducers (with s) → Contains multiple reducer functions inside createSlice().
✔  reducer (without s) → The single combined reducer function created automatically by Redux Toolkit.
✔  Redux store expects one reducer per slice, which is why we export passwordSlice.reducer.
*/

// now we could do something like whenever we add the new password we close the modal and we show the spinner

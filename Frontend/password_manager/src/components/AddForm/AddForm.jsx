import { useDispatch, useSelector } from "react-redux";
import styles from "./AddForm.module.css";
import { IoIosClose } from "react-icons/io";
import { addPasswordToDb } from "../../reduxStore/slices/passwordSlice";
import { ClipLoader } from "react-spinners";

const AddForm = ({ 
	onClose,
	url,
	setUrl,
	password,
	setPassword,
	confirmPassword,
	setConfirmPassword,
	note,
	setNote
}) => {

	const { loading } = useSelector((state) => state.password); 
	// dispatch to dispatch the actions to the store
	const dispatch = useDispatch();

	function handleSubmit(e) {
		e.preventDefault();

		if (password !== confirmPassword) {
			alert("Please enter the same password");
			setPassword("");
			setConfirmPassword("");
			return;
		}

		const addUserData = { url, password, note };

		dispatch(addPasswordToDb(addUserData))
			.unwrap()
			.then(() => {
				// Optional: dispatch(fetchPasswords()); // only if your list needs to refresh
				setUrl("");
				setPassword("");
				setConfirmPassword("");
				setNote("");
				onClose();
			})
			.catch((error) => {
				console.error("Error adding password:", error);
			});
	}

	return (
		<form onSubmit={handleSubmit}>
			<h2 className={styles.form_title}>Add Password</h2>

			{/* Url field */}
			<label className={styles.form_label}>Add Url</label>
			<input
				value={url}
				onChange={(e) => setUrl(e.target.value)}
				className={styles.form_input}
				placeholder="Enter the url"
				type="url"
				required
			/>

			{/* Password field */}
			<label className={styles.form_label}>Add Password</label>
			<input
				value={password}
				onChange={(e) => setPassword(e.target.value)}
				className={styles.form_input}
				placeholder="Enter your password"
				type="password"
				required
				auto-complete="new-password"
			/>

			{/* Confirm Password field */}
			<label className={styles.form_label}>Confirm Password</label>
			<input
				value={confirmPassword}
				onChange={(e) => setConfirmPassword(e.target.value)}
				className={styles.form_input}
				placeholder="Confirm your password"
				type="password"
				required
				auto-complete="new-password"
			/>

			{/* Adding Note Field */}
			<label className={styles.form_label}>Note</label>
			<input 
				value={note}
				onChange={(e) => setNote(e.target.value)}
				className={styles.form_input}
				type="text"
				placeholder="Add the Note ..."
			/>

			{/* Submit button */}
			<button type="submit" className={styles.form_button}>
				{/* <ClipLoader size={10} color={"#ffffff"} loading={true} /> */}
				Submit
			</button>

		</form>
	);
};

export default AddForm;

// SAVING THE STATES INSIDE THE form 
// here we had the state inside the parent component and so every time it rerender the AddForm component it did not change the state
// now what we could do is first let's make the addPasswordApi to return the saved password so what will happen with this is we could be
// able to save the password that returned by the addNewPassword to the database api  
// so the thing is that it should be doing 
// now how do i trigger the fetchPasswords after closing saving the password

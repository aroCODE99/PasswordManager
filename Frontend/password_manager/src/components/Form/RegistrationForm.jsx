import { useRef } from "react";
import styles from "./Login.module.css";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";

const RegistrationForm = () => {
	const emailRef = useRef(null);
	const passwordRef = useRef(null);
	const confirmPasswordRef = useRef(null);
	const navigate = useNavigate();

	// Send registration data to server
	async function addUser(userData) {
		try {
			const res = await axios.post("http://localhost:8080/api/register", userData, {
				headers: {
					"Content-Type": "application/json",
				},
			});
			console.log("User registered: ", res.data);
			navigate("/login");
		} catch (e) {
			console.error("An error occurred", e);
		}
	}

	function handleFormSubmit(event) {
		event.preventDefault();

		const registerForm = {
			email: emailRef.current.value,
			password: passwordRef.current.value,
		};

		let confirmPassword = confirmPasswordRef.current.value;

		if (confirmPassword !== registerForm.password) {
			alert("Passwords do not match");
			confirmPasswordRef.current.value = "";
			return;
		}

		addUser(registerForm);

		emailRef.current.value = "";
		passwordRef.current.value = "";
		confirmPasswordRef.current.value = "";
	}

	return (
		<div className={styles.loginPage}>
			<div className={styles.form}>
				<div className={styles.form_container}>
					<h1 className={styles.form_title}>Register</h1>
					<form onSubmit={handleFormSubmit}>
						<label htmlFor="email">Email: </label>
						<input ref={emailRef} type="email" id="email" placeholder="email..." required />

						<label htmlFor="password">Password: </label>
						<input ref={passwordRef} type="password" id="password" placeholder="password..." required />

						<label htmlFor="confirmPassword">Confirm Password: </label>
						<input ref={confirmPasswordRef} type="password" id="confirmPassword" placeholder="confirm password..." required />

						<button className={styles.submit_button} type="submit">Submit</button>

						<Link className={styles.path} to="/login">
							Already have an account? <span>Login</span>
						</Link>
					</form>
				</div>
				<img src="event-1.png" alt="Event" />
			</div>
		</div>
	);
};

export default RegistrationForm;


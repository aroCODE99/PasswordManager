import { Link } from "react-router-dom";
import styles from "./Login.module.css";
import { useContext, useRef } from "react";
import AuthContext from "../../stores/AuthProvider";

function Login() {
	const emailRef = useRef(null);
	const passwordRef = useRef(null);
	const { login } = useContext(AuthContext);

	async function handleLogin(event) {
		event.preventDefault();
		const loginData = {
			email: emailRef.current.value,
			password: passwordRef.current.value,
		};

		login(loginData); // Calls login function from AuthContext
	}

	return (
		<div className={styles.loginPage}> 
			{/* this is login form */}
			<div className={styles.form}>
				<div className={styles.form_container}>
					<h1 className={styles.form_title}>Login</h1>
					<form onSubmit={handleLogin}>
						<label htmlFor="email">Email: </label>
						<input ref={emailRef} type="email" id="email" name="email" placeholder="email..." required />  

						<label htmlFor="password">Password: </label>
						<input ref={passwordRef} type="password" id="password" name="password" placeholder="password..." required />

						<button className={styles.submit_button} type="submit">Submit</button>

						<Link className={styles.path} to="/register">
							Don't have an account? <span>Register</span>
						</Link>
					</form>
				</div>
				<img src="event-1.png"/>
			</div>
		</div>
	);
}

export default Login;

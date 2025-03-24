import {createContext, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

// first we create the Context
const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
	const [user, setUser] = useState(() => JSON.parse(localStorage.getItem("user")) || null); // i think it will store the email and password

	const [token, setToken] = useState(() => localStorage.getItem("token") || null);

	const navigate = useNavigate();

	async function getToken(loginData) {
		try {
			const res = await axios.post("http://localhost:8080/api/login", loginData);

			let data = res.data;

			return data;
		} catch (e) {
			console.error("Error Logging in " + e);
		}
	}

	const login = async (userData) => {
		try {
			let fetchedToken = await getToken(userData);

			console.log(fetchedToken);
			if (fetchedToken) {
				setToken(fetchedToken);
				setUser({email: userData.email});

				localStorage.setItem("token", fetchedToken);
				localStorage.setItem("user", JSON.stringify(userData.email));

				navigate("/dashboard");
			} else {
				alert("Login failed");
			}
		} catch(e) {
			console.error("Login failed: ", e);
		}
	}

	const logout = () => {
		setUser(null);
		setToken(null);
		localStorage.removeItem("user");
		localStorage.removeItem("token");
		localStorage.removeItem("TOKEN_2FA");
		localStorage.removeItem("activeCard");

		navigate("/login");
	};

	const isAuthenticated = () => {
		return token !== null;
	};

	// now i have to make this to validate the otp
	const validateToken = async () => {
		try {
			let res = await axios.get("http://localhost:8080/api/validateToken", {
				headers: {
					Authorization: `Bearer ${localStorage.getItem("token")}`
				}
			});

			if (res.status === 200) {
				return res.data; // Valid token
			}
		} catch (e) {
			if (e.response && e.response.status === 401) {
				if (e.response.data === "Token expired.") {
					alert("Session expired. Please log in again.");
				} else {
					alert("Invalid token. Logging out.");
				}

				logout(); // your logout function
			} else {
				console.error("Unexpected error validating token:", e);
			}
		}
	};

	return <AuthContext.Provider value={{user, token, login, logout, isAuthenticated, validateToken}}>
		{ children }
	</AuthContext.Provider>;
}

export default AuthContext;

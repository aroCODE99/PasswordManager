import { useContext } from "react";
import AuthContext from "../stores/AuthProvider";
import { Navigate } from "react-router-dom";

const PublicRoute = ({ children }) => {
	// now i have to first validate the token in the localstorage every time the user opens the app
	// for now let's leave this as it is

	const { token } = useContext(AuthContext);

	return token ? <Navigate to="/Dashboard" replace /> : children;
}

export default PublicRoute;

import { useContext, useEffect } from "react";
import { Navigate, Outlet } from "react-router-dom";
import AuthContext from "../stores/AuthProvider";
import axios from "axios";

const PrivateRoute = () => {
	const { token, validateToken, logOut } = useContext(AuthContext); 

	// this is for checking the token
	useEffect(() => {
		validateToken();
	}, [token]);

	return token ? <Outlet /> : <Navigate to="/login" replace />;
}

export default PrivateRoute;

// By default, navigate(path) pushes a new entry into the history stack, allowing users to go back.
// However, navigate(path, { replace: true }) replaces the current history entry, preventing back navigation.

// Feature					<Navigate> Component		useNavigate() Hook
// Usage					Redirects when rendered		Used inside event handlers
// Where to use				Inside JSX					Inside functions/hooks
// Can replace history?		Yes (replace prop)			Yes ({ replace: true })
// Can be used on button	❌ No						✅ Yes
// clicks?	

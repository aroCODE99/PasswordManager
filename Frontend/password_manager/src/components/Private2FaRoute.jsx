import { Navigate, Outlet } from "react-router-dom";

const Private2FaRoute = () => {
	const token = localStorage.getItem("TOKEN_2FA");

	return token ? <Outlet /> : <Navigate to="/dashboard" replace />;
};

export default Private2FaRoute;


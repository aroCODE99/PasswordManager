import {BrowserRouter,  Routes, Route} from "react-router-dom";
import Login from "./components/Form/Login.jsx";
import Register from "./components/Form/RegistrationForm.jsx"
import Layout from "./components/Layout/Layout.jsx";
import ManagedPasswords from "./components/Home/ManagedPasswords.jsx";
import {AuthProvider} from "./stores/AuthProvider.jsx";
import AddForm from "./components/AddForm/AddForm.jsx";
import ModuleToAddForm from "./components/AddForm/ModuleToAddForm.jsx";
import PrivateRoute from "./components/PrivateRoute.jsx";
import Private2FaRoute from "./components/Private2FaRoute.jsx";
import PublicRoute from "./components/PublicRoute.jsx";
import MoreActionLayout from "./components/MoreAction/MoreActionLayout.jsx";
import MoreActionRightSection from "./components/MoreAction/MoreAction/MoreActionRightSection.jsx";
import LandingPage from "./components/LandingPages/LandingPage.jsx";

const App = () => {
	return(
		<BrowserRouter> 
			<AuthProvider>
				<Routes>
					{/* Landing Page (public) */}
					<Route index element={<LandingPage />} /> {/* Public */}

					{/* Public Routes */}
					<Route path="/login" element={<PublicRoute><Login /></PublicRoute>} />
					<Route path="/register" element={<PublicRoute><Register /></PublicRoute>} />

					{/* Private Routes (Require Authentication) */}
					<Route path="/dashboard" element={<Layout />}>
						<Route element={<PrivateRoute />}>
							<Route index element={<ManagedPasswords />} />
							<Route path="AddForm" element={<AddForm />} />
							<Route path="ModuleToAddForm" element={<ModuleToAddForm />} />

							<Route element={<Private2FaRoute />}>
								<Route path="moreAction" element={<MoreActionLayout />}>
									<Route index element={<MoreActionRightSection />} />
								</Route>
							</Route>
						</Route>
					</Route>
				</Routes>
			</AuthProvider>
		</BrowserRouter>
	);
}

export default App;

// so at '/' let's make the deault login page
// so at '/register' let's make the register page
// so first we have to make the route and then we have to link it like we did in the register 
//
//The Outlet component in React Router DOM is a placeholder that renders child routes within a parent route.
//It's a useful tool for creating complex navigation structures and sharing layouts across multiple routes. 

import { useContext, useEffect, useRef, useState } from "react";
import styles from "./NavBar.module.css";
import AuthContext from "../../stores/AuthProvider";
import { TbLogout2 } from "react-icons/tb";
import { Link } from "react-router-dom";
import ThemeContext from "../../stores/ThemeContext";
import { useDispatch } from "react-redux";
import { setSearchQuery } from "../../reduxStore/slices/passwordSlice";
import { getGravatarUrl } from "../utils/getGravatarUrl";
import { getAvatarUrl } from "../utils/getAvatarUrl";

const NavBar = () => {
	const { logout } = useContext(AuthContext);
	const dropdownRef = useRef(null); // Ref for dropdown
	const [toggleDropdown, setToggleDropdown] = useState(false);
	const { toggleTheme } = useContext(ThemeContext); // so this is the themeContext
	const dispatch = useDispatch();
	// const userEmail = user?.email || localStorage.getItem("user")?.replace(/"/g, ""); // fallback if needed
	const userEmail = localStorage.getItem("user").replace(/"/g, "");
	const avatarUrl = getAvatarUrl(userEmail);
	const gravatarUrl = getGravatarUrl(userEmail);
	console.log(userEmail);


	const handleToggleTheme = () => {
		toggleTheme();
	};

	const handleProfileDropDown = () => {
		setToggleDropdown((prev) => !prev);
	};


	const handleSearchQuery = (e) => {
		let query = e.target.value.trim();
		dispatch(setSearchQuery(query));
	};

	// Close dropdown when clicking outside
	useEffect(() => {
		const handleClickOutside = (event) => {
			if (!dropdownRef.current.contains(event.target)) {
				setToggleDropdown(false);
			}
		};
		document.addEventListener("click", handleClickOutside);

		// this is the remove/cleanup function 
		// To remove the event listener when the component unmounts.
		// Prevents memory leaks and ensures no unnecessary event listeners are left behind.
		return () => document.removeEventListener("click", handleClickOutside);
	}, []);

	return (
		<div className={styles.navBar}>
			<ul>
				<Link className={styles.logo} to="/dashboard"> 
					<img src="passLogo.svg" />
					Passhub
				</Link>
			</ul>

			<ul>
				<li> 
					<input onChange={handleSearchQuery} className={styles.searchBar} type="text" placeholder="Search..." />
				</li>

				<li className={styles.profileContainer} ref={dropdownRef}>
					<img
						onClick={handleProfileDropDown}
						src={avatarUrl}
						alt="Profile"
						className={styles.profileImg}
					/>

					{toggleDropdown && (
						<ul className={styles.dropDown}>
							<li className={styles.logout}>
								<Link to="/"> </Link>
								<TbLogout2 /> 
								<button onClick={logout}>Logout</button>
							</li>
						</ul>
					)}
				</li>
			</ul>
		</div>
	);
};

export default NavBar;


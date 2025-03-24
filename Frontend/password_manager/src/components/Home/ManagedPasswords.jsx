import PasswordCard from "./PasswordCard";
import styles from "./ManagedPasswords.module.css";
import ModuleToAdd from "../AddForm/ModuleToAddForm";
import NothingMssg from "./NothingMssg";
import { useDispatch, useSelector } from "react-redux";
import { fetchPasswords } from "../../reduxStore/slices/passwordSlice";
import { useEffect, useState } from "react";
import { ClipLoader } from "react-spinners";
import ErrorPage from "../ErrorPages/ErrorPage.jsx";
import OTPModal from "./OTPModal.jsx";
import { getToken } from "../../reduxStore/slices/otpSlice.js";
import { useNavigate } from "react-router-dom";
import { getSiteName } from "../utils/passwordUtility.js";

const ManagedPasswords = () => {
	const navigate = useNavigate();
	const dispatch = useDispatch();
	let { passwords, loading, error } = useSelector((state) => state.password);
	let { otpFormState } = useSelector((state) => state.moreAction);
	let { token_2fa } = useSelector((state) => state.otp);
	const [moreActionClicked, setMoreActionClicked] = useState(false);
	const { searchQuery } = useSelector((state) => state.password);

	console.log(searchQuery);
	const filteredPasswords = passwords.filter((password) =>
		getSiteName(password.url).toLowerCase().includes(searchQuery.toLowerCase())
	);

	useEffect(() => {
		dispatch(fetchPasswords());
		dispatch(getToken());
	}, [dispatch]);

	if (loading) {
		return (
			<div className={styles.spinner_container}>
				<ClipLoader size={80} color={"#123abc"} loading={true} />
			</div>
		);
	}

	if (error) {
		return <ErrorPage error={error} />
	}

	if (moreActionClicked && token_2fa) {
		navigate("/dashboard/moreAction");
	}

	return (
		<>
			{ !token_2fa && otpFormState && <OTPModal /> }

			{filteredPasswords.length === 0 ? (
				<NothingMssg />
			) : (
				<>
					<div className={styles.insightsPanel}>
						<div>
							<span className={styles.insightLabel}>🔐 Total:</span>
							<span>{passwords.length}</span>
						</div>
						<div>
							<span className={styles.insightLabel}>⚠️ Weak:</span>
							<span>{passwords.filter(p => p.decryptedPassword.length < 8).length}</span>
						</div>
						<div>
							<span className={styles.insightLabel}>🕒 Recently Added:</span>
							<span>
								{
									passwords.filter(p => {
										const added = new Date(p.createdAt); // Assuming p.createdAt exists
										const now = new Date();
										const diffHours = (now - added) / (1000 * 60 * 60);
										return diffHours <= 24;
									}).length
								}
							</span>
						</div>
						<div>
							<span className={styles.insightLabel}>⏱️ Last Sync:</span>
							<span>{new Date().toLocaleTimeString()}</span>
						</div>
					</div>

					{/* Header */}
					<div className={styles.pageLabel}>
						<h2>🔐 Managed Passwords</h2>
					</div>

					<div className={styles.grid}>
						{
							filteredPasswords.map((password) => (
								<PasswordCard
									key={password.passwordId}
									passwordId={password.passwordId}
									passwordUrl={password.url}
									password={password.decryptedPassword}
									setMoreActionClicked={setMoreActionClicked}
								/>
							))
						}
					</div>
					<ModuleToAdd mssg={"Add Password"}/>
				</>
			)}
		</>	

	);
}

export default ManagedPasswords;

// so, What is the favicons ?
// A favicon, short for "favorite icon," is a small image that represents a website and is typically displayed in the browser tab,
// address bar, or bookmarks section, allowing users to easily identify a site among others they have open; it's essentially a tiny logo for a website. 
// so we could use this to fetch the icons and show in the front of the card
// 
// so now let's try to design the cards for the each saved passwords
//
// so i want the CARD To have the followings:
// 1. at the first sight it should display the image of the url and url itself 
// 2. password should be also shown but we also want the clipboard functionality

// now let's discuss the structure of the app
// first we have the managedPassword

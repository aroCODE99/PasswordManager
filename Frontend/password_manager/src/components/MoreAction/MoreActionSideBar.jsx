import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchPasswords } from "../../reduxStore/slices/passwordSlice";
import styles from "./MoreAction.module.css";
import MoreActionPasswordCard from "./MoreActionPasswordCard";
import { getSiteName, getFavicon } from "../utils/passwordUtility";
import { setActiveCard } from "../../reduxStore/slices/moreActionSlice";
import { Cross, Plus, Search, X } from "lucide-react";
import ConfirmDeleteModal from "./DeleteButtonModal/ConfirmDeleteModal";
import ModuleToAdd from "../AddForm/ModuleToAddForm";

const MoreActionSideBar = () => {
	const dispatch = useDispatch();
	const { passwords } = useSelector((state) => state.password);
	const { activeCard } = useSelector((state) => state.moreAction);
	const [searchQuery, setSearchQuery] = useState("");
	const [toggleDeleteModal, setToggleDeleteModal] = useState(false);
	const [passwordIdToDelete, setPasswordIdToDelete] = useState(null);

	useEffect(() => {
		dispatch(fetchPasswords()).unwrap();
	}, [dispatch]);

	useEffect(() => {
		let prevActiveCard = parseInt(localStorage.getItem("activeCard"));

		if (!prevActiveCard) {
			if (passwords.length > 0) {
				dispatch(setActiveCard(passwords[0].passwordId));
			}
		} else {
			dispatch(setActiveCard(prevActiveCard));
		}

	}, [passwords, dispatch]);

	const filteredPasswords = passwords.filter((password) =>
		getSiteName(password.url).toLowerCase().includes(searchQuery.toLowerCase())
	);

	const handleAddPassword = () => {
		console.log("hello from the handleAddPassword");
	};

	return (
		<aside className={styles.sideBarContainer}>
			<div className={styles.sideBarHeader}>
				<h2 className={styles.sideBarTitle}>Saved Passwords</h2>
				<ModuleToAdd mssg={"Add"}/>
			</div>

			{/* Search Bar */}
			<div className={styles.searchContainer}>
				<Search className={styles.searchIcon} size={18} />
				<input
					type="text"
					placeholder="Search passwords..."
					value={searchQuery}
					onChange={(e) => setSearchQuery(e.target.value)}
					className={styles.searchInput}
				/>
				<X style={{cursor: "pointer"}} onClick={() => setSearchQuery("")}/>
			</div>

			{/* Password List */}
			<ul className={styles.passwordList}>
				{filteredPasswords.length > 0 ? (
					filteredPasswords.map((password) => (
						<MoreActionPasswordCard
							key={password.passwordId}
							passwordData={{
								siteName: getSiteName(password.url),
								siteSvg: getFavicon(password.url),
								passwordId: password.passwordId,
								passwordCreatedAt: password.createdAt,
							}}
							setDeleteToggleModal={setToggleDeleteModal}
							activeCard={activeCard}
							setActiveCard={setActiveCard}
							setPasswordIdToDelete={setPasswordIdToDelete}
						/>
					))
				) : (
					<p className={styles.emptyMessage}>No passwords found.</p>
				)}

				{ toggleDeleteModal && <ConfirmDeleteModal passwordId={passwordIdToDelete} setDeleteToggleModal={setToggleDeleteModal}/> }
			</ul> 
		</aside>
	);
};

export default MoreActionSideBar;


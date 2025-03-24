import { useEffect, useState } from "react";
import styles from "./MoreActionRightSection.module.css";
import { Eye, Pencil, Copy, Globe, Lock, Check, X, StickyNote, Delete } from "lucide-react";
import { useSelector, useDispatch } from "react-redux";
import { getSiteName } from "../../utils/passwordUtility";
import { updatePassword } from "../../../reduxStore/slices/passwordSlice";
import PasswordGenerator from "../PasswordGenerator/PasswordGenerator.jsx";
import zxcvbn from "zxcvbn";

const strengthGradient = [
	"linear-gradient(to right, #ff4d4d, #ff9999)", // Weak
	"linear-gradient(to right, #ff9900, #ffcc66)", // Fair
	"linear-gradient(to right, #ffd700, #ffeb3b)", // Good
	"linear-gradient(to right, #32cd32, #66ff66)", // Strong
	"linear-gradient(to right, #008000, #00cc00)", // Very Strong
];

const MoreAction = () => {
	const dispatch = useDispatch();
	const { passwords } = useSelector((state) => state.password);
	const { activeCard } = useSelector((state) => state.moreAction);

	const activePassword = passwords.find((password) => (
		password.passwordId === activeCard
	));

	const [showPassword, setShowPassword] = useState(false);
	const [editMode, setEditMode] = useState(false);
	const [editedPassword, setEditedPassword] = useState("");
	const [editedURL, setEditedURL] = useState("");
	const [editedNote, setEditedNote] = useState("");

	const passwordStrength = zxcvbn(editMode ? editedPassword : activePassword?.decryptedPassword || "");
	const strengthLevels = ["Weak", "Fair", "Good", "Strong", "Very Strong"];

	// syncing the input values
	useEffect(() => {
		if (editMode) {
			handleEditClick(activeCard);
		}
	}, [activeCard, editMode]);


	if (!activePassword) return <div className={styles.placeholder}>No password selected</div>;

	const siteName = getSiteName(activePassword.url).toUpperCase();

	const handleEditClick = () => {
		setEditMode(true);
		setEditedPassword(activePassword.decryptedPassword);
		setEditedURL(activePassword.url);
		setEditedNote(activePassword.note || "");
	};

	const handleSaveClick = async () => {
		await dispatch(updatePassword({
			passwordId: activePassword.passwordId,
			decryptedPassword: editedPassword,
			url: editedURL,
			note: editedNote,
		})).unwrap();

		// window.location.reload();
		setEditMode(false);
	};

	return (
		<div className={styles.moreActionContainer}>

			{/* psuedo Header */}
			<div className={styles.moreActionHeader}>
				<h2 className={styles.siteName}>{siteName}</h2>
				{editMode ? (
					<div className={styles.editModeButtons}>
						<button className={styles.saveButton} onClick={handleSaveClick}>
							<Check size={16} />
						</button>
						<button className={styles.cancelButton} onClick={() => setEditMode(false)}>
							<X size={16} />
						</button>
					</div>
				) : (
					<button className={styles.editButton} onClick={handleEditClick}>
						<Pencil size={16} /> Edit
					</button>
				)}
			</div>

			{/* Password */}
			<div className={styles.detailsCard}>
				<Lock size={16} />
				<span className={styles.label}>Password</span>
				{editMode ? (
					<input
						type="text"
						value={editedPassword}
						onChange={(e) => setEditedPassword(e.target.value)}
						className={styles.inputField}
					/>
				) : (
					<span className={styles.value}>
						{showPassword ? activePassword.decryptedPassword : "*".repeat(activePassword.decryptedPassword.length)}
					</span>
				)}
				{!editMode && (
					<>
						<Eye size={16} onClick={() => setShowPassword(!showPassword)} className={styles.icon} />
						<Copy size={16} className={styles.icon} onClick={() => navigator.clipboard.writeText(activePassword.decryptedPassword)} />
					</>
				)}
			</div>


			{/* Strength bar */}
			{editMode && (
				<div className={styles.passwordStrengthContainer}>
					<div className={styles.progressBarContainer}>
						<div
							className={`${styles.progressBar} ${passwordStrength.score < 2 ? styles.weakPulse : ""}`}
							style={{
								width: `${(passwordStrength.score + 1) * 20}%`,
								background: strengthGradient[passwordStrength.score],
							}}
						></div>
					</div>
					<span className={styles.strengthText}>
						{strengthLevels[passwordStrength.score]}
					</span>

					{/* Show improvement suggestions if password is weak */}
					{passwordStrength.score < 3 && passwordStrength.feedback.suggestions.length > 0 && (
						<ul className={styles.suggestions}>
							{passwordStrength.feedback.suggestions.map((suggestion, index) => (
								<li key={index}>{suggestion}</li>
							))}
						</ul>
					)}
				</div>
			)}

			{/* Website */}
			<div className={styles.detailsCard}>
				<Globe size={16} />
				<span className={styles.label}>Website</span>
				{editMode ? (
					<input 
						type="text"
						value={editedURL}
						onChange={(e) => setEditedURL(e.target.value)}
						className={styles.inputField}
					/>
				) : (
					<a href={activePassword.url} target="_blank" rel="noopener noreferrer" className={styles.website}>
						{activePassword.url}
					</a>
				)}
			</div>

			{/* Note */}
			<div className={styles.detailsCard}>
				<StickyNote size={16} />
				<span className={styles.label}>Note</span>
				{editMode ? (
					<textarea
						value={editedNote}
						onChange={(e) => setEditedNote(e.target.value)}
						className={styles.inputField}
						rows={2}
					/>
				) : (
					<span className={styles.noteValue}>{activePassword.note || "No notes available"}</span>
				)}
			</div>

			{editMode && (
				<details className={styles.generatorWrapper}>
					<summary className={styles.generatorSummary}>Generate Strong Password</summary>
					<PasswordGenerator onGenerate={(pwd) => setEditedPassword(pwd)} />
				</details>
			)}
		</div>
	);
};

export default MoreAction;


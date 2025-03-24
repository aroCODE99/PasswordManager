import React, { useState } from "react";
import styles from "./ModuleToAddForm.module.css";
import AddForm from "./AddForm.jsx";

const ModuleToAdd = ({ mssg }) => {
	// these are the states for the form
	const [url, setUrl] = useState("");
	const [password, setPassword] = useState("");
	const [confirmPassword, setConfirmPassword] = useState("");

	const [isModalOpen, setIsModalOpen] = useState(false);
	const openModal = () => setIsModalOpen(true);
	const closeModal = () => setIsModalOpen(false);

	const [note, setNote] = useState("");

	return (
		<div className={styles.container}>
			<button className={styles.addButton} onClick={openModal}>{ mssg }</button>

			{/* now let's see the working of this form */}
			{ isModalOpen && (
				<div className={styles.modalOverlay} onClick={closeModal}>
					<div className={styles.modalContent} onClick={(e) => e.stopPropagation()}>
						<AddForm
							onClose={closeModal}
							url={url}
							setUrl={setUrl}
							password={password}
							setPassword={setPassword}
							confirmPassword={confirmPassword}
							setConfirmPassword={setConfirmPassword}
							note={note}
							setNote={setNote}
						/>
					</div>
				</div>
			)}
		</div>
	);
};

export default ModuleToAdd;


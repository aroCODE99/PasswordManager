import { useDispatch } from "react-redux";
import styles from "./ConfirmDeleteModal.module.css";
import { deletePassword } from "../../../reduxStore/slices/passwordSlice";

const ConfirmDeleteModal = ({ setDeleteToggleModal, passwordId }) => {
	const dispatch = useDispatch();

	return (
		<div className={styles.modalOverlay}>
			<div className={styles.modal}>
				<h3>Do you really want to delete this password?</h3>
				<div className={styles.buttons}>
					<button 
						className={styles.confirmButton} 
						onClick={() => {
							dispatch(deletePassword(passwordId));
							setDeleteToggleModal(false);
						}}
					>
						Yes, Delete
					</button>
					<button 
						className={styles.cancelButton} 
						onClick={() => {
							// onCancel();
							setDeleteToggleModal(false);
						}}
					>
						Cancel
					</button>
				</div>
			</div>
		</div>
	);
};

export default ConfirmDeleteModal;

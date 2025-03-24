import styles from "./MoreAction.module.css";
import { extractDate } from "../utils/passwordUtility";
import { useDispatch } from "react-redux";
import { Trash2 } from "lucide-react";

const MoreActionPasswordCard = ({passwordData, activeCard, setActiveCard, setDeleteToggleModal, setPasswordIdToDelete}) => {
	const { siteName, siteSvg, passwordId, passwordCreatedAt } = passwordData;
	const dispatch = useDispatch();

	const handleDeleteModal = () => {
		setDeleteToggleModal(true);
		setPasswordIdToDelete(passwordId);
	};

	return (
		<>  
			<div 
				style={activeCard === passwordId ? { background: "lightgrey" } : {}}
				className={styles.passwordCardContainer}>
				<img src={siteSvg}/>
				<div onClick={() => dispatch(setActiveCard(passwordId))} >
					<p className={styles.cardSiteName}> {siteName} </p>
					<p className={styles.subtleText}> Created At: {extractDate(passwordCreatedAt)} </p>
				</div>
				<Trash2 onClick={handleDeleteModal} className={styles.deletePassword} size={18} />
			</div>
		</>);
};

export default MoreActionPasswordCard;

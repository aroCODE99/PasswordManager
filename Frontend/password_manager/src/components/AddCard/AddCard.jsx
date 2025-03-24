import styles from "./AddCard.module.css"
import { IoMdAdd } from "react-icons/io";

const AddCard = () => {
	return (
		<div className={styles.cardContainer}>
			<button className={styles.addButton}><IoMdAdd /></button>
		</div>
	);
}

export default AddCard;

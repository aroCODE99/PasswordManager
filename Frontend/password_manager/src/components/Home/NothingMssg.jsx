import ModuleToAdd from "../AddForm/ModuleToAddForm";
import styles from "./NothingMssg.module.css";

const NothingMssg = () => {
	return (
		<div className={styles.container}>
			<h1 className={styles.heading}>You haven't added any passwords yet!</h1>
			<p className={styles.subtext}>Start by adding one now to keep them secure and accessible.</p>
			<ModuleToAdd mssg={"Add Password"}/>
		</div>
	);
};

export default NothingMssg;


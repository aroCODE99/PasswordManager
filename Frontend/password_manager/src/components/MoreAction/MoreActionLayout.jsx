import { Outlet } from "react-router-dom";
import MoreActionSideBar from "./MoreActionSideBar";
import styles from "./MoreAction.module.css";

const MoreActionLayout = () => {
	return (
		<div className={styles.moreActionContainer}>  
			<MoreActionSideBar />
			<Outlet /> 
		</div>
	);
}

export default MoreActionLayout;
// now this is going to be the big ui


import { Outlet } from "react-router-dom";
import NavBar from "../NavBar/NavBar";
import Footer from "../Footer/Footer.jsx";
import styles from "./Layout.module.css"

const Layout = () => {
	return (
		<div className={styles.layout}>
			<NavBar />
			<main> 
				<Outlet />
			</main>
			<Footer />
		</div>
	);
}

export default Layout;

import { Link } from "react-router-dom";
import styles from "./LandingPage.module.css";

const LandingPage = () => {
	return (
		<div className={styles.hero}>
			<div className={styles.content}>
				<h1>Welcome to <span className={styles.brand}>PassHub</span></h1>
				<p className={styles.tagline}>
					The safest way to store and manage your passwords.
				</p>
				<div className={styles.actions}>
					<Link to="/register" className={styles.buttonPrimary}>Get Started</Link>
					<Link to="/login" className={styles.buttonSecondary}>Login</Link>
				</div>
			</div>
		</div>
	);
};

export default LandingPage;


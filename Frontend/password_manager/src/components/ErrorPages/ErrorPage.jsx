import React from "react";
import { FiAlertTriangle } from "react-icons/fi"; // Feather Icons
import styles from "./ErrorPage.module.css";

const ErrorPage = ({ error }) => {
	return (
		<div className={styles.container}>
			<div className={styles.errorBox}>
				<FiAlertTriangle size={48} color="red" />
				<div className={styles.textContainer}>
					<h1 className={styles.title}>Oops! Something went wrong.</h1>
					<p className={styles.message}>{error || "An unexpected error has occurred."}</p>
				</div>
			</div>
			<button
				onClick={() => window.location.reload()}
				className={styles.retryButton}
			>
				Retry
			</button>
		</div>
	);
};

export default ErrorPage;

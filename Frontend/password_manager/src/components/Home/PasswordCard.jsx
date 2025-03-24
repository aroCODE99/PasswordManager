import PropTypes from "prop-types";
import styles from "./ManagedPasswords.module.css";
import { useState, useCallback, useMemo } from "react";
import { useDispatch, useSelector } from "react-redux";
import { generateTheOtp, setActiveCard } from "../../reduxStore/slices/moreActionSlice";
import { ClipLoader } from "react-spinners";
import { getSiteName, getFavicon, getUrlObject } from "../utils/passwordUtility";

function PasswordCard({ passwordId, passwordUrl, password, setMoreActionClicked }) {
	const dispatch = useDispatch();
	const { loadingPhase } = useSelector((state) => state.moreAction);
	const { token_2fa } = useSelector((state) => state.otp);

	const [copyState, setCopyState] = useState(false);
	const [moreActionClick, setMoreActionClick] = useState(false);

	const urlObject = useMemo(() => getUrlObject(passwordUrl), [passwordUrl]);
	const favicon = useMemo(() => getFavicon(passwordUrl), [passwordUrl]);
	const urlName = useMemo(() => getSiteName(passwordUrl), [passwordUrl]);

	const isWeakPassword = useMemo(() => password.length > 0 && password.length < 6, [password]);

	const handleCopyClipBoard = useCallback(() => {
		navigator.clipboard.writeText(password).then(() => {
			setCopyState(true);
			setTimeout(() => setCopyState(false), 1000);
		});
	}, [password]);

	const handleShowMoreActions = useCallback(async () => {
		if (loadingPhase) return;
		setMoreActionClicked(true);
		dispatch(setActiveCard(passwordId));

		try {
			setMoreActionClick(true);
			if (!token_2fa) {
				await dispatch(generateTheOtp()).unwrap();
			}
		} catch (e) {
			console.error(e);
		} finally {
			setMoreActionClick(false);
		}
	}, [dispatch, loadingPhase]);

	return (
		<div className={styles.cardContainer}>
			<a className={styles.topCard} href={urlObject.href} target="_blank" rel="noopener noreferrer">
				<img src={favicon} alt={`${urlName} favicon`} />
				<div className={styles.urlBind}>
					<h2>{urlName}</h2>
					<p>{urlObject.href}</p>
				</div>
			</a>

			<div className={styles.bottomCard}>
				<button style={{ background: copyState ? "blue" : "" }} onClick={handleCopyClipBoard}>
					{copyState ? <span>Copied</span> : <span>Copy Password</span>}
				</button>

				<button onClick={handleShowMoreActions}>
					{moreActionClick && loadingPhase ? (
						<ClipLoader size={10} color={"#ffffff"} loading={true} />
					) : (
						<span> MORE ACTIONS </span>
					)}
				</button>
			</div>

			{/* Subtle weak password warning */}
			{isWeakPassword && (
				<div className={styles.weakPasswordWarning}>
					⚠️ This password may be too short
				</div>
			)}
		</div>
	);
}

PasswordCard.propTypes = {
	passwordUrl: PropTypes.string.isRequired,
	password: PropTypes.string.isRequired,
	setMoreActionClicked: PropTypes.func.isRequired,
};

export default PasswordCard;


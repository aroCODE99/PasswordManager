import { useDispatch, useSelector } from "react-redux";
import styles from "./OTPModal.module.css";
import { useEffect, useState } from "react";
import { getToken, validateOtp } from "../../reduxStore/slices/otpSlice";
import { ClipLoader } from "react-spinners";
import { useNavigate } from "react-router-dom";

const OTPModal = () => {
	const { loading } = useSelector((state) => state.otp);
	const dispatch = useDispatch();
	const [otp, setOtp] = useState("");
	const [shortOtp, setShortOtp] = useState(false);
	const navigate = useNavigate();

	useEffect(() => {
		dispatch(getToken());
	}, [dispatch]);

	const handleOnChange = (e) => {
		const value = e.target.value.replace(/\D|\s/g, ""); // Remove spaces & non-numeric
		setOtp(value);

		if (value.length < 6) {
			setShortOtp(true);
		} else {
			setShortOtp(false);
		}
	}

	const handleSubmit = async () => {
		if (otp.length !== 6) {
			return;
		}
		await dispatch(validateOtp({ otp: otp })).unwrap();
		navigate("/dashboard/moreAction");
	}

	// now i don't know how will i be able to render this
	return (
		<div className={styles.modalOverlay}>
			<div className={styles.modalContent}>
				<p>Otp validation</p>

				<input 
					type="text"
					value={otp}
					onChange={handleOnChange} 
					placeholder="Enter OTP" 
				/>

				{ shortOtp && <p className={styles.shortOtp}> Otp should be of length 6 </p> }

				<div className={styles.buttonGroup}>
					<button onClick={handleSubmit}>
						{ loading ? <ClipLoader size={10} /> : "Submit" } 
					</button>
				</div>
			</div>
		</div>
	);
};


export default OTPModal;

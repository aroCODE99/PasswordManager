import { useState, useRef } from "react";
import styles from "./OTPForm.module.css";
import { useDispatch } from "react-redux";
import { validateOtp } from "../../reduxStore/slices/otpSlice";
import { useNavigate } from "react-router-dom";

const OTPForm = () => {
	const navigate = useNavigate();
	const dispatch = useDispatch();
	const inputRefs = useRef([]);
	const [otp, setOtp] = useState(new Array(6).fill(""));

	const handleChange = (index, e) => {
		const value = e.target.value;

		if (value === " ") return; // handling the spaces

		if (isNaN(value)) return;

		const newOtp = [...otp];
		newOtp[index] = value.substring(value.length - 1);
		setOtp(newOtp);

		if (value && index < 5) {
			inputRefs.current[index + 1].focus();
		}
	};

	const handleKeyDown = (index, e) => {
		if (e.key === "Backspace" && !otp[index] && index > 0) {
			inputRefs.current[index - 1].focus();
		}
	};

	const handleSubmit = async (e) => {
		e.preventDefault();

		let strOtp = {
			otp: "",
		}

		otp.map((num) => {
			strOtp.otp += num;
		})

		
		await dispatch(validateOtp(strOtp)).unwrap();
		navigate("/moreAction");
	};

	return (
		<div className={styles.container}>
			<form onSubmit={handleSubmit} className={styles.otpForm}>
				<label className={styles.label}>Enter the OTP</label>
				<div className={styles.otpInputs}>
					{otp.map((digit, index) => (
						<input
							key={index}
							ref={(el) => (inputRefs.current[index] = el)}
							type="text"
							value={digit}
							onChange={(e) => handleChange(index, e)}
							onKeyDown={(e) => handleKeyDown(index, e)}
							maxLength="1"
							className={styles.otpInput}
						/>
					))}
				</div>
				<button type="submit" className={styles.submitButton}>
					Verify Otp
				</button>
			</form>
		</div>
	)
};

export default OTPForm;


import { useState } from "react";
import { Copy, RefreshCw } from "lucide-react";
import styles from "./PasswordGenerator.module.css";

const generatePassword = (length, options) => {
	const lowercase = "abcdefghijklmnopqrstuvwxyz";
	const uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	const numbers = "0123456789";
	const symbols = "!@#$%^&*()-_=+[]{}|;:,.<>?";

	let characters = "";
	if (options.lowercase) characters += lowercase;
	if (options.uppercase) characters += uppercase;
	if (options.numbers) characters += numbers;
	if (options.symbols) characters += symbols;

	if (characters.length === 0) return "";

	let password = "";
	for (let i = 0; i < length; i++) {
		const randomIndex = Math.floor(Math.random() * characters.length);
		password += characters[randomIndex];
	}

	return password;
};

const PasswordGenerator = ({ onGenerate }) => {
	const [length, setLength] = useState(12);
	const [options, setOptions] = useState({
		lowercase: true,
		uppercase: true,
		numbers: true,
		symbols: true,
	});
	const [generatedPassword, setGeneratedPassword] = useState("");

	const handleGenerate = () => {
		const password = generatePassword(length, options);
		setGeneratedPassword(password);
		onGenerate?.(password);
	};

	return (
		<div className={styles.generatorContainer}>
			<h3 className={styles.heading}>🔐 Password Generator</h3>

			<div className={styles.section}>
				<label className={styles.sliderLabel}>
					Password Length
					<input
						type="range"
						min="6"
						max="32"
						value={length}
						onChange={(e) => setLength(Number(e.target.value))}
						className={styles.slider}
					/>
					<span className={styles.lengthValue}>{length}</span>
				</label>
			</div>

			<div className={styles.options}>
				{Object.keys(options).map((key) => (
					<label key={key} className={styles.checkboxLabel}>
						<input
							type="checkbox"
							checked={options[key]}
							onChange={() => setOptions({ ...options, [key]: !options[key] })}
						/>
						{key.charAt(0).toUpperCase() + key.slice(1)}
					</label>
				))}
			</div>

			<div className={styles.output}>
				<input
					type="text"
					readOnly
					placeholder="Click refresh to generate"
					value={generatedPassword}
					className={styles.passwordField}
				/>
				<button className={styles.iconButton} onClick={() => navigator.clipboard.writeText(generatedPassword)} title="Copy">
					<Copy size={18} />
				</button>
				<button className={styles.iconButton} onClick={handleGenerate} title="Generate">
					<RefreshCw size={18} />
				</button>
			</div>
		</div>
	);
};

export default PasswordGenerator;


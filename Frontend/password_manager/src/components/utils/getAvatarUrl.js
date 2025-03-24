export const getAvatarUrl = (email, style = "micah") => {
	const seed = encodeURIComponent(email.toLowerCase().trim());
	return `https://api.dicebear.com/7.x/${style}/svg?seed=${seed}`;
};

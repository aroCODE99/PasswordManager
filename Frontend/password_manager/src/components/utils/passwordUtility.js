export const getDomain = (url) => new URL(url.startsWith("http") ? url : `https://${url}`).hostname;

export const getSiteName = (url) => {
	let domain = getDomain(url);
	let parts = domain.split(".");
	return parts.length > 2 ? parts[1] : parts[0]; // Handles subdomains
};

export const getFavicon = (url) => `https://www.google.com/s2/favicons?domain=${getDomain(url)}&sz=128`;

export const getUrlObject = (url) => new URL(url.startsWith("http") ? url : `https://${url}`);

export 	const extractDate = (date) => {
	let extractedDate = "";

	for (let i = 0; i < date.length; i++) {
		if (date[i] == "T") {
			return extractedDate;
		}

		extractedDate += date[i];
	}
	return date;
}


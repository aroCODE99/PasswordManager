const Dashboard = () => {
	const getFavicon = (url) => {
		return `https://www.google.com/s2/favicons?domain=${new URL(url).hostname}&sz=128`;
	};

	// Example Usage
	const url = "https://github.com";
	const handleClick = () => {
		console.log(getFavicon(url)); 
	}
	return (
		<div>
			<h1>Hello</h1>
			<button onClick={handleClick}>Click me</button>
		</div>
	);
}

export default Dashboard


// so we have to make the dashboard // so let's try to show the logged in passwords in the dashboard
//
// so what 	

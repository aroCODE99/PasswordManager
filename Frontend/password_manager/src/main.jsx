import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App'
import { Provider } from 'react-redux'
import store from "./reduxStore/store.js";
import { ThemeProvider } from "../src/stores/ThemeContext.jsx";

createRoot(document.getElementById('root')).render(
	<StrictMode>
		<Provider store={store}> 
			<ThemeProvider>
				<App />
			</ThemeProvider>
		</Provider>
	</StrictMode>,
)

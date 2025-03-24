import { createContext } from "react";

const CrudStoreContext = createContext();

export const CrudStoreProvider = ({ children }) => {
		return <CrudStore.Provider>
			{ children }
		</CrudStore.Provider>
}

export default CrudStoreContext;

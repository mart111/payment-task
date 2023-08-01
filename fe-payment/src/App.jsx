import {createContext, useState} from 'react';
import Homepage from '../src/Homepage';

export const AuthContext = createContext({});

function App() {
  const token = localStorage.getItem('token');
  const [authorized, setAuthorized] = useState(Boolean(token));

  return <AuthContext.Provider value={{
    authorized,
    setAuthorizedHandler: (token, role) => {
      localStorage.setItem("token", token)
      localStorage.setItem("role", role)
      setAuthorized(true)
    },
    setUnauthorizedHandler: () => {
      localStorage.removeItem("token")
      localStorage.removeItem("role")
      setAuthorized(false)
    }
  }}>
    <Homepage />
  </AuthContext.Provider>
}

export default App

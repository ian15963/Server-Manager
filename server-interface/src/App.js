import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Dashboard from "./pages/Dashboard";  
import Login from "./pages/Login"
import './App.css'
import { AuthenticationProvider } from "./context/AuthenticationProvider";
import PrivateRoute from "./components/PrivateRoute";


function App() {

  return(
      <Router>
        <AuthenticationProvider>
          <Routes>
            <Route element={<PrivateRoute/>}>
              <Route element={<Dashboard/>} path="/server" />
            </Route>
            <Route element={<Login/>} exact path="/"/>
            <Route element={<Login/>} path="/login"/>
            
            <Route element={() => <Navigate to="/server" replace/>} path="*"/>  
          </Routes>
        </AuthenticationProvider>
      </Router>
    
  )

  
}

export default App;

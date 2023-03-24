import { Outlet, Navigate } from "react-router-dom"
import { useContext } from "react"
import { AuthContext } from "../context/AuthenticationProvider";

const PrivateRoute  = () =>{

    const {authenticated} = useContext(AuthContext);

    return(
        authenticated ? <Outlet/>: <Navigate to="/login"/>
    )

}

export default PrivateRoute;
import { createContext, useState, useEffect } from "react";
import Api from "../axios/config";
import { useNavigate } from "react-router-dom";
import Cookies from "universal-cookie/cjs/Cookies";

const AuthContext = createContext();

function AuthenticationProvider({children}){

    const cookie = new Cookies();
    const navigate = useNavigate();
    const[authenticated, setAuthenticated] = useState(false);
    const[loading, setLoading] = useState(true)

    useEffect(() => {

        const token = cookie.get("jwt_token")

        if(token){
            Api.defaults.headers.Authorization = `Bearer ${token}`
            setAuthenticated(true)
        }

        setLoading(false)

    }, [])



    async function handleLogin(user){

        const {data: {token}} = await Api.post("http://localhost:8080/api/auth", user)
        console.log(token)
        Api.defaults.headers.Authorization = `Bearer ${token}`
        cookie.set("jwt_token", token)
        navigate("/server")
        setAuthenticated(true)

    }

    return(
        <AuthContext.Provider value={{ authenticated, handleLogin}}>
            {children}
        </AuthContext.Provider>
    )
}


export {AuthContext, AuthenticationProvider}
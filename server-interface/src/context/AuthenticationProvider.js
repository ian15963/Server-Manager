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
    let refresh = false

    function getAccessToken() {
        const accessToken = cookie.get("accessToken");
        return accessToken;
    }

      function getRefreshToken() {
        const refreshToken = cookie.get("refreshToken");
        return refreshToken;
      }

    Api.interceptors.request.use(
        (config) =>{
            const token = getAccessToken()
            if(token) {
                config.headers["Authorization"] = `Bearer ${token}`
            }
            return config
        },(error) => {
          return Promise.reject(error);
        }
    )
    Api.interceptors.response.use(
      (res) => {
        return res;
      },
      async (err) => {
        const originalConfig = err.config;
    
        if (originalConfig.url !== "/api/auth" && err.response) {
          // Access Token was expired
          if (err.response.status === 401 && !originalConfig._retry) {
            originalConfig._retry = true;

            try {
              const response = await refreshToken();
    

              if(response.status === 200){
                console.log(response.data)
                Api.defaults.headers.Authorization = `Bearer ${response.data.accessToken}`
                  
    
              return Api(originalConfig);
            }} catch (_error) {
              return Promise.reject(_error);
            }
          }}
          return Promise.reject(err);  
    }
  )

    useEffect(() => {

      const token = getAccessToken()

      if(token){
        Api.defaults.headers.Authorization = `Bearer ${token}`
        setAuthenticated(true)
      }

      setLoading(false)

    }, [])


    function refreshToken() {
        return new Promise((resolve, reject) => {
          try{
           Api.post("http://localhost:8080/refreshtoken", {
          refreshToken: getRefreshToken()
          }, {withCredentials: true}).then(async (res) =>{
            cookie.set("accessToken", JSON.stringify(res.data.accessToken))
            cookie.set("refreshToken", JSON.stringify(res.data.refreshToken))
            return resolve(res)
          });
        } catch (err) {
          return reject(err);
        }
      })}

    async function handleLogin(user){

        const res = await Api.post("http://localhost:8080/api/auth", user, {withCredentials: true})
        const { token, refreshToken } = res.data;
        console.log(token)
        Api.defaults.headers.Authorization = `Bearer ${token}`
        cookie.set("accessToken", JSON.stringify(token))
        cookie.set("refreshToken", JSON.stringify(refreshToken))
        navigate("/server")
        setAuthenticated(true)

    }

    if(loading){
      return <h1>loading</h1>
    }

    return(
        <AuthContext.Provider value={{ authenticated, setAuthenticated, handleLogin, cookie}}>
            {children}
        </AuthContext.Provider>
    )
}


export {AuthContext, AuthenticationProvider}
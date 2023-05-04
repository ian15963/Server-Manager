import { createContext, useState, useEffect } from "react";
import Api from "../axios/config";
import { useNavigate } from "react-router-dom";


const AuthContext = createContext();

function AuthenticationProvider({children}){

    const navigate = useNavigate();
    const[authenticated, setAuthenticated] = useState(() =>{     
      let userAuthenticated = localStorage.getItem("authenticated")
        if(userAuthenticated){
          return true
      }
      return false
    });
    const[loading, setLoading] = useState(true)

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
              await refreshToken();                 
  

              return Api(originalConfig);
            } catch (_error) {
              return Promise.reject(_error);
            }
          }}
          return Promise.reject(err);  
    }
  )

    useEffect(() => {

      setLoading(false)

    }, [])


    function refreshToken() {
        return new Promise((resolve, reject) => {
          try{
           Api.post("http://localhost:8080/refreshtoken", {
          }, {withCredentials: true}).then(async (res) =>{
            return resolve(res)
          });
        } catch (err) {
          localStorage.removeItem("authenticated")
          return reject(err);
        }
      })}

    async function handleLogin(user){

        await Api.post("http://localhost:8080/api/auth", user, {withCredentials: true})
        navigate("/server")
        setAuthenticated(true)
        localStorage.setItem("authenticated", true)

    }

    if(loading){
      return <h1>loading</h1>
    }

    return(
        <AuthContext.Provider value={{ authenticated, setAuthenticated, handleLogin}}>
            {children}
        </AuthContext.Provider>
    )
}


export {AuthContext, AuthenticationProvider}
import { useState } from "react";
import "./Login.css";   
import { AuthContext } from "../context/AuthenticationProvider";
import { useContext } from "react";
import { Link, Navigate } from "react-router-dom";

class User{
    email
    password
    constructor(username, password){
        this.email = username
        this.password = password
    }
}



const Login = () =>{
    const {handleLogin, authenticated} = useContext(AuthContext)
    const[username, setUsername] = useState();
    const[password, setPassword] = useState();


    function submit(e){
        e.preventDefault();
        const user = new User(username, password)
        handleLogin(user)
        console.log(user)
    }

    return(

        authenticated ? <Navigate to="/server"/>: <div className="center" >
        <h1>Login</h1>
        <form  onSubmit={submit}>
            <div className="txt_field">
                <input type="text" name="username" onChange={e => setUsername(e.target.value)}/>
                <span></span>
                <label>Username</label>
            </div>
            <div className="txt_field">
                <input type="password" name="password" onChange={e => setPassword(e.target.value)}/>
                <span></span>
                <label>Password</label>
            </div>
            <div className="pass">Forgot Password?</div>
            <button type="submit" name="submit">Login</button>
            <div className="signup">
                Not a member? <Link to="/signup">Sign Up</Link>
            </div>
        </form>
    </div>
        
    );
}

export default Login;
import { useState } from "react";
import Api from "../axios/config";
import "./Login.css";
import { useNavigate } from "react-router-dom";

class User{
    email
    password
    matchingPassword
    firstName
    lastName
    constructor(username, password, matchingPassword, firstName, lastName){
        this.email = username
        this.password = password
        this.matchingPassword = matchingPassword
        this.firstName = firstName
        this.lastName = lastName
    }
}

const Register = () =>{

    const navigate = useNavigate();
    const [email, setEmail] = useState();
    const [password, setPassword] = useState();
    const [matchingPassword, setMatchingPassword] = useState();
    const [firstName, setFirstName] = useState();
    const [lastName, setLastName] = useState();

    const handleLogout = (user) => {
        Api.post("http://localhost:8080/user", user)
    
    }
    function submit(e){
        e.preventDefault();
        const user = new User(email, password, matchingPassword, firstName, lastName);
        handleLogout(user)
        navigate("/login")
        console.log(user);
    }

    return(
        <div className="center" >
            <h1>Sign Up</h1>
            <form  onSubmit={submit}>
                <div className="txt_field">
                    <input type="text" name="email" onChange={e => setEmail(e.target.value)}/>
                    <span></span>
                    <label>Email</label>
                </div>
                <div className="txt_field">
                    <input type="text" name="firstName" onChange={e => setFirstName(e.target.value)}/>
                    <span></span>
                    <label>First Name</label>
                </div>
                <div className="txt_field">
                    <input type="text" name="lastName" onChange={e => setLastName(e.target.value)}/>
                    <span></span>
                    <label>Last Name</label>
                </div>
                <div className="txt_field">
                    <input type="password" name="password" onChange={e => setPassword(e.target.value)}/>
                    <span></span>
                    <label>Password</label>
                </div>
                <div className="txt_field">
                    <input type="password" name="matchingPassword" onChange={e => setMatchingPassword(e.target.value)}/>
                    <span></span>
                    <label>Matching Password</label>
                </div>
                <button type="submit" name="submit">Sign Up</button>
                <div className="signup">
                    <a href="/#">Resend Verification Email</a>
                </div>
            </form>
        </div>

    );
}


export default Register


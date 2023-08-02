import {useContext, useState} from "react";
import {AuthContext} from "./App";
import {Link} from "react-router-dom";

export default function Login() {
    const { setAuthorizedHandler } = useContext(AuthContext);
    const [ error, setError ] = useState('');

    function submitHandler(event) {
        event.preventDefault();
        const emailElement = event.target.email;
        const passwordElement = event.target.password;

        const data = {
            username: emailElement.value,
            password: passwordElement.value,
        }

        fetch('http://localhost:8081/api/v1/auth/login', {
            method: "POST",
            body: JSON.stringify(data),
            headers: {
                "Content-Type": "application/json"
            }
        })
        .then(res => res.json())
        .then(res => setAuthorizedHandler(res.authenticationToken.authToken, res.role))
        .catch((error) => setError(error.message))
    }

    return (
        <form onSubmit={submitHandler} style={{
            width: "50%",
            margin: "100px auto 0",
        }}>
            <div className="lead mb-3">Enter your data to log in</div>
            <hr />
            <div className="mb-3">
                <label htmlFor="exampleInputEmail1" className="form-label">Username or email address</label>
                <input required type="email" name="email" className="form-control" id="exampleInputEmail1" aria-describedby="emailHelp" />
            </div>
            <div className="mb-3">
                <label htmlFor="exampleInputPassword1" className="form-label">Password</label>
                <input required type="password" name="password" className="form-control" id="exampleInputPassword1" />
            </div>
            { 
                error ?
                <div className="text-danger">Something went wrong</div> :
                null
            }
            <div className="mb-2">
                <Link to="/sign-up">Dont have an account? Sign up</Link>
                <button type="submit" className="btn btn-primary m-3">Login</button>
            </div>
        </form>
    )
}
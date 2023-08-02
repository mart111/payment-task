import {useState} from "react";
import {Link} from "react-router-dom";

export function Signup() {
    const [error, setError] = useState('');

    function submitHandler(event) {
        event.preventDefault();
        const nameElement = event.target.name;
        const emailElement = event.target.email;
        const passwordElement = event.target.password;

        const data = {
            name: nameElement.value,
            username: emailElement.value,
            password: passwordElement.value,
            description: "Merchant Registration",
            role: "merchant",
        }

        fetch('http://localhost:8081/api/v1/register', {
            method: "POST",
            body: JSON.stringify(data),
            headers: {
                "Content-Type": "application/json"
            }
        })
            .then(res => {
                if (!res.ok) {
                    return;
                }
                window.location.replace('login');
            })
            // .then(() => window.location.replace('login'))
            .catch((error) => setError(error.message))
    }

    return (
        <form onSubmit={submitHandler} style={{
            width: "50%",
            margin: "100px auto 0",
        }}>
            <div className="mb-3">
                <label htmlFor="name" className="form-label mb-0">Your name</label>
                <div className="form-text">Enter your given name</div>
                <input required type="text" name="name" className="form-control" id="name"
                       aria-describedby="emailHelp"/>
            </div>
            <div className="mb-3">
                <label htmlFor="email" className="form-label mb-0">Email address</label>
                <div className="form-text">Well never share your email with anyone else.</div>
                <input required type="email" name="email" className="form-control" id="email"
                       aria-describedby="emailHelp"/>
            </div>
            <div className="mb-3">
                <label htmlFor="password" className="form-label mb-0">Password</label>
                <div className="form-text">Use uppercase, lowercase and special characters for successful sign up</div>
                <input required type="password" name="password" className="form-control" id="password"/>
            </div>
            {
                error ?
                    <div className="text-danger">Something went wrong</div> :
                    null
            }
            <div className="mb-2">
                <Link to="/login">Already have an account? Log in</Link>
                <button type="submit" className="btn btn-primary m-3">Sign up</button>
            </div>
        </form>
    )
}
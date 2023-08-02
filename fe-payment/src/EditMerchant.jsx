import {Link} from "react-router-dom";
import {useState} from "react";

export default function EditMerchant() {
    const [ error, setError ] = useState('');
    const token = localStorage.getItem('token');
    const merchant = JSON.parse(localStorage.getItem("merchant"));

    function submitHandler(event) {
        event.preventDefault();
        const nameElement = event.target.name;
        const emailElement = event.target.email;
        const passwordElement = event.target.password;
        const status = event.target.status;

        const data = {
            name: nameElement.value,
            username: emailElement.value,
            status: status.value,
            password: passwordElement.value,
        }

        fetch('http://localhost:8081/api/v1/merchants/' + merchant.id, {
            method: "PUT",
            body: JSON.stringify(data),
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }
        })
            .then(res => {
                if (res.ok) {
                    localStorage.removeItem("merchant");
                    window.location.replace('/');
                    return;
                }

                setError('Failed to update user.');
            })
    }

    return (
        <form onSubmit={submitHandler} style={{
            width: "50%",
            margin: "100px auto 0",
        }}>
            <div style={{textAlign: "right"}}>
                <Link to="/">{`<- Back to homepage`}</Link>
            </div>
            <div className="mb-3 mt-2">
                <label htmlFor="name" className="form-label mb-0">Name</label>
                <input required type="text" name="name" className="form-control" id="name"
                       defaultValue={merchant.name}/>
            </div>
            <div className="mb-3">
                <label htmlFor="email" className="form-label mb-0">Email</label>
                <input required type="email" name="email" className="form-control" id="email"
                       defaultValue={merchant.email}/>
            </div>
            <div className="mb-3">
                <label htmlFor="status" className="form-label mb-0">Status</label>
                <input required type="text" name="status" className="form-control" id="status"
                       defaultValue={merchant.status}/>
            </div>
            <div className="mb-3">
                <label htmlFor="password" className="form-label mb-0">Password</label>
                <input type="password" name="password" className="form-control" id="password"/>
            </div>

            {
                error ?
                    <div className="text-danger">{error}</div> :
                    null
            }

            <div className="mb-2">
                <button type="submit" className="btn btn-primary m-3">Edit</button>
            </div>
        </form>
    )
}
import {useContext, useEffect, useState} from "react";
import {AuthContext} from "./App";

export default function LoggedUser() {
    const token = localStorage.getItem('token');
    const { setUnauthorizedHandler } = useContext(AuthContext);
    const [data, setData] = useState([])

    useEffect(() => {
        fetch('http://localhost:8081/api/v1/merchant/transactions', {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }
        })
        .then(res => {
            if (res.ok) {
                return res.json()
            }

            throw new Error();
        })
        .then(res => setData(res.transactionResponseList))
    }, [])

    return (
        <div style={{
            width: "60%",
            margin: "60px auto 0",
        }}>
            <div className="display-6">Homepage for authorized merchant</div>
            {
                data ?
                    <>
                        <div className="lead mt-4 mb-2">Transactions data</div>
                        <table className="table">
                            <thead>
                            <tr>
                                <th scope="col">Merchant Id</th>
                                <th scope="col">Amount</th>
                                <th scope="col">Status</th>
                                <th scope="col">Customer phone</th>
                                <th scope="col">Customer email</th>
                            </tr>
                            </thead>
                            <tbody>
                            {
                                data.map(each => (
                                    <tr key={each.transactionId}>
                                        <th>{each.merchantId}</th>
                                        <td>{'' + each.amount}</td>
                                        <td>{each.status}</td>
                                        <td>{each.customerPhone}</td>
                                        <td>{each.customerEmail}</td>
                                    </tr>
                                ))
                            }
                            </tbody>
                        </table>
                    </> :
                    <div>Empty data...</div>
            }
            <button className="btn btn-secondary mt-4" onClick={setUnauthorizedHandler}>Log out</button>
        </div>
    );
}
import {useContext, useEffect, useState} from "react";
import {AuthContext} from "./App";
import {Link} from "react-router-dom";

export default function LoggedAdmin() {
    const token = localStorage.getItem('token');
    const {setUnauthorizedHandler} = useContext(AuthContext);
    const [merchants, setMerchants] = useState([]);
    const [transactions, setTransactions] = useState([]);
    const [transactionsVisible, setTransactionsVisible] = useState(false);

    useEffect(() => {
        fetch('http://localhost:8081/api/v1/merchants', {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }
        })
            .then(res => res.json())
            .then(res => setMerchants(res.merchantResponseList))
    }, [])

    function editMerchantHandler(merchant) {
        localStorage.setItem("merchant", JSON.stringify(merchant));
    }

    function deleteMerchantHandler(id) {
        fetch('http://localhost:8081/api/v1/merchants/' + id, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }
        })
            .then(res => {
                if (res.ok) {
                    setTransactionsVisible(false)
                    setMerchants(merchants.filter(item => item.id !== id))
                }
            });
    }

    function fetchTransactions(email) {
        fetch('http://localhost:8081/api/v1/merchants/transactions?merchantEmail=' + email, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            }
        })
            .then(res => res.json())
            .then(res => setTransactions(res.transactionResponseList))
            .finally(() => setTransactionsVisible(true))
    }

    return (
        <div style={{
            width: "80%",
            margin: "20px auto 0",
        }}>

            <div className="display-6 d-inline-block mt-3">Homepage for authorized admin</div>
            <Link className="btn btn-secondary m-3 pointer" to="/import">Import csv file</Link>
            <hr/>
            <div className="lead">Merchants data</div>
            <table className="table">
                <thead>
                <tr>
                    <th scope="col">Name</th>
                    <th scope="col">Username</th>
                    <th scope="col">Description</th>
                    <th scope="col">Total sum</th>
                    <th scope="col">Status</th>
                    <th scope="col">Transactions</th>
                    <th scope="col" style={{textAlign: "center"}} colSpan={2}>Actions</th>
                </tr>
                </thead>
                <tbody>
                {
                    merchants.map(each => (
                        <tr key={each.id}>
                            <th>{each.name}</th>
                            <td>{each.email}</td>
                            <td>{each.description}</td>
                            <td>{each.transactionTotalSum || 0}</td>
                            <td>{each.status}</td>
                            <td className="text-danger pointer" onClick={() => fetchTransactions(each.email)}>Fetch</td>
                            <th className="pointer" onClick={() => editMerchantHandler(each)}><Link
                                to="/edit">Edit</Link></th>
                            <td className="text-danger pointer" onClick={() => deleteMerchantHandler(each.id)}>Delete
                            </td>
                        </tr>
                    ))
                }
                </tbody>
            </table>
            <br/>
            {
                transactionsVisible ?
                    <>
                        <div style={{
                            textAlign: 'right',
                        }}>
                            <button className="btn btn-secondary"
                                    style={{
                                        marginBottom: '-30px'
                                    }}
                                    onClick={() => setTransactionsVisible(false)}>Collapse
                            </button>
                        </div>
                        <div className="lead">Transactions data</div>
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
                                transactions.map(each => (
                                    <tr key={each.transactionId}>
                                        <th>{each.merchantId}</th>
                                        <td>{each.amount}</td>
                                        <td>{each.status}</td>
                                        <td>{each.customerPhone}</td>
                                        <td>{each.customerEmail}</td>
                                    </tr>
                                ))
                            }
                            </tbody>
                        </table>
                        <hr/>
                    </> :
                    null
            }
            <button className="btn btn-secondary mt-3" onClick={setUnauthorizedHandler}>Log out</button>
        </div>
    );
}
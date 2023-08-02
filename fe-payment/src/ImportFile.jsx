import {useState} from "react";
import {Link, useNavigate} from "react-router-dom";

export default function ImportFile() {
    const navigate = useNavigate();
    const token = localStorage.getItem('token');
    const [file, setFile] = useState(null);

    function submitHandler(event) {
        event.preventDefault();

        const formData = new FormData();
        formData.append("file", file)

        fetch('http://localhost:8081/api/v1/merchants/import', {
            method: "POST",
            body: formData,
            headers: {
                "Authorization": "Bearer " + token
            }
        })
            .then((res) => {
                if (res.ok) {
                    navigate(-1);
                }
            });
    }

    return (
        <form onSubmit={submitHandler} style={{
            width: "50%",
            margin: "100px auto 0",
            textAlign: "center"
        }}>
            <Link to="/">{`<- Back to homepage`}</Link>
            <div className="lead m-2">
                <span className="m-2 ml-0">Browse your PC</span>
            </div>
            <small className="mb-3">Hint: select only .csv files</small>
            <hr/>
            <div className="mb-3">
                <input
                    hidden
                    required
                    id="file"
                    type="file"
                    name="file"
                    accept="text/csv"
                    onChange={(evt) => setFile(evt.target.files[0])}
                />
                <div>
                    {
                        file ?
                            <>
                                <div>Attached {file.name}</div>
                                <label htmlFor="file" className="cursor-pointer form-label">Change</label>
                            </> :
                            <label htmlFor="file" className="cursor-pointer form-label">Attach file</label>
                    }
                </div>
            </div>
            <button type="submit" className="btn btn-primary">Add</button>
        </form>
    )
}
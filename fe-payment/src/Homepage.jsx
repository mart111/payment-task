import {useContext} from 'react';
import {Route, Routes} from 'react-router-dom';
import Login from './Login';
import {Signup} from './Signup';
import {AuthContext} from './App';
import ImportFile from './ImportFile';
import LoggedUser from './LoggedUser';
import LoggedAdmin from './LoggedAdmin';
import EditMerchant from './EditMerchant';
import './App.css';

function Homepage() {
    const role = localStorage.getItem('role');
    const { authorized } = useContext(AuthContext);

    return !authorized ?
        <Routes>
            <Route path='/sign-up' element={<Signup />} />
            <Route path='*' element={<Login />} />
        </Routes> :
            role === "admin" ?
                <Routes>
                    <Route path='/edit' element={<EditMerchant />} />
                    <Route path='/import' element={<ImportFile />} />
                    <Route path='*' element={<LoggedAdmin />} />
                </Routes> :
                <Routes>
                    <Route path='*' element={<LoggedUser />} />
                </Routes>;
        }

export default Homepage
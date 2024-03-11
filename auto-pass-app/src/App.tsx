import React from 'react';
import Header from "./components/Header/Header";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import LandingPage from './components/LandingPage/LandingPage';
import ProtectedRoutesGuests from './routes/ProtectedRoutesAnonymous';
import Login from './components/Login/Login';
import ProtectedRoutesUser from './routes/ProtectedRoutesUser';
import ForgotPassword from './components/ForgotPassword/ForgotPassword';
import ChangePassword from './components/ChangePassword/ChangePassword';
import SignUp from './components/SignUp/SignUp';
import Footer from './components/Footer/Footer';
import Dashboard from './components/Dashboard/Dashboard';
import UserDetailsPage from './components/UserDetails/UserDetailsPage';
import ErrorPage from './components/ErrorPage/ErrorPage';


const App = () => {


    return (

        <Router>
            <div className="App">
                <Routes>

                    <Route element={<ProtectedRoutesGuests/>}>
                        <Route path='/' element={<LandingPage/>}/>
                        <Route path='/signup' element={<SignUp/>}/>
                        <Route path='/login' element={<Login/>}/>
                        <Route path='/forgot-password' element={<ForgotPassword/>}/>
                        <Route path='/change-password' element={<ChangePassword/>}/>
                        
                        

                    </Route>

                    <Route element={<ProtectedRoutesUser/>}>

                        <Route path='/profile' element={<UserDetailsPage/>}/>
                        <Route path='/home' element={<Dashboard/>}/>
                        
                        
                    </Route>
                    <Route path='/*' element={<ErrorPage/>}/>   
                </Routes>
                {/* <Footer/> */}
            </div>
        </Router>
    );
}

export default App;

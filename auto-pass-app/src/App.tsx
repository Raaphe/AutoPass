import React from 'react';
import Header from "./components/Header/Header";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import LandingPage from './components/LandingPage/LandingPage';
import ProtectedRoutesGuests from './routes/ProtectedRoutesAnonymous';
import Login from './components/Login/Login';
import ProtectedRoutesUser from './routes/ProtectedRoutesUser';
import UserLandingPage from './components/UserLandingPage/UserLandingPage';


const App = () => {


    return (

        <Router>
            <div className="App">
                <Header/> 
                <Routes>

                    <Route element={<ProtectedRoutesGuests/>}>
                        <Route path='/' element={<LandingPage/>}/>
                        <Route path='/signup' element={<LandingPage/>}/>
                        <Route path='/login' element={<Login/>}/>
                    </Route>

                    <Route element={<ProtectedRoutesUser/>}>
                        <Route path='/home' element={<UserLandingPage/>}/>
                        {/* Edit user (/edit/:userId), user details (/:userId)*/}
                        
                    </Route>

                </Routes>
            </div>
        </Router>
    );
}

export default App;

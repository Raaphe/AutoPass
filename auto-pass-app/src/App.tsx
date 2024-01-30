import React from 'react';
import Header from "./components/Header/Header";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import LandingPage from './components/LandingPage/LandingPage';
import ProtectedRoutesGuests from './routes/ProtectedRoutesAnonymous';
import Login from './components/Login/Login';
// import { AuthenticationApi, SignInDTO } from "./Service"


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
                    {/*
                    <Route element={<ProtectedRoutesGuests />}>
                        <Route path='/'  element={<LandingPage/>}/>
                        <Route path='/signup'  element={<LandingPage/>}/>
                        <Route path='/login'  element={<LandingPage/>}/>
                    </Route>


 */}


                </Routes>
            </div>
        </Router>
    );
}

export default App;

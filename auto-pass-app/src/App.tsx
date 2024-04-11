import React from 'react';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import LandingPage from './components/LandingPage/LandingPage';
import ProtectedRoutesGuests from './routes/ProtectedRoutesAnonymous';
import Login from './components/Login/Login';
import ProtectedRoutesUser from './routes/ProtectedRoutesUser';
import ForgotPassword from './components/ForgotPassword/ForgotPassword';
import ChangePassword from './components/ChangePassword/ChangePassword';
import SignUp from './components/SignUp/SignUp';
import Dashboard from './components/Dashboard/Dashboard';
import UserDetailsPage from './components/UserDetails/UserDetailsPage';
import ErrorPage from './components/ErrorPage/ErrorPage';
import WalletDetails from './components/WalletDetails/WalletDetails';
import Scanners from './components/Scanners/Scanners';
import ScannerDetails from './components/ScannerDetails/ScannerDetails';
import Products from './components/Products/Products';
import CheckoutForm from './components/StripeModule/CheckoutForm';
import Return from './components/StripeModule/Return';
import Invoices from './components/Invoices/Invoices';

/**
* App - 2024-04-02
* Raaphe, Lamb, Ikacef
*
* AutoPass
*/
const App = () => {


    return (

        <Router>
            <div className="App">
                <Routes>

                    <Route element={<ProtectedRoutesGuests />}>
                        <Route path='/' element={<LandingPage />} />
                        <Route path='/signup' element={<SignUp />} />
                        <Route path='/login' element={<Login />} />
                        <Route path='/forgot-password' element={<ForgotPassword />} />
                        <Route path='/change-password' element={<ChangePassword />} />
                    </Route>

                    <Route element={<ProtectedRoutesUser />}>

                        <Route path='/profile' element={<UserDetailsPage />} />
                        <Route path='/home' element={<Dashboard />} />
                        <Route path='/wallet' element={<WalletDetails />} />
                        <Route path='/scanners' element={<Scanners />} />
                        <Route path='/scanner/:id' element={<ScannerDetails />} />
                        <Route path='/scanner' element={<ScannerDetails />} />
                        <Route path='/products' element={<Products />} />
                        <Route path="/checkout" element={<CheckoutForm/>} />
                        <Route path="/return" element={<Return />} />
                        <Route path="/invoices" element={<Invoices/>} />

                    </Route>
                    <Route path='/*' element={<ErrorPage />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;

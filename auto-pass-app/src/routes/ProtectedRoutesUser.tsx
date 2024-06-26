import React, {useEffect, useState} from 'react';
import {Navigate, Outlet} from 'react-router-dom';
import ClientAuthService from '../ClientAuthService';
import Header from '../components/Header/Header';
import Footer from '../components/Footer/Footer';

/**
* ProtectedRoutesUser - 2024-04-02
* Raaphe
*
* AutoPass
*/
const ProtectedRoutesUser = () => {
    const [isAuth, setIsAuth] = useState(true);
    const [isAdmin, setIsAdmin] = useState(false);

    useEffect(() => {
        const checkAuth = async () => {
            if (ClientAuthService.isUserLoggedOut()) {
                setIsAuth(false);
            }
            const auth = await ClientAuthService.isUserLoggedIn();
            setIsAuth(auth);
        };

        const checkAdminState = async () => {
            var role = await ClientAuthService.getPrincipalAuthority();

            setIsAdmin(role === "ADMIN");
        }

        checkAdminState();
        checkAuth();
    }, []);

    return isAuth ? 
        <div style={{display:"flex", flexDirection:"column", minHeight:"100vh"}}>
            <Header isAdmin={isAdmin} isAuth={isAuth}/>
            <div style={{flex:1, marginBottom:"30px"}}>
                <Outlet/>
            </div>
            <Footer/>

        </div>
        : 
        <Navigate to="/login"/>;
};

export default ProtectedRoutesUser;

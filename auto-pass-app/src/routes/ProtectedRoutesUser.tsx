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
        <>
            <Header isAdmin={isAdmin} isAuth={isAuth}/>
            <Outlet/> 
            <Footer/>

        </>
        : 
        <Navigate to="/login"/>;
};

export default ProtectedRoutesUser;

import React, {useEffect, useState} from 'react';
import {Navigate, Outlet} from 'react-router-dom';
import ClientAuthService from '../ClientAuthService';
import Header from '../components/Header/Header';

const ProtectedRoutesUser = () => {
    const [isAuth, setIsAuth] = useState(true);

    useEffect(() => {
        const checkAuth = async () => {
            if (ClientAuthService.isUserLoggedOut()) {
                setIsAuth(false);
            }
            const auth = await ClientAuthService.isUserLoggedIn();
            setIsAuth(auth);
        };

        checkAuth();
    }, []);

    return isAuth ? 
        <>
            <Header isAuth={isAuth}/>
            <Outlet/> 
        </>
        : 
        <Navigate to="/login"/>;
};

export default ProtectedRoutesUser;

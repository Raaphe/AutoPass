import { FC, useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
import ClientAuthService from "../ClientAuthService"

interface ProtectedRoutesScannerProps {

}

const ProtectedRoutesScanner : FC<ProtectedRoutesScannerProps> = () => {

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
            <Outlet/> 
        </>
        : 
        <Navigate to="/"/>;
}

export default ProtectedRoutesScanner;
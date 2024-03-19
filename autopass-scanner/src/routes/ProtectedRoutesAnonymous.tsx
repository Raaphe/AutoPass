import { FC, useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
import ClientAuthService  from "../ClientAuthService";

interface ProtectedRoutesAnonumousProps {

}

const ProtectedRoutesAnonymous : FC<ProtectedRoutesAnonumousProps> = () => {
    const [isAuth, setIsAuth] = useState(false);

    useEffect(() => {
        const checkAuth = async () => {
            if (ClientAuthService.isUserLoggedOut()) {
                return <Navigate to="/"/>
            }
            const auth = await ClientAuthService.isUserLoggedIn();
            setIsAuth(auth);
        };

        checkAuth();
    }, []);

    if (isAuth) {
        return <Navigate to="/home"/>
    } else {
        return (
            <>
                <Outlet/>
            </>
        )
    }
}

export default ProtectedRoutesAnonymous;
import React, {useEffect, useState} from "react";
import {Navigate, Outlet} from "react-router-dom";
import ClientAuthService from "../ClientAuthService";
import Header from "../components/Header/Header";
import Footer from "../components/Footer/Footer";

/**
* ProtectedRoutesAnonymous - 2024-04-02
* Raaphe
*
* AutoPass
*/
const ProtectedRoutesGuests = () => {
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
                <Header isAdmin={false} isAuth={isAuth}/>
                <div style={{flex:1}}>
                    <Outlet/>
                </div>
                <Footer/>

            </>
        )
    }
}

export default ProtectedRoutesGuests;
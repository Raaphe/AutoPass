import React, {useEffect, useState} from "react";
import {Navigate, Outlet} from "react-router-dom";
import ClientAuthService from "../ClientAuthService";
import Header from "../components/Header/Header";
import Footer from "../components/Footer/Footer";
import { CoPresent } from "@mui/icons-material";

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
            <div style={{display:"flex", flexDirection:"column", minHeight:"100vh"}}>
                <Header isAdmin={false} isAuth={isAuth}/>
                <div style={{flexGrow:1}}>
                    <Outlet/>
                </div>
                <Footer/>

            </div>
        )
    }
}

export default ProtectedRoutesGuests;
import React from "react";
import {Navigate, Outlet} from "react-router-dom";
import ClientAuthService from "../ClientAuthService";


const isAuth = await ClientAuthService.isUserLoggedIn();

const ProtectedRoutesUser = () => {
    return isAuth ? <Outlet/> : <Navigate to="/login"/>;
};

export default ProtectedRoutesUser;
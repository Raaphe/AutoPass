import React from "react";
import {Navigate, Outlet} from "react-router-dom";
import ClientAuthService from "../ClientAuthService";

const isUserLoggedIn: boolean = await ClientAuthService.isUserLoggedIn();

const ProtectedRoutesGuests = () => {
    console.log(isUserLoggedIn);

    if (isUserLoggedIn) {
        return <Navigate to="/home"/>
    } else {
        return <Outlet/>
    }
}

export default ProtectedRoutesGuests;
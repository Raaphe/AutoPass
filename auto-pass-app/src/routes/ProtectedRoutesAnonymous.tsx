import React from "react";
import {Outlet} from "react-router-dom";


const isAuth = async (): Promise<Boolean> => {
    return true;

}

const ProtectedRoutesGuests = () => {
    return <Outlet/>
}

export default ProtectedRoutesGuests;
import React, { FC, useEffect, useState } from "react";
import styles from "./Dashboard.module.scss";
import { motion } from "framer-motion";
import ClientAuthService from "../../ClientAuthService";
import { useNavigate } from "react-router-dom";
import {User, UserControllerApi, UserWallet} from "../../Service";
import StripeModule from "../StripeModule/Return";
import MembershipStatusGraph from "../Views/MembershipStatusGraph";
import ClientUtil from "../../ClientUtil";
import {Card} from "@mui/material";
import DashboardMobile from "./DashboardMobile";
import DashboardDesktop from "./DashboardDesktop";


interface DashboardProps {
}

/**
* Dashboard - 2024-04-02
* Raaphe, Lamb
*
* AutoPass
*/
const Dashboard: FC<DashboardProps> = () => {

  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 550);
    }

    window.addEventListener('resize', handleResize);
  }, [])
 

  return (

    isMobile ? 
      <DashboardMobile/>
    
    :
      <DashboardDesktop/>

  );

}

export default Dashboard;
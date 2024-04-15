import React, { FC, useEffect, useState } from "react";
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

  const [isMobile, setIsMobile] = useState(window.innerWidth < 1000);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 1000);
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
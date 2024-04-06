import { FC, useEffect, useState } from "react";
import DashboardDesktop from "./DashboardDesktop";
import DashboardMobile from "./DashboardMobile";


interface DashboardProps {
}

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
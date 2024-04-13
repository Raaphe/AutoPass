import React, { FC, useEffect, useState } from "react";
import TransitLogsMobile from "./TransitLogsMobile";
import TransitLogsDesktop from "./TransitLogsDesktop";


interface TransitLogsProps {
}

/**
* Dashboard - 2024-04-02
* Raaphe, Lamb
*
* AutoPass
*/
const TransitLogs: FC<TransitLogsProps> = () => {

  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 550);
    }

    window.addEventListener('resize', handleResize);
  }, [])
 

  return (

    isMobile ? 
      <TransitLogsMobile/>
    
    :
      <TransitLogsDesktop/>

  );

}

export default TransitLogs;
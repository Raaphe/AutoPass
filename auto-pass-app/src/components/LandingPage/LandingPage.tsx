import React, { FC, useEffect, useState } from "react";
import LandingPageDesktop from "./LandingPageDesktop";
import LandingPageMobile from "./LandingPageMobile";


interface LandingPageProps {
}

/**
* LandingPage - 2024-04-02
* Raaphe, Lamb
*
* AutoPass
*/
const LandingPage: FC<LandingPageProps> = () => {

  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 550);
    }

    window.addEventListener('resize', handleResize);
  }, [])
 

  return (

    isMobile ? 
      <LandingPageMobile/>
    
    :
      <LandingPageDesktop/>

  );

}

export default LandingPage;
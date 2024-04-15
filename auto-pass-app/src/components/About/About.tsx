import { FC, useEffect, useState } from "react";
import AboutMobile from "./AboutMobile";
import AboutDesktop from "./AboutDesktop";


interface AboutProps {

}

/**
* About - 2024-04-02
* Raaphe
*
* AutoPass
*/
const About: FC<AboutProps> = () => {

  const [isMobile, setIsMobile] = useState(window.innerWidth < 800);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 800);
    }

    window.addEventListener('resize', handleResize);
  }, [])
 

  return (

    isMobile ? 
      <AboutMobile />
    :
      <AboutDesktop/>

  );

}

export default About;
import  { FC, useEffect, useState } from 'react';
import ScannersMobile from './ScannersMobile';
import ScannersDesktop from './ScannersDesktop';

/**
* Scanners - 2024-04-02
* Raaphe
*
* AutoPass
*/
const Scanners: FC = () => {

  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 550);
    }

    window.addEventListener('resize', handleResize);
  }, [])
 

  return (

    isMobile ? 
      <ScannersMobile/>
    :
      <ScannersDesktop/>

  );
};

export default Scanners;

import  { FC, useEffect, useState } from 'react';
import ScannerDetailsDesktop from './ScannerDetailsDesktop';
import ScannerDetailsMobile from './ScannerDetailsMobile';



/**
* ScannerDetails - 2024-04-02
* Raaphe
*
* AutoPass
*/
const ScannerDetails: FC = () => {

  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 550);
    }

    window.addEventListener('resize', handleResize);
  }, [])
 

  return (

    isMobile ? 
      <ScannerDetailsMobile/>
    :
      <ScannerDetailsDesktop/>

  );
};

export default ScannerDetails;

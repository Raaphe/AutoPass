import React, { FC, useEffect, useState } from "react";
import "./WalletDetails.module.scss";
import WalletDetailsDesktop from "./WalletDetailsDesktop";
import WalletDetailsMobile from "./WalletDetailsMobile";

interface WalletDetailsProps {
}

/**
* WalletDetails - 2024-04-02
* Raaphe
*
* AutoPass
*/
const WalletDetails: FC<WalletDetailsProps> = () => {
  
  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 550);
    }

    window.addEventListener('resize', handleResize);
    return;
  }, [])
 
  return (
    isMobile ? 
      <WalletDetailsMobile/>
    :
      <WalletDetailsDesktop/>
  );
};

export default WalletDetails;

import { FC, useEffect, useState } from "react";
import InvoicesDesktop from "./InvoicesDesktop";
import InvoicesMobile from "./InvoicesMobile";


interface InvoicesProps {
}

/**
* Invoices - 2024-04-10
* Raaphe
* The list of invoices. Routing for mobile desktop.
* AutoPass
*/
const Invoices: FC<InvoicesProps> = () => {

  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 550);
    }

    window.addEventListener('resize', handleResize);
  }, [])
 

  return (

    isMobile ? 
      <InvoicesMobile/>
    :
      <InvoicesDesktop />

  );

}

export default Invoices;
import React, { FC, useEffect, useState } from "react";
import "./Products.module.scss";
import ProductsDesktop from "./ProductsDesktop";
import ProductsMobile from "./ProductsMobile";

interface ProductsProps {
}

/**
* Products - 2024-04-02
* Lamb
*
* AutoPass
*/
const Products: FC<ProductsProps> = () => {
  
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
      <ProductsMobile/>
    :
      <ProductsDesktop/>
  );
};

export default Products;

import { FC, useEffect, useState } from "react";
import HeaderMobile from "./HeaderMobile";
import HeaderDesktop from "./HeaderDesktop";


interface HeaderProps {
  isAuth: boolean,
}

const Header: FC<HeaderProps> = ({isAuth}) => {

  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 550);
    }

    window.addEventListener('resize', handleResize);
  }, [])
 

  return (

    isMobile ? 
      <HeaderMobile isAuth={isAuth}/>
    :
      <HeaderDesktop isAuth={isAuth}/>

  );

}

export default Header;
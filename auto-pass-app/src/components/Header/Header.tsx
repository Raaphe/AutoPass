import { FC, useEffect, useState } from "react";
import HeaderMobile from "./HeaderMobile";
import HeaderDesktop from "./HeaderDesktop";


interface HeaderProps {
  isAuth: boolean,
  isAdmin: boolean
}

const Header: FC<HeaderProps> = ({isAuth, isAdmin}) => {

  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth < 550);
    }

    window.addEventListener('resize', handleResize);
  }, [])
 

  return (

    isMobile ? 
      <HeaderMobile isAdmin={isAdmin} isAuth={isAuth}/>
    :
      <HeaderDesktop isAdmin={isAdmin} isAuth={isAuth}/>

  );

}

export default Header;
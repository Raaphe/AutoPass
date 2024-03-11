import React, { FC, useEffect, useState } from "react";
import { styled } from "@mui/system";
import { buttonClasses } from "@mui/base/Button";
import { Tabs } from "@mui/base/Tabs";
import { Tab as BaseTab, tabClasses } from "@mui/base/Tab";
import { TabsList as BaseTabsList } from "@mui/base/TabsList";
import Logo from "../../assets/7.png";
import "./Header.module.scss";
import { useLocation, useNavigate } from "react-router-dom";
import ClientAuthService from "../../ClientAuthService";
import { Avatar } from "@mui/material";
import * as API from "../../Service";

interface HeaderProps {
  isAuth: boolean;
}

const Header: FC<HeaderProps> = ({ isAuth }) => {


  const navigate = useNavigate();
  const location = useLocation();
  const currentPath = location.pathname;
  const [avatarUrl, setAvatarUrl] = useState("");

  useEffect(() => {
    const getUserImage = () => {
      if (!isAuth) return;
      const config = ClientAuthService.getApiConfig();
      const userApi = new API.UserControllerApi(config);
      userApi.getUserImage(ClientAuthService.getUserId())
        .then(res => {
          if (res.status !== 200) return;
          setAvatarUrl(res.data)
        })
        .catch(_ => {
        })
    }
    getUserImage();
  }, [navigate])

  const handleTabChange = (event: any, newPath: any) => {
    console.log(newPath);

    if (newPath === "/logout") {
      console.log("logging out");
      ClientAuthService.logout();
      navigate("/");
      return;
    }
    navigate(newPath);
  }

  const handleLogoSelect = () => {
    if (isAuth) {
      navigate("/");
    } else {
      navigate("/home");
    }
  }

  const grey = {
    50: "#F3F6F9",
    100: "#E5EAF2",
    200: "#DAE2ED",
    300: "#C7D0DD",
    400: "#B0B8C4",
    500: "#9DA8B7",
    600: "#6B7A90",
    700: "#434D5B",
    800: "#303740",
    900: "#1C2025",
  };
  const blue = {
    50: "#F0F7FF",
    100: "#C2E0FF",
    200: "#80BFFF",
    300: "#66B2FF",
    400: "#3399FF",
    500: "#007FFF",
    600: "#0072E5",
    700: "#0059B2",
    800: "#004C99",
    900: "#003A75",
  };

  const AvatarIcon = styled(Avatar)`
    cursor: pointer;
    background-color: transparent;
    
    margin-right: 30px; // Adjust this value as needed
    margin-left: 16px; 

    
    width: 45px; // Adjust this value as needed
    height: 45px; // Adjust this value as needed

    // Add flex properties if needed
    display: flex;
    justify-content: center;
    align-items: center;

    &:hover {
      background-color: ${blue[400]};
    }

    &:focus {
      color: #fff;
      outline: 3px solid ${blue[200]};
    }

    &.${tabClasses.selected} {
      background-color: #fff;
      color: ${blue[600]};
    }

    &.${buttonClasses.disabled} {
      opacity: 0.5;
      cursor: not-allowed;
    }
  `;

  const Tab = styled(BaseTab)`
    font-family: "IBM Plex Sans", sans-serif;
    color: white;
    cursor: pointer;
    font-size: 0.875rem;
    font-weight: bold;
    background-color: transparent;
    width: 20%;
    padding: 12px;
    margin: 6px;
    border: none;
    border-radius: 7px;
    display: flex;
    justify-content: center;

    .button {
      display:flex
    }

    &:hover {
      background-color: ${blue[400]};
    }

    &:focus {
      color: #fff;
      outline: 3px solid ${blue[200]};
    }

    &.${tabClasses.selected} {
      background-color: #fff;
      color: ${blue[600]};
    }

    &.${buttonClasses.disabled} {
      opacity: 0.5;
      cursor: not-allowed;
  `;

  const TabsList = styled(BaseTabsList)(
    ({ theme }) => `
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: ${blue[500]};
    border-radius: 12px;
    margin-bottom: 16px;
    box-shadow: 0px 4px 8px ${theme.palette.mode === "dark" ? grey[900] : grey[200]};
    `
  );

  // New styled component for the logo container
  const LogoContainer = styled('div')`
    flex-grow: 1;
    display: flex;
    justify-content: flex-start; 
  `;

  const TabsContainer = styled('div')`
    flex-grow: 1;
    display: flex;
    justify-content: flex-end; 
  `;

  return (
    <Tabs
      className="m-3"
      defaultValue={currentPath}
      selectionFollowsFocus
      aria-label="nav-tab"
      onChange={handleTabChange}
    >
      <TabsList >
        <LogoContainer>
          <img src={Logo} alt="Logo" id="logo" onClick={handleLogoSelect} />
        </LogoContainer>

        <TabsContainer>
          {isAuth ?
            <>
              <Tab label="About" value="/about">About</Tab>
              <Tab label="Wallet" value="/wallet">Wallet</Tab>
              <Tab label="Logout" value="/logout">Logout</Tab>
              <AvatarIcon alt="You" onClick={() => navigate("profile")} src={avatarUrl} />
            </>
            :
            <>
              <Tab label="About" value="/about">About</Tab>
              <Tab label="SignUp" value="/signup">Sign Up</Tab>
              <Tab label="Login" value="/login">Login</Tab>
            </>
          }
        </TabsContainer>
      </TabsList>
    </Tabs>
  );
};

export default Header;

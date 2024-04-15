import React, { FC, useEffect, useState } from "react";
import QrCodeScannerOutlinedIcon from "@mui/icons-material/QrCodeScannerOutlined";
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
import { UserControllerApi } from "../../Service";
import LogoutIcon from "@mui/icons-material/Logout";
import InfoIcon from "@mui/icons-material/Info";
import LoginIcon from "@mui/icons-material/Login";
import WalletOutlinedIcon from "@mui/icons-material/WalletOutlined";

interface HeaderProps {
  isAuth: boolean;
  isAdmin: boolean;
}


/**
* HeaderMobile - 2024-04-02
* Raaphe
*
* AutoPass
*/
const Header: FC<HeaderProps> = ({ isAuth, isAdmin }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const currentPath = location.pathname;
  const [avatarUrl, setAvatarUrl] = useState("");

  useEffect(() => {
    const getUserImage = () => {
      if (!isAuth) return;
      const config = ClientAuthService.getApiConfig();
      const userApi = new UserControllerApi(config);
      userApi
        .getUserImage(ClientAuthService.getUserId())
        .then((res) => {
          if (res.status !== 200) return;
          setAvatarUrl(res.data);
        })
        .catch((_) => {});
    };
    getUserImage();
  }, [navigate]);

  const handleTabChange = (event: any, newPath: any) => {

    if (currentPath === "/profile") {
    }

    if (newPath === "/logout") {
      console.log("logging out");
      ClientAuthService.logout();
      navigate("/");
      return;
    }
    navigate(newPath);
  };

  const handleLogoSelect = () => {
    if (isAuth) {
      navigate("/");
    } else {
      navigate("/home");
    }
  };

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
    margin-right: 30px;
    margin-left: 8px;
    margin-bottom: 10px;
    width: 45px;
    height: 45px;
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

  const LogOutIcon = styled(LogoutIcon)`
    cursor: pointer;
    background-color: transparent;
    color: white;
    border: none;
    border-radius: 7px;
    width: 45px;
    height: 45px;
    transform: scale(0.59);
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

  const AboutIcon = styled(InfoIcon)`
    cursor: pointer;
    background-color: transparent;
    color: white;
    border: none;
    border-radius: 7px;
    width: 45px;
    height: 45px;
    transform: scale(0.59);
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
    width: 30%;
    padding: 12px;
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
    box-shadow: 0px 4px 8px ${
      theme.palette.mode === "dark" ? grey[900] : grey[200]
    };
    `
  );

  // New styled component for the logo container
  const LogoContainer = styled("div")`
    flex-grow: 1;
    display: flex;
    justify-content: flex-start;
  `;

  const TabsContainer = styled("div")`
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
      <TabsList>
        <LogoContainer>
          <img src={Logo} alt="Logo" id="logo" onClick={handleLogoSelect} />
        </LogoContainer>

        <TabsContainer style={{ display: "flex", justifyContent: "center" }}>
          {isAuth ? (
            <>
              <Tab label="Wallet" value="/wallet">
                <WalletOutlinedIcon sx={{marginTop:"24%", transform:"scale(1.2)"}}/>
              </Tab>
              {isAdmin && (
                <Tab label="scanners" value="/scanners">
                  <QrCodeScannerOutlinedIcon sx={{marginTop:"24%", transform:"scale(1.2)"}}/>
                </Tab>
              )}
              <LogOutIcon
                onClick={() => {
                  ClientAuthService.logout();
                  navigate("/");
                }}
                style={{ marginTop: "11px" }}
              />
              <AvatarIcon
                style={{ marginTop: "11px" }}
                alt="You"
                onClick={() => {
                  navigate("profile");
                }}
                src={avatarUrl}
              />
            </>
          ) : (
            <>
              <Tab label="About" value="/about">
                <AboutIcon className="mx-2" />
              </Tab>
            </>
          )}
        </TabsContainer>

        <TabsContainer style={{ display: "flex", justifyContent: "center" }}>
          {isAuth ? (
            <></>
          ) : (
            <>
              <Tab label="SignUp" value="/signup" className="mx-3">
                Register
              </Tab>
              <Tab label="Login" value="/login" className="mx-3">
                <LoginIcon className="mx-2" />
              </Tab>
            </>
          )}
        </TabsContainer>
      </TabsList>
    </Tabs>
  );
};

export default Header;

import React, { FC, useEffect, useState } from "react";
import LogoutIcon from "@mui/icons-material/Logout";
import ClientAuthService from "../../ClientAuthService";
import { useNavigate } from "react-router-dom";
import { Card, IconButton, buttonClasses, styled, tabClasses } from "@mui/material";
import Typewriter from 'typewriter-effect';
import * as API from "../../Service/api";
import utilService from "../../ClientUtil";
import "./ScannerTerminal.scss"
import QRScannerInput from "../Views/QRScannerInput/QRScannerInput";
import ScannerScreen from "../Views/ScannerScreen/ScannerScreen";


interface ScannerTerminalProps { }

const ScannerTerminal: FC<ScannerTerminalProps> = () => {

  const navigate = useNavigate();

  const config = ClientAuthService.getApiConfig();
  const terminalApi = new API.TerminalControllerApi(config);
  const userAPI = new API.UserControllerApi(config);

  const [scannerInfo, setScannerInfo] = useState<API.User>();
  const [scannerScreenInfo, setScannerScreenInfo] = useState<API.PassValidationResponseViewModel>();
  const [isScanned, setIsScanned] = useState(false);

  useEffect(() => {

    const fetchScannerInfo = () => {
      userAPI.getUser()
        .then((res) => {
          if (res.status !== 200) {
            return;
          }
          else {
            setScannerInfo(res.data);
          }
        })

    }


    fetchScannerInfo();

  }, [navigate, isScanned])




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


  const LogOutIcon = styled(LogoutIcon)`
    cursor: pointer;
    border: none;
    border-radius: 7px;
    width: 45px;
    height: 40px;
    transform: scale(0.59); 
    &.${tabClasses.selected} {
      background-color: #fff;
      color: ${blue[600]};
    }

    &.${buttonClasses.disabled} {
      opacity: 0.5;
      cursor: not-allowed;
    }
  `;

  const onScan = (codeValue: string) => {
    terminalApi.validatePass(codeValue)
      .then((res) => {
        if (res.status !== 200) {
          return;
        }

        setIsScanned(true);
        setScannerScreenInfo(res.data);

      
        console.log(res.data);
      })
  }

  return (
    <>
      <Card>
        <IconButton aria-label="fingerprint" color="error" sx={{ maxWidth: "40%" }} onClick={() => { ClientAuthService.logout(); navigate("/"); }}>
          <LogOutIcon style={{ marginTop: "6px" }} />
        </IconButton>
        <div style={{ fontSize: '48px', textAlign: 'center' }}>
          <Typewriter
            options={{
              strings: [
                'Scan Your Pass                  ',
                (scannerInfo?.firstName ?? "") + " - " + utilService.getBusNumberFromEmail(scannerInfo?.email ?? "") + "                     "
              ],
              autoStart: true,
              loop: true,
              delay: 65,
              deleteSpeed: 25,
            }}
          />
        </div>


      </Card>

      <div className="body">

        <QRScannerInput onScan={onScan}/>
        <ScannerScreen passValidationResponse={scannerScreenInfo ?? {responseMessage:"NOT_SCANNED"}} scanned={isScanned} setScan={setIsScanned}/>
      </div>
    </>
  );
};

export default ScannerTerminal;

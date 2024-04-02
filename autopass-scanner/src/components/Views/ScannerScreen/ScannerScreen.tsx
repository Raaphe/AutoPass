import React, { FC, useEffect, useState } from "react";
import { Card, Stack } from "@mui/material";
import * as API from "../../../Service/api";
import "./ScannerScreen.scss"
import QrCodeScannerIcon from '@mui/icons-material/QrCodeScanner';
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';

interface ScannerScreenProps {
  passValidationResponse: API.PassValidationResponseViewModel;
  scanned: boolean;
  setScan: any;
}

const ScannerScreen: FC<ScannerScreenProps> = ({ passValidationResponse, scanned, setScan }) => {

  const [height, setHeight] = useState(window.innerHeight);
  const [width, setWidth] = useState(window.innerWidth);

  useEffect(() => {

    if (scanned) {
      const timer = setTimeout( ()=>setScan(false) ,1600)

      return () => clearTimeout(timer);
    }

    const handleResize = () => {
      setWidth(window.innerWidth);
      setHeight(window.innerHeight);
    };

    // Set resize listener
    window.addEventListener('resize', handleResize);

    // Clean up function
    return () => {
      window.removeEventListener('resize', handleResize);
    };

  }, [scanned])


  if (!scanned) {
    return (
      <Card sx={{backgroundColor:"#282c34", minWidth:"100%", minHeight:height-150, display:"flex", alignContent:"center", justifyContent:"center", flexDirection:"column"}}>
        
        <Stack sx={{display:"flex", alignItems:"center"}}>
          <Card elevation={13}>
            <h1 className="display-4 m-3">Scan Here</h1>
            <QrCodeScannerIcon className="m-3 mb-4" id="icon"/>

          </Card>
        </Stack>
      </Card>
    )
  }

  return (
    <>
      <Card sx={{backgroundColor:passValidationResponse.isValid ? 'green' : 'red', minWidth:"100%", minHeight:height-150, display:"flex", alignContent:"center", justifyContent:"center", flexDirection:"column"}}>
        
        <Stack >
          
        </Stack>

        <Stack sx={{display:"flex", alignItems:"center"}}>
            
          <h1 className="display-4 m-3">{passValidationResponse.responseMessage}</h1>
          {passValidationResponse.isValid ? 
            <CheckIcon className="m-3 mb-4" id="icon"/>
          :
            <CloseIcon className="m-3 mb-4" id="icon"/>
          }

        </Stack>
      </Card>
    </>
  );
};

export default ScannerScreen;

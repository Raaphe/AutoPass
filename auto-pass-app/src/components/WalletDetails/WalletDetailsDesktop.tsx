import React, { FC, useEffect, useState } from "react";
import "./WalletDetails.module.scss";
import { useNavigate } from "react-router-dom";
import ClientAuthService from "../../ClientAuthService";
import ClienUtil from "../../ClientUtil";
import { UserWallet, WalletControllerApi } from "../../Service";
import { Button, Card, Fab, Typography } from "@mui/material";
import Divider from '@mui/material/Divider';
import { CircularProgressbarWithChildren, buildStyles } from "react-circular-progressbar";
import utilService from "../../ClientUtil";
import DirectionsBusIcon from '@mui/icons-material/DirectionsBus';
import ConfirmationNumberIcon from "@mui/icons-material/ConfirmationNumber"
import MembershipStatusGraph from "../Views/MembershipStatusGraph";
import ClientUtil from "../../ClientUtil";

interface WalletDetailsDesktopProps {
}


/**
* WalletDetailsDesktop - 2024-04-02
* Raaphe, Lamb
*
* AutoPass
*/

const WalletDetailsDesktop: FC<WalletDetailsDesktopProps> = () => {
  const navigate = useNavigate();
  const [walletInfo, setWalletInfo] = useState<UserWallet>({});
  const [daysUntilExpiry, setDaysUntilExpiry] = useState(0);

  useEffect(() => {
    ClientUtil.getUserWalletInfo(setDaysUntilExpiry, setWalletInfo, walletInfo);
  }, [navigate])

  function HandleSeeCatalog(): void {
    alert("not yet implemented")
  }

  return (

    <Card className="container"  elevation={12} variant="outlined">
      <button type="submit" className="btn btn-outline-primary mt-3" onClick={() => navigate("/home")}>
        <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" className="bi bi-arrow-left m-1" viewBox="0 0 16 16">
          <path fillRule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8" />
        </svg>
      </button>
      <h2 className="display-3 m-2">Your wallet</h2>
      <div className="row" style={{ display: 'flex', justifyContent: 'center' }}>

        {/* Membership card  */}
        <Card className="col-6 m-2 " elevation={12} style={{ display: "flex", justifyContent: "center", alignItems: "center", flexDirection: 'column', width: "46%", height:"407px" }}>
          <Typography style={{ alignSelf: 'start' }} className="mt-2" gutterBottom variant="h5" component="div">
            Memberships
          </Typography>
          <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />

          <div className="m-5" style={{ width: "30vh", height: "30vh" }}>
            <MembershipStatusGraph daysUntilExpiry={daysUntilExpiry} walletInfo={walletInfo}/>
          </div>

          <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />

          {walletInfo.membershipActive ?
            <Typography style={{ alignSelf: 'start', fontSize: 16, }} className="mt-2" gutterBottom variant="h5" component="div">
              {`Your Membership plan ends on ${ClienUtil.getFriendlyDateFromMs(walletInfo.memberShipEnds ?? 0)}`}
            </Typography>
            :
            <>
              <Fab variant="extended" className="m-4" onClick={HandleSeeCatalog}>
                <DirectionsBusIcon sx={{ mr: 1 }} />
                View Plans
              </Fab>
            </>
          }



        </Card>

        {/* Tickets card */}
        <Card className="col-6 m-2 " elevation={12} style={{ display: "flex", justifyContent: "center", alignItems: "center", flexDirection: 'column', width: "46%", height:"407px",  }}>
          <Typography style={{ alignSelf: 'start' }} className="mt-2" gutterBottom variant="h5" component="div">
            Tickets
          </Typography>
          <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />

          <Card className="container-fluid m-3" elevation={12} style={{  borderRadius: '16px',backgroundColor: 'rgba(80, 200, 255, 0.5)', display: "flex", justifyContent: "center", alignItems: "center", flexDirection: 'column', width: "60%", height:"407px" }}>
            <Typography className="mt-3" gutterBottom variant="h6" component="div">
              you have 
            </Typography>
            <h2 className="display-1 m-1">{walletInfo.ticketAmount}</h2> 
            <Typography className="mb-3 mt-2" gutterBottom variant="h6" component="div">
              tickets
            </Typography>
          </Card>



          <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />
          <Fab variant="extended" className="m-4" onClick={HandleSeeCatalog}>
            <ConfirmationNumberIcon sx={{ mt:3,mb:3,ml:1, mr:1 }} />
            Add More
          </Fab>


        </Card>

      </div>
    </Card>


  );
};

export default WalletDetailsDesktop;

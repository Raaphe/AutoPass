import React, { FC, useEffect, useState } from "react";
import "./WalletDetails.module.scss";
import { useNavigate } from "react-router-dom";
import ClientAuthService from "../../ClientAuthService";
import ClienUtil from "../../ClientUtil";
import * as API from "../../Service";
import { UserWallet } from "../../Service";
import { Button, Card, Fab, Typography } from "@mui/material";
import Divider from '@mui/material/Divider';
import { CircularProgressbarWithChildren, buildStyles } from "react-circular-progressbar";
import utilService from "../../ClientUtil";
import DirectionsBusIcon from '@mui/icons-material/DirectionsBus';


interface WalletDetailsProps {
}



const WalletDetails: FC<WalletDetailsProps> = () => {
  const navigate = useNavigate();
  const [walletInfo, setWalletInfo] = useState<UserWallet>({});
  const [daysUntilExpiry, setDaysUntilExpiry] = useState(0);

  useEffect(() => {
    const getUserImage = () => {
      const config = ClientAuthService.getApiConfig();
      const walletAPI = new API.WalletControllerApi(config);

      walletAPI.getUserWalletByUserId(ClientAuthService.getUserId())
        .then(res => {
          if (res.status !== 200) return;
          setWalletInfo(res.data)
          setDaysUntilExpiry(walletInfo.membershipActive ?
            utilService.msToDays(walletInfo.memberShipEnds ?? 0) - utilService.msToDays(ClienUtil.getUTCNow().getTime())
            :
            0
          );
        })
        .catch(_ => {
        })
    }
    getUserImage();
  }, [navigate])



  function HandleSeeCatalog(): void {
    alert("not yet implemented")
  }

  return (

    <Card sx={{ maxWidth: "75%" }} className="container-fluid" elevation={12} variant="outlined">
      <button type="submit" className="btn btn-outline-primary mt-3" onClick={() => navigate(-1)}>
        <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" className="bi bi-arrow-left m-1" viewBox="0 0 16 16">
          <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8" />
        </svg>
      </button>
      <h2 className="display-3 m-2">Your wallet</h2>
      <div className="row" style={{ display: 'flex', justifyContent: 'center' }}>

        <Card className="col-6 m-2 " elevation={12} style={{ display: "flex", justifyContent: "center", alignItems: "center", flexDirection: 'column', width: "48%", height: "85%" }}>
          <Typography style={{ alignSelf: 'start' }} className="mt-2" gutterBottom variant="h5" component="div">
            Memberships
          </Typography>
          <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />

          <div style={{ width: "30vh", height: "30vh" }}>
            <CircularProgressbarWithChildren
              minValue={0}
              className="m-3"
              value={daysUntilExpiry}
              maxValue={walletInfo.membershipType?.membershipDurationDays ?? 1}
              styles={buildStyles({
                pathColor: "#027FFF",
                trailColor: "#D6D6D6"
              })}
            >
              <div style={{ fontSize: "125%" }}>
                <strong>
                  {walletInfo.membershipActive ? `${daysUntilExpiry} days Remaining` :
                    <Button>See Options</Button>
                  }

                </strong>
              </div>

            </CircularProgressbarWithChildren>
          </div>

          <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />

          {walletInfo.membershipActive ?
            <Typography style={{ alignSelf: 'start', fontSize: 16, }} className="mt-2" gutterBottom variant="h5" component="div">
              {`Your Membership plan ends on ${ClienUtil.getFriendlyDateFromMs(walletInfo.memberShipEnds ?? 0)}`}
            </Typography>
            :
            <>
              <Fab variant="extended" className="m-4" onClick={HandleSeeCatalog}>
                <DirectionsBusIcon sx={{ mr: 1}} />
                View Plans
              </Fab>
            </>
          }



        </Card>
        <Card className="col-6 m-2" elevation={12} style={{ display: "flex", justifyContent: "center", alignItems: "center", flexDirection: 'column', width: "48%", height: "85%" }}>
          <Typography style={{ alignSelf: 'start' }} className="mt-2" gutterBottom variant="h5" component="div">
            Tickets
          </Typography>
          <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />
        </Card>

      </div>
    </Card>


  );
};

export default WalletDetails;

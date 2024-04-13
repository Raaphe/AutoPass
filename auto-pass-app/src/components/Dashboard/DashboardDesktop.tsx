import React, { FC, useEffect, useState } from "react";
import "./Dashboard.module.scss";
import { motion } from "framer-motion";
import ClientAuthService from "../../ClientAuthService";
import { useNavigate } from "react-router-dom";
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import HistoryIcon from '@mui/icons-material/History';
import ReceiptIcon from '@mui/icons-material/Receipt';
import AccountBoxIcon from '@mui/icons-material/AccountBox';
import { GoogleWalletControllerApi, GoogleWalletPassURLViewModel, User, UserControllerApi, UserWallet } from "../../Service";
import MembershipStatusGraph from "../Views/MembershipStatusGraph";
import ClientUtil from "../../ClientUtil";
import { Button, Card, Divider, IconButton, Typography } from "@mui/material";
import WalletIcon from '@mui/icons-material/Wallet';
import GoogleAddButton from "../../assets/enCA_add_to_google_wallet_add-wallet-badge.png";


interface DashboardDesktopProps { }


const DashboardDesktop: FC<DashboardDesktopProps> = () => {
  const navigate = useNavigate();
  const [userData, setUserData] = useState<User>();
  const [walletInfo, setWalletInfo] = useState<UserWallet>({});
  const [daysUntilExpiry, setDaysUntilExpiry] = useState(0);
  const [role, setRole] = useState("");
  const [googleWalletSaveURL, setGoogleWalletSaveURL] = useState<GoogleWalletPassURLViewModel>();

  useEffect(() => {
    const config = ClientAuthService.getApiConfig();
    const userAPI = new UserControllerApi(config);
    const googleWalletApi = new GoogleWalletControllerApi(config);

    const fetchUserData = async () => {
      await userAPI
        .getUser()
        .then((res) => {
          setUserData(res.data);
          setRole(res.data.role ?? "")
        })
        .catch(() => {
          navigate("/");
        });
    };

    const getSavePassURL = async () => {
      await googleWalletApi.getSavePassURL(ClientAuthService.getUserId())
        .then(res => {
          if (res.status !== 200 || res.data === null || res.data.passUrl === "") {
            return;
          }

          setGoogleWalletSaveURL(res.data);
        });
    }

    getSavePassURL();

    ClientUtil.getUserWalletInfo(setDaysUntilExpiry, setWalletInfo, walletInfo);
    fetchUserData();
  }, [navigate]);



  return (
    <Card elevation={14} className="container mt-5" sx={{ maxWidth: "55%" }}>
      <motion.div
        className="row mb-4 mt-4"
        initial={{ opacity: 0, scale: 0.5 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5 }}
      >
        {/* Welcome Card */}
        <div className="col-sm-4 d-flex justify-content-center align-items-center" >
          <Card className="card p-3 h-100" style={{ width: "100%" }} elevation={15}>
            <h5 className="display-6">Welcome to AutoPass, {userData?.firstName} 🚏</h5>
            <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />
            <Typography className="mt-3" gutterBottom variant="h6" component="div">
              This is your transit overview. Access you profile here
              <IconButton aria-label="profile" color="primary" onClick={() => navigate("/profile")}>
                <AccountBoxIcon></AccountBoxIcon>
              </IconButton>
            </Typography>
          </Card>
        </div>

        {/* Invoices */}
        <div className="col-sm-4 d-flex justify-content-center align-items-center" >
          <Card className="card p-3 h-100" style={{ width: "100%" }} elevation={15}>
            <h5 className="display-6">Invoices</h5>
            <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />
            <IconButton aria-label="products" className="m-3 mt-4" color="primary" sx={{ cursor: "pointer", fontSize: '7rem' }} onClick={() => navigate("/invoices")}>
              <ReceiptIcon sx={{ fontSize: 'inherit' }}></ReceiptIcon>
            </IconButton>
          </Card>
        </div>

        {/* Membership Details */}
        <div className="col-sm-4 d-flex justify-content-center align-items-center" >
          <Card className="card p-3 h-100" style={{ width: "100%" }} elevation={15}>
            <h5 className="display-6">Membership</h5>
            <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />
            <MembershipStatusGraph daysUntilExpiry={daysUntilExpiry} walletInfo={walletInfo} />
          </Card>
        </div>
      </motion.div>

      <motion.div
        className="row mb-4 mt-4"
        initial={{ opacity: 0, scale: 0.5 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5 }}
      >
        {/* Travel History List */}
        <div className="col-sm-4 d-flex justify-content-center align-items-center" >
          <Card className="card p-3 h-100" style={{ width: "100%" }} elevation={15}>
            <h5 className="display-6">View Transit History</h5>
            <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />
            <IconButton aria-label="transit-history" className="m-3 mt-4" color="primary" sx={{ cursor: "pointer", fontSize: '7rem' }} onClick={() => navigate("/transit-history")}>
              <HistoryIcon sx={{ fontSize: 'inherit' }}></HistoryIcon>
            </IconButton>
          </Card>
        </div>

        {/* Link to google wallet pass or option to link your google */}
        <div className="col-sm-4 d-flex justify-content-center align-items-center">
          {!(userData?.googleAccessToken === "") || role === "GOOGLE_USER" ?

            userData?.isGoogleWalletPassAdded ?
              <Card className="card p-3 h-100" style={{ width: "100%" }} elevation={15}>
                <h5 className="display-6">View your wallet pass</h5>
                <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />
                <Typography className="mt-3" gutterBottom variant="h6" component="div">
                  See your Google wallet pass here
                  <IconButton aria-label="wallet-pass" color="primary" onClick={() => navigate("/profile")} href={googleWalletSaveURL?.passUrl ?? ""}>
                    <WalletIcon></WalletIcon>
                  </IconButton>
                </Typography>
              </Card>
              :
              <Card className="card p-3 h-100" style={{ width: "100%" }} elevation={15}>
                <h5 className="display-6">Add the Google wallet pass</h5>
                <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />
                <Button className="mt-4" href={googleWalletSaveURL?.passUrl}>
                  <img src={GoogleAddButton} style={{transform:"scale(2.7)"}}/>
                </Button>
              </Card>
            :
            <Card className="card p-3 h-100" style={{ width: "100%" }} elevation={15}>
              <h5 className="display-6">Link your google account</h5>
              <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />
              <Typography className="mt-3" gutterBottom variant="h6" component="div">
                Link your Google account to add your own wallet in google wallet here
                <IconButton aria-label="profile" color="primary" onClick={() => navigate("/profile")}>
                  <AccountBoxIcon></AccountBoxIcon>
                </IconButton>
              </Typography>
            </Card>


          }

        </div>

        {/* Products button */}
        <div className="col-sm-4 d-flex justify-content-center align-items-center">
          <Card className="card p-3 h-100" style={{ width: "100%" }} elevation={15}>
            <h5 className="display-6">View Our Products</h5>
            <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />
            <IconButton aria-label="products" className="m-3 mt-4" color="primary" sx={{ cursor: "pointer", fontSize: '7rem' }} onClick={() => navigate("/products")}>
              <AddShoppingCartIcon sx={{ fontSize: 'inherit' }}></AddShoppingCartIcon>
            </IconButton>
          </Card>
        </div>

      </motion.div>

    </Card>
  );
};

export default DashboardDesktop;

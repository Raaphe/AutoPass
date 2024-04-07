import React, { FC, useEffect, useState } from "react";
import styles from "./Dashboard.module.scss";
import { motion } from "framer-motion";
import ClientAuthService from "../../ClientAuthService";
import { useNavigate } from "react-router-dom";
import {User, UserControllerApi, UserWallet} from "../../Service";
import StripeModule from "../StripeModule/Return";
import MembershipStatusGraph from "../Views/MembershipStatusGraph";
import ClientUtil from "../../ClientUtil";
import {Card} from "@mui/material";


interface DashboardProps {}


/**
* Dashboard - 2024-04-02
* Raaphe, Lamb
*
* AutoPass
*/
const Dashboard: FC<DashboardProps> = () => {
  const navigate = useNavigate();
  const [userData, setUserData] = useState<User>();
  const [walletInfo, setWalletInfo] = useState<UserWallet>({});
  const [daysUntilExpiry, setDaysUntilExpiry] = useState(0);

  useEffect(() => {
    // This is how we set up the access token inside the subsequent request's `Authorization Header` like so :
    // "Bearer <access_token>"
    const config = ClientAuthService.getApiConfig();
    const userAPI = new UserControllerApi(config);

    const fetchUserData = async () => {
      await userAPI
        .getUser()
        .then((res) => {
          setUserData(res.data);
        })
        .catch(() => {
          navigate("/");
        });
    };

    ClientUtil.getUserWalletInfo(setDaysUntilExpiry, setWalletInfo, walletInfo);
    fetchUserData();
  }, [navigate]);



  return (
    <Card elevation={14} className="container mt-5">
      <motion.div
        className="row g-5"
        initial={{ opacity: 0, y: 50 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        <div className="col-sm-3">
          <motion.div
            className="card h-100"
            initial={{ opacity: 0, scale: 0.5 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.5 }}
          >
            <MembershipStatusGraph daysUntilExpiry={daysUntilExpiry} walletInfo={walletInfo}/>
          </motion.div>
        </div>
        <div className="col-sm-9">
          <motion.div
            className="card h-100"
            initial={{ opacity: 0, scale: 0.5 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.5 }}
          >
            <div className="card-body d-flex flex-column justify-content-center">
              <div className="text-center">Current card</div>
              <div className={styles.blackDebitCard}>
                <div className={styles.chip}></div>
                <div className={styles.cardNumber}>
                  <div className={styles.number}>1234 5678 9012 3456</div>
                </div>
                <div className={styles.cardInfo}>
                  <div className={styles.cardHolder}>John Doe</div>
                  <div className={styles.validThru}>Valid Thru</div>
                  <div className={styles.expiration}>12/24</div>
                </div>
              </div>
            </div>
          </motion.div>
        </div>
      </motion.div>
      <StripeModule/>
      <motion.div
        className="row mb-5"
        initial={{ opacity: 0, scale: 0.5 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.5 }}
      >
        <div className="col-sm-4 d-flex flex-column">
          <motion.div
            className="card flex-grow-1"
            initial={{ opacity: 0, scale: 0.5 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.5 }}
          >
            <div className="card-body">
              <h5 className="card-title">Info card</h5>
              <p className="card-text">
                Some quick example text to build on the card title and make up
                the bulk of the card's content.
              </p>
            </div>
            <div className="mt-auto p-2">
              <a href="#" className="btn btn-primary btn-block">
                Go somewhere
              </a>
            </div>
          </motion.div>
        </div>
        <div className="col-sm-4 d-flex flex-column">
          <motion.div
            className="card flex-grow-1"
            initial={{ opacity: 0, scale: 0.5 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.5 }}
          >
            <div className="card-body">
              <h5 className="card-title">Your last trip</h5>
              <p className="card-text">
                You can view your last trip and see what
              </p>
              <p className="card-text">
                <small className="text-body-secondary">
                  Last updated 3 mins ago
                </small>
              </p>
            </div>
            <img
              src="..."
              className="card-img-bottom img-fluid"
              alt="..."
              style={{ height: "100%" }}
            />
            <div className="mt-auto p-2">
              <a href="#" className="btn btn-primary btn-block">
                Go somewhere
              </a>
            </div>
          </motion.div>
        </div>
        <div className="col-sm-4 d-flex flex-column">
          <motion.div
            className="card flex-grow-1"
            initial={{ opacity: 0, scale: 0.5 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.5 }}
          >
            <div className="card-body">
              <h5 className="card-title">Current payment plan</h5>
              <h2 className="card-text text-center">50$/month</h2>
            </div>
            <div className="mt-auto p-2">
              <a href="#" className="btn btn-primary btn-block">
                Go somewhere
              </a>
            </div>
          </motion.div>
        </div>
      </motion.div>
    </Card>
  );
};

export default Dashboard;

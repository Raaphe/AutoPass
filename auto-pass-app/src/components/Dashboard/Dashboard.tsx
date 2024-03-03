import React, { FC, useEffect, useState } from "react";
import styles from "./Dashboard.module.scss";
import { motion } from "framer-motion";
import ClientAuthService from "../../ClientAuthService";
import { useNavigate } from "react-router-dom";
import * as Api from "../../Service";


interface DashboardProps {}


const Dashboard: FC<DashboardProps> = () => {
  const navigate = useNavigate();
  const [userData, setUserData] = useState<Api.User>();

  const handleLogout = () => {
    ClientAuthService.logout();
    navigate("/login");
  };

  useEffect(() => {
    // This is how we setup the access token inside the subsequent request's `Authorization Header` like so :
    // "Bearer <access_token>"
    console.log(
      `user token when making call${ClientAuthService.getAccessTokenOrDefault()}`
    );
    const config = ClientAuthService.getApiConfig();
    const userAPI = new Api.UserControllerApi(config);

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
    fetchUserData();
  }, [navigate]);

  return (
    <div className="container mt-5">
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
            <div className="card-body d-flex flex-column justify-content-center">
              <img
                className="rounded mx-auto d-block"
                src="/qrPlaceholder.svg"
                width="180"
                height="180"
                alt="QR Code"
              />
              <div className="text-center">QR Code</div>
            </div>
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
    </div>
  );
};

export default Dashboard;

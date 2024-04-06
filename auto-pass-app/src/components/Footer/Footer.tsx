import React, { FC, useEffect, useState } from 'react';
import { motion, useAnimation } from 'framer-motion';
import styles from './Footer.module.scss';
import Logo from "../../assets/6.png";
import { useNavigate } from 'react-router-dom';
import GitHubIcon from '@mui/icons-material/GitHub';


/**
* Footer - 2024-04-02
* Raaphe, Lamb
*
* AutoPass
*/
const Footer: FC = () => {
  const navigate = useNavigate()

  return (
    <div
      className={`${styles.Footer} mt-5`}>
      <footer className="container py-5">
        <div className="row">
          <div className="col-12 col-md">

            <small className="d-block mb-3 text-muted">
              © 2024
              <img src={Logo} />
            </small>
          </div>
          <div className="col-6 col-md">
            <h5>Features</h5>
            <ul className="list-unstyled text-small">
              <li><a className="text-muted" href="#">Secure Authentication</a></li>
              <li><a className="text-muted" href="#" onClick={() => navigate("/wallet")}>Personal Wallet</a></li>
            </ul>
          </div>
          <div className="col-6 col-md">
            <h5>Resources</h5>
            <ul className="list-unstyled text-small">
              <li><a className="text-muted" href="https://www.stm.info/en" target='_blank' rel="noopener">Bus hours</a></li>
              <li><a className="text-muted" target='_blank' rel="noopener" href="#">Rates</a></li>
            </ul>
          </div>
          <div className="col-6 col-md">
            <h5>Developers</h5>
            <ul className="list-unstyled text-small">
              <li><a className="text-muted" href="https://github.com/Raaphe"
                target='_blank'
                rel="noopener"
              >
                <GitHubIcon className='mx-2' />
                @raphe

              </a>
              </li>
              <li><a className="text-muted" href="https://github.com/AliteralLamb"
                target='_blank'
                rel="noopener"
              >
                <GitHubIcon className='mx-2' />
                @AliteralLamb

              </a>
              </li>
              <li><a className="text-muted" href="https://github.com/ikacef"
                target='_blank'
                rel="noopener"
              >
                <GitHubIcon className='mx-2' />
                @ikacef

              </a>
              </li>
            </ul>
          </div>
          <div className="col-6 col-md">
            <h5>About</h5>
            <ul className="list-unstyled text-small">
              <li><a className="text-muted"
                onClick={() => navigate("/about")}
              >
                Team
              </a>
              </li>
              <li><a
                className="text-muted"
                href="https://cegepmv.ca/programmes/techniques-de-informatique"
                target='_blank'
                rel="noopener">
                Locations
              </a></li>
              <li><a className="text-muted" target='_blank'
                rel="noopener" href="https://www.stm.info/en/about/surveys/join-your-voice/my-voice-my-stm-privacy-policy">Privacy</a></li>
            </ul>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Footer;

import React, { FC, useEffect, useState } from 'react';
import { motion, useAnimation } from 'framer-motion';
import styles from './Footer.module.scss';
import { AiFillGithub } from "react-icons/ai";

const Footer: FC = () => {
  const [showFooter, setShowFooter] = useState(true);
  const controls = useAnimation();

  useEffect(() => {
    let lastScrollTop = 0;

    function handleScroll() {
      const currentScrollTop = window.scrollY || document.documentElement.scrollTop;

      setShowFooter(currentScrollTop > lastScrollTop);
      lastScrollTop = currentScrollTop;
    }

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  useEffect(() => {
    if (showFooter) {
      controls.start({ opacity: 1, y: 0 });
    } else {
      controls.start({ opacity: 0, y: 50 });
    }
  }, [showFooter, controls]);

  return (
    <motion.div
      className={`${styles.Footer}`}
      initial={{ opacity: 1, y: 0 }}
      animate={controls}
      transition={{ duration: 0.5 }}
    >
      <div className='container-fluid'>
        <div className='row p-2 mt-4'>
          <p className='col-2 text-light'>Copyright Ⓒ 2024 AutoPass</p>
          <div className='col-7'></div>
          <a className='col text-light'
            href='https://github.com/Raaphe'
            target='_blank'
            rel="noopener"
            aria-label='@Raaphe'><AiFillGithub />@Raaphe</a>
          <a className='col text-light'
            href='https://github.com/ikacef'
            target='_blank'
            rel="noopener"
            aria-label='@ikacef'><AiFillGithub />@ikacef</a>
          <a className='col text-light'
            href='https://github.com/AliteralLamb'
            target='_blank'
            rel="noopener"
            aria-label='@Aliterallamb'><AiFillGithub />@AliteralLamb</a>
        </div>
      </div>
    </motion.div>
  );
};

export default Footer;

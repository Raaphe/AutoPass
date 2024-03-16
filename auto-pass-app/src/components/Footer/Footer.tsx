import React, { FC, useEffect, useState } from 'react';
import { motion, useAnimation } from 'framer-motion';
import styles from './Footer.module.scss';
import { AiFillGithub } from "react-icons/ai";
import { Grid, useMediaQuery } from "@mui/material";



const Footer: FC = () => {
  const [showFooter, setShowFooter] = useState(true);
  const controls = useAnimation();

  const isMobile = useMediaQuery('(max-width:600px)');

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
      className={`${styles.Footer} mx-2`}
      initial={{ opacity: 1, y: 0 }}
      animate={controls}
      transition={{ duration: 0.5 }}
    >
      <Grid container spacing={2} alignItems="center" className='p-3'>
        <Grid item xs={12} sm={6} md={4}>
          <p className='text-light'>Copyright Ⓒ 2024 AutoPass</p>
        </Grid>
        <Grid item xs={12} sm={6} md={4} textAlign={isMobile ? "center" : "right"}>
          <a className='text-light m-3' 
            href='https://github.com/Raaphe'
            target='_blank'
            rel="noopener"
            aria-label='@Raaphe'><AiFillGithub />@Raaphe</a>
          <a className='text-light m-3'
            href='https://github.com/ikacef'
            target='_blank'
            rel="noopener"
            aria-label='@ikacef'><AiFillGithub />@ikacef</a>
          <a className='text-light m-3'
            href='https://github.com/AliteralLamb'
            target='_blank'
            rel="noopener"
            aria-label='@AliteralLamb'><AiFillGithub />@AliteralLamb</a>
        </Grid>
      </Grid>
    </motion.div>
  );
};

export default Footer;

import React, {FC} from 'react';
import styles from './Footer.module.scss';
import { AiFillGithub } from "react-icons/ai";

interface FooterProps {
}

const Footer: FC<FooterProps> = () => (
    <div className={styles.Footer}>
        <div className='row p-2 mt-4 '>
            <p className='col-4'>Copyright Ⓒ 2024 AutoPass</p>
            <img className='col-4 ' src='' alt='logo'/>
            <a className='col'
            href='https://github.com/Raaphe'
            target='_blank'
            rel="noopener"
            aria-label='@Raaphe'><AiFillGithub />@Raaphe</a>
            <a className='col'
            href='https://github.com/ikacef'
            target='_blank'
            rel="noopener"
            aria-label='@ikacef'><AiFillGithub />@ikacef</a>
            <a className='col'
            href='https://github.com/AliteralLamb'
            target='_blank'
            rel="noopener"
            aria-label='@Aliterallamb'><AiFillGithub />@AliteralLamb</a>
        </div>
    </div>
);

export default Footer;

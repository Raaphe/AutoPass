import React, {FC} from 'react';
import styles from './Header.module.scss';

interface HeaderProps {
}

const Header: FC<HeaderProps> = () => (
    <div className={styles.Header}>
        Header Component
    </div>
);

export default Header;

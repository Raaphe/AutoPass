import React, {FC} from 'react';
import styles from './UserLandingPage.module.scss';
import ClientAuthService from '../../ClientAuthService';
import {useNavigate} from 'react-router-dom';

interface UserLandingPageProps {
}

const UserLandingPage: FC<UserLandingPageProps> = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        ClientAuthService.logout();
        navigate("/login")

    }


    return (
        <div className={styles.UserLandingPage}>
            UserLandingPage Component
            <button onClick={handleLogout} className='btn btn-dark'>Logout</button>
        </div>

    );
};

export default UserLandingPage;

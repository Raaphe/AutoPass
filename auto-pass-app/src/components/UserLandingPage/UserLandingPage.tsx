import React, {FC} from 'react';
import styles from './UserLandingPage.module.scss';

interface UserLandingPageProps {
}

const UserLandingPage: FC<UserLandingPageProps> = () => (
    <div className={styles.UserLandingPage}>
        UserLandingPage Component
    </div>
);

export default UserLandingPage;

import React, {FC} from 'react';
import styles from './Dashboard.module.scss';

interface DashboardProps {
}

const Dashboard: FC<DashboardProps> = () => (
    <div className={styles.Dashboard}>
        Dashboard Component
    </div>
);

export default Dashboard;

import React, {FC} from 'react';
import './LandingPage.scss';
import {Link} from 'react-router-dom';

interface LandingPageProps {
}

const LandingPage: FC<LandingPageProps> = () => (
    <div className="container-fluid">

        <Link to="/login">
            <button className="btn btn-primary">Go to Login</button>
        </Link>
        LandingPage Component
    </div>
);

export default LandingPage;

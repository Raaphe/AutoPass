import React, {FC} from 'react';
import './LandingPage.scss';
import {useNavigate} from 'react-router-dom';

interface LandingPageProps {
}

const LandingPage: FC<LandingPageProps> = () => {

    const navigate = useNavigate();

    function handleClickLogon(): void {
        navigate("/login");
    }

    return (

        <div className="container-fluid">
            <button onClick={handleClickLogon} className="btn btn-primary">Go to Login</button>
            LandingPage Component
        </div>

    );
};

export default LandingPage;

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

    function handleClickSignUp(): void {
        navigate("/signup");
    }

    return (
        <div className="container-fluid m-5">
            <button onClick={handleClickLogon} className="btn btn-primary">Go to Login</button>
            <button onClick={handleClickSignUp} className="btn btn-primary">Signup</button>
            <button onClick={handleClickLogon} className="btn btn-primary m-3">Go to Login</button>
            LandingPage Component
        </div>

    );
};

export default LandingPage;

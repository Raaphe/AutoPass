import React, { FC, useState } from 'react';
import './LandingPage.scss';
import { useNavigate } from 'react-router-dom';

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
    const [showButtons, setShowButtons] = useState(false);

    const handleGetStartedClick = () => {
        setShowButtons(true);
    };

    return (
        <div className="container-fluid">
            <div className="row justify-content-center align-items-center vh-100">
                <div className="col-md-6 text-center">
                    <h1 className="display-4 fw-bold">Virtual Pass for Public Transportation</h1>
                    <p className="lead">Simplify your commute with our digital pass system. No more hassle, no more queues.</p>
                    {showButtons ? (
                        <div className="d-grid gap-3">
                            <a href="#" className="btn btn-primary btn-lg" onClick={handleClickLogon}>Login</a>
                            <a href="#" className="btn btn-secondary btn-lg" onClick={handleClickSignUp}>Sign Up</a>
                        </div>
                    ) : (
                        <button className="btn btn-primary btn-lg" onClick={handleGetStartedClick}>Get Started</button>
                    )}
                </div>
            </div>
        </div>
    );
};

export default LandingPage;

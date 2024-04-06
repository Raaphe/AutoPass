import React, { FC } from 'react';
import { useNavigate } from 'react-router-dom';
import ErrorPageImage from "../../assets/9NonTransparentHD.png"

interface ErrorPageProps { }


/**
* ErrorPage - 2024-04-02
* Raaphe, Lamb
*
* AutoPass
*/
const ErrorPage: FC<ErrorPageProps> = () => {
    const navigate = useNavigate();

    function handleClickBack(): void {
        navigate(-1);
    }

    const containerStyle = {
        backgroundImage: `url(${ErrorPageImage})`,
        backgroundPosition: 'center center',
        backgroundRepeat: 'no-repeat',
        width: '100vw',
        backgroundSize: '500px auto',
        height: '100vh',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
    };

    return (
        <div style={containerStyle} className="container-fluid d-flex justify-content-center align-items-center vh-100">
            <div className="text-center">
                <h1 className="display-4">404 - Page Not Found</h1>
                <h5 className="lead">Oops! The page you are looking for does not exist.</h5>
                <button onClick={handleClickBack} className="btn btn-dark mt-3">Go Back
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="m-1 bi bi-arrow-return-left" viewBox="0 0 16 16">
                        <path fill-rule="evenodd" d="M14.5 1.5a.5.5 0 0 1 .5.5v4.8a2.5 2.5 0 0 1-2.5 2.5H2.707l3.347 3.346a.5.5 0 0 1-.708.708l-4.2-4.2a.5.5 0 0 1 0-.708l4-4a.5.5 0 1 1 .708.708L2.707 8.3H12.5A1.5 1.5 0 0 0 14 6.8V2a.5.5 0 0 1 .5-.5" />
                    </svg>
                </button>
            </div>
        </div>
    );
};

export default ErrorPage;

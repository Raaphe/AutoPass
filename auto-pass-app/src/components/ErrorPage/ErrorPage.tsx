import React, { FC } from 'react';
import { useNavigate } from 'react-router-dom';

interface ErrorPageProps { }

const ErrorPage: FC<ErrorPageProps> = () => {
    const navigate = useNavigate();

    function handleClickBack(): void {
        navigate(-1);
    }

    return (
        <div className="container-fluid d-flex justify-content-center align-items-center vh-100">
            <div className="text-center">
                <h1 className="display-4">404 - Page Not Found</h1>
                <p className="lead">Oops! The page you are looking for does not exist.</p>
                <button onClick={handleClickBack} className="btn btn-primary mt-3">Go Back
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-arrow-return-left" viewBox="0 0 16 16">
                        <path fill-rule="evenodd" d="M14.5 1.5a.5.5 0 0 1 .5.5v4.8a2.5 2.5 0 0 1-2.5 2.5H2.707l3.347 3.346a.5.5 0 0 1-.708.708l-4.2-4.2a.5.5 0 0 1 0-.708l4-4a.5.5 0 1 1 .708.708L2.707 8.3H12.5A1.5 1.5 0 0 0 14 6.8V2a.5.5 0 0 1 .5-.5" />
                    </svg></button>
            </div>
        </div>
    );
};

export default ErrorPage;

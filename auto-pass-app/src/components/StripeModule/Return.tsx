import React, { FC, useEffect, useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import ClientAuthService from "../../ClientAuthService";
import * as API from "../../Service/api";
import { Card } from "@mui/material";

interface ReturnProps {
}

const Return: FC<ReturnProps> = () => {
    const [status, setStatus] = useState("");
    const [customerEmail, setCustomerEmail] = useState("");
    const config = ClientAuthService.getApiConfig();
    const stripeApi = new API.StripeControllerApi(config);
    const navigate = useNavigate();

    useEffect(() => {
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const sessionId = urlParams.get('session_id');

        const getSessionData = async () => {
            await stripeApi.getSessionStatus(sessionId ?? "")
                .then((res) => {

                    if (res.status !== 200) {
                        return null;
                    }

                    setStatus(res.data.status ?? "");
                    setCustomerEmail(res.data.customerEmail ?? "");
                })
                .catch((_) => {
                });
        }

        getSessionData();
    }, []);

    if (status === 'open') {
        return (
            <Navigate to="/checkout" />
        )
    }

    if (status === 'complete') {
        return (
            <Card className="container mt-5" elevation={12} >
                <button type="submit" className="btn btn-outline-primary mb-3 mt-3" onClick={() => navigate("/wallet")}>
                    <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" className="bi bi-arrow-left m-1" viewBox="0 0 16 16">
                        <path fillRule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8" />
                    </svg>
                </button>
                <section id="success">
                    <p>
                        <h1 className="display-4">We appreciate your business!</h1>
                        A confirmation email will be sent to {customerEmail}.

                        If you have any questions, please email <a href="mailto:orders@example.com">raphaelpaquin19@gmail.com</a>.
                    </p>
                </section>
            </Card>
        )
    }

    return null;
}

export default Return;
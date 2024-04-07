import React, { useCallback, FC } from "react";
import { loadStripe } from '@stripe/stripe-js';
import {
    EmbeddedCheckoutProvider,
    EmbeddedCheckout
} from '@stripe/react-stripe-js';
import * as API from "../../Service/api";
import ClientService from "../../ClientAuthService";
import { useLocation, useNavigate } from 'react-router-dom';
import { Card } from "@mui/material";

interface CheckoutFormProps {
}

const CheckoutForm: FC<CheckoutFormProps> = () => {

    const config = ClientService.getApiConfig();
    const stripeAPI = new API.StripeControllerApi(config);
    const stripePromise = loadStripe("pk_test_51Osu0DH4PBFYm1VAT5JSXmaL4Q7WJHl3I0qWMmOxOmP86Gy67L9IFx1iOppab0q8MsWiJf7wUAwbGNMutz5UA0fk00Fh99vScQ");

    const location = useLocation();
    const secretRequestDto: API.GetClientStripeSecretDTO = location.state || {};

    const navigate = useNavigate();

    const fetchClientSecret = useCallback(async () => {
        return await stripeAPI.getClientSecret(secretRequestDto)
            .then((res) => {
                if (res.status !== 200) {
                    return "";
                }
                return res.data ?? "";
            })
            .catch((e) => {
                alert("Error loading checkout page... Try again later or contact support.")
                return ""
            })
    }, []);

    const options = { fetchClientSecret };

    return (
        <Card id="checkout" elevation={7} className="container p-3">
            <button type="submit" className="btn btn-outline-primary mb-3" onClick={() => navigate(-1)}>
                <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" className="bi bi-arrow-left m-1" viewBox="0 0 16 16">
                    <path fillRule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8" />
                </svg>
            </button>
            <EmbeddedCheckoutProvider
                stripe={stripePromise}
                options={options}
            >
                <EmbeddedCheckout />
            </EmbeddedCheckoutProvider>
        </Card>
    );

}

export default CheckoutForm;


import React, { FC } from "react";
import Stripe from "react-stripe-checkout";
import axios from "axios";
import AuthClientService from "../../ClientAuthService";
import { Fab } from "@mui/material";

const StripeModule = () => {
    const handleToken = async (token) => {
        console.log(token);
        await axios
            .post("http://localhost:9090/api/payment/charge", "", {
                headers: {
                    token: token.id,
                    amount: 500,
                    Authorization: `Bearer ${AuthClientService.getAccessTokenOrDefault()}`
                },
            })
            .then(() => {
                alert("Payment Success");
            })
            .catch((error) => {
                alert(error);
            });
    }
    return (
        <div className="App">
            <Stripe
                stripeKey="pk_test_51Osu0DH4PBFYm1VAT5JSXmaL4Q7WJHl3I0qWMmOxOmP86Gy67L9IFx1iOppab0q8MsWiJf7wUAwbGNMutz5UA0fk00Fh99vScQ"
                token={handleToken}
            >
                <Fab variant="extended">

                    Pay
                </Fab>
            </Stripe>
        </div>
    );
}
export default StripeModule;
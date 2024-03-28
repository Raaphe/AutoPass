import React, { ChangeEvent, FC, useState } from 'react';
import { AuthenticationApi } from '../../Service';
import { useNavigate } from 'react-router-dom';
import Loading from './Loading';

interface ForgotPassword {
}

const ForgotPassword: FC<ForgotPassword> = () => {

    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [isEmailSent, setIsEmailSent] = useState(false);
    const [isEmailReceived, setIsEmailReceived] = useState<any>(null);
    const api: AuthenticationApi = new AuthenticationApi();


    const handleSubmit = async (e: any) => {
        e.preventDefault();
        setIsEmailSent(true);
        try {
            const res = await api.forgotPassword(email.trim());
            setIsEmailReceived(res.data);
        } catch (error) {
            setIsEmailReceived(false);
        }
    };

    function updateField(event: ChangeEvent<HTMLInputElement>): void {
        setEmail(event.target.value);
    }
    console.log(isEmailSent + " did you click?");
    console.log(isEmailReceived + " did you receive?");

    return (
        <div className="row m-5">
            <div className="col-md-6 offset-md-3">
                <button type="submit" className="btn btn-outline-primary mb-3" onClick={() => navigate(-1)}>
                    <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" className="bi bi-arrow-left m-1" viewBox="0 0 16 16">
                        <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8" />
                    </svg>
                </button>
                <h2 className="text-center">Reset Password</h2>
                <form onSubmit={(e) => handleSubmit(e)}>
                    <div className="mb-3">
                        <div className="input-group mb-3">
                            <input
                                type="email"
                                id='email'
                                value={email}
                                onChange={updateField}
                                required
                                className="form-control"
                                placeholder="Enter your email"
                                aria-label="Enter your email"
                                aria-describedby="button-addon2"
                            />
                            <button className="btn btn-outline-secondary" type="submit" id="button-addon2">
                                Change Password
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-envelope-arrow-up m-1" viewBox="0 0 16 16">
                                    <path d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v4.5a.5.5 0 0 1-1 0V5.383l-7 4.2-1.326-.795-5.64 3.47A1 1 0 0 0 2 13h5.5a.5.5 0 0 1 0 1H2a2 2 0 0 1-2-1.99zm1 7.105 4.708-2.897L1 5.383zM1 4v.217l7 4.2 7-4.2V4a1 1 0 0 0-1-1H2a1 1 0 0 0-1 1" />
                                    <path d="M12.5 16a3.5 3.5 0 1 0 0-7 3.5 3.5 0 0 0 0 7m.354-5.354 1.25 1.25a.5.5 0 0 1-.708.708L13 12.207V14a.5.5 0 0 1-1 0v-1.717l-.28.305a.5.5 0 0 1-.737-.676l1.149-1.25a.5.5 0 0 1 .722-.016" />
                                </svg>
                            </button>
                        </div>
                    </div>
                </form>

                <form onSubmit={handleSubmit}>
                    {/* Your form fields here */}
                    <Loading isEmailSent={isEmailSent} isEmailReceived={isEmailReceived} />
                </form>

            </div>
        </div>

    );
};

export default ForgotPassword;

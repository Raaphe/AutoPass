import React, {ChangeEvent, FC, useState} from 'react';
import { AuthenticationApi } from '../../Service'; 

interface ForgotPassword {
}

const ForgotPassword: FC<ForgotPassword> = () => {
    const [email, setEmail] = useState("");
    const api: AuthenticationApi = new AuthenticationApi();

    async function handleSubmit (e: React.FormEvent<HTMLFormElement>) {
        await api.forgotPassword(email.trim());
    }

    function updateField(event: ChangeEvent<HTMLInputElement>): void {
        setEmail(event.target.value);
    }

    return (
    <div className="row m-5">
        <div className="col-md-6 offset-md-3">
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
                        <button className="btn btn-outline-secondary" type="submit" id="button-addon2">Change Password</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    );
};

export default ForgotPassword;

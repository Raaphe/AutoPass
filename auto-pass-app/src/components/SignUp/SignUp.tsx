import React, {FC, useState} from 'react';
import './Login.scss';
import {SignUpDTO} from '../../Service';
import ClientAuthService from '../../ClientAuthService';
import {useNavigate} from 'react-router-dom';

interface SignUpProps {
}

const SignUp: FC<SignUpDTO> = () => {

    const navigate = useNavigate();
    const [signUpData, setSignUpData] = useState<SignUpDTO>({
        firstname: "",
        lastname: "",
        email: "",
        password: "",
        role: 'USER'
    });

    const [signUpData, setSignUpData] = useState<SignUpDTO>({
        firstname: "",
        lastname: "",
        email: "",
        password: "",
        role: "" // Ajoutez la propriété role ici avec une valeur initiale appropriée
    });

    const handleLogin = async (event: React.FormEvent) => {

        // Prevents page reload. may be removed
        event.preventDefault();

        var isCredentialCorrect: boolean = await ClientAuthService.login(signUpData);

        console.log(isCredentialCorrect);
        

        if (!isCredentialCorrect) {
            setSignUpData({email: "", password: "",});
        } else {
            navigate("/home");
        }
        
    }

    return (
        <div className="container mt-5">
            <div className="row">
                <div className="col-md-6 offset-md-3">
                    <h2 className="text-center">Login</h2>
                    <form onSubmit={(e) => handleLogin(e)}>
                        <div className="mb-3">
                            <label htmlFor="email" className="form-label">Email Address</label>
                            <input
                                type="email"
                                className="form-control"
                                id="email"
                                value={signUpData.email}
                                onChange={updateField}
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="password" className="form-label">Password</label>
                            <input
                                type="password"
                                className="form-control"
                                id="password"
                                value={signUpData.password}
                                onChange={updateField}
                                required
                            />
                        </div>
                        <button type="submit" className="btn btn-primary">Login</button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default SignUp;

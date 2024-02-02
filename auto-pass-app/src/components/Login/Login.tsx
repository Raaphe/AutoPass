import React, {FC, useState} from 'react';
import './Login.scss';
import {SignInDTO} from '../../Service';
import ClientAuthService from '../../ClientAuthService';
import {useNavigate} from 'react-router-dom';

interface LoginProps {
}

const Login: FC<LoginProps> = () => {

    const navigate = useNavigate();
    const [signInData, setSignInData] = useState<SignInDTO>({
        email: "",
        password: "",
    });

    const updateField = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSignInData({
            ...signInData,
            [e.target.id]: e.target.value.trim()
        });
    };

    const handleLogin = async (event: React.FormEvent) => {

        // Prevents page reload. may be removed
        event.preventDefault();

        var isCredentialCorrect: boolean = await ClientAuthService.login(signInData);

        console.log(isCredentialCorrect);
        

        if (!isCredentialCorrect) {
            setSignInData({email: "", password: "",});
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
                                value={signInData.email}
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
                                value={signInData.password}
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

export default Login;

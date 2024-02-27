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
            <div className="row justify-content-end"> {/* justify-content-end aligns items to the right */}
                <div className="col-md-6">
                    <div className="card shadow thinner-card"  style={{ marginLeft: 'auto', marginRight: 0 }}>
                        <div className="card-body">
                            <h2 className="card-title text-center mb-4">Login</h2>
                            <form onSubmit={handleLogin}>
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
                                
                                <hr className="my-4" />
                                <div className="mb-3 form-check">
                                    <input
                                        type="checkbox"
                                        className="form-check-input"
                                        id="rememberMe"
                                    />
                                    <label className="form-check-label" htmlFor="rememberMe">Remember Me</label>
                                </div>
                                <button onClick={() => navigate("/forgot-password")} type="button" className="btn btn-link">Forgot Password</button>
                                <div className="text-end"> {/* text-end aligns the content to the right */}
                                    <button type="submit" className="btn btn-primary">Login</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    );
};

export default Login;

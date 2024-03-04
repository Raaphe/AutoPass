import React, { FC, useState } from 'react';
import './Login.scss';
import { AuthenticationResponse, SignInDTO } from '../../Service';
import ClientAuthService from '../../ClientAuthService';
import { useNavigate, useLocation } from 'react-router-dom';


const useQuery = () => {
  return new URLSearchParams(useLocation().search);
}

interface LoginProps {
}

const Login: FC<LoginProps> = () => {
    const query = useQuery();
    const navigate = useNavigate();

    const [signInData, setSignInData] = useState<SignInDTO>({
        email: "",
        password: "",
    });

    if (query.get("accessToken") !== null) {
        var authResponse: AuthenticationResponse = {
            access_token: query.get("accessToken") ?? "",
            token_type: query.get('tokenType') ?? "", 
            user_id: parseInt(query.get('id') ?? "-1"),
            refresh_token: query.get('refreshToken') ?? ""
        }

        ClientAuthService.setAuthenticationResponseInMemory(authResponse);
        navigate("/home");
    }

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

    const handleLoginWithGoogle = (): void => {
        window.location.href = "http://localhost:9090/oauth2/authorization/google";
    }

    return (
        <div className="container mt-5">
            <div className="row">
                <div className="col-md-6 offset-md-3">
                    <h2 className="text-center">Login</h2>
                    <div>
                        With Google <button onClick={handleLoginWithGoogle} className='btn btn-link'>click here</button>
                    </div>
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
                            <button onClick={() => navigate("/forgot-password")} type="button" className="btn btn-link">Forgot Password</button>
                        </div>
                        <button type="submit" className="btn btn-primary">Login</button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Login;

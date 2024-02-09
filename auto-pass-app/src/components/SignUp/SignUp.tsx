import React, {FC, useState} from 'react';
import './SignUp.scss';
import {SignUpDTO} from '../../Service';
import ClientAuthService from '../../ClientAuthService';
import {useNavigate} from 'react-router-dom';

interface SignUpProps {

}

const SignUp: FC<SignUpProps> = () => {

    const navigate = useNavigate();
    const loginInHandle = () => navigate('/login');
    const [signUpData, setSignUpData] = useState<SignUpDTO>({
        firstname: "",
        lastname: "",
        email: "",
        password: "",
        role: "USER"

    });

    const updateField = (e: React.ChangeEvent<HTMLInputElement>) => {
        setSignUpData({
            ...signUpData,
            [e.target.id]: e.target.value.trim()
        });
    };

    const handleSignUp = async (event: React.FormEvent) => {

        // Prevents page reload. may be removed
        event.preventDefault();

        var isCredentialCorrect: boolean = await ClientAuthService.login(signUpData);

        console.log(isCredentialCorrect);
        

        if (!isCredentialCorrect) {
            setSignUpData(
                {firstname: "",
                lastname: "",
                email: "",
                password: "",
                role: "USER"
                });
        } else {
            navigate("/home");
        }
        
    }

    return (
        <div className="container mt-5">
            <div className="row">
                <div className="col-md-6 offset-md-3">
                    <h2 className="text-center">SignUp</h2>
                    <form onSubmit={(e) => handleSignUp(e)}>
                        <div className="mb-3">
                            <label htmlFor="first-name" className="form-label">First Name</label>
                            <input
                                type="text"
                                placeholder='John'
                                className="form-control"
                                id="firstname"
                                value={signUpData.firstname}
                                onChange={updateField}
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="password" className="form-label">Last Name</label>
                            <input
                                type="text"
                                placeholder='Doe'
                                className="form-control"
                                id="lastname"
                                value={signUpData.lastname}
                                onChange={updateField}
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label htmlFor="email" className="form-label">Email Address</label>
                            <input
                                type="email"
                                placeholder='JohnDoe@gmail.com'
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
                                placeholder='**********'
                                className="form-control"
                                id="password"
                                value={signUpData.password}
                                onChange={updateField}
                                required
                            />
                        </div>
                        <button type="submit" className="btn btn-primary">Register</button>
                        <p>Alreay have an account ?</p>
                        <button type="submit" className="btn btn-secondary" onClick={loginInHandle}>Login</button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default SignUp;

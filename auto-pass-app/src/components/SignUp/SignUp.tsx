import React, { FC, useState } from 'react';
import './SignUp.scss';
import { SignUpDTO } from '../../Service';
import ClientAuthService from '../../ClientAuthService';
import { useNavigate } from 'react-router-dom';
import { Card } from '@mui/material';

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

        var signupSuccess: boolean = await ClientAuthService.signup(signUpData);

        if (!signupSuccess) {
            setSignUpData(
                {
                    firstname: "",
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
                    <Card elevation={16}>
                        <button type="submit" className="btn btn-outline-primary m-3" onClick={() => navigate(-1)}>
                            <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" className="bi bi-arrow-left m-1" viewBox="0 0 16 16">
                                <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8" />
                            </svg>
                        </button>
                        <h1 className="display-3 text-center">Register</h1>
                        <form className='m-3' onSubmit={(e) => handleSignUp(e)}>
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
                                    placeholder='*****'

                                    className="form-control"
                                    id="password"
                                    value={signUpData.password}
                                    onChange={updateField}
                                    required
                                />
                                <button type="submit" className="btn btn-link" onClick={loginInHandle}>Alreay have an account ?</button>
                            </div>
                            <button type="submit" className="btn btn-primary">Register</button>


                        </form>
                    </Card>
                </div>


            </div>
        </div>
    );
};

export default SignUp;

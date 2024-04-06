import React, {FC, useState} from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { AuthenticationApi, ChangePasswordDTO } from '../../Service';

interface ChangePasswordProps {
}


/**
* ChangePassword - 2024-04-02
* Raaphe
*
* AutoPass
*/
const ChangePassword: FC<ChangePasswordProps> = () => {
    const [passwords, setPasswords] = useState({
        password1: "",
        password2: "",
    });
    const navigate = useNavigate();
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const token = queryParams.get('token');
    const authApi = new AuthenticationApi();


    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>): Promise<void> => {

        e.preventDefault();
        if (passwords.password1 === passwords.password2) {
            let changePasswordDTO: ChangePasswordDTO = {
                password: passwords.password1,
                token: token ?? ""
            }

            await authApi.changePassword(changePasswordDTO)
                .then(res => {
                    if (res.data) {
                        navigate("/login");
                        alert("successfully changed password");
                    }
                })
                .catch(() => {
                    setPasswords({
                        password1: passwords.password1,
                        password2: ""
                    })        
                })

        } else {
            setPasswords({
                password1: passwords.password1,
                password2: ""
            })
        }
    }

    const updateField = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPasswords({
            ...passwords,
            [e.target.id]: e.target.value.trim()
        });
    };

    return (
        <div className="container mt-5">
            <h1>Change Password</h1>
                <form onSubmit={(e) => handleSubmit(e)}>
                    <div className="mb-3">
                        <div className="input-group mb-3">
                            <input 
                                type="password"   
                                id='password1' 
                                value={passwords.password1} 
                                onChange={updateField} 
                                required 
                                className="form-control" 
                                placeholder="Enter your password" 
                                aria-label="Enter your password" 
                                aria-describedby="button-addon2"
                            />
                            <input 
                                type="password"   
                                id='password2' 
                                value={passwords.password2} 
                                onChange={updateField} 
                                required 
                                className="form-control" 
                                placeholder="Confirm your password" 
                                aria-label="Confirm your password" 
                                aria-describedby="button-addon2"
                            />
                            <button className="btn btn-outline-secondary" type="submit" id="button-addon2">Change Password</button>
                        </div>
                    </div>
                </form>
        </div>
    );
};

export default ChangePassword;


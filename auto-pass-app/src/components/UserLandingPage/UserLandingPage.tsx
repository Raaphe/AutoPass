import React, {FC, useEffect, useState} from 'react';
import styles from './UserLandingPage.module.scss';
import ClientAuthService from '../../ClientAuthService';
import { useNavigate } from 'react-router-dom';
import * as Api from '../../Service'; 

interface UserLandingPageProps {
}

const UserLandingPage: FC<UserLandingPageProps> = () => {
    const navigate = useNavigate();
    const [userData, setUserData] = useState<Api.User>();

    const handleLogout = () => {
        ClientAuthService.logout();
        navigate("/login");
    }

    useEffect(() => {
       
        // This is how we setup the access token inside the subsequent request's `Authorization Header` like so :
        // "Bearer <access_token>"

        const config = ClientAuthService.getApiConfig();
        const userAPI = new Api.UserControllerApi(config);

        const fetchUserData = async () => {
            await userAPI.getUser()
                .then((res) =>{
                    setUserData(res.data);
                })  
                .catch(() => {
                    navigate("/");
                })
        }
        fetchUserData();
    },[])


    return (
        <div className={styles.UserLandingPage}>
            UserLandingPage Component
            <button onClick={handleLogout} className='btn btn-dark'>Logout</button>
            <h2>{userData?.email}</h2>
        </div>

    );
};

export default UserLandingPage;

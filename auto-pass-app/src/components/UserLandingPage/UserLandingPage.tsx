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
        console.log(`user token when making call${ClientAuthService.getAccessTokenOrDefault()}`);
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
    },[navigate])


    return (
        <div className={styles.UserLandingPage}>
            UserLandingPage Component
            <button onClick={handleLogout} className='btn btn-dark'>
                Logout
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-box-arrow-left m-1" viewBox="0 0 16 16">
                    <path fill-rule="evenodd" d="M6 12.5a.5.5 0 0 0 .5.5h8a.5.5 0 0 0 .5-.5v-9a.5.5 0 0 0-.5-.5h-8a.5.5 0 0 0-.5.5v2a.5.5 0 0 1-1 0v-2A1.5 1.5 0 0 1 6.5 2h8A1.5 1.5 0 0 1 16 3.5v9a1.5 1.5 0 0 1-1.5 1.5h-8A1.5 1.5 0 0 1 5 12.5v-2a.5.5 0 0 1 1 0z"/>
                    <path fill-rule="evenodd" d="M.146 8.354a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L1.707 7.5H10.5a.5.5 0 0 1 0 1H1.707l2.147 2.146a.5.5 0 0 1-.708.708z"/>
                </svg>
            </button>
            <h2>{userData?.email}</h2>
        </div>

    );
};

export default UserLandingPage;

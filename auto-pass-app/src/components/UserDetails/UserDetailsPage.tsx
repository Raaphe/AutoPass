import React, { FC, useEffect, useState } from 'react';
import { RiUserSettingsLine, RiInformationLine, RiLogoutBoxLine } from 'react-icons/ri';
import { SiGoogle } from 'react-icons/si';
import styles from './UserDetailsPage.module.scss';
import ClientAuthService from '../../ClientAuthService';
import { useNavigate } from 'react-router-dom';
import SplitCard from './SplitCard';
import AvatarModal from './AvatarModal';
import { motion } from 'framer-motion';
import * as API from "../../Service/api"
interface UserDetailsPageProps { }

const UserDetailsPage: FC<UserDetailsPageProps> = () => {
    const navigate = useNavigate();
    const [googleLinked, setGoogleLinked] = useState(false);
    const [isAvatarModalOpen, setAvatarModalOpen] = useState(false);
    const [avatarUrl, setAvatarUrl] = useState("https://via.placeholder.com/150"); // Initial placeholder avatar URL
    const [isHovered, setIsHovered] = useState(false);
    const [userData, setUserData] = useState<API.User>();

    useEffect(() => {

        // This is how we setup the access token inside the subsequent request's `Authorization Header` like so :
        // "Bearer <access_token>"
        const config = ClientAuthService.getApiConfig();
        const userAPI = new API.UserControllerApi(config);

        const fetchUserData = async () => {
            await userAPI.getUser()
                .then((res) => {
                    setUserData(res.data);
                })
                .catch(() => {
                    navigate("/");
                })
        }
        fetchUserData();
    }, [navigate])


    // Render loading indicator while fetching data
    if (!userData) {
        return <div>Loading...</div>;
    }

    const handleGoogleLink = () => {
        // Placeholder for linking Google account
        console.log("Linking Google account...");
        setGoogleLinked(true); // Set state to indicate Google account linked
    };

    const handleLogout = () => {
        ClientAuthService.logout();
        navigate("/login");
    };

    const handleAvatarChange = (file: File) => {
        // Handle avatar change
        const imageUrl = URL.createObjectURL(file);
        setAvatarUrl(imageUrl);
        setAvatarModalOpen(false); // Close modal after selecting a file
    };

    const handleSavePersonalInfo = () => {
        // Handle saving personal information
        console.log("Saving personal information:");
        // You can add logic here to save the edited personal information
    };

    return (
        <div className="container-fluid" style={{ marginTop: '60px', paddingBottom: '50px' }}>
            <div className="row">
                <div className={`${styles.NavigationMenu} col-md-3`}>
                    <ul className="list-group list-group-flush">
                        <li className="list-group-item d-flex align-items-center justify-content-start">
                            <RiUserSettingsLine style={{ marginRight: '10px' }} />
                            <a href="#user-details" className="text-decoration-none" style={{ color: 'black' }}>User Details</a>
                        </li>
                        <li className="list-group-item d-flex align-items-center justify-content-start">
                            <RiInformationLine style={{ marginRight: '10px' }} />
                            <a href="#personal-info" className="text-decoration-none" style={{ color: 'black' }}>Personal Information</a>
                        </li>
                        <li className="list-group-item d-flex align-items-center justify-content-start">
                            <RiLogoutBoxLine style={{ marginRight: '10px' }} />
                            <a href="#logout" className="text-decoration-none" style={{ color: 'black' }}>Logout</a>
                        </li>
                    </ul>
                </div>
                <div className="col-md-9">
                    {/* User Details Card */}
                    <SplitCard id="user-details" title="User Details" description="Edit your personal information below">
                        <motion.div
                            className="avatar-container"
                            whileHover={{ scale: 0.9 }}
                            whileTap={{ scale: 0.8 }}
                            onClick={() => setAvatarModalOpen(true)}
                            onMouseEnter={() => setIsHovered(true)}
                            onMouseLeave={() => setIsHovered(false)}
                        >
                            <img className="avatar" src="https://via.placeholder.com/150" alt="User Avatar" />
                            {isHovered && <div className="hover-overlay"></div>}
                        </motion.div>
                        <div className="user-details">
                            <input
                                type="text"
                                placeholder="First Name"
                                name="firstName"
                                value={userData.firstName}
                                onChange={() => { }}
                                className="form-control"
                            />
                            <input
                                type="text"
                                placeholder="Last Name"
                                name="lastName"
                                value={userData.lastName}
                                onChange={() => { }}
                                className="form-control mt-2"
                            />
                            <button onClick={handleSavePersonalInfo} className="btn btn-success float-end m-2">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-floppy" viewBox="0 0 16 16">
                                    <path d="M11 2H9v3h2z" />
                                    <path d="M1.5 0h11.586a1.5 1.5 0 0 1 1.06.44l1.415 1.414A1.5 1.5 0 0 1 16 2.914V14.5a1.5 1.5 0 0 1-1.5 1.5h-13A1.5 1.5 0 0 1 0 14.5v-13A1.5 1.5 0 0 1 1.5 0M1 1.5v13a.5.5 0 0 0 .5.5H2v-4.5A1.5 1.5 0 0 1 3.5 9h9a1.5 1.5 0 0 1 1.5 1.5V15h.5a.5.5 0 0 0 .5-.5V2.914a.5.5 0 0 0-.146-.353l-1.415-1.415A.5.5 0 0 0 13.086 1H13v4.5A1.5 1.5 0 0 1 11.5 7h-7A1.5 1.5 0 0 1 3 5.5V1H1.5a.5.5 0 0 0-.5.5m3 4a.5.5 0 0 0 .5.5h7a.5.5 0 0 0 .5-.5V1H4zM3 15h10v-4.5a.5.5 0 0 0-.5-.5h-9a.5.5 0 0 0-.5.5z" />
                                </svg>
                            </button>
                        </div>
                    </SplitCard>

                    {/* Personal Information Card */}
                    <SplitCard id="personal-info" title="Personal Information" description="Provide your personal details below">
                        <input
                            type="text"
                            placeholder="Email"
                            name="Email"
                            value={userData.email}
                            onChange={() => { }}
                            className="form-control"
                        />
                        <input
                            type="text"
                            placeholder="Date of Birth"
                            name="dob"
                            value=""
                            onChange={() => { }}
                            className="form-control mt-2"
                        />
                        <button onClick={handleSavePersonalInfo} className="btn btn-success float-end m-2">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-floppy" viewBox="0 0 16 16">
                                <path d="M11 2H9v3h2z" />
                                <path d="M1.5 0h11.586a1.5 1.5 0 0 1 1.06.44l1.415 1.414A1.5 1.5 0 0 1 16 2.914V14.5a1.5 1.5 0 0 1-1.5 1.5h-13A1.5 1.5 0 0 1 0 14.5v-13A1.5 1.5 0 0 1 1.5 0M1 1.5v13a.5.5 0 0 0 .5.5H2v-4.5A1.5 1.5 0 0 1 3.5 9h9a1.5 1.5 0 0 1 1.5 1.5V15h.5a.5.5 0 0 0 .5-.5V2.914a.5.5 0 0 0-.146-.353l-1.415-1.415A.5.5 0 0 0 13.086 1H13v4.5A1.5 1.5 0 0 1 11.5 7h-7A1.5 1.5 0 0 1 3 5.5V1H1.5a.5.5 0 0 0-.5.5m3 4a.5.5 0 0 0 .5.5h7a.5.5 0 0 0 .5-.5V1H4zM3 15h10v-4.5a.5.5 0 0 0-.5-.5h-9a.5.5 0 0 0-.5.5z" />
                            </svg>
                        </button>
                    </SplitCard>

                    {/* Link Google Account Card */}
                    <SplitCard id="google-link" title="Link Google Account" description="Link your Google account to enable additional features">
                        <div className="text-center">
                            {!googleLinked ? (
                                <button onClick={handleGoogleLink} className="btn btn-primary">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-google m-1" viewBox="0 0 16 16">
                                        <path d="M15.545 6.558a9.4 9.4 0 0 1 .139 1.626c0 2.434-.87 4.492-2.384 5.885h.002C11.978 15.292 10.158 16 8 16A8 8 0 1 1 8 0a7.7 7.7 0 0 1 5.352 2.082l-2.284 2.284A4.35 4.35 0 0 0 8 3.166c-2.087 0-3.86 1.408-4.492 3.304a4.8 4.8 0 0 0 0 3.063h.003c.635 1.893 2.405 3.301 4.492 3.301 1.078 0 2.004-.276 2.722-.764h-.003a3.7 3.7 0 0 0 1.599-2.431H8v-3.08z" />
                                    </svg>
                                    Link Google Account</button>
                            ) : (
                                <p>Google account linked successfully!</p>
                            )}
                        </div>
                    </SplitCard>

                    {/* Logout Button Card */}
                    <SplitCard id="logout" title="Logout" description="Click the button below to log out of your account">
                        <div className='text-center'><button onClick={handleLogout} className='btn btn-sm btn-danger'>Logout</button></div>
                    </SplitCard>

                    {/* Avatar Modal */}
                    <AvatarModal
                        isOpen={isAvatarModalOpen}
                        onClose={() => setAvatarModalOpen(false)}
                        onFileChange={handleAvatarChange}
                    />
                </div>
            </div>
        </div>
    );
};

export default UserDetailsPage;

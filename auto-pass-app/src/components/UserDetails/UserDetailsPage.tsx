import React, { FC, useEffect, useState } from 'react';
import { RiUserSettingsLine, RiInformationLine, RiLogoutBoxLine } from 'react-icons/ri';
import { SiGoogle } from 'react-icons/si';
import styles from './UserDetailsPage.module.scss';
import ClientAuthService from '../../ClientAuthService';
import { useNavigate } from 'react-router-dom';
import SplitCard from './SplitCard';
import AvatarModal from './AvatarModal';
import { motion } from 'framer-motion';

interface UserDetailsPageProps {}

const UserDetailsPage: FC<UserDetailsPageProps> = () => {
    const navigate = useNavigate();
    const [googleLinked, setGoogleLinked] = useState(false);
    const [isAvatarModalOpen, setAvatarModalOpen] = useState(false);
    const [avatarUrl, setAvatarUrl] = useState("https://via.placeholder.com/150"); // Initial placeholder avatar URL
    const [isHovered, setIsHovered] = useState(false);
    const [userData, setUserData] = useState<any>(null); 

    // useEffect(() => {
    //     // Fetch user data when the component mounts
    //     fetchUserData();
    // }, []);

    // const fetchUserData = async () => {
    //     try {
    //         // Make an HTTP GET request to fetch user data from your backend
    //         const response = await fetch('AutoPass/src/main/java/backend/autopass/model/entities/User.java');
    //         if (response.ok) {
    //             // If the response is successful, parse the JSON response body
    //             const data = await response.json();
    //             setUserData(data); // Update the state with the fetched user data
    //         } else {
    //             console.error('Failed to fetch user data');
    //         }
    //     } catch (error) {
    //         console.error('Error fetching user data:', error);
    //     }
    // };

    // // Render loading indicator while fetching data
    // if (!userData) {
    //     return <div>Loading...</div>;
    // }

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
                            whileTap={{ scale: 0.8}}
                            onClick={() => setAvatarModalOpen(true)}
                            onMouseEnter={() => setIsHovered(true)}
                            onMouseLeave={() => setIsHovered(false)}
                        >
                            <img className="avatar" src="" alt="User Avatar" />
                            {isHovered && <div className="hover-overlay"></div>}
                        </motion.div>
                        <div className="user-details">
                            <input
                                type="text"
                                placeholder="First Name"
                                name="firstName"
                                // value={userData.firstName}
                                onChange={() => {}} 
                                className="form-control"
                            />
                            <input
                                type="text"
                                placeholder="Last Name"
                                name="lastName"
                                // value={userData.lastName}
                                onChange={() => {}} 
                                className="form-control mt-2"
                            />
                            <button onClick={handleSavePersonalInfo} className="btn btn-success float-end">Save</button>
                        </div>
                    </SplitCard>

                    {/* Personal Information Card */}
                    <SplitCard id="personal-info" title="Personal Information" description="Provide your personal details below">
                        <input
                            type="text"
                            placeholder="Postal Code"
                            name="postalCode"
                            value=""
                            onChange={() => {}}
                            className="form-control"
                        />
                        <input
                            type="text"
                            placeholder="Date of Birth"
                            name="dob"
                            value=""
                            onChange={() => {}}
                            className="form-control mt-2"
                        />
                        <button onClick={handleSavePersonalInfo} className="btn btn-success float-end">Save</button>
                    </SplitCard>

                    {/* Link Google Account Card */}
                    <SplitCard id="google-link" title="Link Google Account" description="Link your Google account to enable additional features">
                        <div className="text-center">
                            {!googleLinked ? (
                                <button onClick={handleGoogleLink} className="btn btn-primary">Link Google Account</button>
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

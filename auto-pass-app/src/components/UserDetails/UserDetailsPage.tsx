import AttachEmailIcon from '@mui/icons-material/AttachEmail';
import React, { FC, useEffect, useState } from 'react';
import { RiUserSettingsLine, RiInformationLine, RiLogoutBoxLine } from 'react-icons/ri';
import DeleteIcon from '@mui/icons-material/Delete';
import styles from './UserDetailsPage.module.scss';
import ClientAuthService from '../../ClientAuthService';
import { useNavigate } from 'react-router-dom';
import SplitCard from './SplitCard';
import AvatarModal from './AvatarModal';
import { motion } from 'framer-motion';
import WalletIcon from '@mui/icons-material/Wallet';
import * as API from "../../Service";
import TextField from '@mui/material/TextField/TextField';
import GoogleWalletButton from "../../assets/enCA_add_to_google_wallet_add-wallet-badge.png"
import { Button, ButtonGroup, Fab, Skeleton, Stack } from '@mui/material';
import { red } from '@mui/material/colors';
import "./UserDetailsPage.module.scss"

interface UserDetailsPageProps { }

/**
* UserDetailsPage - 2024-04-02
* Raaphe, Lamb, Ikacef(L-96)
*
* AutoPass
*/
const UserDetailsPage: FC<UserDetailsPageProps> = () => {
    const navigate = useNavigate();
    const [googleLinked, setGoogleLinked] = useState(false);
    const [isAvatarModalOpen, setAvatarModalOpen] = useState(false);
    const [isHovered, setIsHovered] = useState(false);
    const [userData, setUserData] = useState<API.User>();
    const [isEmailSent, setIsEmailSent] = useState(false);
    const [addGoogleWalletURL, setGoogleWalletSaveURL] = useState<API.GoogleWalletPassURLViewModel>();
    const [canResetPassword, setCanResetPassword] = useState(true);
    const [isLoading, setIsLoading] = useState(true);
    const [basicUserInfo, setBasicUserInfo] = useState<API.BasicUserInfoDTO>();

    // This is how we set up the access token inside the subsequent request's `Authorization Header` like so :
    // "Bearer <access_token>"
    const config = ClientAuthService.getApiConfig();
    const userAPI = new API.UserControllerApi(config);
    const authAPI = new API.AuthenticationApi(config);
    const googleWalletApi = new API.GoogleWalletControllerApi(config);

    useEffect(() => {

        setIsLoading(true);
        const fetchUserData = async () => {
            await userAPI.getUser()
                .then((res) => {
                    setUserData(res.data);
                    if (res.data.role === "ADMIN" || res.data.role === "GOOGLE_USER") {
                        setGoogleLinked(true);
                    } else {
                        setGoogleLinked(false);
                    }

                    setBasicUserInfo({id:ClientAuthService.getUserId(),email: res.data.email, firstName:res.data.firstName, lastName: res.data.lastName})
                })
                .catch(() => {
                    navigate("/");
                })
        }
        const getSavePassURL = async () => {
            await googleWalletApi.getSavePassURL(ClientAuthService.getUserId())
                .then(res => {
                    if (res.status !== 200 || res.data === null || res.data.passUrl === "") {
                        return;
                    }

                    setGoogleWalletSaveURL(res.data);
                });
        }

        fetchUserData();
        getSavePassURL();

        if (userData?.role === "ADMIN" && userData.googleAccessToken !== null) {
            setCanResetPassword(false);
        } else if (userData?.role === "GOOGLE_USER") {
            setCanResetPassword(false);
        } else { setCanResetPassword(true); }

        setIsLoading(false);
    }, [navigate, isLoading])


    const handleLoginWithGoogle = async (): Promise<void> => {
        let ip: string = await authAPI.getAppIp()
            .then(res => {
                return res.data ?? "localhost"
            })
            .catch((e) => {
                alert(e);
                return "";
            });

        let url = "http://" + ip + ".nip.io:9090/oauth2/authorization/google";
        window.location.href = url;
    };

    // Render loading indicator while fetching data
    if (!userData) {
        return <div>Loading...</div>;
    }

    function isGmailAddress(email: string): boolean {
        const gmailRegex = /^[^@\s]+@gmail\.com$/;
        return gmailRegex.test(email);
    }

    const handleAccountDelete = async () => {
        await userAPI.markUserAsDeleted();
        handleLogout();
    }

    const handleLogout = () => {
        ClientAuthService.logout();
        navigate("/login");
    };

    const onModalClose = (): void => {
        setAvatarModalOpen(false);
    }

    const handleSavePersonalInfo = () => {
        userAPI.saveBasicUserInfo(basicUserInfo ?? {})
            .then((res) => {
                if (res.status !== 200) {
                    alert("error saving info.");
                    return;
                }

                setUserData({...userData, email: res.data.email, firstName: res.data.firstName, lastName: res.data.lastName})
            })
    };

    const handleImageSave = async (setImageRequest: API.SetUserImageRequest) => {
        const formData = new FormData();
        formData.append('file', setImageRequest.file);

        let ip: string = await authAPI.getAppIp()
            .then(res => {
                return res.data ?? "localhost"
            })
            .catch((e) => {
                alert(e);
                return "";
            });

        fetch(`http://${ip}:9090/user/upload-profile-image`, {
            method: 'POST',
            body: formData,
            headers: [["Authorization", `Bearer ${ClientAuthService.getAccessTokenOrDefault()}`]]
        })
            .then(response => {
                var data = response.json()

                setAvatarModalOpen(false);
                window.location.reload();
            })
            .catch(error => console.error('Error:', error));
    }

    return (
        <div className="container-fluid" style={{ marginTop: '60px', paddingBottom: '50px' }}>
            <div className="row">
                <div className={`${styles.NavigationMenu} col-md-3`}>
                    <ul className="list-group list-group-flush">
                        <li className="list-group-item d-flex align-items-center justify-content-start">
                            <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" className="bi bi-arrow-left m-1" viewBox="0 0 16 16">
                                <path fillRule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8" />
                            </svg>
                            <a href="/home" className="text-decoration-none mx-1" style={{ color: 'black' }}>Back To Dashboard</a>
                        </li>
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
                            <a onClick={handleLogout} className="text-decoration-none" style={{ color: 'black' }}>Logout</a>
                        </li>
                    </ul>
                </div>
                <div className="col-md-9">

                    {isLoading ?

                        <Stack spacing={1}>
                            {/* For variant="text", adjust the height via font-size */}
                            <Skeleton variant="text" sx={{ fontSize: '1rem' }} />

                            {/* For other variants, adjust the size with `width` and `height` */}
                            <Skeleton variant="circular" width={40} height={40} />
                            <Skeleton variant="rectangular" width={210} height={60} />
                            <Skeleton variant="rounded" width={210} height={60} />
                        </Stack>

                        :

                        <>

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
                                    <img width={100} height={100} className="mx-4 mt-3 mb-2 " src={userData.profileImageUrl} alt="User Avatar" />
                                    {isHovered && <div className="hover-overlay"></div>}
                                </motion.div>
                                <div className="user-details">
                                    <TextField
                                        id="outlined-helperText"
                                        label="First Name"
                                        defaultValue="John"
                                        onChange={(e) => {setBasicUserInfo({...basicUserInfo, firstName:e.currentTarget.value})}}
                                        className='m-2 mt-4 col-12'
                                        value={basicUserInfo?.firstName}
                                    />
                                    <TextField
                                        className='m-2 col-12'
                                        id="outlined-helperText"
                                        label="Last Name"
                                        onChange={(e) => {setBasicUserInfo({...basicUserInfo, lastName:e.currentTarget.value})}}
                                        defaultValue="Doe"
                                        value={basicUserInfo?.lastName}
                                    />
                                    <TextField
                                        className='m-2 col-12'
                                        id="outlined-helperText"
                                        type='email'
                                        label="Email"
                                        onChange={(e) => {setBasicUserInfo({...basicUserInfo, email:e.currentTarget.value})}}
                                        disabled={googleLinked}
                                        value={basicUserInfo?.email}
                                    />
                                    <button 
                                        onClick={handleSavePersonalInfo} 
                                        disabled={basicUserInfo?.email === "" || basicUserInfo?.firstName === "" || basicUserInfo?.lastName == ""} 
                                        className="btn btn-success float-end m-2"
                                    >
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-floppy" viewBox="0 0 16 16">
                                            <path d="M11 2H9v3h2z" />
                                            <path d="M1.5 0h11.586a1.5 1.5 0 0 1 1.06.44l1.415 1.414A1.5 1.5 0 0 1 16 2.914V14.5a1.5 1.5 0 0 1-1.5 1.5h-13A1.5 1.5 0 0 1 0 14.5v-13A1.5 1.5 0 0 1 1.5 0M1 1.5v13a.5.5 0 0 0 .5.5H2v-4.5A1.5 1.5 0 0 1 3.5 9h9a1.5 1.5 0 0 1 1.5 1.5V15h.5a.5.5 0 0 0 .5-.5V2.914a.5.5 0 0 0-.146-.353l-1.415-1.415A.5.5 0 0 0 13.086 1H13v4.5A1.5 1.5 0 0 1 11.5 7h-7A1.5 1.5 0 0 1 3 5.5V1H1.5a.5.5 0 0 0-.5.5m3 4a.5.5 0 0 0 .5.5h7a.5.5 0 0 0 .5-.5V1H4zM3 15h10v-4.5a.5.5 0 0 0-.5-.5h-9a.5.5 0 0 0-.5.5z" />
                                        </svg>
                                    </button>
                                </div>
                            </SplitCard>

                            {/* Link Google Account Card */}
                            <SplitCard id="google-link" title="Link Google Account" description="Link your Google account to enable additional features">
                                <div className="text-center">
                                    {!googleLinked ? (

                                        isGmailAddress(userData.email ?? "") ? 
                                            <p>Please change your email address to a gmail email...</p>    
                                        :                                        
                                            <Fab variant="extended" color='primary' onClick={() => {handleLoginWithGoogle()}}>
                                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-google m-1" viewBox="0 0 16 16">
                                                    <path d="M15.545 6.558a9.4 9.4 0 0 1 .139 1.626c0 2.434-.87 4.492-2.384 5.885h.002C11.978 15.292 10.158 16 8 16A8 8 0 1 1 8 0a7.7 7.7 0 0 1 5.352 2.082l-2.284 2.284A4.35 4.35 0 0 0 8 3.166c-2.087 0-3.86 1.408-4.492 3.304a4.8 4.8 0 0 0 0 3.063h.003c.635 1.893 2.405 3.301 4.492 3.301 1.078 0 2.004-.276 2.722-.764h-.003a3.7 3.7 0 0 0 1.599-2.431H8v-3.08z" />
                                                </svg>
                                                Link Google Account
                                            </Fab>
                                    ) : (
                                        <p>Google account linked successfully!</p>
                                    )}
                                </div>
                            </SplitCard>

                            {/* Google Wallet Card  */}
                            {(userData.role === "GOOGLE_USER" || userData.googleAccessToken !== null) &&
                                <SplitCard id="Google-Wallet" title="Google Wallet Pass" description="Add Google Wallet Pass with a capable android device.">
                                    <div className="text-center">
                                        {!googleLinked ? (
                                            <p>Link Google account to add an AutoPass to your Google wallet <WalletIcon className='mx-2' /> </p>
                                        ) :
                                            userData.isGoogleWalletPassAdded ?
                                                // be able to expire it or view it
                                                <ButtonGroup variant='outlined' aria-label="Medium-sized button group">
                                                    <Button color='error' disabled={addGoogleWalletURL?.isExpired} onClick={async () => { await googleWalletApi.expirePass(userData?.email ?? ""); window.location.reload(); setIsLoading(true) }}>
                                                        Archive Pass
                                                    </Button>
                                                    <Button onClick={async () => { await googleWalletApi.activatePass(userData.email ?? "") }} href={addGoogleWalletURL?.passUrl}>
                                                        View or Add
                                                    </Button>
                                                </ButtonGroup>
                                                :
                                                // be able to add it (SCENARIO 1)
                                                <a href={addGoogleWalletURL?.passUrl} target="_blank">
                                                    <img src={GoogleWalletButton} style={{ cursor: "pointer" }} alt="" />
                                                </a>
                                        }
                                    </div>
                                </SplitCard>
                            }


                            {canResetPassword &&
                                <SplitCard id="password-reset" title="Reset Password" description="Forgot your Password? Reset it.">
                                    <div className="text-center">
                                        {!isEmailSent ? (
                                            <Fab variant="extended" onClick={() => { authAPI.forgotPassword(userData.email ?? ""); setIsEmailSent(true) }}>
                                                Reset Password
                                            </Fab>
                                        ) : (
                                            <p className='mt-4'><AttachEmailIcon />  Email Sent ! </p>
                                        )}
                                    </div>
                                </SplitCard>
                            }

                            {/* Logout Button Card */}
                            <SplitCard id="account-removal" title="Delete Account" description="Permenently delete your account?">
                                <div className='text-center'>
                                    <Fab variant="extended" onClick={handleAccountDelete}>
                                        <DeleteIcon sx={{ mr: 1, color: red }} />
                                        Delete Account
                                    </Fab>
                                </div>
                            </SplitCard>

                            {/* Avatar Modal */}
                            <AvatarModal
                                isOpen={isAvatarModalOpen}
                                onClose={onModalClose}
                                onFileChange={handleImageSave}
                                imageUrl={userData.profileImageUrl !== "" || userData.profileImageUrl !== null ? userData.profileImageUrl ?? "https://via.placeholder.com/150" : "https://via.placeholder.com/150"}
                            />

                        </>

                    }


                </div>
            </div>
        </div>
    );
};

export default UserDetailsPage;

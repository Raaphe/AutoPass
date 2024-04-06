import Chip from '@mui/material/Chip';
import React, { FC } from 'react';
import CircularProgress from '@mui/material/CircularProgress';
import Box from '@mui/material/Box';

interface LoadingProps {
    isEmailSent: any;
    isEmailReceived: any;
}

/**
* Loading - 2024-04-02
* Raaphe
*
* AutoPass
*/
const Loading: FC<LoadingProps> = ({ isEmailReceived, isEmailSent }) => {

    if (isEmailSent && isEmailReceived === null) {
        return (
            <Box sx={{ display: 'flex' }}>
                <CircularProgress className='text-center' />
            </Box>
        );
    } else if (isEmailSent && isEmailReceived === false) {
        return (
            <Chip className='text-center' label="Something went wrong while sending your reset password email ❗" variant="outlined" />
        );
    } else if (isEmailSent && isEmailReceived === true) {
        return (
            <Chip className='text-center' label="Email Sent ✅" variant="outlined" />
        );
    } else {
        return null; 
    }
}

export default Loading;

import { Button } from '@mui/material';
import SaveIcon from '@mui/icons-material/Save';
import React, { FC, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { SetUserImageRequest } from '../../Service';

interface AvatarModalProps {
    isOpen: boolean; 
    onClose: () => void; 
    onFileChange: (setImageRequest: SetUserImageRequest) => void;
    imageUrl: string;
}

const AvatarModal: FC<AvatarModalProps> = ({ isOpen, onClose, onFileChange, imageUrl }) => {

    const [isFileValid, setIsFileValid] = useState(false);
    const [fileState, setFileState] = useState<File>();
    const navigate = useNavigate();

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (file) {
            setIsFileValid(true);
            setFileState(file);
        }
    };

    async function handleSaveFile(): Promise<void> {
        
        if (fileState) {
            try {
                var res = onFileChange({file:fileState});
            } catch (e) {
                console.log("Error saving image.");
            }
        }
        onClose();
        navigate("/home");
    }
    

    return (
        <div className={`modal ${isOpen ? 'show' : ''}`} style={{ display: isOpen ? 'block' : 'none' }}>
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title">Upload New Avatar</h5>
                        <button type="button" className="btn-close" onClick={() => onClose()}></button>
                    </div>
                    <div className="modal-body">
                        <img
                            width={65}
                            height={65}
                            className="avatar"
                            src={imageUrl}
                            alt="User Avatar"
                        />
                        <input
                            type="file"
                            accept="image/*"
                            onChange={handleFileChange}
                            className="form-control mt-2"
                        />
                
                        <Button 
                           className='m-2 float-end'
                           variant='outlined'   
                           disabled={isFileValid?false:true}
                           onClick={handleSaveFile}
                        >
                            <SaveIcon/>
                        </Button>
                     

                    </div>
                </div>
            </div>
        </div>
    );
};

export default AvatarModal;

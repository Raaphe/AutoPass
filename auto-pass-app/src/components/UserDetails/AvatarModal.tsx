import React, { FC, useState } from 'react';

interface AvatarModalProps {
    isOpen: boolean; 
    onClose: () => void; 
    onFileChange: (file: File) => void; 
}

const AvatarModal: FC<AvatarModalProps> = ({ isOpen, onClose, onFileChange }) => {
    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (file) {
            onFileChange(file);
            onClose(); 
        }
    };

    return (
        <div className={`modal ${isOpen ? 'show' : ''}`} style={{ display: isOpen ? 'block' : 'none' }}>
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title">Upload New Avatar</h5>
                        <button type="button" className="btn-close" onClick={onClose}></button>
                    </div>
                    <div className="modal-body">
                        <img
                            className="avatar"
                            src="https://via.placeholder.com/150"
                            alt="User Avatar"
                        />
                        <input
                            type="file"
                            accept="image/*"
                            onChange={handleFileChange}
                            className="form-control mt-2"
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AvatarModal;

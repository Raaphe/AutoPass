import React, { FC, useState } from 'react';

interface AvatarModalProps {
    isOpen: boolean; // Indicates whether the modal is open or closed
    onClose: () => void; // Function to close the modal
    onFileChange: (file: File) => void; // Function to handle file change when user uploads a new avatar image
}

const AvatarModal: FC<AvatarModalProps> = ({ isOpen, onClose, onFileChange }) => {
    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0]; // Get the first selected file
        if (file) {
            onFileChange(file); // Call the onFileChange callback with the selected file
            onClose(); // Close the modal after selecting a file
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

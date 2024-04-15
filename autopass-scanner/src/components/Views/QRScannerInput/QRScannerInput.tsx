import React, { FC, useEffect, useRef, useState } from 'react';
import "./QrScannerInput.scss"
interface QRScannerInputProps {
    onScan: any
}

const QRScannerInput: FC<QRScannerInputProps> = ({ onScan }) => {
    const inputRef = useRef<HTMLInputElement>(null);
    const [scannerData, setScannerData] = useState("");

    useEffect(() => {
        const focusInput = () => {
            inputRef.current && inputRef.current.focus();
        };

        focusInput();
        window.addEventListener('focus', focusInput);

        return () => {
            window.removeEventListener('focus', focusInput);
        };
    }, []);

    const handleInputChange = (event: { target: { value: string; }; }) => {
        setScannerData(event.target.value)
    };

    const handleKeyDown = (ev: any) => {
        if (ev.key === 'Enter') {
            ev.preventDefault();
            console.log('Scanned Data:', scannerData);
            setScannerData("");
            onScan(scannerData);
            ev.target.value = ''; 
        }
    };

    return (
        <input
            ref={inputRef}
            type="text"
            onKeyDown={handleKeyDown}
            onChange={handleInputChange}
            id='qrCode'
            style={{ opacity: 0, position: 'absolute', left: '-10000px', top: '-10000px' }}
            autoFocus
        />
    );
};

export default QRScannerInput;

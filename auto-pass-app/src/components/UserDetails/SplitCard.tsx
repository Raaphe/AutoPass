import React, { FC, ReactNode } from 'react';

interface SplitCardProps {
    id: string;
    title: string;
    description: string;
    children: ReactNode;
}

const SplitCard: FC<SplitCardProps> = ({ id, title, description, children }) => {
    return (
        <div id={id} className="card mb-3">
            <div className="card-body p-0">
                <div className="row m-0">
                    <div className="col-md-6 p-0" style={{ backgroundColor: '#50B8E7', color: 'white', borderTopLeftRadius: '10px', borderBottomLeftRadius: '10px', borderRight: '4px solid lightblue' }}>
                        <div className="p-3">
                            <h5 className="card-title">{title}</h5>
                            <p className="card-text">{description}</p>
                        </div>
                    </div>
                    <div className="col-md-6 p-0" style={{ borderTopRightRadius: '10px', borderBottomRightRadius: '10px'}}>
                        <div className="p-3">
                            {children}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SplitCard;

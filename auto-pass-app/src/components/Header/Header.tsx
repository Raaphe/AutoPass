import React, { FC } from 'react';
import styles from './Header.module.scss'; // Make sure to import your styles

interface HeaderProps {}

const Header: FC<HeaderProps> = () => {
  return (
    <div className={`${styles.Header} shadow-sm p-2`}>
      <div className="container-fluid">
        <div className="row align-items-center">
          <div className="col-md-6 text-md-end">
            <img src="https://via.placeholder.com/60" alt="" className="me-2" width="60" height="60" />
            <h2 className="mb-0 d-inline fs-4">AutoPass</h2>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Header;


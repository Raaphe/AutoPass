import React, {lazy, Suspense} from 'react';

const LazyFooter = lazy(() => import('./Footer'));

const Header = (props: JSX.IntrinsicAttributes & { children?: React.ReactNode; }) => (
    <Suspense fallback={null}>
        <LazyFooter {...props} />
    </Suspense>
);

export default Header;

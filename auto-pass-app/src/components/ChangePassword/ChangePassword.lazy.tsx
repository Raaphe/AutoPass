import React, {lazy, Suspense} from 'react';

const LazyChangePassword = lazy(() => import('./ChangePassword'));

const ChangePassword = (props: JSX.IntrinsicAttributes & { children?: React.ReactNode; }) => (
    <Suspense fallback={null}>
        <LazyChangePassword {...props} />
    </Suspense>
);

export default ChangePassword;

import React, {lazy, Suspense} from 'react';

const LazyForgotPassword = lazy(() => import('./ForgotPassword'));

const ForgotPassword = (props: JSX.IntrinsicAttributes & { children?: React.ReactNode; }) => (
    <Suspense fallback={null}>
        <LazyForgotPassword {...props} />
    </Suspense>
);

export default ForgotPassword;

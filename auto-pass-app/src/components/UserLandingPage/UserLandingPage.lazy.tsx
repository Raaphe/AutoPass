import React, {lazy, Suspense} from 'react';

const LazyUserLandingPage = lazy(() => import('./UserLandingPage'));

const UserLandingPage = (props: JSX.IntrinsicAttributes & { children?: React.ReactNode; }) => (
    <Suspense fallback={null}>
        <LazyUserLandingPage {...props} />
    </Suspense>
);

export default UserLandingPage;

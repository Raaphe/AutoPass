import React, {lazy, Suspense} from 'react';

const LazyLogin = lazy(() => import('./SignUp'));

const Login = (props: JSX.IntrinsicAttributes & { children?: React.ReactNode; }) => (
    <Suspense fallback={null}>
        <LazyLogin {...props} />
    </Suspense>
);

export default Login;

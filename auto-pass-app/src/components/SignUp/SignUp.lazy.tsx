import React, {lazy, Suspense} from 'react';
const LazySignUp = lazy(() => import('./SignUp'));

const SignUp = (props: JSX.IntrinsicAttributes & { children?: React.ReactNode; }) => (
    <Suspense fallback={null}>
        <LazySignUp {...props} />
    </Suspense>
);

export default SignUp;


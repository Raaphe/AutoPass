import logo from './logo.svg';
import './App.scss';
// import React, {useState} from 'react';
import React from 'react';
// import { AuthenticationApi, SignInDTO } from "./Service"


const App = () => {

    // const authApi = new AuthenticationApi();
    // const [user, setUser] = useState(null);


    // const login = ():Boolean => {


    //     // var 
    //     authApi.

    //     return false;
    // }

    return (


        <div className="App">

            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>
                    Edit <code>src/App.js</code> and save to reload.
                </p>
                <a
                    className="App-link"
                    href="https://reactjs.org"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Learn React
                </a>
                {/* <button onClick={() => login()}>

                </button> */}
            </header>
        </div>
    );
}

export default App;

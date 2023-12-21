import logo from './logo.svg';
import UserService from './webService/userService';
import './App.css';
import React, {useEffect} from 'react';


function App() {

    let userService = new UserService();

    useEffect(() => {
        userService.getUser(1)
            .then(user => {
                console.log(user);
            })
            .catch(e => {
                console.error(e);
            });
    }, []);


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
            </header>
        </div>
    );
}

export default App;

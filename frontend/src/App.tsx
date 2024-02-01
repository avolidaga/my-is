import React from 'react';
import logo from './logo.svg';
import './App.css';
import axios from "axios";



const App: React.FC = () => {
    const sendUserData = () => {
        axios.post('http://localhost:8080/api/auth/register', {
            username: "string1",
            password: "string1"
        })

    }
  return (
    <div className="App">

        <div>
            <button onClick={sendUserData}>
                send
            </button>
        </div>

    </div>
  );
}

export default App;

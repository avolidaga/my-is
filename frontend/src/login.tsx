import React from 'react';
import logo from './logo.svg';
import './App.css';
import axios from "axios";
import {saveTokenInLocalStorage} from "./utils/saveDataInLocalstorage";



const LoginPage: React.FC = () => {
    const sendUserData = () => {
        axios.post('http://localhost:8080/api/auth/login', {
            username: "string",
            password: "string"
        }).then((response => saveTokenInLocalStorage(response.data.credentials)))

        console.log(localStorage.getItem("accessToken"))

    }

    const tryPost = () => {
        axios.post('http://localhost:8080/api/test/post', {}, {headers: {
            'Authorization': `Basic ${localStorage.getItem("accessToken")}`,
        }}).then((response => console.log(response)))

        localStorage.removeItem("accessToken");

    }
    return (
        <div className="App">

            <div>
                <button onClick={sendUserData}>
                    send
                </button>
                <button onClick={tryPost}>
                    post
                </button>
            </div>

        </div>
    );
}

export default LoginPage;

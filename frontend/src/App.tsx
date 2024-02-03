import React from 'react';
import logo from './logo.svg';
import './App.css';
import axios from "axios";
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import LoginPage from "./Authentication/login";
import Registration from "./Authentication/registration";
import UserDataForm from "./UserDataForm";
import SpacesuitForm from "./SpacesuitForm";

const App: React.FC = () => {

  return (
      <BrowserRouter>
          <Routes>
              <Route
                  path="*"
                  element={<Navigate to="/" replace={true} />}
              />
              <Route path="/" element={<LoginPage/>}/>
              <Route path="/register" element={<Registration/>}/>
              <Route path="/form" element={ <UserDataForm/>}/>
              <Route path="/spacesuit-form" element={ <SpacesuitForm/>}/>

          </Routes>
      </BrowserRouter>
  );
}

export default App;

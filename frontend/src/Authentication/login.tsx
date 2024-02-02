import React from 'react';
import '../App.css';
import axios from "axios";
import {saveTokenInLocalStorage} from "../utils/saveDataInLocalstorage";
import {Controller, SubmitHandler, useForm} from "react-hook-form";
import {Box, Button, TextField, Typography} from "@mui/material";
import {IFormInput} from "./registration";
import axiosApiInstance from "../utils/tokenHelper";
import {useNavigate} from "react-router-dom";



const LoginPage: React.FC = () => {
    const {control, handleSubmit} = useForm<IFormInput>();
    const navigate = useNavigate();

    //тестим post с access токеном
    const tryPost = () => {
        axiosApiInstance.post('/test/post').then((response => console.log(response)))

        // localStorage.removeItem("accessToken");

    }
    const onSubmit: SubmitHandler<IFormInput> = data => {

        axiosApiInstance.post('/auth/login', {
            username: data.username,
            password: data.password
        }).then((response => saveTokenInLocalStorage(response.data.credentials)))
        tryPost();
        localStorage.removeItem("accessToken");

    }

    return (
        <Box
            sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                height: "100vh", // Высота 100% видимой области страницы
            }}
        >
            <form onSubmit={handleSubmit(onSubmit)}>
                <Box
                    sx={{
                        width: "300px",
                        height: "500px",
                        flexDirection: "column",
                        justifyContent: "center",
                        display: "flex",
                        position: "relative",
                    }}
                >
                    <Controller
                        name="username"
                        control={control}
                        defaultValue=""
                        render={({ field }) => <TextField variant="standard" required fullWidth type="text" label="username" {...field}/>}
                    />
                    <Controller
                        name="password"
                        control={control}
                        defaultValue=""
                        render={({ field }) => <TextField variant="standard" required fullWidth type="password" label="password" {...field}/>}
                    />
                    <Button
                        style={{
                            margin: "15px 0",
                            height: "40px",
                            fontSize: "18px",
                            color: "#FFFFFF"
                        }}
                        size="large"
                        variant="contained"
                        type="submit"

                    >
                        Войти
                    </Button>
                    {(localStorage.getItem("accessToken") != "") ?
                        <Button onClick={() => navigate("/form")}>
                            Перейти к анкете
                        </Button> : <Button></Button>}

                    <Box sx={{ position: "relative",   width:"60%", height:"7%", margin: "10px auto",
                        display: "block",  alignItems: "center",}} >
                        <Typography sx={{ color: "#48A1D3", textAlign: "center", minWidth: "60%"}}>Нет учетной записи?</Typography>

                        <Button sx={{left: "3%"}}
                            onClick={() => navigate("/register")}

                        >
                            Зарегистрироваться.</Button>

                    </Box>
                </Box>



            </form>


        </Box>
    )
}

export default LoginPage;

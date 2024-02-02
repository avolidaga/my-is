import {Box, Button, styled, TextField, Typography} from "@mui/material";
import React from "react";
import { SubmitHandler, useForm, Controller } from 'react-hook-form';
import useNavigate  from 'react-router-dom';
import axiosApiInstance from "../utils/tokenHelper";

export interface IFormInput {
    username: string,
    password: string,
}

const Registration: React.FC = () => {

    const onSubmit: SubmitHandler<IFormInput> = data => {

        axiosApiInstance.post('/auth/register', {
            username: data.username,
            password: data.password
        })

    }
    const {control, handleSubmit} = useForm<IFormInput>();


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
                            height: "40px", // Уменьшаем высоту кнопки
                            fontSize: "18px",
                            color: "#FFFFFF"
                        }}
                        size="large"
                        variant="contained"
                        type="submit"

                    >
                        Зарегистрироваться
                    </Button>

                    <Box sx={{ position: "relative",   width:"60%", height:"7%", margin: "10px auto",
                        display: "block",  alignItems: "center",}} >
                        <Typography sx={{ color: "#48A1D3", textAlign: "center", minWidth: "60%"}}>Уже есть учетная запись?</Typography>

                        <Button sx={{left: "3%"}}

                        >
                            Войти.</Button>

                    </Box>
                </Box>



            </form>


        </Box>
    )
}

export default Registration;

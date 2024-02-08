import {Box, Button, styled, TextField, Typography} from "@mui/material";
import React from "react";
import { SubmitHandler, useForm, Controller } from 'react-hook-form';
import {useNavigate}  from 'react-router-dom';
import axiosApiInstance from "../../utils/tokenHelper";
import bg from "../../images/bg/reg.jpg";

export interface IFormInput {
    username: string,
    password: string,
}

const Registration: React.FC = () => {

    const onSubmit: SubmitHandler<IFormInput> = data => {
        //TODO fix interceptors
        localStorage.removeItem("accessToken");

        axiosApiInstance.post('/auth/register', {
            username: data.username,
            password: data.password
        })

    }
    const navigate = useNavigate();

    const {control, handleSubmit} = useForm<IFormInput>();


    return (
        <Box
            sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                height: "100vh",
                backgroundImage: `url(${bg})`, backgroundRepeat: `no-repeat`, backgroundSize: "cover",

            }}
        >
            <Box
                sx={{
                    width: "400px",
                    height: "420px",
                    border: `1px solid #BB7B85`,
                    borderRadius: "20px",
                    alignSelf: "center",
                    zIndex: "2",
                    display: "flex",
                    flexDirection: "column",
                    position: "relative",
                    backgroundColor: "#fbeffa",
                    justifyContent: "center",
                    alignItems: "center",
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
                        <Typography fontSize="30px" sx={{ color: "#b358cb", textAlign: "center", minWidth: "60%", marginBottom: "15px"}}>Registration</Typography>

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
                            margin: "30px 0",
                            height: "40px",
                            fontSize: "18px",
                            color: "#e285ee",
                            borderRadius: "15px",
                            width: "240px",
                            alignSelf: "center",
                            border: `1px solid #e285ee`,

                        }}
                        size="large"
                        variant="outlined"
                        type="submit"

                    >
                        Зарегистрироваться
                    </Button>

                    <Box sx={{ position: "relative",   width:"80%", height:"7%", margin: "10px auto",
                        display: "inline-grid",  alignItems: "center",                             justifyContent: "center",
                    }} >
                        <Typography sx={{ color: "#e285ee", textAlign: "center", minWidth: "60%"}}>Уже есть учетная запись?</Typography>

                        <Button sx={{textAlign: "center",  alignSelf: "center", color: "#e285ee", }} onClick={() => navigate("/")}
                        >
                            Войти.</Button>

                    </Box>

                </Box>



            </form>
            </Box>
        </Box>
    )
}

export default Registration;

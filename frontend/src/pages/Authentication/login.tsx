import React from 'react';
import '../../App.css';
import axios from "axios";
import {saveTokenInLocalStorage} from "../../utils/saveDataInLocalstorage";
import {Controller, SubmitHandler, useForm} from "react-hook-form";
import {Box, Button, TextField, Typography} from "@mui/material";
import {IFormInput} from "./registration";
import axiosApiInstance from "../../utils/tokenHelper";
import {useNavigate} from "react-router-dom";
import bg from '../../images/bg/login.jpg';
import {toast, ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';





const LoginPage: React.FC = () => {
    const {control, handleSubmit} = useForm<IFormInput>();
    const navigate = useNavigate();

    //тестим post с access токеном
    const tryPost = () => {
        axiosApiInstance.post('/test/post').then((response => console.log(response)))


    }

    const match = () => {
        axios.post('http://localhost:8080/api/connection/getConnections', {}, {headers: {'Authorization' : `Basic ${localStorage.getItem("accessToken")}`}}).then((response => console.log(response)))
    }
    const onSubmit: SubmitHandler<IFormInput> =  data => {
        localStorage.removeItem("accessToken");
         axiosApiInstance.post('/auth/login', {
            username: data.username,
            password: data.password
        }).then((response => {
             saveTokenInLocalStorage(response.data.credentials);
             localStorage.setItem("username", data.username);
             if (response.data.role === "manager") {
                 localStorage.setItem("role", response.data.role);
                 navigate("/requests");

             }
             else{
                 navigate("/form");
             }
         }))
             .catch((error) => toast.error("Неверный логин или пароль"))
        // toast.error("Неверный логин или пароль")




    }

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
                    <Typography fontSize="30px" sx={{ color: "#b358cb", textAlign: "center", minWidth: "60%", marginBottom: "15px"}}>Login</Typography>

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
                            width: "100px",
                            alignSelf: "center",
                            border: `1px solid #e285ee`,




                        }}
                        size="large"
                        variant="outlined"
                        type="submit"

                    >
                        Войти
                    </Button>
                    {/*{(localStorage.getItem("accessToken") != "") ?*/}
                    {/*    <Button sx={{left: "3%", backgroundColor: "#FFCCCC"}} onClick={() => navigate("/form")}>*/}
                    {/*        Перейти к анкете*/}
                    {/*    </Button> : <Button></Button>}*/}

                    <Box sx={{ position: "relative",   width:"60%", height:"7%", margin: "10px auto",
                        display: "block",  alignItems: "center",}} >
                        <Typography sx={{ color: "#e285ee", textAlign: "center", minWidth: "60%"}}>Нет учетной записи?</Typography>

                        <Button sx={{ color: "#e285ee",}}
                            onClick={() => navigate("/register")}
                        >
                            Зарегистрироваться.
                        </Button>

                    </Box>

                </Box>



            </form>

            </Box>


            <ToastContainer limit={3} position="bottom-right" />

        </Box>
    )
}

export default LoginPage;

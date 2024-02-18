import {Box, Button, FormControlLabel, Radio, RadioGroup, styled, TextField, Typography} from "@mui/material";
import React, {useState} from "react";
import { SubmitHandler, useForm, Controller } from 'react-hook-form';
import axiosApiInstance from "../../utils/tokenHelper";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import axios from "axios";
import bg from "../../images/bg/login2.jpg";
import {useNavigate}  from 'react-router-dom';

export interface SpacesuitFormInput {
    head: number,
    chest: number,
    waist: number,
    hips: number,
    foot_size: number,
    height: number,
    fabric_texture_id: number
}

const SpacesuitForm: React.FC = () => {
    const navigate = useNavigate();

    const onSubmit: SubmitHandler<SpacesuitFormInput> = data => {
        console.log(data)

        //post запрос
        if(localStorage.getItem("accessToken") ) {

            axiosApiInstance.post('/formSpacesuit/submitFormSpacesuit',
            {
            head: data.head,
                chest: data.chest, waist: data.waist, hips: data.hips, foot_size: data.foot_size,
                height: data.foot_size, fabric_texture_id: data.fabric_texture_id

        }, {headers: {'Authorization' : `Basic ${localStorage.getItem("accessToken")}`}})
            .then((response => {
                console.log(response);
                navigate("/profile")
            }))
            .catch((error) => {
                console.error(error);
            });}

    }
    const {control, handleSubmit} = useForm<SpacesuitFormInput>();


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
                    width: "450px",
                    height: "650px",
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
                        <Typography fontSize="26px" sx={{ color: "#722961", textAlign: "center", minWidth: "60%", marginBottom: "15px"}}>Данные для скафандра</Typography>

                        <Controller
                        name="head"
                        defaultValue={0}
                        control={control}
                        render={({ field }) => <TextField variant="standard" required fullWidth type="number" label="обхват головы" {...field}/>}
                    />
                    <Controller
                        name="chest"
                        defaultValue={0}
                        control={control}
                        render={({ field }) => <TextField variant="standard" required fullWidth type="number" label="обхват груди" {...field}/>}
                    />
                    <Controller
                        name="waist"
                        defaultValue={0}
                        control={control}
                        render={({ field }) => <TextField variant="standard" required fullWidth type="number" label="обхват талии" {...field}/>}
                    />
                    <Controller
                        name="hips"
                        defaultValue={0}
                        control={control}
                        render={({ field }) => <TextField variant="standard" required fullWidth type="number" label="обхват бедер" {...field}/>}
                    />
                    <Controller
                        name="height"
                        defaultValue={0}
                        control={control}
                        render={({ field }) => <TextField variant="standard" required fullWidth type="number" label="рост" {...field}/>}
                    />
                    <Controller
                        name="foot_size"
                        defaultValue={0}
                        control={control}
                        render={({ field }) => <TextField variant="standard" required fullWidth type="number" label="размер ноги" {...field}/>}
                    />
                        <Typography fontSize="20px" sx={{ color: "#90334f", minWidth: "60%", margin: "10px 0 0"}}>Любимая текстура ткани: </Typography>

                        <Controller
                        name="fabric_texture_id"
                        control={control}
                        defaultValue={0}
                        render={({ field }) => (
                            <RadioGroup {...field} row>
                                <FormControlLabel value={1} control={<Radio />} label="Шелк" />
                                <FormControlLabel value={2} control={<Radio />} label="Хлопок" />
                                <FormControlLabel value={3} control={<Radio />} label="Шерсть" />
                                <FormControlLabel value={4} control={<Radio />} label="Полиэстер" />
                                <FormControlLabel value={5} control={<Radio />} label="Флис" />
                            </RadioGroup>
                        )}
                    />
                        <Button
                            style={{
                                margin: "15px 0",
                                height: "40px",
                                width: "150px",
                                fontSize: "18px",
                                color: "#90334f",
                                textAlign: "center",  alignSelf: "center",
                                border: `1px solid #90334f`,
                                borderRadius: "15px",

                            }}
                            size="large"
                            variant="outlined"
                            type="submit"
                            // onClick={() => navigate("/gallery")}
                        >
                        Отправить
                    </Button>


                </Box>


            </form>


        </Box>
        </Box>
    );
}

export default SpacesuitForm;

import {Box, Button, FormControlLabel, Radio, RadioGroup, styled, TextField, Typography} from "@mui/material";
import React, {useState} from "react";
import { SubmitHandler, useForm, Controller } from 'react-hook-form';
import useNavigate  from 'react-router-dom';
import axiosApiInstance from "../../utils/tokenHelper";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import axios from "axios";
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

    const onSubmit: SubmitHandler<SpacesuitFormInput> = data => {
        console.log(data)

        //post запрос
        axiosApiInstance.post('api/formSpacesuit/submitFormSpacesuit',
            {
            head: data.head,
                chest: data.chest, waist: data.waist, hips: data.hips, foot_size: data.foot_size,
                height: data.foot_size, fabric_texture_id: data.fabric_texture_id

        }, {headers: {'Authorization' : `Basic ${localStorage.getItem("accessToken")}`}})
            .then((response => {
                console.log(response);
            }))
            .catch((error) => {
                console.error(error);
            });

    }
    const {control, handleSubmit} = useForm<SpacesuitFormInput>();


    return (
        <Box
            sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                height: "100vh",
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
                    <Controller
                        name="fabric_texture_id"
                        control={control}
                        defaultValue={0}
                        render={({ field }) => (
                            <RadioGroup {...field} row>
                                <FormControlLabel value={0} control={<Radio />} label="Шелк" />
                                <FormControlLabel value={1} control={<Radio />} label="Лен" />
                                <FormControlLabel value={2} control={<Radio />} label="Хлопок" />
                                <FormControlLabel value={3} control={<Radio />} label="Шерсть" />
                                <FormControlLabel value={4} control={<Radio />} label="?" />
                            </RadioGroup>
                        )}
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
                        Отправить
                    </Button>


                </Box>


            </form>


        </Box>
    );
}

export default SpacesuitForm;

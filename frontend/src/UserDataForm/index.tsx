import {Box, Button, FormControlLabel, Radio, RadioGroup, styled, TextField, Typography} from "@mui/material";
import React, {useState} from "react";
import { SubmitHandler, useForm, Controller } from 'react-hook-form';
import useNavigate  from 'react-router-dom';
import axiosApiInstance from "../utils/tokenHelper";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "./style.css"
export interface UserDataFormInput {
    birth_date: Date,
    sex: string,
    weight: number,
    height: number,
    hair_color: string,
    location: string,
}

const UserDataForm: React.FC = () => {

    const onSubmit: SubmitHandler<UserDataFormInput> = data => {

        //тут дата в нужный формат преобразовывается
        const year = data.birth_date.getFullYear();
        const month = String(data.birth_date.getMonth() + 1).padStart(2, '0');
        const day = String(data.birth_date.getDate()).padStart(2, '0');
        const formattedDate = `${year}-${month}-${day}`;

        console.log(formattedDate);
        console.log(data)

        //post запрос

    }
    const {control, handleSubmit} = useForm<UserDataFormInput>();


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
                        name="birth_date"
                        control={control}

                        render={({field}) => (
                            // TODO ширину пофиксить
                            <div className="customDatePickerWidth">
                                    <DatePicker wrapperClassName="datepicker"
                                                placeholderText='Select date'
                                                onChange={(date) => field.onChange(date)}
                                                selected={field.value}

                                    />
                            </div>
                        )}
                    />
                    <Controller
                        name="sex"
                        control={control}
                        defaultValue=""
                        render={({ field }) => (
                            <RadioGroup {...field} row>
                                <FormControlLabel value="MEN" control={<Radio />} label="Мужчина" />
                                <FormControlLabel value="WOMEN" control={<Radio />} label="Женщина" />
                            </RadioGroup>
                        )}
                    />
                    <Controller
                        name="weight"
                        control={control}

                        render={({ field }) => <TextField variant="standard" required fullWidth type="number" label="вес" {...field}/>}
                    />
                    <Controller
                        name="height"
                        control={control}
                        render={({ field }) => <TextField variant="standard" required fullWidth type="number" label="рост" {...field}/>}
                    />
                    <Controller
                        name="hair_color"
                        control={control}
                        render={({ field }) => <TextField variant="standard" required fullWidth type="text" label="цвет волос" {...field}/>}
                    />
                    <Controller
                        name="location"
                        control={control}
                        render={({ field }) => (
                            <RadioGroup {...field} row>
                                <FormControlLabel value="MARS" control={<Radio />} label="Марс" />
                                <FormControlLabel value="EARTH" control={<Radio />} label="Земля" />
                            </RadioGroup>
                            )} />
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

export default UserDataForm;

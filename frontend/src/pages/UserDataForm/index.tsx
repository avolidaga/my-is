import {
    Box,
    Button,
    FormControlLabel,
    MenuItem,
    Radio,
    RadioGroup,
    Select,
    styled,
    TextField,
    Typography
} from "@mui/material";
import React, {useState} from "react";
import { SubmitHandler, useForm, Controller } from 'react-hook-form';
import {useNavigate}  from 'react-router-dom';
import axiosApiInstance from "../../utils/tokenHelper";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "./style.css"
import bg from "../../images/bg/form-u1.jpg";
import {toast, ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
export interface UserDataFormInput {
    firstname: string,
    birth_date: Date,
    sex: string,
    weight: number,
    height: number,
    hair_color: string,
    location: string,
}

const UserDataForm: React.FC = () => {
    const navigate = useNavigate();

    const onSubmit: SubmitHandler<UserDataFormInput> = async data => {

        //тут дата в нужный формат преобразовывается
        const year = data.birth_date.getFullYear();
        const month = String(data.birth_date.getMonth() + 1).padStart(2, '0');
        const day = String(data.birth_date.getDate()).padStart(2, '0');
        const formattedDate = `${year}-${month}-${day}`;

        console.log(formattedDate);
        console.log(data)
        const parts = formattedDate.split("-");

        const birthYear = parseInt(parts[0], 10);
        const birthMonth = parseInt(parts[1], 10);
        const birthDay = parseInt(parts[2], 10);

        const currentDate = new Date();

        const currentYear = currentDate.getFullYear();
        const currentMonth = currentDate.getMonth() + 1;
        const currentDay = currentDate.getDate();

        let age = currentYear - birthYear;

        if (currentMonth < birthMonth || (currentMonth === birthMonth && currentDay < birthDay)) {
            age--;
        }
        if (age < 18) {
            toast.error("Вам точно больше 18?)")
        }
        else if(data.weight <=0 || data.height <= 0 || data.height > 300){
            toast.error("Вес и рост должны быть целыми числами больше нуля. Рост не более 300 см)")

        }
        else{
            //post запрос
            if(localStorage.getItem("accessToken") ) {

                await axiosApiInstance.post('/form/submitForm', {
                firstname: data.firstname, birth_date: formattedDate,
                sex: data.sex,
                weight: Math.round(data.weight),
                height: Math.round(data.height),
                hair_color: data.hair_color,
                location: data.location
            }).then((re) => navigate("/gallery"))
                .catch((error) => toast.error("Вы точно зашли в аккаунт?)"))
        }}



    }
    const {control, handleSubmit} = useForm<UserDataFormInput>();


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
                        <Typography fontSize="30px" sx={{ color: "#722961", textAlign: "center", minWidth: "60%", marginBottom: "15px"}}>Анкета</Typography>

                        <Controller
                        name="firstname"
                        control={control}

                        render={({ field }) => <TextField variant="standard" required fullWidth type="text" label="имя" {...field}/>}
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
                        <Typography fontSize="20px" sx={{ color: "#90334f", minWidth: "60%", margin: "10px 0 0"}}>Пол: </Typography>

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
                        <Typography fontSize="20px" sx={{ color: "#90334f", minWidth: "60%", margin: "10px 0 0"}}>Местоположение: </Typography>

                        <Controller
                        name="location"
                        control={control}
                        render={({ field }) => (
                            <RadioGroup {...field} row>
                                <FormControlLabel value="MARS" control={<Radio />} label="Марс" />
                                <FormControlLabel value="EARTH" control={<Radio />} label="Земля" />
                            </RadioGroup>
                        )} />
                    <Typography fontSize="20px" sx={{ color: "#90334f", minWidth: "60%", margin: "10px 0 5px"}}>
                        Дата рождения:
                    </Typography>
                    <Controller
                        name="birth_date"
                        control={control}

                        render={({field}) => (
                            // TODO ширину пофиксить
                            <div className="customDatePickerWidth">
                                    <DatePicker wrapperClassName="datepicker"
                                                placeholderText='Введите дату, начиная с года рождения'
                                                onChange={(date) => field.onChange(date)}
                                                selected={field.value}

                                    />
                            </div>
                        )}
                    />

                    <Typography fontSize="20px" sx={{ color: "#90334f", minWidth: "60%", margin: "15px 0 5px"}}>
                        Цвет волос:
                    </Typography>
                    <Controller
                        name="hair_color"
                        control={control}
                        defaultValue=""
                        render={({ field }) => (
                            <Select
                                sx={{maxHeight: "40px"}}
                                labelId="hairColorLabel"
                                {...field}
                            >
                                <MenuItem value="" disabled>Выберите цвет</MenuItem>
                                <MenuItem value="black">Черный</MenuItem>
                                <MenuItem value="brown">Коричневый</MenuItem>
                                <MenuItem value="blonde">Блонд</MenuItem>
                                <MenuItem value="red">Рыжий</MenuItem>
                                <MenuItem value="gray">Седой</MenuItem>
                            </Select> )}
                            />

                    <Button
                        style={{
                            margin: "15px 0",
                            height: "40px",
                            width: "130px",
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
            <ToastContainer limit={3} position="bottom-right" />

        </Box>
    );
}

export default UserDataForm;

import React, { useEffect, useState } from 'react';
import '../../App.css';
import axios from "axios";
import { saveTokenInLocalStorage } from "../../utils/saveDataInLocalstorage";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import {Box, Button, Grid, IconButton, TextField, Typography} from "@mui/material";
import axiosApiInstance from "../../utils/tokenHelper";
import { useNavigate } from "react-router-dom";
import PersonCard from "../../components/PersonCard";
import blonde_young from '../../images/Mars/women/blonde_young.png';
import FavoriteIcon from "@mui/icons-material/Favorite";
export interface UserData {
    id: number,
    firstname: string,
    birthdate: string,
    sex: string,
    weight: number,
    height: number,
    hairColor: string,
    location: string,
}
interface ISpacesuit {
    status: string
}

const UserProfilePage: React.FC = () => {
    const navigate = useNavigate();
    const [resultArray, setResultArray] = useState<any[]>([]);
    const [myUserData, setMyUserData] = useState<UserData[]>([]);
    const [mySpacesuit, setMySpacesuit] = useState<string>("");
    let status = "j";



    const getCurrentUserData = async () => {

            await axiosApiInstance.post('/test/getCurrUserData', {}, {headers: {'Authorization': `Basic ${localStorage.getItem("accessToken")}`}})
                .then((response => {
                    setMyUserData(response.data);
                    getSpacesuitStatus();

                }))
                .catch((error) => {
                    console.error(error);
                });


    }

    const getSpacesuitStatus = async () => {
        let response ;
        try {
            response = await axiosApiInstance.post('test/getCurrUserSpacesuitData', {});
            console.log(response);
            setMySpacesuit(response.data);
        } catch (error) {
            console.error(error);
        }

    }





    useEffect(() => {
        getCurrentUserData();
        getSpacesuitStatus();

    }, []);

    return (
        <Box sx={{width: "100vw", margin: "0px", height: "100vh",overflowX: "hidden",             backgroundColor: "#fce8e9"

            // backgroundImage: `url(${bg})`, backgroundRepeat: `no-repeat`, backgroundSize: "cover",
        }}><Grid container justifyContent="center"
                 alignItems="center" >
            <Grid item xs={7} >
                <Button style={{
                    margin: "30px 15px",
                    height: "40px",
                    fontSize: "18px",
                    color: "#90334f",
                    borderRadius: "15px",
                    width: "300px",
                    alignSelf: "center",
                    border: `1px solid #90334f`,
                }}                    onClick={() => navigate("/gallery")}>
                    Вернуться к просмотру
                </Button>

            </Grid>


            <Grid item xs={3} >
                <Button style={{
                    margin: "30px 15px",
                    height: "40px",
                    fontSize: "18px",
                    color: "#90334f",
                    borderRadius: "15px",
                    width: "300px",
                    alignSelf: "center",
                    border: `1px solid #90334f`,
                }}
                        onClick={() => navigate("/match")}>
                    Посмотреть мэтчи
                </Button>

            </Grid>
            <Grid item xs={2}>
                <Button style={{
                    margin: "30px 15px",
                    height: "40px",
                    fontSize: "18px",
                    color: "#90334f",
                    borderRadius: "15px",
                    width: "150px",
                    alignSelf: "center",
                    border: `1px solid #90334f`,
                }} onClick={()=>{
                    localStorage.removeItem("accessToken");
                    localStorage.removeItem("role");
                    localStorage.removeItem("username");
                    navigate("/");
                }}>
                    Выйти
                </Button>
            </Grid>

        </Grid>
            <Typography fontSize="30px" sx={{ color: "#722961", textAlign: "center", minWidth: "60%", marginBottom: "15px"}}>Профиль</Typography>

            <Box sx={{width: "94.8vw", margin: "50px"}}>

                <Box sx={{ flex: "1"}}>
                    <Grid
                        container
                        columns={{xl: 12, md: 12, xs: 12}}
                        rowSpacing={3}
                        direction="row"
                        columnSpacing={"20px"}
                        sx={{
                            marginBottom: "40px"
                        }}

                    >
                        <Grid item xs={6}>
                            {myUserData.map((item, index) => (
                                <Grid key={item.id} item xs={9} style={{ minWidth: "350px", justifyContent: "center",
                                    alignItems: "center", }}>
                                    <PersonCard
                                        id={item.id}
                                        firstname={item.firstname}
                                        birth_date={item.birthdate}
                                        sex={item.sex}
                                        weight={item.weight}
                                        height={item.height}
                                        hair_color={item.hairColor}
                                        location={item.location}
                                    />
                                    <Box sx={{  width: "80%",
                                        display: 'flex', justifyContent: 'center', marginTop: "5px" }}>

                                        <Button
                                            style={{
                                                margin: "15px 0",
                                                height: "40px",
                                                width: "180px",
                                                fontSize: "18px",
                                                color: "#90334f",
                                                textAlign: "center",  alignSelf: "center",
                                                border: `1px solid #90334f`,
                                                borderRadius: "15px",

                                            }}
                                            size="large"
                                            variant="outlined"
                                            onClick={() => navigate("/form")}
                                        >
                                            Редактировать
                                        </Button>
                                    </Box>

                                </Grid>
                            ))}
                        </Grid>
                        {mySpacesuit ?
                        <Grid item xs={6}>
                            <Typography fontSize="26px" sx={{ color: "#722961", textAlign: "center", minWidth: "60%", marginBottom: "15px"}}>
                            Статус готовности вашего скафандра:
                        </Typography>
                            <Grid  item xs={12} style={{ minWidth: "350px", justifyContent: "center",
                                alignItems: "center", }}>
                                <Box sx={{  width: "80%",
                                    display: 'flex', justifyContent: 'center', marginTop: "5px" }}>


                                    <Button
                                        style={{
                                            margin: "15px 0",
                                            height: "40px",
                                            width: "180px",
                                            fontSize: "18px",
                                            color: "#90334f",
                                            textAlign: "center",  alignSelf: "center",
                                            border: `1px solid #90334f`,
                                            borderRadius: "15px",

                                        }}
                                        size="large"
                                        variant="outlined"
                                    >
                                        {mySpacesuit}
                                    </Button>
                                    {mySpacesuit === "DECLINED" ?
                                        <Button
                                            style={{
                                                margin: "15px 0",
                                                height: "40px",
                                                width: "180px",
                                                fontSize: "18px",
                                                color: "#90334f",
                                                textAlign: "center",  alignSelf: "center",
                                                border: `1px solid #90334f`,
                                                borderRadius: "15px",

                                            }}
                                            size="large"
                                            variant="outlined"
                                            onClick={()=> navigate("/spacesuit-form")}
                                        >
                                            Отправить повторно
                                        </Button> : ""}
                                </Box>

                            </Grid>
                        </Grid> : ""}


                    </Grid>
                </Box>
            </Box>
        </Box>

    );
}

export default UserProfilePage;

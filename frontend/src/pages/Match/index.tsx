import React, { useEffect, useState } from 'react';
import '../../App.css';
import axios from "axios";
import { saveTokenInLocalStorage } from "../../utils/saveDataInLocalstorage";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import {Box, Button, Grid, TextField, Typography} from "@mui/material";
import axiosApiInstance from "../../utils/tokenHelper";
import { useNavigate } from "react-router-dom";
import PersonCard from "../../components/PersonCard";

const MatchPage: React.FC = () => {
    const navigate = useNavigate();
    const [resultArray, setResultArray] = useState<any[]>([]);

    const match = () => {
        axios.post('http://localhost:8080/api/test/getAllUserData', {}, {headers: {'Authorization' : `Basic ${localStorage.getItem("accessToken")}`}})
            .then((response => {
                console.log(response);
                setResultArray(response.data);
            }))
            .catch((error) => {
                console.error(error);
                setResultArray([]);
            });
    }

    const connectUsers = (userId: number) => {
        axios.post(
            'http://localhost:8080/api/connection/connectUsers',
            {user2: userId},
            { headers: {'Authorization': `Basic ${localStorage.getItem("accessToken")}`} }
        )
            .then((response) => {
                console.log(response);
                // Дополнительные действия после успешного соединения пользователей
            })
            .catch((error) => {
                console.error(error);
                // Обработка ошибок при соединении пользователей
            });
    }

    useEffect(() => {
        match();
    }, []);

    return (
        <Box sx={{width: "94.8vw", margin: "100px"}}>

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
                {resultArray.map((item, index) => (
                        <Grid key={item.id} item xl={4} md={6} xs={12} style={{ minWidth: "350px" }}>
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
                            <Button
                                variant="contained"
                                color="primary"
                                onClick={() => connectUsers(item.id)}
                            >
                                Connect Users
                            </Button>
                        </Grid>
                    ))
                }

            </Grid>
        </Box>
        </Box>
    //             {resultArray.map((item, index) => (
    //                 <PersonCard
    //                     id={item.id}
    //                     birth_date={item.birthdate}
    //                     sex={item.sex}
    //                     weight={item.weight}
    //                     height={item.height}
    //                     hair_color={item.hairColor}
    //                     location={item.location}
    //                 />
    //
    //
    //                 // <div key={index} style={{ borderBottom: "1px solid #000", padding: "10px", display: "flex", justifyContent: "space-between" }}>
    //                 //     <div>
    //                 //         <Typography variant="body1">
    //                 //             <strong>Birthdate:</strong> {item.birthdate}
    //                 //         </Typography>
    //                 //         <Typography variant="body1">
    //                 //             <strong>Sex:</strong> {item.sex}
    //                 //         </Typography>
    //                 //         <Typography variant="body1">
    //                 //             <strong>Weight:</strong> {item.weight}
    //                 //         </Typography>
    //                 //         <Typography variant="body1">
    //                 //             <strong>Height:</strong> {item.height}
    //                 //         </Typography>
    //                 //         <Typography variant="body1">
    //                 //             <strong>Hair Color:</strong> {item.hairColor}
    //                 //         </Typography>
    //                 //         <Typography variant="body1">
    //                 //             <strong>Location:</strong> {item.location}
    //                 //         </Typography>
    //                 //     </div>
    //                     <Button
    //                         variant="contained"
    //                         color="primary"
    //                         onClick={() => connectUsers(item.id)}
    //                     >
    //                         Connect Users
    //                     </Button>
    //                 // </div>
    //             ))}
    //         </Box>
    );
}

export default MatchPage;

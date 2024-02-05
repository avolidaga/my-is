import {Box, Button, Grid} from "@mui/material";
import PersonCard from "../../components/PersonCard";
import React, {useEffect, useState} from "react";
import axios from "axios";
import axiosApiInstance from "../../utils/tokenHelper";


const GalleryPage: React.FC = () => {

    const [personArray, setPersonArray] = useState<any[]>([]);

    const getAllPartners = () => {
        axios.post('http://localhost:8080/api/test/getAllUserData', {}, {headers: {'Authorization' : `Basic ${localStorage.getItem("accessToken")}`}})
            .then((response => {
                console.log(response);
                setPersonArray(response.data);
            }))
            .catch((error) => {
                console.error(error);
                setPersonArray([]);
            });
    }
    const connectUsers = (userId: number) => {
        axiosApiInstance.post(
            '/connection/connectUsers',
            {user2: userId},
            { headers: {'Authorization': `Basic ${localStorage.getItem("accessToken")}`} }
        )
            .then((response) => {
                console.log(response);
            })
            .catch((error) => {
                console.error(error);
            });
    }

    useEffect(() => {
        getAllPartners();
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
                    {personArray.map((item, index) => (
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
    )
}

export default GalleryPage;
import React, { useEffect, useState } from "react";
import {Box, Button, Grid, Typography} from "@mui/material";
import RequestTabs from "../../components/RequestTabs/index";
import axiosApiInstance from "../../utils/tokenHelper";
import { useNavigate } from "react-router-dom";

const ManagerPage: React.FC = () => {
    const [requestArray, setRequestArray] = useState<any[]>([]);
    const [inProgressArray, setInProgressArray] = useState<any[]>([]);
    const [readyArray, setReadyArray] = useState<any[]>([]);
    const [declinedArray, setDeclinedArray] = useState<any[]>([]);
    const navigate = useNavigate();

    const getAllRequest = () => {
        axiosApiInstance.post("/request/getAllUserRequest")
            .then(response => { setRequestArray(response.data) });
    };

    const getInProgress = () => {
        axiosApiInstance.post("/request/getInProgressUserRequest")
            .then(response => { setInProgressArray(response.data) });
    }
    const getReady = () => {
        axiosApiInstance.post("/request/getReadyUserRequest")
            .then(response => { setReadyArray(response.data) });
    }
    const getDeclined = () => {
        axiosApiInstance.post("/request/getDeclinedUserRequest")
            .then(response => { setDeclinedArray(response.data) });
    }

    useEffect(() => {
        getAllRequest();
        getInProgress();
        getDeclined();
        getReady();
    }, []);


    return (
        <Box sx={{ width: "100vw", margin: "0px", height: "100vh", overflowX: "hidden"  }}>
            <Grid container justifyContent="center"
                  alignItems="center" >
                <Grid item xs={8} textAlign="center">
                    <Typography fontSize="30px" sx={{ color: "#722961", textAlign: "center", minWidth: "60%", marginTop: "15px"}}>
                        Страница заявок
                    </Typography>
                </Grid>
                <Grid item xs={2}>
                    <Typography fontSize="20px" sx={{ color: "#722961", textAlign: "center", minWidth: "60%", marginTop: "15px"}}>
                        manager: {localStorage.getItem("username")}
                    </Typography>
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



            <Box sx={{ width: "96vw", margin: "40px 15px", }}>
                <RequestTabs
                    requestInProgress={inProgressArray}
                    requestDeclinedArray={declinedArray}
                    requestAcceptedArray={readyArray}
                    requestArray={requestArray}
                    getInProgress={getInProgress}
                    getReady={getReady}
                    getDeclined={getDeclined}
                    getAllRequest={ getAllRequest}
                />
            </Box>
        </Box>
    );
};

export default ManagerPage;

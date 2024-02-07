import {Box, Button, Dialog, DialogContent, DialogTitle, Grid, IconButton} from "@mui/material";
import PersonCard from "../../components/PersonCard";
import React, {useEffect, useState} from "react";
import axios from "axios";
import axiosApiInstance from "../../utils/tokenHelper";
import bg from '../../images/bg/gallery.png'
import FavoriteIcon from '@mui/icons-material/Favorite';
import {useNavigate} from "react-router-dom";
interface UserData {
    id: number;
    firstname: string;
    birth_date: Date;
    sex: string;
    weight: number;
    height: number;
    hair_color: string;
    location: string;
}
const GalleryPage: React.FC = () => {

    const [personArray, setPersonArray] = useState<any[]>([]);
    const [isMatchDialogOpen, setIsMatchDialogOpen] = useState(false);
    const [matchedUserId, setMatchedUserId] = useState<number | null>(null);
    const navigate = useNavigate();

    const getAllPartners = async () => {
        await axios.post('http://localhost:8080/api/test/getAllUserData', {}, {headers: {'Authorization': `Basic ${localStorage.getItem("accessToken")}`}})
            .then((response => {
                console.log(response);
                setPersonArray(response.data);
            }))
            .catch((error) => {
                console.error(error);
                setPersonArray([]);
            });
    }
    const connectUsers = async (userId: number) => {
        await axiosApiInstance.post(
            '/connection/connectUsers',
            {user2: userId},
            {headers: {'Authorization': `Basic ${localStorage.getItem("accessToken")}`}}
        )
            .then((response) => {
                console.log(response);
                getMatches(userId)
            })
            .catch((error) => {
                console.error(error);
            });
    }

    const getMatches = (userId: number) => {
        axiosApiInstance.post('/connection/getConnections', {}, {headers: {'Authorization' : `Basic ${localStorage.getItem("accessToken")}`}})
            .then((response => {
                console.log(response);
                console.log(userId);
                if (response.data.some((item : UserData) => item.id === userId)) {setIsMatchDialogOpen(true),
                    setMatchedUserId(userId)}
            }))
            .catch((error) => {
                console.error(error);
            });
    }
    const handleCloseMatchDialog = () => {
        setIsMatchDialogOpen(false);
        setMatchedUserId(null);
    }
    useEffect(() => {
        getAllPartners();
    }, []);
    return (
        <Box sx={{width: "100vw", margin: "0px", height: "100vh",
            // backgroundImage: `url(${bg})`, backgroundRepeat: `no-repeat`, backgroundSize: "cover",
        }}>
            <Button sx = {{ margin: "15px", border: "1px solid", borderColor: "#FF69B4"}}
            onClick={() => navigate("/match")}>
                Посмотреть мэтчи
            </Button>

            <Box sx={{ width: "94.8vw", margin: "100px" }}>
                <Box sx={{ flex: "1" }}>
                    <Grid
                        container
                        columns={{ xl: 12, md: 12, xs: 12 }}
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
                                <Box sx={{  width: "80%",
                                    display: 'flex', justifyContent: 'center', marginTop: "5px" }}>

                                    <IconButton
                                        sx={{ '&:hover': { bgcolor: '#FF69B4' } }}
                                        onClick={() => connectUsers(item.id)}
                                    ><FavoriteIcon/>LIKE</IconButton>
                                </Box>
                            </Grid>
                        ))}
                    </Grid>
                </Box>
            </Box>
            <Dialog open={isMatchDialogOpen} onClose={handleCloseMatchDialog}>
                <DialogTitle>It's a match! </DialogTitle>
                <Button onClick={() => navigate("/match")}>
                    Перейти ко всем мэтчам
                </Button>
            </Dialog>
        </Box>

    )
}

export default GalleryPage;
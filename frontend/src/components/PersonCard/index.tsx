import {Avatar, Box, Divider, Stack, Typography} from "@mui/material";
import images from './imagesImport';

interface IPersonCard{
    id: number,
    firstname: string,
    birth_date: string,
    sex: string,
    weight: number,
    height: number,
    hair_color: string,
    location: string,
}

const PersonCard: React.FC<IPersonCard> = (props) => {
    const {id, firstname, birth_date, sex, weight, height, hair_color, location} = props;

    const parts = birth_date.split("-");

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

    const imageFolderPrefix = location === 'MARS' ? 'Mars' : 'Earth';
    const genderFolder = sex === 'MEN' ? 'Men' : 'Women';
    const ageFolder = age > 35 ? 'Old' : 'Young';
    const hairColorFolder = ['black', 'brown', 'blonde', 'red', 'gray'].includes(hair_color) ? hair_color : 'brown';

    const imageKey = `${hairColorFolder}${genderFolder}${imageFolderPrefix}${ageFolder}`;

    const selectedImage = images[imageKey];

    return (
        <Box
            sx={{
                width: "80%",
                minWidth: "230px",
                height: "420px",
                boxSizing: "border-box",
                position: "relative",
                borderRadius: "15px",
                backgroundColor: "#FFFFFF",
                border: `2px solid black`,
                display: "flex",
                flexDirection: "column",
                alignItems: "center", // Выравнивание по центру по горизонтали
            }}
        >
            <Avatar
                src={selectedImage}
                sx={{ width: '180px', height: '180px', border: '2px solid #42b1d6', marginTop: '10px' }}
            />
            <Box sx={{ marginTop: "10px", textAlign: "center" }}>
                <Typography sx={{ fontSize: 16, justifyContent: 'center', cursor: 'pointer' }}>Имя: {firstname}</Typography>
            </Box>
            <Box sx={{ marginTop: "5px", textAlign: "center" }}>
                <Typography sx={{ fontSize: 16, justifyContent: 'center', cursor: 'pointer' }}>Возраст: {age}</Typography>
            </Box>

            <Box sx={{ marginTop: "5px", textAlign: "center" }}>
                <Typography sx={{ fontSize: 16, justifyContent: 'center', cursor: 'pointer' }}>Местоположение: {location == "EARTH" ? "Земля" : "Марс"}</Typography>
            </Box>
            <Box sx={{ marginTop: "5px", textAlign: "center" }}>
                <Typography sx={{ fontSize: 16, justifyContent: 'center', cursor: 'pointer' }}>Пол: {sex == 'MEN' ? "Мужской" : "Женский"}</Typography>
            </Box>
            <Box sx={{ marginTop: "5px", textAlign: "center" }}>
                <Typography sx={{ fontSize: 16, justifyContent: 'center', cursor: 'pointer' }}>Вес: {weight}</Typography>
            </Box>
            <Box sx={{ marginTop: "5px", textAlign: "center" }}>
                <Typography sx={{ fontSize: 16, justifyContent: 'center', cursor: 'pointer' }}>Рост: {height}</Typography>
            </Box>
            <Box sx={{ marginTop: "5px", textAlign: "center" }}>
                <Typography sx={{ fontSize: 16, justifyContent: 'center', cursor: 'pointer' }}>Цвет волос: {hair_color}</Typography>
            </Box>


            {/*<Stack >*/}
            {/*    <Avatar*/}
            {/*        // alt={`${user.name} ${user.lastName}`}*/}
            {/*        // src={`${user.photo}`}*/}
            {/*        sx={{ width: '180px', height: '180px',  border:'2px solid #42b1d6', marginLeft:'75px', marginTop:'10px' }}*/}

            {/*    />*/}

            {/*</Stack>*/}


            {/*<Stack direction="row" spacing={2} marginTop="40px" height='500px' width='350px'>*/}
            {/*    <div>*/}
            {/*        <div style={{display:"table"}}>*/}
            {/*            <div style={{display:"table-row"}}>*/}
            {/*                <div style={{display:"table-cell"}}>*/}
            {/*                    <Typography variant="h3" align="center" width='200px' marginLeft='65px' marginBottom='45px' >name</Typography>*/}
            {/*                </div>*/}



            {/*            </div>*/}
            {/*        </div>*/}

            {/*        <Divider sx= {{width: "calc(85%)", height: "2px", margin: "5px 0px 30px 40px", top: 140, backgroundColor: "rgba(72, 161, 211, 1)" , }}></Divider>*/}

            {/*        <Box>*/}

            {/*        </Box>*/}

            {/*        <Stack marginLeft='50px' >*/}
            {/*            <Box width='250px' >*/}
            {/*                <div>*/}
            {/*                    <Box display='inline-flex' >*/}
            {/*                        <Typography variant="h4">Дата рождения:</Typography>*/}

            {/*                        <Typography align="right" width='40px' fontSize='20px' >*/}
            {/*                            {birth_date}*/}
            {/*                        </Typography>*/}
            {/*                    </Box>*/}

            {/*                    <Box display='inline-flex'>*/}
            {/*                        <Typography variant="h4">Планета: </Typography>*/}

            {/*                        <Typography align="right" width='65px' fontSize='20px' >*/}
            {/*                            {location}*/}
            {/*                        </Typography>*/}

            {/*                    </Box>*/}

            {/*                    /!*<Box display='inline-flex'>*!/*/}
            {/*                    /!*    <Typography variant="h4"> Популярность:</Typography>*!/*/}
            {/*                    /!*    <Typography align="right" width='94px' fontSize='20px' >*!/*/}
            {/*                    /!*        {profileStatistics.popularity}*!/*/}
            {/*                    /!*    </Typography>*!/*/}
            {/*                    /!*</Box>*!/*/}

            {/*                    /!*<Box display='inline-flex'>*!/*/}
            {/*                    /!*    <Typography variant="h4">Получено лайков:</Typography>*!/*/}
            {/*                    /!*    <Typography align="right" width='64px' fontSize='20px' >*!/*/}
            {/*                    /!*        {profileStatistics.likes}*!/*/}
            {/*                    /!*    </Typography>*!/*/}
            {/*                    /!*</Box>*!/*/}

            {/*                    /!*<Box display='inline-flex'>*!/*/}
            {/*                    /!*    <Typography variant="h4">Коллеги:</Typography>*!/*/}

            {/*                    /!*    <Typography align="right" width='153px' fontSize='20px' >*!/*/}
            {/*                    /!*        {profileStatistics.colleagues}*!/*/}
            {/*                    /!*    </Typography>*!/*/}
            {/*                    /!*</Box>*!/*/}
            {/*                </div>*/}
            {/*            </Box>*/}

            {/*        </Stack>*/}
            {/*    </div>*/}
            {/*    <Stack marginBottom='20px' height='20px'>*/}

            {/*    </Stack>*/}
            {/*</Stack>*/}


            {/*<FavoriteBorderOutlinedIcon*/}
            {/*sx={{marginLeft:'170px', color:'rgba(72, 161, 211, 1)',width:'50px', height:'50px', marginTop:'40px'}}></FavoriteBorderOutlinedIcon>*/}

            {/*\\ <Typography variant="body1"></Typography>*/}
            {/*<Typography variant="body1" sx={{marginTop: "8px"}}>{shortDescription}</Typography>*/}
        </Box>
        // <Box sx={{ width: "80vw", margin: "auto", minWidth: "1200px" }}>
        //
        //             <div style={{minWidth:"1200px", width:"100%", height:"100%", display:"table", marginTop:"25px"}}>
        //
        //                 <div style={{width:"100%", height:"100%", display:"table-row"}}>
        //                     <div style={{width:"400px", display:"table-cell"}}>
        //                         <Stack border='2px solid #42b1d6' borderRadius='10px' height="585px" width="350px" >
        //                             <Stack >
        //                                 <Avatar
        //                                     // alt={`${user.name} ${user.lastName}`}
        //                                     // src={`${user.photo}`}
        //                                     sx={{ width: '180px', height: '180px',  border:'2px solid #42b1d6', marginLeft:'75px', marginTop:'10px' }}
        //
        //                                 />
        //
        //                             </Stack>
        //
        //
        //                             <Stack direction="row" spacing={2} marginTop="40px" height='500px' width='350px'>
        //                                 <div>
        //                                     <div style={{display:"table"}}>
        //                                         <div style={{display:"table-row"}}>
        //                                             <div style={{display:"table-cell"}}>
        //                                                 <Typography variant="h3" align="center" width='200px' marginLeft='65px' marginBottom='45px' >name</Typography>
        //                                             </div>
        //
        //
        //
        //                                         </div>
        //                                     </div>
        //
        //                                     <Divider sx= {{width: "calc(85%)", height: "2px", margin: "5px 0px 30px 40px", top: 140, backgroundColor: "rgba(72, 161, 211, 1)" , }}></Divider>
        //
        //                                     <Box>
        //
        //                                     </Box>
        //
        //                                     <Stack marginLeft='50px' >
        //                                             <Box width='250px' >
        //                                                 <div>
        //                                                     <Box display='inline-flex' >
        //                                                         <Typography variant="h4">Дата рождения:</Typography>
        //
        //                                                         <Typography align="right" width='40px' fontSize='20px' >
        //                                                             {birth_date}
        //                                                         </Typography>
        //                                                     </Box>
        //
        //                                                     <Box display='inline-flex'>
        //                                                         <Typography variant="h4">Планета: </Typography>
        //
        //                                                         <Typography align="right" width='65px' fontSize='20px' >
        //                                                             {location}
        //                                                         </Typography>
        //
        //                                                     </Box>
        //
        //                                                     {/*<Box display='inline-flex'>*/}
        //                                                     {/*    <Typography variant="h4"> Популярность:</Typography>*/}
        //                                                     {/*    <Typography align="right" width='94px' fontSize='20px' >*/}
        //                                                     {/*        {profileStatistics.popularity}*/}
        //                                                     {/*    </Typography>*/}
        //                                                     {/*</Box>*/}
        //
        //                                                     {/*<Box display='inline-flex'>*/}
        //                                                     {/*    <Typography variant="h4">Получено лайков:</Typography>*/}
        //                                                     {/*    <Typography align="right" width='64px' fontSize='20px' >*/}
        //                                                     {/*        {profileStatistics.likes}*/}
        //                                                     {/*    </Typography>*/}
        //                                                     {/*</Box>*/}
        //
        //                                                     {/*<Box display='inline-flex'>*/}
        //                                                     {/*    <Typography variant="h4">Коллеги:</Typography>*/}
        //
        //                                                     {/*    <Typography align="right" width='153px' fontSize='20px' >*/}
        //                                                     {/*        {profileStatistics.colleagues}*/}
        //                                                     {/*    </Typography>*/}
        //                                                     {/*</Box>*/}
        //                                                 </div>
        //                                             </Box>
        //
        //                                     </Stack>
        //                                 </div>
        //                                 <Stack marginBottom='20px' height='20px'>
        //
        //                                 </Stack>
        //                             </Stack>
        //
        //                         </Stack>
        //                     </div>
        //
        //
        //                 </div>
        //             </div>
        //
        //
        // </Box>

    )
}

export default PersonCard;
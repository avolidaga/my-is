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


        </Box>

    )
}

export default PersonCard;
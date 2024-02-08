import React, {useEffect, useState} from 'react';
import {Box, Typography, Button, Dialog, DialogTitle, Divider} from '@mui/material';
import axiosApiInstance from "../../utils/tokenHelper";

interface RequestItem {
    user_request_id: number;
    user_spacesuit_data_id: {
        id: number;
        head: number;
        chest: number;
        waist: number;
        hips: number;
        foot_size: number;
        height: number;
        fabricTextureId: {
            id: number;
            name: string;
        };
    };
    status: string;
}

const RequestTabs: React.FC<{
    requestInProgress: RequestItem[];
    requestDeclinedArray: RequestItem[];
    requestAcceptedArray: RequestItem[];
    requestArray: RequestItem[];
    getInProgress: () => void;
    getReady: () => void;
    getDeclined: () => void;
    getAllRequest: () => void;
}> = ({ requestInProgress, requestDeclinedArray, requestAcceptedArray, requestArray, getInProgress, getDeclined, getReady, getAllRequest }) => {
    const [activeTab, setActiveTab] = useState<'inProgress' | 'declined' | 'ready' | 'all'>('inProgress');
    const [selectedRequest, setSelectedRequest] = useState<RequestItem | null>(null);

    const handleTabChange = (tab: 'inProgress' | 'declined' | 'ready' | 'all') => {
        setActiveTab(tab);
    };

    const handleRequestClick = (request: RequestItem) => {
        setSelectedRequest(request);
    };

    const handleCloseDialog = () => {
        setSelectedRequest(null);
    };

    const renderRequestArray = (array: RequestItem[]) => {
        return array.map((item, index) => (
            <Box
                key={index}
                sx={{
                    width: '1200px',
                    margin: '0 auto',
                    border: '1px solid #90334f',
                    padding: '10px',
                    marginBottom: '10px',
                    position: 'relative',
                    borderRadius: "10px",


                }}
            >
                <Typography>
                    User Request ID: {item.user_request_id}
                </Typography>
                <Typography>
                    Status: <span style={{ color: getStatusColor(item.status) }}>{item.status}</span>
                </Typography>
                {item.status === 'IN_PROGRESS' && (
                    <Box
                        sx={{
                            position: 'absolute',
                            top: '30px',
                            right: '10px',
                        }}
                    >
                        <Button sx={{
                            margin: '0 2px',
                            height: "40px",
                            border: '1px solid success',
                            borderRadius: "10px",
                        }} variant="outlined" color="success" onClick={() => acceptRequest(item)}>Ready</Button>
                        <Button sx={{
                            margin: '0 2px',
                            height: "40px",
                            border: '1px solid error',
                            borderRadius: "10px",
                        }} variant="outlined" color="error" onClick={() => declineRequest(item)}>Decline</Button>
                    </Box>
                )}
                <Button style={{
                    margin: "5px 0",
                    height: "40px",
                    fontSize: "18px",
                    borderRadius: "15px",
                    alignSelf: "center",
                    border: `1px solid #90334f`,
                    color: "#90334f"
                }} variant="outlined" onClick={() => handleRequestClick(item)}>View Details</Button>
            </Box>
        ));
    };

    const getStatusColor = (status: string) => {
        switch (status) {
            case 'DECLINED':
                return 'red';
            case 'READY':
                return 'green';
            case 'IN_PROGRESS':
                return '#608EDD';
            default:
                return 'black';
        }
    };

    const acceptRequest = (item: RequestItem) => {
        axiosApiInstance.post("/request/updateStatusReady", {user_spacesuit_data_id: item.user_spacesuit_data_id.id})
            .then(() => {
                getInProgress();
                getReady();
                getAllRequest()
            })
            .catch(error => {
                console.error("Error accepting request:", error);
            });
    };

    const declineRequest = (item: RequestItem) => {
        axiosApiInstance.post("/request/updateStatusDeclined", { user_spacesuit_data_id: item.user_spacesuit_data_id.id })
            .then(() => {
                getInProgress();
                getDeclined();
                getAllRequest()
            })
            .catch(error => {
                console.error("Error declining request:", error);
            });
    };
    useEffect(() => {
        getInProgress();
        getDeclined();
        getReady();
        getAllRequest()
    }, []);

    return (
        <div>
            <div>
                <Button
                    style={{
                        margin: "5px 0",
                        height: "40px",
                        fontSize: "18px",
                        borderRadius: "15px 15px 0 0 ",
                        alignSelf: "center",
                        border: `1px solid #90334f`,
                    }}
                    sx={{ backgroundColor: activeTab === 'inProgress' ? '#90334f' : '', mr: 1,
                        color: activeTab === 'inProgress' ? '#ffffff' : "#90334f",}}
                    variant="outlined"
                    onClick={() => handleTabChange('inProgress')}
                >
                    In Progress
                </Button>
                <Button
                    style={{
                        margin: "5px 0",
                        height: "40px",
                        fontSize: "18px",
                        borderRadius: "15px 15px 0 0 ",
                        alignSelf: "center",
                        border: `1px solid #90334f`,
                    }}
                    sx={{ backgroundColor: activeTab === 'declined' ? '#90334f' : '', mr: 1,
                        color: activeTab === 'declined' ? '#ffffff' : "#90334f"}}

                    variant="outlined"
                    onClick={() => handleTabChange('declined')}
                >
                    Declined
                </Button>
                <Button
                    style={{
                        margin: "5px 0",
                        height: "40px",
                        fontSize: "18px",
                        borderRadius: "15px 15px 0 0 ",
                        alignSelf: "center",
                        border: `1px solid #90334f`,
                    }}
                    sx={{ backgroundColor: activeTab === 'ready' ? '#90334f' : '', mr: 1,
                        color: activeTab === 'ready' ? '#ffffff' : "#90334f"}}

                    variant="outlined"
                    onClick={() => handleTabChange('ready')}
                >
                    Ready
                </Button>
                <Button
                    style={{
                        margin: "5px 0",
                        height: "40px",
                        fontSize: "18px",
                        borderRadius: "15px 15px 0 0 ",
                        alignSelf: "center",
                        border: `1px solid #90334f`,
                    }}
                    sx={{ backgroundColor: activeTab === 'all' ? '#90334f' : '', mr: 1,
                        color: activeTab === 'all' ? '#ffffff' : "#90334f"}}

                    variant="outlined"
                    onClick={() => handleTabChange('all')}
                >
                    All
                </Button>

                <Divider sx={{ my: 2, backgroundColor:  "#90334f"}} />
            </div>
            <div>
                {activeTab === 'inProgress' && renderRequestArray(requestInProgress)}
                {activeTab === 'declined' && renderRequestArray(requestDeclinedArray)}
                {activeTab === 'ready' && renderRequestArray(requestAcceptedArray)}
                {activeTab === 'all' && renderRequestArray(requestArray)}
            </div>


            <Dialog open={selectedRequest !== null} onClose={handleCloseDialog}>
                <DialogTitle>User Spacesuit Data</DialogTitle>
                {selectedRequest && (
                    <Box sx={{ padding: '20px' }}>
                        <Typography>ID: {selectedRequest.user_spacesuit_data_id.id}</Typography>
                        <Typography>Head: {selectedRequest.user_spacesuit_data_id.head}</Typography>
                        <Typography>Chest: {selectedRequest.user_spacesuit_data_id.chest}</Typography>
                        <Typography>Waist: {selectedRequest.user_spacesuit_data_id.waist}</Typography>
                        <Typography>Hips: {selectedRequest.user_spacesuit_data_id.hips}</Typography>
                        <Typography>Foot Size: {selectedRequest.user_spacesuit_data_id.foot_size}</Typography>
                        <Typography>Height: {selectedRequest.user_spacesuit_data_id.height}</Typography>
                    </Box>
                )}
            </Dialog>
        </div>
    );
};

export default RequestTabs;

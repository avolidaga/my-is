import {
    FETCH_CONNECTIONS_FAILURE,
    FETCH_CONNECTIONS_REQUEST,
    FETCH_CONNECTIONS_SUCCESS, FetchGetConnectionsFailureAction,
    FetchGetConnectionsRequestAction, FetchGetConnectionsSuccessAction, IUserData,
} from "./types";
import {Dispatch} from "redux";
import axiosApiInstance from "../../utils/tokenHelper";
import axios from "axios";




export const fetchConnectionsRequest = () : FetchGetConnectionsRequestAction => ({ type: FETCH_CONNECTIONS_REQUEST });
export const fetchConnectionsSuccess = (connections: IUserData[]) : FetchGetConnectionsSuccessAction=> ({
    type: FETCH_CONNECTIONS_SUCCESS,
    payload: { connections },
});
export const fetchConnectionsFailure = () : FetchGetConnectionsFailureAction=> ({ type: FETCH_CONNECTIONS_FAILURE });



export function fetchConnections(){
    return function (dispatch: Dispatch<FetchGetConnectionsRequestAction | FetchGetConnectionsSuccessAction | FetchGetConnectionsFailureAction>) {
        // dispatch<FetchGetConnectionsRequestAction>({ type: FETCH_CONNECTIONS_REQUEST });
        dispatch(fetchConnectionsRequest())
        axios.post('http://localhost:34000/api/connection/getConnections', {}, {headers: {'Authorization' : `Basic ${localStorage.getItem("accessToken")}`}})
            .then(response => {
                dispatch(fetchConnectionsSuccess(response.data))
                // dispatch<FetchGetConnectionsSuccessAction>({
                //     type: FETCH_CONNECTIONS_SUCCESS,
                //     payload: {
                //         connections: response.data, // response.data должен быть массивом IUserData
                //     },
                // });
            })
            .catch(() => {
                dispatch(fetchConnectionsFailure())
            });
    };
};
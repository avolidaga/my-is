// userReducer.ts

import {
    FETCH_CONNECTIONS_REQUEST,
    FETCH_CONNECTIONS_SUCCESS,
    FETCH_CONNECTIONS_FAILURE,
    IUserState,
    UserActionTypes,
} from './types';

const initialState: IUserState = {
    userdata: null,
    connections: null,
};

const userReducer = (state = initialState, action: UserActionTypes): IUserState => {
    switch (action.type) {
        case FETCH_CONNECTIONS_REQUEST:
            return state; // Ничего не меняем, просто указываем, что начался запрос

        case FETCH_CONNECTIONS_SUCCESS:
            return {
                ...state,
                connections: action.payload.connections,
            };

        case FETCH_CONNECTIONS_FAILURE:
            return {
                ...state,
                connections: null, // Обнуляем connections в случае ошибки
            };

        // Другие case для других действий

        default:
            return state;
    }
};

// Тип для корневого состояния Redux
export type RootStateType = ReturnType<typeof userReducer>;

export default userReducer;

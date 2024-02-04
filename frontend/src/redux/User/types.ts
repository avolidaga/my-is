// types.ts

export const FETCH_CONNECTIONS_REQUEST = 'FETCH_CONNECTIONS_REQUEST';
export const FETCH_CONNECTIONS_SUCCESS = 'FETCH_CONNECTIONS_SUCCESS';
export const FETCH_CONNECTIONS_FAILURE = 'FETCH_CONNECTIONS_FAILURE';

export interface IUser {
    userdata: IUserData | null;
    connections: IUserData[] | null
}

export interface IUserState {
    userdata: IUserData | null;        // Информация о текущем пользователе
    connections: IUserData[] | null; // Массив подключений пользователя
    // Другие свойства состояния пользователя
}


export interface IUserData {
    birth_date: Date,
    sex: string,
    weight: number,
    height: number,
    hair_color: string,
    location: string,
}

export interface IUserSpacesuitData{
    head: number,
    chest: number,
    waist: number,
    hips: number,
    foot_size: number,
    height: number,
    fabric_texture_id: number
}

// Добавим эти интерфейсы
export interface FetchGetConnectionsRequestAction {
    type: typeof FETCH_CONNECTIONS_REQUEST;
}

export interface FetchGetConnectionsSuccessAction {
    type: typeof FETCH_CONNECTIONS_SUCCESS;
    payload: {
        connections: IUserData[];
    };
}

export interface FetchGetConnectionsFailureAction {
    type: typeof FETCH_CONNECTIONS_FAILURE;
}

// Объединим все типы действий
export type UserActionTypes =
    | FetchGetConnectionsRequestAction
    | FetchGetConnectionsSuccessAction
    | FetchGetConnectionsFailureAction;

// ... Другие определения типов

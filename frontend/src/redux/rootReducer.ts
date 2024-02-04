// rootReducer.ts

import { combineReducers } from 'redux';
import userReducer from './User/reducer'; // Замените путем к вашему userReducer

const rootReducer = combineReducers({
    user: userReducer,
});

export default rootReducer;

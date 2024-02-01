export const saveTokenInLocalStorage = (token: string) => {
    localStorage.setItem("accessToken", token);
};
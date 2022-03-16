import axios from 'axios';

const HOST = process.env.REACT_APP_SERVER ?? "localhost";
const PORT = process.env.REACT_APP_SERVER_PORT ?? 8082;
const BASE_URL = `http://${HOST}:${PORT}`;

const fetchClient = () => {
    const defaultOptions = {
        baseURL: BASE_URL,
        headers: {
            'Content-Type': 'application/json',
        },
    };

    let instance = axios.create(defaultOptions);

    instance.interceptors.response.use(
        function (response) {
            return response;
        },
        function (error) {
            //Error from server resolve as normal response
            return Promise.resolve(error.response);
        },
    );

    return instance;
};

export const API = fetchClient();

export const setApiToken = (jwt: string) => {
    API.defaults.headers.common['Authorization'] = jwt ? `Bearer ${jwt}` : '';

    return API;
};


export default API;

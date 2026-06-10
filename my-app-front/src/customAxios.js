import axios from 'axios';

export default function customAxios(url, callback) {
    axios({
        url: '/api' + url,
        method: 'post',
        withCredentials: true,
    }).then(function (response) {
        callback(response.data);
    });
}
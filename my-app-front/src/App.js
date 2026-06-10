import React, { useState, useEffect } from 'react';
import './App.css';
import customAxios from './customAxios';

function App() {
    const [ip, setIp] = useState('');

    function callback(data) {
        setIp(data);
    }

    useEffect(() => {
        customAxios('/ip', callback);
    }, []);

    return (
        <div className="App">
            <header className="App-header">
                이 기기의 IP주소는 {ip}입니다.
            </header>
        </div>
    );
}

export default App;
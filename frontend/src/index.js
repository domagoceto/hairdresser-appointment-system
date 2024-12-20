import React from 'react';
import ReactDOM from 'react-dom/client'; // React 18 için uygun
import './index.css';
import App from './App';

// createRoot ile root öğesini oluşturun
const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);

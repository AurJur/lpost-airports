import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import CsvUploadComponent from './components/CsvUploadComponent';
import TravelComponent from './components/TravelComponent';
import AirportDetails from './components/AirportDetails';
import HomePage from './components/HomePage';

ReactDOM.render(
    <React.StrictMode>
        <Router>
            <Routes>
                <Route path="/" element={<HomePage/>}/>
                <Route path="/import-airports" element={<CsvUploadComponent labelText="Import airports (.csv)"
                                                                            endpoint="airport/import-csv"/>}/>
                <Route path="/import-countries" element={<CsvUploadComponent labelText="Import countries (.csv)"
                                                                             endpoint="country/import-csv"/>}/>
                <Route path="/import-regions"
                       element={<CsvUploadComponent labelText="Import regions (.csv)" endpoint="region/import-csv"/>}/>
                <Route path="/travel" element={<TravelComponent/>}/>
                <Route path="/airport-details/:id" element={<AirportDetails/>}/>
            </Routes>
        </Router>
    </React.StrictMode>,
    document.getElementById('root')
);

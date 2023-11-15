import React, {useState} from 'react';
import {Link, Route, Routes} from 'react-router-dom';
import Container from '@mui/material/Container';
import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import CircularProgress from '@mui/material/CircularProgress';
import AirplanemodeActiveIcon from '@mui/icons-material/AirplanemodeActive';
import {styled} from '@mui/system';
import AirportDetails from './AirportDetails';
import HomeLink from './HomeLink';
import {REACT_APP_API_HOST} from '../config';

const TravelComponent = () => {
    const classes = styled({link: {textDecoration: 'underline', color: 'blue', cursor: 'pointer',},});
    const [countries, setCountries] = useState([]);
    const [isListFetched, setListFetched] = useState(false);
    const [selectedCountry, setSelectedCountry] = useState(null);
    const [regions, setRegions] = useState([]);
    const [selectedRegion, setSelectedRegion] = useState(null);
    const [airports, setAirports] = useState([]);
    const [selectedAirport, setSelectedAirport] = useState(null);
    const [isLoading, setLoading] = useState(false);

    const fetchCountries = () => {
        setLoading(true);
        fetch(`${REACT_APP_API_HOST}/api/country/get-all`)
            .then(response => response.json())
            .then(data => {
                console.log('Received data:', data);

                setCountries(data);
                setListFetched(true);
            })
            .catch(error => {
                console.error('Error fetching countries:', error);
            })
            .finally(() => setLoading(false));
    };

    const fetchRegions = (countryCode) => {
        setLoading(true);
        fetch(`${REACT_APP_API_HOST}/api/region/get-all-by-country?countryCode=${countryCode}`)
            .then(response => response.json())
            .then(data => {
                console.log('Received regions:', data);

                setRegions(data);
            })
            .catch(error => {
                console.error('Error fetching regions:', error);
            })
            .finally(() => setLoading(false));
    };

    const fetchAirports = (regionCode) => {
        setLoading(true);
        fetch(`${REACT_APP_API_HOST}/api/airport/get-info-by-region?regionCode=${regionCode}`)
            .then(response => response.json())
            .then(data => {
                console.log('Received airports:', data);

                setAirports(data);
            })
            .catch(error => {
                console.error('Error fetching airports:', error);
            })
            .finally(() => setLoading(false));
    };

    const handleCountryChange = (event, newValue) => {
        setLoading(true);
        setSelectedCountry(newValue);

        if (newValue) {
            fetchRegions(newValue.code);
        }
    };

    const handleRegionChange = (event, newValue) => {
        setLoading(true);
        setSelectedRegion(newValue);

        if (newValue) {
            fetchAirports(newValue.code);
        }
    };

    const handleAirportChange = (event, newValue) => {
        setSelectedAirport(newValue);
    };

    const handleButtonClick = () => {
        if (!isListFetched) {
            fetchCountries();
        }
    };

    return (
        <div className="App-common">
            <Container style={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                <HomeLink/>
                <Button variant="contained" color="primary" onClick={handleButtonClick}
                        startIcon={<AirplanemodeActiveIcon/>}>
                    Click for Prices!
                </Button>
                {isLoading && <CircularProgress style={{marginTop: '10px'}}/>}
                {isListFetched && (
                    <div>
                        <Autocomplete
                            options={countries}
                            getOptionLabel={option => option.name}
                            style={{width: 300, marginTop: '10px'}}
                            renderInput={params => <TextField {...params} label="Select a Country"/>}
                            onChange={handleCountryChange}
                        />
                        {selectedCountry && (
                            <Autocomplete
                                options={regions}
                                getOptionLabel={option => option.name}
                                style={{width: 300, marginTop: '10px'}}
                                renderInput={params => <TextField {...params} label="Select a Region"/>}
                                onChange={handleRegionChange}
                            />
                        )}
                        {selectedRegion && (
                            <Autocomplete
                                options={airports}
                                getOptionLabel={(option) => `${option.name} - ${option.municipality} - Lowest price: ${option.lowestPrice}`}
                                style={{width: 300, marginTop: '10px'}}
                                renderInput={params => <TextField {...params} label="Select an Airport"/>}
                                onChange={handleAirportChange}
                            />
                        )}
                    </div>
                )}
                {selectedAirport && (
                    <Link to={`/airport-details/${selectedAirport.id}`} className={classes.link}
                          style={{fontSize: '0.6em', marginTop: '30px'}}>
                        Selected Airport: {selectedAirport.name} - Municipality: {selectedAirport.municipality} -
                        Lowest price: {selectedAirport.lowestPrice}
                    </Link>
                )}
                <Routes>
                    <Route path="/airport/:id" element={<AirportDetails selectedAirport={selectedAirport}/>}/>
                </Routes>
                <hr style={{width: '85%', margin: '20px 0', backgroundColor: 'white'}}/>
            </Container>
        </div>
    );
};

export default TravelComponent;

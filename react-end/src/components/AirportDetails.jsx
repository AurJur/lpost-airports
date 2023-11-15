import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import CircularProgress from '@mui/material/CircularProgress';
import HomeLink from './HomeLink';
import {REACT_APP_API_HOST} from '../config';

const AirportDetails = () => {
    const {id} = useParams();
    const [airportDetails, setAirportDetails] = useState(null);
    const renderValue = (label, value) => (
        <Typography key={label}>
            {label}: {value || 'not available'}
        </Typography>
    );

    useEffect(() => {
        const fetchAirportDetails = async () => {
            try {
                const response = await fetch(`${REACT_APP_API_HOST}/api/airport/get-details/${id}`);
                const data = await response.json();
                setAirportDetails(data);
            } catch (error) {
                console.error('Error fetching airport details:', error);
            }
        };

        fetchAirportDetails();
    }, [id]);

    return (
        <div className="App-common">
            <Container>
                <HomeLink/>
                {airportDetails ? (
                    <div style={{textAlign: 'center', marginTop: '20px'}}>
                        <Typography variant="h4" component="h2" gutterBottom>
                            {airportDetails.name}
                        </Typography>
                        {renderValue('ID', airportDetails.id)}
                        {renderValue('Type', airportDetails.type)}
                        {renderValue('Location', `${airportDetails.latitudeDeg}, ${airportDetails.longitudeDeg}`)}
                        {renderValue('Elevation', `${airportDetails.elevationFt} ft`)}
                        {renderValue('Continent', airportDetails.continent)}
                        {renderValue('ISO Country', airportDetails.isoCountry)}
                        {renderValue('ISO Region', airportDetails.isoRegion)}
                        {renderValue('Municipality', airportDetails.municipality)}
                        {renderValue('Scheduled Service', airportDetails.scheduledService)}
                        {renderValue('GPS Code', airportDetails.gpsCode)}
                        {renderValue('IATA Code', airportDetails.iataCode)}
                        {renderValue('Local Code', airportDetails.localCode)}
                        {Object.entries(airportDetails.prices).map(([provider, price]) => (
                            <Typography key={provider} variant="body2" style={{fontSize: '0.7em'}}>
                                {provider} Price: {price}
                            </Typography>
                        ))}
                    </div>
                ) : (
                    <CircularProgress style={{marginTop: '20px'}}/>
                )}
            </Container>
        </div>
    );
};

export default AirportDetails;

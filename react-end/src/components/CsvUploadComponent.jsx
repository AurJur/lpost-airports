import React, {useState} from 'react';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import CircularProgress from '@mui/material/CircularProgress';
import '../App.css';
import HomeLink from './HomeLink';
import {REACT_APP_API_HOST} from '../config';

const CsvUploadComponent = ({labelText, endpoint}) => {
    const [uploadStatus, setUploadStatus] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleUpload = async (file) => {
        const formData = new FormData();
        formData.append('file', file);

        try {
            setLoading(true);

            const response = await fetch(`${REACT_APP_API_HOST}/api/${endpoint}`, {
                method: 'POST',
                body: formData,
            });

            const result = await response.json();
            console.log('Upload result:', result);

            if (response.status === 200) {
                setUploadStatus({
                    type: 'success',
                    message: `${result.text}`,
                });
            } else {
                setUploadStatus({
                    type: 'error',
                    message: `${result.text}`,
                });
            }
        } catch (error) {
            console.error('Error uploading file:', error);
            setUploadStatus({
                type: 'error',
                message: `Error uploading file "${file.name}". Please try again.`,
            });
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="App-common">
            <Container>
                <HomeLink/>
                <Typography variant="h4" component="h2" gutterBottom>
                    {labelText}
                </Typography>
                <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
                    <label htmlFor={`fileInput-${endpoint}`}>
                        <Button variant="contained" component="span" startIcon={<CloudUploadIcon/>}>
                            Import
                        </Button>
                        <input
                            type="file"
                            onChange={(e) => {
                                const file = e.target.files[0];
                                if (file) {
                                    handleUpload(file);
                                }
                            }}
                            accept=".csv"
                            style={{display: 'none'}}
                            id={`fileInput-${endpoint}`}
                        />
                    </label>
                    {loading && <CircularProgress style={{marginTop: '10px'}}/>}
                </div>
                {uploadStatus && (
                    <Typography
                        variant="body1"
                        style={{
                            marginTop: '10px',
                            color: uploadStatus.type === 'success' ? 'green' : 'red',
                        }}
                    >
                        {uploadStatus.message}
                    </Typography>
                )}
                <hr style={{width: '85%', margin: '20px auto', backgroundColor: 'white'}}/>
            </Container>
        </div>
    );
};

export default CsvUploadComponent;

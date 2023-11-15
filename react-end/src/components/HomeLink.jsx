import React from 'react';
import {Link} from 'react-router-dom';

const HomeLink = () => {
    return (
        <Link to="/" style={{
            textDecoration: 'underline',
            color: 'blue',
            cursor: 'pointer',
            fontFamily: 'inherit',
            fontSize: '0.6em',
            display: 'inline-block',
            marginBottom: '20px',
        }}>
            Go to Home
        </Link>
    );
};

export default HomeLink;

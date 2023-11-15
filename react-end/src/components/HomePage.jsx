import React from 'react';
import {Link} from 'react-router-dom';
import {styled} from '@mui/system';

const Root = styled('div')({
    textAlign: 'center',
    padding: (theme) => theme.spacing(2),
});

const Title = styled('h1')({
    fontSize: '2rem',
    marginBottom: (theme) => theme.spacing(2),
});

const Nav = styled('nav')({
    listStyle: 'none',
    padding: 0,
    margin: 0,
    '& li': {
        margin: (theme) => theme.spacing(1),
        '& a': {
            textDecoration: 'none',
            color: (theme) => theme.palette.primary.main,
            fontSize: '1.2rem',
        },
    },
});

const HomePage = () => {
    return (
        <Root>
            <Title>Welcome to the Airports App</Title>
            <Nav>
                <ul>
                    <h3>
                        <Link to="/import-airports">Import Airports</Link>
                    </h3>
                    <h3>
                        <Link to="/import-countries">Import Countries</Link>
                    </h3>
                    <h3>
                        <Link to="/import-regions">Import Regions</Link>
                    </h3>
                    <h3>
                        <Link to="/travel">Search Airports and Prices</Link>
                    </h3>
                </ul>
            </Nav>
        </Root>
    );
};

export default HomePage;

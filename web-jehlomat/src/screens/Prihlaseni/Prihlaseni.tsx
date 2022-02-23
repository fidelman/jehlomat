import { FC, useContext } from 'react';
import { Box } from '@mui/material';
import Grid from '@mui/material/Grid';
import TitleBar from '../../Components/Navigation/TitleBar';
import { useHistory } from 'react-router-dom';

import { Container } from '@mui/material';
import { white } from '../../utils/colors';
import { ChevronLeft } from '@mui/icons-material';
import LoginForm from '../../Components/LoginForm/LoginForm';
import { LINKS } from 'routes';
import Link from 'Components/Link';
import { WithModal } from 'types';

const Login: FC<WithModal> = ({ inModal }) => {
    let history = useHistory();

    const renderContent = (children: React.ReactNode) => {
        if (!inModal) {
            return (
                <Container sx={{ height: '100vh', width: '100%' }}>
                    <Grid container justifyContent="start" sx={{ height: '100%', width: '100%' }}>
                        <Box marginBottom={'85px'}>
                            <TitleBar
                                icon={<ChevronLeft sx={{ color: white, fontSize: 40 }} />}
                                onIconClick={() => {
                                    history.goBack();
                                }}
                            />
                        </Box>
                        {children}
                    </Grid>
                </Container>
            );
        } else {
            return <>{children}</>;
        }
    };

    return renderContent(
        <Grid container direction="column" sx={{ height: 'auto', width: '100%', paddingX: '20px' }} justifyContent="start" alignItems="center">
            <LoginForm />
                <div style={{marginTop: 30}}>
                    <Link to={LINKS.FORGOTTEN_PASSWORD}> Zapomněli jste heslo? </Link>
                </div>
        </Grid>
    );
};

export default Login;

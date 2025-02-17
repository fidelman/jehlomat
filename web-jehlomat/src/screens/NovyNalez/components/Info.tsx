import { faCheck, faEdit, faMap } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { FC } from 'react';
import styled from '@emotion/styled';
import PrimaryButton from '../../../Components/Buttons/PrimaryButton/PrimaryButton';
import Navigation from '../../../Components/Navigation/Navigation';
import { primaryDark, white } from '../../../utils/colors';
import { Header } from '../../../Components/Header/Header';
import { STEPS } from '../NovyNalez';
import whiteArrow from '../../../../src/assets/images/white-arrow.png';
import Box from '@mui/material/Box';
import { useMediaQuery } from '@mui/material';
import { media } from '../../../utils/media';

interface iInfo {
    handleStepChange: (newStep: STEPS) => void;
}

const Container = styled.div`
    display: flex;
    position: relative;
    flex-direction: column;
    height: 100%;
    @media (min-width: 700px) {
        flex-direction: row;
    }
`;
interface iCard {
    backgroundColor: string;
}

const Card = styled.div<iCard>`
    display: flex;
    position: relative;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background: ${props => props.backgroundColor};
    padding: 1rem 0rem;
    flex-grow: 1;
    @media (min-width: 700px) {
        padding: 1rem 0 0.5rem;
        &:last-child {
            display: none;
            background-color: red;
        }
    }
`;

const Icon = styled.div`
    background-color: ${white};
    margin: auto;
    border-radius: 50%;
    border: 3px solid ${primaryDark};
    width: 56px;
    height: 56px;
    display: flex;
    justify-content: center;
    align-items: center;
    @media (min-width: 700px) {
    }
`;

const Title = styled.h6`
    text-align: center;
    font-size: 16px;
    line-height: 18.75px;
    color: ${primaryDark};
    margin: 0.5rem 0;
`;

const MutedText = styled.p`
    text-align: center;
    color: #898a8d;
    font-size: 14px;
    font-weight: 400;
    line-height: 16.4px;
    margin: 0 0 25px;
    @media (min-width: 700px) {
        display: none;
    }
`;

const Arrow = styled.img`
    width: 20px;
    height: 20px;
    margin: 15px;
    position: absolute;
    top: 140px;
    z-index: 2;
    @media (min-width: 700px) {
        display: none;
    }
`;

const Info: FC<iInfo> = ({ handleStepChange }) => {
    const isMobile = useMediaQuery(media.lte('mobile'));
    return (
        <>
            <Header mobileTitle="Zadávaní nálezu" />
            <Box minHeight={'100vh'}>
                <Container>
                    <Card backgroundColor="#BFE3E0">
                        <Icon>
                            <FontAwesomeIcon icon={faMap} size="2x" color={primaryDark} />
                        </Icon>
                        <Title>Přidat do mapy</Title>
                        <MutedText>Nejprve označte místo nálezu do mapy</MutedText>
                        <Arrow src={whiteArrow}></Arrow>
                    </Card>
                    <Card backgroundColor="#CDEAE7">
                        <Icon>
                            <FontAwesomeIcon icon={faEdit} size="2x" color={primaryDark} />
                        </Icon>
                        <Title>Přidat podrobnosti do mapy</Title>
                        <MutedText>Poté vložte podrobnosti o nálezu a jeho fotografii.</MutedText>
                        <Arrow src={whiteArrow}></Arrow>
                    </Card>
                    <Card backgroundColor="#DEF1EF">
                        <Icon>
                            <FontAwesomeIcon icon={faCheck} size="2x" color={primaryDark} />
                        </Icon>
                        <Title>Úspěšné vložení nálezu</Title>
                        <MutedText>Nález bude profesionálně zlikvidován</MutedText>
                    </Card>
                    {isMobile && (
                        <Card backgroundColor="#EEF8F7">
                            <PrimaryButton text="Zadat nález do mapy" onClick={() => handleStepChange(STEPS.Mapa)} />
                        </Card>
                    )}

                    <Navigation></Navigation>
                </Container>
            </Box>
        </>
    );
};

export default Info;

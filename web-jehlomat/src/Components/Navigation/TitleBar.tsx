import { FC, MouseEventHandler } from 'react';
import * as s from './TitleBarStyles';
import { NavigationTitle } from '../../utils/typography';
import { IconButton } from '@mui/material';

interface ITitleBar {
    icon?: any | undefined;
    onIconClick?: MouseEventHandler | undefined;
}

const TitleBar: FC<ITitleBar> = ({ icon, children, onIconClick }) => {
    return (
        <s.Container>
            {icon && (
                <s.NavIcon onClick={onIconClick}>
                    <IconButton aria-label="Zpět">{icon}</IconButton>
                </s.NavIcon>
            )}
            <s.Content isCentered={!icon}>
                <NavigationTitle>{children}</NavigationTitle>
            </s.Content>
        </s.Container>
    );
};

export default TitleBar;

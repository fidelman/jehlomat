import styled from '@emotion/styled';
// import { media } from '../../utils/media';
// import { size } from '../../utils/spacing';
import { 
    fontFamilyRoboto, 
    fontWeight500,
    fontWeightBold
} from '../../utils/typography';
import { 
    // primary, 
    primaryDark, 
    primaryLight 
} from '../../utils/colors';

export const TextHeader = styled.h1`
    ${fontFamilyRoboto};
    color: ${primaryDark};
    font-style: normal;
    ${fontWeight500};
    font-size: 20px;
    line-height: 16px;
    letter-spacing: 1.25px;
    text-transform: uppercase;
`;

export const FilterLink = styled.a`
    ${fontFamilyRoboto};
    font-style: normal;
    ${fontWeight500};
    font-size: 20px;
    line-height: 16px;
    text-align: right;
    letter-spacing: 1.25px;
    text-decoration-line: underline;
    text-decoration-thickness: 1px;
    text-underline-offset: 2px;
    color: ${primaryDark};
    margin-left: 24px;
    &:hover {
        color: ${primaryLight};
    }
`

export const ListWrapper = styled.table`
    background: rgba(47, 166, 154, 0.4);
    border-radius: 8px 8px 0px 0px;
    padding: 20px 43px;
    min-height: 100vh;
    width: 100%;
`

export const ListHeader = styled.tr`
    witdh: 100%;
`

export const ListHeaderItem = styled.th`
    ${fontFamilyRoboto};
    font-style: normal;
    ${fontWeightBold};
    font-size: 14px;
    line-height: 24px;
    letter-spacing: 0.15px;
    color: #808285;
`
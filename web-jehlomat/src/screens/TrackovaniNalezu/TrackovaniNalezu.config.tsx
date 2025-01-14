import React from 'react';

export enum STEPS {
    ZadatKod,
    ZobraitStav,
}

export enum SyringeStateType {
    DESTROYED = 'DESTROYED',
    RESERVED = 'RESERVED',
    ANNOUNCED = 'ANNOUNCED',
    NOTFOUND = 'NOTFOUND',
}

export interface ISyringeState {
    hasCheckMark: boolean;
    firstLine: string;
    secondLine: string | React.ReactNode;
}

export type syringeStateTypes = {
    [key in SyringeStateType]: ISyringeState;
};

export const syringeStates: syringeStateTypes = {
    [SyringeStateType.DESTROYED]: {
        hasCheckMark: true,
        firstLine: 'nález byl úspěšně',
        secondLine: 'ZLIKVIDOVÁN',
    },
    [SyringeStateType.RESERVED]: {
        hasCheckMark: false,
        firstLine: 'pracujeme na tom, nález je',
        secondLine: (
            <>
                REZEROVAVNÝ
                <br /> k likvidaci
            </>
        ),
    },
    [SyringeStateType.ANNOUNCED]: {
        hasCheckMark: true,
        firstLine: 'nález je',
        secondLine: (
            <>
                NAHLÁŠENÝ
                <br /> na městskou policii
            </>
        ),
    },
    [SyringeStateType.NOTFOUND]: {
        hasCheckMark: false,
        firstLine: 'jehla',
        secondLine: 'NEBYLA nalezena',
    },
};

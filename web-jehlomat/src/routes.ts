import Layout from './Components/Layout/Layout';
import { lazy, ComponentType, LazyExoticComponent } from 'react';

const Prihlaseni = lazy(() => import('./screens/Prihlaseni/Prihlaseni'));
const Welcome = lazy(() => import('./screens/Prihlaseni/Welcome')); // Temp welcome
const Profil = lazy(() => import('./screens/Profil/Profil'));
const NovyNalez = lazy(() => import('./screens/NovyNalez/NovyNalez'));
const Organizace = lazy(() => import('./screens/Organizace/Organizace'));
const Nalezy = lazy(() => import('./screens/Nalezy/Nalezy'));
const DekujemeOrganizace = lazy(() => import('./screens/RegistraceOrganizace/Dekujeme'));
const RegistraceOrganizace = lazy(() => import('./screens/RegistraceOrganizace/RegistraceOrganizace'));
const RegistraceUzivatele = lazy(() => import('./screens/RegistraceUzivatele/RegistraceUzivatele'));
const OvereniEmailu = lazy(() => import('./screens/RegistraceUzivatele/OvereniEmailu'));

const DekujemeUzivatel = lazy(() => import('./screens/RegistraceUzivatele/Dekujeme'));
const SeznamUzivatelu = lazy(() => import('./screens/SeznamUzivatelu/SeznamUzivatelu'));
const PridatUzivatele = lazy(() => import('./screens/RegistraceUzivatele/PridatUzivatele'));
const ErrorPage = lazy(() => import('./screens/ErrorPage/ErrorPage'));
const TrackovaniNalezu = lazy(() => import('./screens/TrackovaniNalezu/TrackovaniNalezu'));
const LandingPage = lazy(() => import('./screens/LandingPage'));
const NavodLikvidace = lazy(() => import('./screens/NavodLikvidace/NavodLikvidace'));
const About = lazy(() => import('./screens/AboutPage'));

export enum Routes {
    HOME = 'HOME',
    LOGIN = 'LOGIN',
    USER = 'USER',
    USER_NEW = 'USER_NEW',
    USER_VALIDATION = 'USER_VALIDATION',
    USER_REGISTRATION = 'USER_REGISTRATION',
    USER_THANK_YOU = 'USER_THANK_YOU',
    ORGANIZATION = 'ORGANIZATION',
    ORGANIZATION_REGISTRATION = 'ORGANIZATION_REGISTRATION',
    ORGANIZATION_THANK_YOU = 'ORGANIZATION_THANK_YOU',
    PROFILE = 'PROFILE',
    NEW_FIND = 'NEW_FIND',
    FINDINGS = 'FINDINGS',
    ERROR = 'ERROR',
    TRACKING_FIND = 'TRACKING_FIND',
    WELCOME = 'WELCOME',
    DISPOSAL_INSTRUCTIONS = 'DISPOSAL_INSTRUCTIONS',
    POLICE_ASSISTANCE = 'POLICE_ASSISTANCE',
    FORGOTTEN_PASSWORD = 'FORGOTTEN_PASSWORD',
    ABOUT = 'ABOUT',
}

interface Route {
    id: Routes;
    Component: LazyExoticComponent<any>;
    AdditionalComponents?: ComponentType;
    path: string | Function;
    protectedRoute?: boolean;
    exact?: boolean;
    title?: string;
}

const USER_URL_PATH_ = 'uzivatel';
const ORGANIZATION_URL_PATH = 'organizace';
const FINDINGS_URL_PATH = 'nalezy';

export const routes: Route[] = [
    {
        id: Routes.LOGIN,
        Component: Prihlaseni,
        path: '/prihlaseni',
    },
    {
        id: Routes.USER_NEW,
        Component: PridatUzivatele,
        path: `/${USER_URL_PATH_}/novy`,
        protectedRoute: true,
    },
    {
        id: Routes.USER_VALIDATION,
        Component: OvereniEmailu,
        path: `/${USER_URL_PATH_}/validace`,
    },
    {
        id: Routes.USER_REGISTRATION,
        Component: RegistraceUzivatele,
        path: `/${USER_URL_PATH_}/registrace`,
    },
    {
        id: Routes.USER_THANK_YOU,
        Component: DekujemeUzivatel,
        path: `/${USER_URL_PATH_}/dekujeme`,
    },
    {
        id: Routes.USER,
        Component: SeznamUzivatelu,
        path: `/${USER_URL_PATH_}`,
        protectedRoute: true,
    },
    {
        id: Routes.ORGANIZATION_REGISTRATION,
        Component: RegistraceOrganizace,
        path: `/${ORGANIZATION_URL_PATH}/registrace`,
    },
    {
        id: Routes.ORGANIZATION_THANK_YOU,
        Component: DekujemeOrganizace,
        path: `/${ORGANIZATION_URL_PATH}/dekujeme`,
    },
    {
        id: Routes.ORGANIZATION,
        Component: Organizace,
        path: `/${ORGANIZATION_URL_PATH}/`,
    },
    {
        id: Routes.PROFILE,
        Component: Profil,
        path: '/profil',
    },
    {
        id: Routes.NEW_FIND,
        Component: NovyNalez,
        path: (step?: 0 | 1 | 2 | 3 | 4) => `/${FINDINGS_URL_PATH}/novy-nalez${step ? `?step=${step}` : ''}`,
        AdditionalComponents: Layout,
    },
    {
        id: Routes.TRACKING_FIND,
        Component: TrackovaniNalezu,
        path: `/${FINDINGS_URL_PATH}/trackovani-nalezu`,
        AdditionalComponents: Layout,
    },
    {
        id: Routes.FINDINGS,
        Component: Nalezy,
        path: `/${FINDINGS_URL_PATH}/`,
        AdditionalComponents: Layout,
    },
    {
        id: Routes.ERROR,
        Component: ErrorPage,
        path: '/error',
    },
    {
        id: Routes.DISPOSAL_INSTRUCTIONS,
        Component: NavodLikvidace,
        path: '/navod-likvidace',
    },
    {
        id: Routes.POLICE_ASSISTANCE,
        Component: LandingPage,
        path: '/ohlasit-nalez-policii',
    },
    {
        id: Routes.FORGOTTEN_PASSWORD,
        Component: LandingPage,
        path: '/zapomenute-heslo',
    },
    {
        id: Routes.USER_THANK_YOU,
        Component: Welcome,
        path: '/vitejte',
    },
    {
        id: Routes.ABOUT,
        Component: About,
        path: '/about',
    },
    {
        id: Routes.HOME,
        Component: LandingPage,
        path: '/',
    },
];

export type Links = {
    [key in keyof typeof Routes]: Route['path'];
};

export const routesById = routes.reduce<Partial<Record<Routes, Route>>>((obj, route) => {
    obj[route.id] = route;
    return obj;
}, {});

export const LINKS = Object.values(Routes).reduce<Record<Routes, string>>((obj, key) => {
    const route = routesById[key];
    if (route) {
        obj[key] = typeof route?.path == 'string' ? route?.path : '';
    } else {
        obj[key] = '';
    }
    return obj;
}, {} as Record<Routes, string>);

export const LINKS_WITH_PARAMS = routes.reduce<Partial<Record<Routes, Function>>>((obj, { id, path }) => {
    if (typeof path === 'function') obj[id] = path;
    return obj;
}, {});

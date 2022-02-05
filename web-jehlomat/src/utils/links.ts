export const LINKS = {
    home: '/',
    login: '/prihlaseni',
    user: '/uzivatel',
    userNew: '/uzivatel/novy',
    userValidation: '/uzivatel/validace',
    userRegistration: '/uzivatel/registrace',
    userThankYou: '/uzivatel/dekujeme',
    organization: '/organizace',
    organizationRegistration: '/organizace/registrace',
    organizationThankYou: '/organizace/dekujeme',
    profile: '/profil',
    newFind: (step?: 0 | 1 | 2 | 3 | 4) => `/novy-nalez${step ? `?step=${step}` : ''}`,
    findings: '/nalezy',
    error: '/error',
    trackingFind: '/nalezy/trackovani-nalezu',
    disposalInstructions: '/navod-likvidace',
    policeAssistance: '/ohlasit-nalez-policii',
    forgottenPassword: '/zapomenute-heslo',
    welcome: '/vitejte',
    about: '/about',
} as const;

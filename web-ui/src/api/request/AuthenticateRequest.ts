export type AuthenticateRequest = {
    email: string;
    password: string;
};

export type RecoveryPasswordRequest = {
    email: string;
};

export type AuthenticationWithCodeRequest = {
    validationCode: string;
};

export type ChangePasswordAndLoginWithCode = {
    validationCode: string;
    newPassword: string;
}
export type TenantPrivilege = {
    privilegeName: string;
    privilegeSystemName: string;
};

export type TenantDetails = {
    tenantName: string;
    tenantId:string;
    isDefault:boolean;
    tenantPrivilegeList: TenantPrivilege[];
};

export type AuthenticateResponse = {
    refreshToken:string
    token: string
    tenantDetailsList: TenantDetails[]
};

export type RecoverPasswordResponse = {
    message:string
};
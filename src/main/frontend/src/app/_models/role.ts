export enum Role {
    Administrator = 'ROLE_ADMIN',
    Editor = 'ROLE_USER',
    Viewer = 'ROLE_VIEWER'
}

export const reverseRole = new Map<string, Role>();
Object.keys(Role).forEach((role: Role) => {
    const roleValue: string = Role[<any>role];
    reverseRole.set(roleValue, role);
});

export const rolesArray = Object.keys(Role).map(key => ({ roleValue: Role[key], roleName: key }));
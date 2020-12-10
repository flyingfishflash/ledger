export enum BasicAuthUserRole {
  Administrator = "ROLE_ADMIN",
  Editor = "ROLE_USER",
  Viewer = "ROLE_VIEWER",
}

export const reverseRole = new Map<string, BasicAuthUserRole>();
Object.keys(BasicAuthUserRole).forEach((role: BasicAuthUserRole) => {
  const roleValue: string = BasicAuthUserRole[<any>role];
  reverseRole.set(roleValue, role);
});

export const rolesArray = Object.keys(BasicAuthUserRole).map((key) => ({
  roleValue: BasicAuthUserRole[key],
  roleName: key,
}));

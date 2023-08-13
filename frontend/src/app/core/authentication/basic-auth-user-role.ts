export enum BasicAuthUserRole {
  administrator = 'ROLE_ADMIN',
  editor = 'ROLE_USER',
  viewer = 'ROLE_VIEWER',
}

export const reverseRole = new Map<string, BasicAuthUserRole>()
Object.keys(BasicAuthUserRole).forEach((role: BasicAuthUserRole) => {
  const roleValue: string = BasicAuthUserRole[role] as any
  reverseRole.set(roleValue, role)
})

export const rolesArray = Object.keys(BasicAuthUserRole).map((key) => ({
  roleValue: BasicAuthUserRole[key],
  roleName: key,
}))

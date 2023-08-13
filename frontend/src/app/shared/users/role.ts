export enum Role {
  administrator = 'ROLE_ADMIN',
  editor = 'ROLE_USER',
  viewer = 'ROLE_VIEWER',
}

export const reverseRole = new Map<string, Role>()
Object.keys(Role).forEach((role: Role) => {
  const roleValue: string = Role[role] as any
  reverseRole.set(roleValue, role)
})

export const rolesArray = Object.keys(Role).map((key) => ({
  roleValue: Role[key],
  roleName: key,
}))

export enum Role {
  administrator = 'ROLE_ADMIN',
  editor = 'ROLE_USER',
  viewer = 'ROLE_VIEWER',
}

export const reverseRole = new Map<string, string>()

Object.keys(Role).forEach((key) => {
  const roleValue: string = Role[key as keyof typeof Role] as string
  reverseRole.set(roleValue, key)
})

export const rolesArray = Object.keys(Role).map((key) => ({
  roleValue: Role[key as keyof typeof Role],
  roleName: key,
}))

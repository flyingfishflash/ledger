export enum BasicAuthUserRole {
  administrator = 'ROLE_ADMIN',
  editor = 'ROLE_USER',
  viewer = 'ROLE_VIEWER',
}

export const reverseRole = new Map<string, string>()

Object.keys(BasicAuthUserRole).forEach((key) => {
  const roleValue: string = BasicAuthUserRole[
    key as keyof typeof BasicAuthUserRole
  ] as string
  reverseRole.set(roleValue, key)
})

export const rolesArray = Object.keys(BasicAuthUserRole).map((key) => ({
  roleValue: BasicAuthUserRole[key as keyof typeof BasicAuthUserRole],
  roleName: key,
}))

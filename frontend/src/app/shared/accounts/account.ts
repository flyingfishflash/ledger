export interface IAccount {
  id: number
  guid: string
  name: string
  longName: string
  code: string
  description: string
  note: string
  placeholder: boolean
  hidden: boolean
  taxRelated: boolean
  category: string
  type: string
  normalBalance: string
  currency: string
  // TODO: need to create a commodity class
  // commodity: customCommodityType;
  treeLeft: number
  treeRight: number
  treeLevel: number
  parentId: number
  discriminator: string
  rootNode: boolean
}

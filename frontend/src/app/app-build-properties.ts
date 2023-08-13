/**
 * Type describing the application build properties.
 */
export interface BuildProperties {
  artifact: string
  ciPipelineId: string
  ciPlatform: string
  commit: string
  group: string
  name: string
  time: string
  version: string
}

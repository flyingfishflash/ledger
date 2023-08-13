export const environment = {
  production: true,
  api: {
    server: {
      // eslint-disable-next-line @typescript-eslint/dot-notation
      url: window['env']['apiServerUrl'] || 'http://localhost:8181/api/v1',
    },
  },
}

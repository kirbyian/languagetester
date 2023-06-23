
export const oktaConfig = {
  clientId: process.env.REACT_APP_OKTA_CLIENT_ID,
  issuer: process.env.REACT_APP_OKTA_ISSUER,
  redirectUri: process.env.REACT_APP_OKTA_REDIRECT_URI,
  scopes: ["openid", "profile", "email"],
  pkce: true,
  disableHttpsCheck: true,
  useClassicEngine: true,
};

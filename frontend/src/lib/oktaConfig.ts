export const oktaConfig = {
  clientId: "0oa8souevqBftLBvz5d7",
  issuer: "https://dev-92011066.okta.com/oauth2/default",
  redirectUri: "http://localhost:3000/login/callback",
  scopes: ["openid", "profile", "email"],
  pkce: true,
  disableHttpsCheck: true,
  useClassicEngine: true,
};

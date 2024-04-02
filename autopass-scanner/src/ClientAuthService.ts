import { Configuration, ConfigurationParameters } from "./Service";
import * as API from "./Service/api";

class AuthenticationService {
  authApi = new API.AuthenticationApi();

  public async login(
    loginDTO: API.SignInDTO
  ): Promise<boolean> {
    console.log("logging in in service");

    // login
    let statusCode = 0;
    await this.authApi
      .authenticate(loginDTO)
      .then((res) => {
        this.setAuthenticationResponseInMemory(res.data);
        statusCode = res.status;
      })
      .catch(() => {
        return false;
      });

    return statusCode === 200;
  }

  public async signup(signUpData: API.SignUpDTO): Promise<boolean> {
    let statusCode = 0;
    await this.authApi
      .register(signUpData)
      .then((res) => {
        this.setAuthenticationResponseInMemory(res.data);
        statusCode = res.status;
      })
      .catch(() => {
        return false;
      });
    return statusCode === 200;
  }

  public logout = () => {
    this.authApi.logout(this.getUserId());
    localStorage.clear();
    sessionStorage.clear();

    document.cookie.split(";").forEach((cookie) => {
      document.cookie = cookie
        .replace(/^ +/, "")
        .replace(/=.*/, `=;expires=${new Date().toUTCString()};path=/`);
    });
  };

  // This method will use authentication api to check if the access token in local and session storage are valid.
  // This method will then be called when routing. Each new route a user takes will trigger this function to handle authentication between pages.
  // When an access token is invalid, this method will handle refreshing it
  public async isUserLoggedIn(): Promise<boolean> {
    const config = this.getApiConfig();
    const authenticatedAuthApi = new API.AuthenticationApi(config);
    const accessToken = this.getAccessTokenOrDefault();

    const refreshTokenDTO: API.RefreshTokenDTO = {
      refreshToken: this.getRefreshTokenOrDefault(),
    };

    const loggedInDTO: API.IsLoggedInDTO = {
      accessToken: accessToken,
      userId: parseInt(sessionStorage.getItem("user-id") ?? "-1"),
    };

    try {
      const res = await authenticatedAuthApi.isLogged(loggedInDTO);
      if (res.status !== 200) {
        this.logout();
        return false;
      }

      if (!res.data) {
        const isRefreshTokenExpired = await authenticatedAuthApi.isRefreshTokenExpired(refreshTokenDTO);
        if (isRefreshTokenExpired.data) {
          console.log("Bad refresh token logged out :" + refreshTokenDTO.refreshToken);
          return false;
        } else {
          // Refresh the token
          const refreshedToken = await authenticatedAuthApi.refreshAccessToken(refreshTokenDTO.refreshToken ?? "");
          sessionStorage.setItem("access-token", refreshedToken.data);
          return true;
        }
      }

      return true;
    } catch (e) {
      console.error("An error occurred:", e);
      this.logout();
      return false;
    }
  }

  public async getPrincipalAuthority() : Promise<string> {
    if (this.isUserLoggedOut()) {
      return "";
    }

    return await this.authApi.getUserRole(this.getAccessTokenOrDefault())
        .then(res => {
          if (res.status !== 200) {
            return ""
          }
          return res.data;
        });
  }

  

  public isUserLoggedOut() {
    return this.getAccessTokenOrDefault() === "" &&
        this.getRefreshTokenOrDefault() === "";
  }

  public getAccessTokenOrDefault = () => {
    return sessionStorage.getItem("access-token") ?? "";
  };

  public getRefreshTokenOrDefault = () => {
    return localStorage.getItem("refresh-token") ?? "";
  };

  public getUserId = () => {
    return parseInt(sessionStorage.getItem("user-id") ?? "-1");
  };

  public getApiConfig = () => {
    const configParam: ConfigurationParameters = {
      accessToken: this.getAccessTokenOrDefault(),
    };
    return new Configuration(configParam);
  };

  public getApiConfigWithToken = (token: string) => {
    const configParam: ConfigurationParameters = {
      accessToken: token,
    };
    return new Configuration(configParam);
  };

  public setAuthenticationResponseInMemory(
    authResponse: API.AuthenticationResponse
  ) {
    sessionStorage.setItem("access-token", authResponse.access_token ?? "");
    localStorage.setItem("refresh-token", authResponse.refresh_token ?? "");
    sessionStorage.setItem("user-id", authResponse.user_id?.toString() ?? "");
  }

  public async getLocalHostIp(): Promise<string> {
    let ip = "";

    await this.authApi.getAppIp()
        .then(res => {
          if (res.status != 200) {
            return "";
          }

          return res.data;
        })
        .catch(e => {
          return "";
        })

    return ip;
  }
}

const authenticationService = new AuthenticationService();

export default authenticationService;

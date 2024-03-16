import { AuthenticationApi, IsLoggedInDTO, RefreshTokenDTO } from "./Service";
import * as Api from "./Service";

class AuthenticationService {
  authApi = new AuthenticationApi();

  public async login(
    loginDTO: import("./Service").SignInDTO
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

  public async signup(signUpData: Api.SignUpDTO): Promise<boolean> {
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
    const authenticatedAuthApi = new Api.AuthenticationApi(config);
    const accessToken = this.getAccessTokenOrDefault();

    const refreshTokenDTO: RefreshTokenDTO = {
      refreshToken: this.getRefreshTokenOrDefault(),
    };

    const loggedInDTO: IsLoggedInDTO = {
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


  public isUserLoggedOut() {
    if (
      this.getAccessTokenOrDefault() === "" &&
      this.getRefreshTokenOrDefault() === ""
    ) {
      return true;
    }
    return false;
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
    const configParam: Api.ConfigurationParameters = {
      accessToken: this.getAccessTokenOrDefault(),
    };
    return new Api.Configuration(configParam);
  };

  public getApiConfigWithToken = (token: string) => {
    const configParam: Api.ConfigurationParameters = {
      accessToken: token,
    };
    return new Api.Configuration(configParam);
  };

  public setAuthenticationResponseInMemory(
    authResponse: Api.AuthenticationResponse
  ) {
    sessionStorage.setItem("access-token", authResponse.access_token ?? "");
    localStorage.setItem("refresh-token", authResponse.refresh_token ?? "");
    sessionStorage.setItem("user-id", authResponse.user_id?.toString() ?? "");
  }
}

const authenticationService = new AuthenticationService();

export default authenticationService;

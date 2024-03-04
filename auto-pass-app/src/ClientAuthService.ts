import { AuthenticationApi, IsLoggedInDTO, RefreshTokenDTO } from "./Service";
import * as Api from "./Service";

class AuthenticationService {
  authApi = new AuthenticationApi();

  public async login(
    loginDTO: import("./Service").SignInDTO
  ): Promise<boolean> {
    console.log("loggin in in service");

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
  };

  // This method will use authentication api to check if the access token in local and session storage are valid.
  // This method will then be called when routing. Each new route a user takes will trigger this function to handle authentication between pages.
  // When an access token is invalid, this method will handle refreshing it
  public async isUserLoggedIn() {
    console.log(this.getAccessTokenOrDefault());
    const config = this.getApiConfig();
    const authenticatedAuthApi = new Api.AuthenticationApi(config);

    let access_token = this.getAccessTokenOrDefault();

    let refreshTokenDTO: RefreshTokenDTO = {
      refreshToken: this.getRefreshTokenOrDefault(),
    };

    let loggedInDTO: IsLoggedInDTO = {
      accessToken: access_token,
      userId: parseInt(sessionStorage.getItem("user-id") ?? "-1"),
    };

    // this condition only validates access token.
    if (!(await authenticatedAuthApi.isLogged(loggedInDTO)).data) {
      // if refresh token is invalid completely restrict access.
      var isRefreshTokenExpired = (
        await authenticatedAuthApi.isRefreshTokenExpired(refreshTokenDTO)
      ).data;

      if (isRefreshTokenExpired) {
        console.log(
          "Bad refresh token logged out :" + refreshTokenDTO.refreshToken
        );
        return false;
      } else {
        // refresh token if refresh token is valid and access token is expired.
        sessionStorage.setItem(
          "access-token",
          (
            await authenticatedAuthApi.refreshAccessToken(
              refreshTokenDTO.refreshToken ?? ""
            )
          ).data
        );
        return true;
      }
    } else {
      return true;
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

  public setAuthenticationResponseInMemory(authResponse: Api.AuthenticationResponse) {
    sessionStorage.setItem("access-token", authResponse.access_token ?? "");
    localStorage.setItem("refresh-token", authResponse.refresh_token ?? "");
    sessionStorage.setItem("user-id", authResponse.user_id?.toString() ?? "");
  }
}

const authenticationService = new AuthenticationService();

export default authenticationService;

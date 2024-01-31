import {AuthenticationApi, IsLoggedInDTO, RefreshTokenDTO} from "./Service";

class AuthenticationService {

    authApi = new AuthenticationApi();

    public async login(loginDTO: import("./Service").SignInDTO): Promise<boolean> {

        // /login
        await this.authApi.authenticate(loginDTO)
            .then((res) => {
                console.log(res);
                sessionStorage.setItem("access-token", res.data.access_token ?? "")
                localStorage.setItem("refresh-token", res.data.refresh_token ?? "")
                sessionStorage.setItem("user-id", res.data.user_id?.toString() ?? "")
                return true;
            })
            .catch((error) => {
                console.log(error);
                this.logout();
                return false;
            });

        return false;
    }

    public logout = () => {
        this.authApi.logout(sessionStorage.getItem("refresh-token") ?? "");
        localStorage.clear();
        sessionStorage.clear();
        window.location.reload();
    }

    // This method will use authentication api to check if the access token in local and session storage are valid. 
    // This method will then be called when routing. Each new route a user takes will trigger this function to handle authentication between pages.
    // When an access token is invalid, this method will handle refreshing it
    public async isUserLoggedIn() {

        let access_token = sessionStorage.getItem("access-token");

        let refreshTokenDTO: RefreshTokenDTO = {
            refreshToken: sessionStorage.getItem("refresh-token") ?? ""
        }

        let loggedInDTO: IsLoggedInDTO = {
            accessToken: access_token ?? "",
            userId: parseInt(sessionStorage.getItem("user-id") ?? "-1")
        }

        // this condition only validates access token.
        if (!await this.authApi.isLogged(loggedInDTO)) {

            // if refresh token is invalid completely restrict access.
            if (await this.authApi.isRefreshTokenExpired(refreshTokenDTO)) {
                this.logout();
                return false;
            } else {
                // refresh token if refresh token is valid and access token is expired.
                sessionStorage.setItem(
                    "access-token", (await this.authApi.refreshTokenCookie(refreshTokenDTO.refreshToken ?? "")).data)
                return true;
            }
        } else {
            return true;
        }
    }

}

export default new AuthenticationService();
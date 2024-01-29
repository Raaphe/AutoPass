import {AuthenticationApi} from "./Service";

class AuthenticationService {

    authApi = new AuthenticationApi();

    async login(loginDTO: import("./Service").SignInDTO): Promise<boolean> {

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

    logout() {
        this.authApi.logout(sessionStorage.getItem("refresh-token") ?? "");
        localStorage.clear();
        sessionStorage.clear();
        window.location.reload();
    }

    // This method will use authentication api to check if the tokens in local and session storage are valid. 
    // This method will then be called when routing. Each new route a user takes will trigger this function to handle authentication between pages.
    isUserLoggedIn() {

        let refresh_token = sessionStorage.getItem("refresh-token");
        let access_token = sessionStorage.getItem("access-token");

        this.authApi.isLogged();


        // if (this.authApi.)

        // let role = sessionStorage.getItem("role");
        // if (role !== "user") {
        //     return false;
        // } else {
        //     return true;
        // }
    }
}

export default new AuthenticationService();
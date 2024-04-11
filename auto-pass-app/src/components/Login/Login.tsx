import React, { FC, useState } from "react";
import "./Login.scss";
import {AuthenticationApi, AuthenticationResponse, SignInDTO} from "../../Service";
import ClientAuthService from "../../ClientAuthService";
import { useNavigate, useLocation } from "react-router-dom";


const useQuery = () => {
  return new URLSearchParams(useLocation().search);
};

interface LoginProps { }


/**
* Login - 2024-04-02
* Raaphe, Lamb
*
* AutoPass
*/
const Login: FC<LoginProps> = () => {
  const query = useQuery();
  const navigate = useNavigate();
  const authApi: AuthenticationApi = new AuthenticationApi();

  const [signInData, setSignInData] = useState<SignInDTO>({
    email: "",
    password: "",
  });

  if (query.get("accessToken") !== null) {
    let authResponse: AuthenticationResponse = {
      access_token: query.get("accessToken") ?? "",
      token_type: query.get("tokenType") ?? "",
      user_id: parseInt(query.get("id") ?? "-1"),
      refresh_token: query.get("refreshToken") ?? "",
    };

    ClientAuthService.setAuthenticationResponseInMemory(authResponse);
    navigate("/home");
  }

  const updateField = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSignInData({
      ...signInData,
      [e.target.id]: e.target.value.trim(),
    });
  };

  const handleLogin = async (event: React.FormEvent) => {
    // Prevents page reload. may be removed
    event.preventDefault();

    const isCredentialCorrect: boolean = await ClientAuthService.login(
        signInData
    );

    if (!isCredentialCorrect) {
      setSignInData({ email: "", password: "" });
    } else {
      navigate("/home");
    }
  };

  const handleLoginWithGoogle = async (): Promise<void> => {
    let ip: string = await authApi.getAppIp()
        .then(res => {
          return res.data ?? "localhost"
        })
        .catch((e) => {
          alert(e);
          return "";
        });

    let url = "http://" + ip + ".nip.io:9090/oauth2/authorization/google";
    window.location.href = url;
  };

  return (
    <div className="container mt-5">
      <div className="row">
      </div>
      <div className="row justify-content-end">
        <div className="col-md-6">
          <div className="card shadow thinner-card" style={{ marginLeft: 'auto', marginRight: 0 }}>
            <div className="card-body">
              <button type="submit" className="btn btn-outline-primary mb-3" onClick={() => navigate(-1)}>
                <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" className="bi bi-arrow-left m-1" viewBox="0 0 16 16">
                  <path fillRule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8" />
                </svg>
              </button>
              <button onClick={handleLoginWithGoogle} type="button" className="btn btn-primary col-12 mb-3">
                <div className="row">
                  <div>
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-google col-2 m-1" viewBox="0 0 16 16">
                      <path d="M15.545 6.558a9.4 9.4 0 0 1 .139 1.626c0 2.434-.87 4.492-2.384 5.885h.002C11.978 15.292 10.158 16 8 16A8 8 0 1 1 8 0a7.7 7.7 0 0 1 5.352 2.082l-2.284 2.284A4.35 4.35 0 0 0 8 3.166c-2.087 0-3.86 1.408-4.492 3.304a4.8 4.8 0 0 0 0 3.063h.003c.635 1.893 2.405 3.301 4.492 3.301 1.078 0 2.004-.276 2.722-.764h-.003a3.7 3.7 0 0 0 1.599-2.431H8v-3.08z"></path>
                    </svg>
                  </div>
                  <div className="col-6"></div>
                </div>
              </button>
              <form onSubmit={handleLogin}>
                <div className="mb-3">
                  <input
                    type="email"
                    placeholder="Email"
                    className="form-control"
                    id="email"
                    value={signInData.email}
                    onChange={updateField}
                    required
                  />
                </div>
                <div>
                  <input
                    type="password"
                    placeholder="Password"
                    className="form-control"
                    id="password"
                    value={signInData.password}
                    onChange={updateField}
                    required
                  />
                </div>
                <button
                  onClick={() => navigate("/forgot-password")}
                  type="button"
                  className="btn btn-link"
                >
                  Forgot Password
                  <svg xmlns="http://www.w3.org/2000/svg" width="10" height="10" fill="currentColor" className="bi bi-box-arrow-in-down-right m-1" viewBox="0 0 16 16">
                    <path fillRule="evenodd" d="M6.364 2.5a.5.5 0 0 1 .5-.5H13.5A1.5 1.5 0 0 1 15 3.5v10a1.5 1.5 0 0 1-1.5 1.5h-10A1.5 1.5 0 0 1 2 13.5V6.864a.5.5 0 1 1 1 0V13.5a.5.5 0 0 0 .5.5h10a.5.5 0 0 0 .5-.5v-10a.5.5 0 0 0-.5-.5H6.864a.5.5 0 0 1-.5-.5" />
                    <path fillRule="evenodd" d="M11 10.5a.5.5 0 0 1-.5.5h-5a.5.5 0 0 1 0-1h3.793L1.146 1.854a.5.5 0 1 1 .708-.708L10 9.293V5.5a.5.5 0 0 1 1 0z" />
                  </svg>
                </button>
                <hr className="my-4" />
                <div className="mb-3 form-check">
                  <input
                    type="checkbox"
                    className="form-check-input"
                    id="rememberMe"
                  />
                  <label className="form-check-label" htmlFor="rememberMe">Remember Me</label>
                </div>
                <div className="text-end">
                  <button type="submit" className="btn btn-primary">Login</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;

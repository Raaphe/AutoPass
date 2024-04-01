import React, { FC, useState } from "react";
import "./Login.scss";
import { SignInDTO } from "../../Service/api";
import ClientAuthService from "../../ClientAuthService";
import { useNavigate } from "react-router-dom";
import { Box, Card, IconButton, Stack, TextField } from "@mui/material";
import logo from "../../assets/ConsoleLogo.png";
import Fingerprint from '@mui/icons-material/Fingerprint';



interface LoginProps { }

const Login: FC<LoginProps> = () => {
  const navigate = useNavigate();

  const [signInData, setSignInData] = useState<SignInDTO>({
    email: "",
    password: "",
  });

  const handleLogin = async (event: React.FormEvent) => {
    // Prevents page reload. may be removed

    event.preventDefault();

    var isCredentialCorrect: boolean = await ClientAuthService.login(
      signInData
    );

    if (!isCredentialCorrect) {
      setSignInData({ email: "", password: "" });
    } else {
      navigate("/terminal");
    }
  };

  return (
    <Box className="container-fluid" sx={{ backgroundColor: "#282c34", maxWidth: "100%", maxHeight: "100%", display: "flex", justifyContent: "center", alignItems: "center", flexDirection: 'column' }}>

      <Box
        component="img"
        sx={{
          marginTop: "3%",
          width: '75%',
          maxWidth: '1800px',
          height: 'auto'
        }}
        src={logo}
        alt="Your image"
      />

      <Card sx={{ backgroundColor: "dark-grey" }} style={{ maxWidth: "100%", width: "1000px" }}>
        <Stack direction="column" className="mx-3 mb-4" style={{ display: "flex", justifyContent: "center", alignItems: "center", maxWidth: "100%", width: "100%" }}>

          <TextField
            id="standard-email-input"
            label="Email"
            type="Email"
            autoComplete="current-password"
            variant="standard"
            fullWidth
            className="m-3"
            value={signInData.email}
            onChange={(e) => setSignInData({...signInData, email:e.currentTarget.value})}
            required
          />
          <TextField
            id="standard-password-input"
            label="Password"
            type="password"
            autoComplete="current-password"
            variant="standard"
            className="m-3"
            fullWidth
            value={signInData.password}
            onChange={(e) => setSignInData({...signInData, password:e.currentTarget.value})}
            required
          />
          <IconButton aria-label="fingerprint" color="secondary" sx={{maxWidth:"40%"}} onClick={handleLogin}>
            <Fingerprint />
          </IconButton>
        </Stack>

      </Card>
    </Box>
  );
};

export default Login;

import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import './App.css';
import ProtectedRoutesAnonymous from "./routes/ProtectedRoutesAnonymous";
import ProtectedRoutesScanner from "./routes/ProtectedRoutesScanner";
import Login from "./components/Login/Login";
import ScannerTerminal from "./components/ScannerTerminal/ScannerTerminal";

// for enabling dark mode theme
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';

const darkTheme = createTheme({
  palette: {
    mode: 'dark'
  },
});

function App() {
  return (
    <ThemeProvider theme={darkTheme}>
      <CssBaseline/>
      <Router>
        <div className="App">
          <Routes>

            <Route element={<ProtectedRoutesAnonymous />}>
              <Route path='/' element={<Login />} />



            </Route>

            <Route element={<ProtectedRoutesScanner />}>

              <Route path='/terminal' element={<ScannerTerminal />} />

            </Route>
          </Routes>
        </div>
      </Router>
    </ThemeProvider>
  );
}

export default App;

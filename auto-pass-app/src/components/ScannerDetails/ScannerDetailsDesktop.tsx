import { FC, useEffect, useState } from 'react';
import { Box, Button, Card, Divider, IconButton, Modal, Stack, TextField, Typography } from '@mui/material';
import { useLocation, useNavigate } from 'react-router-dom';
import { ScannerControllerApi, User, ScannerRegistrationDTO } from "../../Service/api";
import ClientAuthService from "../../ClientAuthService";
import "./ScannerDetails.module.scss";
import DeleteIcon from '@mui/icons-material/Delete';
import SaveAsIcon from '@mui/icons-material/SaveAs';
import ClientUtil from "../../ClientUtil";

interface ScannerDetailsDesktopProps {

}

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
};

const ScannerDetailsDesktop: FC<ScannerDetailsDesktopProps> = () => {
  const location = useLocation();
  const busNumber = location.state?.busNum;


  const [scannerInfo, setScannerInfo] = useState<User>();
  const [scannerName, setScannerName] = useState("");
  const [busNumberState, setBusNumberState] = useState<number>(parseInt(busNumber));
  const [isFormReady, setisFormReady] = useState<boolean>();
  const [open, setOpen] = useState(false);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const navigate = useNavigate();
  const config = ClientAuthService.getApiConfig();
  const scannerAPI = new ScannerControllerApi(config);

  useEffect(() => {
  
    if (
      scannerInfo?.firstName === "" || scannerInfo?.firstName === null ||
      scannerInfo?.password === "" || scannerInfo?.password === null || 
      busNumberState < 0 ||
      (scannerInfo?.email !== "" && ClientUtil.getBusNumberFromEmail(scannerInfo?.email ?? "") !== busNumberState)
      
    ) {
      setisFormReady(false);
    } else {
      setisFormReady(true);
    }
    
  }, [scannerInfo, busNumberState])

  useEffect(() => {
    getScanner();
  }, [navigate]);

  const getScanner = async () => {
    await scannerAPI.getScannerByBusNumber(busNumber)
      .then(res => {
        if (res.status !== 200) {
          return;
        }
        setScannerInfo(res.data);
        setScannerName(res.data.firstName ?? "New Scanner")
      })

  }

  const deleteScanner = async (busNum: number) => {

    await scannerAPI.deleteScanner(busNum)
      .then(res => {
        if (res.status !== 200) {
          return;
        }
      })
      .catch(e => {
        alert("error deleting scanner \n" + e);
      })
      .finally(() => {
        handleClose();
      })

  };

  const handleSave = (): void => {
    var dto: ScannerRegistrationDTO = {
      busNumber: busNumberState,
      pwd: scannerInfo?.password,
      routeName: scannerInfo?.firstName
    }
    scannerAPI.createScannerAccount(dto)
      .then(res => {
        if (res.status !== 200) {
          alert("Error")
        } 
        setScannerInfo(res.data);
        setBusNumberState(ClientUtil.getBusNumberFromEmail(res.data.email ?? ""))
        setScannerName(res.data.firstName ?? "");
        return;
      })
      .catch((e) => {
        alert(e);
      });
  }

  return (
    <Card className="container" elevation={12} variant="outlined">

      <button type="submit" className="btn btn-outline-primary mt-4 mx-2 col" onClick={() => navigate(-1)}>
        <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" className="bi bi-arrow-left m-1" viewBox="0 0 16 16">
          <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8" />
        </svg>
      </button>

      <h2 className="display-4 m-2">{scannerName} - {busNumberState === -1 ? "" : busNumberState}</h2>

      <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />

      <Stack direction="row" className='m-3' spacing={8}>
        <TextField
          required
          label="Email"
          disabled
          value={scannerInfo?.email ?? ""}
          variant="standard"
          fullWidth
        />
        <TextField
          required
          label="Password"
          variant="standard"
          fullWidth
          value={scannerInfo?.password ?? ""}
          onChange={(e) => { setScannerInfo({ ...scannerInfo, password: e.currentTarget.value }) }}
        />
        <TextField
          required
          label="Name"
          variant="standard"
          fullWidth
          value={scannerName}
          onChange={(e) => { setScannerName(e.currentTarget.value); setScannerInfo({ ...scannerInfo, firstName: e.currentTarget.value }) }}
        />
        <TextField
          required
          label="Bus Number"
          fullWidth
          variant="standard"
          type='number'
          value={busNumberState ?? 0}
          onChange={(e) => { setBusNumberState(parseInt(e.currentTarget.value))}}
        />
      </Stack>
      <Stack direction="row-reverse" className='m-3' spacing={3}>
        <Button variant="contained" endIcon={<SaveAsIcon />} onClick={handleSave} disabled={!isFormReady}>
          Save
        </Button>
        <Button variant="outlined" color="error" startIcon={<DeleteIcon />} onClick={handleOpen} disabled={busNumberState === -1}>
          Delete
        </Button>
      </Stack>

      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
          <Typography id="modal-modal-title" variant="h6" component="h2">
            Are you sure you want to delete bus {busNumberState} ?
          </Typography>

            <Stack spacing={19} direction="row">
              <Button color="error" variant='outlined' className='mt-5' onClick={async () => {await deleteScanner(busNumberState); navigate(-1)}}>Delete</Button>
              <Button variant="outlined" className='mt-5' onClick={() => {handleClose()}}>Cancel</Button>
            </Stack>
          
        </Box>
      </Modal>
    </Card>

  );
};

export default ScannerDetailsDesktop;

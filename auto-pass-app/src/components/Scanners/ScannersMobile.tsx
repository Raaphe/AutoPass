import { FC, useEffect, useState } from 'react';
import { Box, Button, ButtonGroup, Card, Divider, IconButton, Modal, Stack, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { ScannerControllerApi, User } from "../../Service";
import ClientAuthService from "../../ClientAuthService";
import ClientUtil from "../../ClientUtil";
import "./Scanners.module.scss";
import DeleteIcon from '@mui/icons-material/Delete';
import CreateIcon from '@mui/icons-material/Create';
import AddCircleIcon from '@mui/icons-material/AddCircle';

interface ScannersMobileProps {

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

/**
* ScannersMobile - 2024-04-02
* Raaphe
*
* AutoPass
*/

const ScannersMobile: FC<ScannersMobileProps> = () => {

  const [scannerListInfo, setScannerListInfo] = useState<Array<User>>();
  const [selectedScanner, setSelectedScanner] = useState<number>();
  const [open, setOpen] = useState(false);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const navigate = useNavigate();
  const config = ClientAuthService.getApiConfig();
  const scannerAPI = new ScannerControllerApi(config);

  useEffect(() => {
    fetchScanners();
  }, [navigate]);

  const fetchScanners = () => {
    scannerAPI.getAllScanners()
      .then(res => {
        if (res.status !== 200) {
          return null;
        }

        setScannerListInfo(res.data);
      })
  }

  const handleScannerSelect:  React.MouseEventHandler<HTMLButtonElement> = (event) => {
    navigate(`/scanner/${event.currentTarget.id}`, {state: {busNum: event.currentTarget.id }} )
  }

  const handleDeleteClick: React.MouseEventHandler<HTMLButtonElement> = (event) => {
    setSelectedScanner(parseInt(event.currentTarget.id));
    handleOpen();
  };

  const deleteScanner = async () => {
    
    await scannerAPI.deleteScanner(selectedScanner ?? -1)
      .then(res => {
        if (res.status !== 200) {
          setSelectedScanner(-1);
          return;
        }
      })
      .catch(e => {
        alert("error deleting scanner \n" + e);
      })
      .finally(() => {
        handleClose();
        fetchScanners();
      })
    
  };

  return (
    <Card className="container" elevation={12} variant="outlined">
      <div className='row'>
        <button type="submit" className="btn btn-outline-primary m-4 col" onClick={() => navigate(-1)}>
          <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" className="bi bi-arrow-left m-1" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8" />
          </svg>
        </button>

        <div className='col-9'></div>

        <Button
          component="label"
          role={undefined}
          variant="contained"
          startIcon={<AddCircleIcon />}
          className='col m-4'
        >
          Add
        </Button>
      </div>
      <h2 className="display-4 m-2">Scanners</h2>

      <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />

      <table className="table m-3 mb-4">
        <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">Name</th>
            <th scope="col">Email</th>
            <th scope="col"></th>
          </tr>
        </thead>
        <tbody>

          {scannerListInfo?.map(scanner => (
            <tr className='tableRow'>
              <th>{ClientUtil.getBusNumberFromEmail(scanner.email ?? "")}</th>
              <td>{scanner.firstName}</td>
              <td>{scanner.email}</td>
              <td style={{ textAlign: "right" }}>
                <IconButton aria-label="Delete" id={ClientUtil.getBusNumberFromEmail(scanner.email ?? "").toString()} onClick={handleDeleteClick}>
                  <DeleteIcon />
                </IconButton>
                <IconButton color="secondary" id={ClientUtil.getBusNumberFromEmail(scanner.email ?? "").toString()} aria-label="Edit" onClick={handleScannerSelect}>
                  <CreateIcon />
                </IconButton>

              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box sx={style}>
          <Typography id="modal-modal-title" variant="h6" component="h2">
            Are you sure you want to delete bus {selectedScanner} ?
          </Typography>

            <Stack spacing={19} direction="row">
              <Button color="error" variant='outlined' className='mt-5' onClick={deleteScanner}>Delete</Button>
              <Button variant="outlined" className='mt-5' onClick={() => {handleClose(); setSelectedScanner(-1)}}>Cancel</Button>
            </Stack>
          
        </Box>
      </Modal>
    </Card>

  );
};

export default ScannersMobile;

import React, { FC, useEffect, useState } from "react";
import "./TransitLogs.module.scss";
import {
  Card,
  Container,
  FormControl,
  InputLabel,
  MenuItem,
  Pagination,
  Select,
  SelectChangeEvent,
  Stack,
  Typography,
} from "@mui/material";
import * as API from "../../Service";
import ClientAuthService from "../../ClientAuthService";
import { useNavigate } from "react-router-dom";
import ClientUtil from "../../ClientUtil";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";

interface TransitLogsMobileProps {}

const TransitLogsMobile: FC<TransitLogsMobileProps> = () => {
  const [page, setPage] = useState(1);
  const [month, setMonth] = useState(0);
  const [year, setYear] = useState(0);
  const [logs, setLogs] = useState<API.TransitLog[]>();
  const [maxPage, setMaxPage] = useState<number>();
  const navigate = useNavigate();

  
  useEffect(() => {
    const config = ClientAuthService.getApiConfig();
    const transitLogApi = new API.TransitLogControllerApi(config);

    const getPage = async () => {
      let dto: API.TransitLogPageDTO = {
        month: month,
        page: page,
        userId: ClientAuthService.getUserId(),
        year: year,
      };
      await transitLogApi
        .getUserTransitHistory(dto)
        .then((res) => {
          if (res.status !== 200) return;
          setMaxPage(res.data.pageCount);
          setLogs(res.data?.transitLogs);
        })
        .catch((_) => {
          alert("There was an error fetching the history..");
        });
      return;
    };

    getPage();
  }, [navigate, page, year, month]);

  const getYears = (): number[] => {
    let years: number[] = [];
    for (let i = 2015; i <= ClientUtil.getUTCNow().getFullYear(); i++) {
      years.push(i);
    }
    return years;
  };

  const handleChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value);
  };

  const handleChangeMonth = (event: SelectChangeEvent) => {
    setMonth(parseInt(event.target.value));
  };

  const handleChangeYear = (event: SelectChangeEvent) => {
    setYear(parseInt(event.target.value));
  };

  return (
    <Container className="container">
      <button
        type="submit"
        className="btn btn-outline-primary mb-3"
        onClick={() => navigate(-1)}
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="12"
          height="12"
          fill="currentColor"
          className="bi bi-arrow-left m-1"
          viewBox="0 0 16 16"
        >
          <path
            fillRule="evenodd"
            d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8"
          />
        </svg>
      </button>

      <h1 className="display-4">Transit History</h1>

      <Card elevation={13}>
        <Stack direction={"row"}>
          <FormControl sx={{ m: 1, minWidth: 120 }} size="small">
            <InputLabel id="month-label">Month</InputLabel>
            <Select
              labelId="month"
              id="month"
              value={month.toString()}
              label="Month"
              onChange={handleChangeMonth}
            >
              <MenuItem value={0}>
                <em>None</em>
              </MenuItem>
              <MenuItem value={1}>January</MenuItem>
              <MenuItem value={2}>February</MenuItem>
              <MenuItem value={3}>March</MenuItem>
              <MenuItem value={4}>April</MenuItem>
              <MenuItem value={5}>May</MenuItem>
              <MenuItem value={6}>June</MenuItem>
              <MenuItem value={7}>July</MenuItem>
              <MenuItem value={8}>August</MenuItem>
              <MenuItem value={9}>September</MenuItem>
              <MenuItem value={10}>October</MenuItem>
              <MenuItem value={11}>November</MenuItem>
              <MenuItem value={12}>December</MenuItem>
            </Select>
          </FormControl>
          <FormControl sx={{ m: 1, minWidth: 120 }} size="small">
            <InputLabel id="year-label">Year</InputLabel>
            <Select
              labelId="year"
              id="year"
              value={year.toString()}
              label="Year"
              onChange={handleChangeYear}
            >
              <MenuItem value={0}>
                <em>None</em>
              </MenuItem>
              {getYears().map((y) => (
                <MenuItem key={y} value={y}>
                  {y}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Stack>
      </Card>

      <TableContainer component={Paper} className="mt-3">
        <Table sx={{ minWidth: 650 }} aria-label="simple table">
          <TableHead>
            <TableRow sx={{ border: "solid" }}>
              <TableCell sx={{ fontWeight: "bold" }}>#</TableCell>
              <TableCell sx={{ fontWeight: "bold" }} align="right">
                Date
              </TableCell>
              <TableCell sx={{ fontWeight: "bold" }} align="right">
                Resource
              </TableCell>
              <TableCell sx={{ fontWeight: "bold" }} align="right">
                Bus #
              </TableCell>
              <TableCell sx={{ fontWeight: "bold" }} align="right">
                Line Name
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {logs?.map((log, index) => (
              <TableRow
                key={index}
                sx={{
                  "&:last-child td, &:last-child th": { border: 0 },
                  backgroundColor: log.authorized ? "#6dcf7f" : "#CD5C5C",
                  color: "white",
                }}
              >
                <TableCell component="th" scope="row">
                  {index}
                </TableCell>
                <TableCell align="right">
                  {ClientUtil.getFriendlyDateFromMs(log.date ?? 0)}
                </TableCell>
                <TableCell align="right">{log.resourceType}</TableCell>
                <TableCell align="right">{log.busNumber}</TableCell>
                <TableCell align="right">{log.busName}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Stack spacing={2} className="mt-3">
        <Typography>Page: {page}</Typography>
        <Pagination count={maxPage} page={page} onChange={handleChange} />
      </Stack>
    </Container>
  );
};

export default TransitLogsMobile;

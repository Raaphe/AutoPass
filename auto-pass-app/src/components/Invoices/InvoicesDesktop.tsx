import React, { FC, useEffect, useState } from "react";
import "./Invoices.module.scss";
import { useNavigate } from "react-router-dom";
import {
  Button,
  Card,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import * as API from "../../Service";
import ClientAuthService from "../../ClientAuthService";
import ClientUtil from "../../ClientUtil";

interface InvoicesDesktopProps {}

/**
 * HeaderDesktop - 2024-04-02
 * Raaphe
 *
 * AutoPass
 */
const InvoicesDesktop: FC<InvoicesDesktopProps> = () => {
  const navigate = useNavigate();
  const [invoices, setInvoices] = useState<API.Invoice[]>();

  const invoicesApi = new API.InvoiceControllerApi(
    ClientAuthService.getApiConfig()
  );

  useEffect(() => {
    const fetchInvoices = async () => {
      await invoicesApi
        .getAllInvoicesByUserId(ClientAuthService.getUserId() ?? -1)
        .then((res) => {
          if (res.status !== 200) {
            alert("Issue fetching invoices. \nContact an administrator.");
          }
          setInvoices(res.data);
        });
    };

    fetchInvoices();
  }, [navigate]);

  return (
    <Card elevation={14} className="m-5">
      <div className="row">
        <button
          type="submit"
          className="btn btn-outline-primary m-4 col"
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
              fill-rule="evenodd"
              d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8"
            />
          </svg>
        </button>

        <div className="col-11"></div>

      </div>
      <h1 className="display-4 m-3">Invoices</h1>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
          <TableHead>
            <TableRow>
              <TableCell>#</TableCell>
              <TableCell align="right">Date</TableCell>
              <TableCell align="right">Name</TableCell>
              <TableCell align="right">Price</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {invoices?.map((row, index) => (
              <TableRow
                key={index}
                sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
              >
                <TableCell component="th" scope="row">
                  {index + 1}
                </TableCell>
                <TableCell align="right">
                  {ClientUtil.convertMsToDate(row.date ?? 0).toDateString()}
                </TableCell>
                <TableCell align="right">{row.productName}</TableCell>
                <TableCell align="right">{row.price + "$"}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Card>
  );
};

export default InvoicesDesktop;

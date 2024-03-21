import React, { FC, useState } from "react";
import { Card, Button, Typography, Divider, IconButton } from "@mui/material"; // Import Material-UI components
import { CircularProgressbarWithChildren } from "react-circular-progressbar"; // Import CircularProgressbar
import "react-circular-progressbar/dist/styles.css"; // Import styles for CircularProgressbar
import { BsArrowLeft, BsArrowRight } from "react-icons/bs"; // Import Bootstrap icons for navigation buttons
import { MdConfirmationNumber } from "react-icons/md"; // Import Material Design icon for ticket

interface ProductsDesktopProps {
}

interface Membership {
  price: number;
  membershipDurationDays: number;
}

interface Ticket {
  ticketAmount: number;
  price: number;
}
//Ticket data cause i don't know how to code the api yet
const ProductsDesktop: FC<ProductsDesktopProps> = () => {
  const memberships: Membership[] = [
    { price: 11, membershipDurationDays: 1 },
    { price: 21.25, membershipDurationDays: 3 },
    { price: 30, membershipDurationDays: 7 },
    { price: 97, membershipDurationDays: 30 },
    { price: 226, membershipDurationDays: 140 },
  ];

  const tickets: Ticket[] = [
    { ticketAmount: 1, price: 3.75 },
    { ticketAmount: 2, price: 7 },
    { ticketAmount: 10, price: 32.50 },
  ];

  const [membershipIndex, setMembershipIndex] = useState(0);
  const [ticketIndex, setTicketIndex] = useState(0);

  const handleMembershipNext = () => {
    setMembershipIndex((prevIndex) => (prevIndex + 1) % memberships.length);
  };

  const handleMembershipPrev = () => {
    setMembershipIndex((prevIndex) => (prevIndex - 1 + memberships.length) % memberships.length);
  };

  const handleTicketNext = () => {
    setTicketIndex((prevIndex) => (prevIndex + 1) % tickets.length);
  };

  const handleTicketPrev = () => {
    setTicketIndex((prevIndex) => (prevIndex - 1 + tickets.length) % tickets.length);
  };

  const handleAddPlan = () => {
    alert("Add Plan button clicked");
  };

  const handleAddTicket = () => {
    alert("Add Ticket button clicked");
  };

  return (
    <Card elevation={12} variant="outlined" style={{ padding: "20px", margin: "20px auto", maxWidth: "800px" }}>
      <Typography variant="h2" gutterBottom style={{ textAlign: "center" }}>
        All Products
      </Typography>
      <Divider style={{ marginBottom: "20px" }} />
      <div style={{ display: 'flex', justifyContent: 'center' }}>
        {/* Membership card */}
        <Card elevation={12} style={{ width: "300px", height: "450px", margin: "10px", padding: "20px", borderRadius: "10px", boxShadow: "0px 4px 10px rgba(0, 0, 0, 0.1)" }}>
          <Typography variant="h4" gutterBottom style={{ textAlign: "center" }}>
            Memberships
          </Typography>
          <Divider style={{ marginBottom: "20px" }} />
          <div style={{ display: "flex", justifyContent: "center", alignItems: "center", flexDirection: "column" }}>
            <div style={{ display: "flex", alignItems: "center" }}>
              <IconButton onClick={handleMembershipPrev} style={{ marginRight: "auto", backgroundColor: "#eee" }}>
                <BsArrowLeft />
              </IconButton>
              <div style={{ padding: "20px", textAlign: "center" }}>
                <Typography variant="h5" gutterBottom>
                  Membership Duration: {memberships[membershipIndex].membershipDurationDays} days
                </Typography>
                <Typography variant="body1" gutterBottom>
                  Price: ${memberships[membershipIndex].price}
                </Typography>
              </div>
              <IconButton onClick={handleMembershipNext} style={{ marginLeft: "auto", backgroundColor: "#eee" }}>
                <BsArrowRight />
              </IconButton>
            </div>
          </div>
          <Button onClick={handleAddPlan} variant="outlined" style={{ margin: "20px auto", display: "block" }}>Add Plan</Button>
        </Card>

        {/* Tickets card */}
        <Card elevation={12} style={{ width: "300px", height: "450px", margin: "10px", padding: "20px", borderRadius: "10px", boxShadow: "0px 4px 10px rgba(0, 0, 0, 0.1)" }}>
          <Typography variant="h4" gutterBottom style={{ textAlign: "center" }}>
            Tickets
          </Typography>
          <Divider style={{ marginBottom: "20px" }} />
          <div style={{ display: "flex", justifyContent: "center", alignItems: "center", flexDirection: "column" }}>
            <div style={{ display: "flex", alignItems: "center" }}>
              <IconButton onClick={handleTicketPrev} style={{ marginRight: "auto", backgroundColor: "#eee" }}>
                <BsArrowLeft />
              </IconButton>
              <div style={{ padding: "20px", textAlign: "center" }}>
                <Typography variant="h5" gutterBottom>
                  Ticket Amount: {tickets[ticketIndex].ticketAmount} <MdConfirmationNumber style={{ verticalAlign: "middle", marginLeft: "5px" }} />
                </Typography>
                <Typography variant="body1" gutterBottom>
                  Price: ${tickets[ticketIndex].price}
                </Typography>
              </div>
              <IconButton onClick={handleTicketNext} style={{ marginLeft: "auto", backgroundColor: "#eee" }}>
                <BsArrowRight />
              </IconButton>
            </div>
          </div>
          <Button onClick={handleAddTicket} variant="outlined" style={{ margin: "20px auto", display: "block" }}>Add Ticket</Button>
        </Card>
      </div>
    </Card>
  );
};

export default ProductsDesktop;

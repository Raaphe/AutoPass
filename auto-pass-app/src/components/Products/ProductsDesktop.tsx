import React, { FC, useState, useEffect } from "react";
import { Card, Button, Typography, Divider, IconButton } from "@mui/material"; // Import Material-UI components
import { BsArrowLeft, BsArrowRight } from "react-icons/bs"; // Import Bootstrap icons for navigation buttons
import { MdConfirmationNumber } from "react-icons/md"; // Import Material Design icon for ticket
import ClientAuthService from "../../ClientAuthService";
import * as API from "../../Service/api";
import { useNavigate } from "react-router-dom";

interface ProductsDesktopProps {
}

/**
* ProductsDesktop - 2024-04-02
* Lamb
*
* AutoPass
*/

const ProductsDesktop: FC<ProductsDesktopProps> = () => {

  const productsAPI = new API.ProductsControllerApi(ClientAuthService.getApiConfig())
  const navigate = useNavigate();

  useEffect(() => {

    const getProducts = () => {
      productsAPI.getAllProducts()
        .then((res) => {
          if (res.status !== 200) {
            return;
          }
          setProductsInfo(res.data)
        })
    }
    getProducts()
  }, [])


  const [productsInfo, setProductsInfo] = useState<API.ProductsViewModel>()
  const [membershipIndex, setMembershipIndex] = useState(0);
  const [ticketIndex, setTicketIndex] = useState(0);

  const getMembershipSize = () => {
    if (productsInfo?.membershipList?.length === null || productsInfo?.membershipList?.length === undefined) {
      return 0
    }
    else {
      return productsInfo?.membershipList.length
    }
  }

  const getTicketsSize = () => {
    if (productsInfo?.ticketsList?.length === null || productsInfo?.ticketsList?.length === undefined) {
      return 0
    }
    else {
      return productsInfo?.ticketsList.length
    }
  }


  const handleMembershipNext = () => {
    setMembershipIndex((prevIndex) => (prevIndex + 1) % getMembershipSize());
  };

  const handleMembershipPrev = () => {
    setMembershipIndex((prevIndex) => (prevIndex - 1 + getMembershipSize()) % getMembershipSize());
  };

  const handleTicketNext = () => {
    setTicketIndex((prevIndex) => (prevIndex + 1) % getTicketsSize());
  };

  const handleTicketPrev = () => {
    setTicketIndex((prevIndex) => (prevIndex - 1 + getTicketsSize()) % getTicketsSize());
  };

  const handleAddPlan = () => {
    navigate("/checkout", { state: { priceId: productsInfo?.membershipList?.at(membershipIndex)?.stripePriceId, email: ClientAuthService.getEmail() }})
  };

  const handleAddTicket = () => {
    navigate("/checkout", { state: { priceId: productsInfo?.ticketsList?.at(ticketIndex)?.stripePriceId, email: ClientAuthService.getEmail() }})
  };

  return (
    <Card elevation={12} variant="outlined" style={{ padding: "20px", margin: "20px auto", maxWidth: "800px" }}>
      <Typography variant="h2" gutterBottom style={{ textAlign: "center" }}>
        All Products
      </Typography>
      <Divider style={{ marginBottom: "20px" }} />
      <div style={{ display: 'flex', justifyContent: 'center' }}>
        {/* Membership card */}
        <Card elevation={12} style={{ width: "300px", height: "450px", margin: "10px", padding: "20px", borderRadius: "10px" }}>
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
                  Membership Duration in days: {productsInfo?.membershipList?.at(membershipIndex)?.membershipDurationDays}
                </Typography>
                <Typography variant="body1" gutterBottom>
                  Price: ${productsInfo?.membershipList?.at(membershipIndex)?.price}
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
        <Card elevation={12} style={{ width: "300px", height: "450px", margin: "10px", padding: "20px", borderRadius: "10px" }}>
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
                  Ticket Amount: {productsInfo?.ticketsList?.at(ticketIndex)?.ticketAmount} <MdConfirmationNumber style={{ verticalAlign: "middle", marginLeft: "5px" }} />
                </Typography>
                <Typography variant="body1" gutterBottom>
                  Price: ${productsInfo?.ticketsList?.at(ticketIndex)?.price}
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

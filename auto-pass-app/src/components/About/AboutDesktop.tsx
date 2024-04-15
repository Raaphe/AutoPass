import React, { FC } from "react";
import "./About.module.scss";
import { Card, Divider } from "@mui/material";
import team from "../../assets/group.png";
import raph from "../../assets/raph.png";
import lam from "../../assets/lam.png";
import { useNavigate } from "react-router-dom";

interface AboutDesktopProps {

}

/**
* AboutDesktop - 2024-04-02
* Raaphe
*
* AutoPass
*/
const AboutDesktop: FC<AboutDesktopProps> = () => {
  const navigate = useNavigate();

  return (

    <>
      <Card className="container p-3 d-grow" elevation={15} sx={{ minWidth: "70%", minHeight: "60vh" }}  >
        <button type="submit" className="btn btn-outline-primary m-3" onClick={() => navigate(-1)}>
          <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" className="bi bi-arrow-left m-1" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8" />
          </svg>
        </button>
        <Card elevation={10} className="row m-1">
          <img src={team} className="col" style={{ minWidth: "40%", minHeight: "380px" }} alt="" />
          <div className="col" style={{ marginLeft: "45px" }}>
            <h1 className="display-4">Our Team 🏢</h1>
            <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />
            <p className="h6 mt-2">Our team was hard at work to make this service. We are a Montreal based group of students from the Cégep Marie-Victorin, a lively CEGEP with a new IT program that launched in 2023. Marie-Victorin is situated in the north-east end of the island which is fairly remote especially for our local public transit system. Our team was tasked of building an app so the idea of it being tied to transit was a no-brainer. Thus, AutoPass was born.</p>

          </div>
        </Card>
        <Card elevation={10} className="row m-1 mt-3">
          <div className="col" style={{ marginRight: "45px" }}>
            <h1 className="display-4">Raphael Paquin 🔵</h1>
            <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />
            <p className="h6 mt-2">AutoPass was fun to work on and I'm sure I'll look back on this project with nostalgia. Ultimately I learned a lot from it and it helped me progress imensly. Follow me on ig : @raph____p</p>

          </div>
          <img src={raph} className="col" style={{ minWidth: "40%", minHeight: "350px" }} alt="" />
        </Card>
        <Card elevation={10} className="row m-1 mt-3">
          <img src={lam} className="col" style={{ minWidth: "40%", minHeight: "350px" }} alt="" />
          <div className="col" style={{ marginLeft: "45px" }}>
            <h1 className="display-4">Lam Nguyen 🌹</h1>
            <Divider sx={{ alignSelf: 'stretch', color: "black", backgroundColor: 'gray' }} />
            <p className="h6 mt-2">Autopass was a fun project, I got to revise a lot of React notions and learned a lot about spring boot and spring security. For a first full stack project I think we did an amazing job.</p>
          </div>
        </Card>



      </Card>
    </>
  );
};

export default AboutDesktop;

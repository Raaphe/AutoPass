import React, {FC, useState} from "react";
import {buildStyles, CircularProgressbarWithChildren} from "react-circular-progressbar";
import {Button} from "@mui/material";
import {UserWallet} from "../../Service";
import ClientUtil from "../../ClientUtil";
import { useNavigate } from "react-router-dom";

interface MembershipStatusGraph {
    daysUntilExpiry: number;
    walletInfo: UserWallet;
}

/**
* MembershipStatusGraph - 2024-04-02
* Raaphe
*
* AutoPass
*/

const MembershipStatusGraph: FC<MembershipStatusGraph> = ({daysUntilExpiry, walletInfo}) => {

    // const [daysPassed, setDaysPassed] = useState(ClientUtil.msToDays());
    const navigate = useNavigate()

    return (

        <>
            <CircularProgressbarWithChildren
                minValue={0}
                className="m-3"
                value={daysUntilExpiry}
                maxValue={walletInfo.membershipType?.membershipDurationDays ?? 1}
                styles={buildStyles({
                    pathColor: "#027FFF",
                    trailColor: "#D6D6D6",
                })}
            >
                <div style={{ fontSize: "125%" }}>
                    <strong>
                        {ClientUtil.isMembershipActive(walletInfo) ? `${daysUntilExpiry} days Remaining` :
                            <Button onClick={() => navigate("/products")}>See Options</Button>
                        }
                    </strong>
                </div>

            </CircularProgressbarWithChildren>

        </>

    );
}

export default MembershipStatusGraph;
import React, {FC} from "react";
import {buildStyles, CircularProgressbarWithChildren} from "react-circular-progressbar";
import {Button} from "@mui/material";
import {UserWallet} from "../../Service";

interface MembershipStatusGraph {
    daysUntilExpiry: number;
    walletInfo: UserWallet;
}

const MembershipStatusGraph: FC<MembershipStatusGraph> = ({daysUntilExpiry, walletInfo}) => {

    return (

        <>
            <CircularProgressbarWithChildren
                minValue={0}
                className="m-3"
                value={daysUntilExpiry}
                maxValue={walletInfo.membershipType?.membershipDurationDays ?? 1}
                styles={buildStyles({
                    pathColor: "#027FFF",
                    trailColor: "#D6D6D6"
                })}
            >
                <div style={{ fontSize: "125%" }}>
                    <strong>
                        {walletInfo.membershipActive ? `${daysUntilExpiry} days Remaining` :
                            <Button>See Options</Button>
                        }

                    </strong>
                </div>

            </CircularProgressbarWithChildren>

        </>

    );
}

export default MembershipStatusGraph;
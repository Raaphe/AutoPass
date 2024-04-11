import { Dict } from "styled-components/dist/types";
import ClientAuthService from "./ClientAuthService";
import {Membership, UserWallet, WalletControllerApi} from "./Service";
import React from "react";

/**
* ClientUtil - 2024-04-02
* Raaphe
*
* AutoPass
*/
class UtilService {


    private months: Dict = {
        0: "January",
        1: "February",
        2: "March",
        3: "April",
        4: "May",
        5: "June",
        6: "July",
        7: "August",
        8: "September",
        9: "October",
        10: "November",
        11: "December",
    }

    /**
     * Converts an int representing the time in ms since Jan 1st 1970 to a `Date`.
     */
    public convertMsToDate(time: number): Date {
        return new Date(time);
    }

    /**
     * Gets a friendly formated date from the ms date time format.
     */
    public getFriendlyDateFromMs(time: number): string {
        var date: Date = this.convertMsToDate(time);
        return `${date.getFullYear()}, ${date.getDate()} ${this.months[date.getUTCMonth()]}`;
    }


    /**
     * Gets a friendly formated date from a `Date`.
     */
    public getFriendlyDateFromDate(date: Date): string {
        return `${date.getFullYear()}, ${date.getDate()} ${this.months[date.getUTCMonth()]}`;
    }

    /**
     * Get Current Date.
     */
    public getUTCNow(): Date {
        return new Date();
    }

    /**
     * Converts ms to days.
     * @param t Time in ms.
     * @returns The number of days
     */
    public msToDays(t: number) {
        return Math.ceil(t / (24 * 60 * 60 * 1000));
    }

    /**
     * Checks if a user's wallet's membership is ongoing.
     * @param userWallet The user's wallet.
     */
    public isMembershipActive(userWallet: UserWallet) : boolean {
        return this.isDateFuture(userWallet?.memberShipEnds ?? 0);
    }

    /**
     * Checks if a date (in ms unix epoch) is ahead of current time.
     * @param date The date to verify if is in the future.
     */
    public isDateFuture(date: number) {
        return Date.now() < date;
    }

    /**
     * Takes a scanner email and extracts the bus number.
     * @param email The target scanner email.
     * @returns The found bus number
     */
    public getBusNumberFromEmail(email: string) : number {

        const match = email.match(/.*_(\d+)@/);

        if (match) {
            return parseInt(match[1]);
        }
        else return -1;
    }

    public getUserWalletInfo = async (
        setDaysUntilExpiry : React.Dispatch<React.SetStateAction<number>>,
        setWalletInfo:  React.Dispatch<React.SetStateAction<UserWallet>>,
        currentWalletInfo: UserWallet
    ) => {
    const config = ClientAuthService.getApiConfig();
    const walletAPI = new WalletControllerApi(config);

    await walletAPI.getUserWalletByUserId(ClientAuthService.getUserId())
    
        .then(res => {
            if (res.status !== 200) return;
            setWalletInfo(res.data);

            setDaysUntilExpiry(this.isDateFuture(res.data.memberShipEnds ?? 0) ?
                utilService.msToDays(res.data.memberShipEnds ?? 0) - utilService.msToDays(this.getUTCNow().getTime())
                :
                0
            );
        })
        .catch(_ => {
        })
    }
}

const utilService = new UtilService();

export default utilService;

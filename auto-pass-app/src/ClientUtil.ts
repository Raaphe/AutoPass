import { Dict } from "styled-components/dist/types";

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
}

const utilService = new UtilService();

export default utilService;

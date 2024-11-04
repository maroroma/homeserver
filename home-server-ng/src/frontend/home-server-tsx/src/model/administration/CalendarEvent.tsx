import Formatters from "../../components/Formatters";

export class CalendarFixedDate {

    public static toDateString(input: CalendarFixedDate): string {
        return `${Formatters.leadingZero(input.day)}/${Formatters.leadingZero(input.month)}`;
    }


    constructor(public day: number, public month: number) { }
}

export class CalendarEvent {

    public static default(): CalendarEvent {
        return new CalendarEvent("",
            false,
            new CalendarFixedDate(1, 1),
            new CalendarFixedDate(1, 1),
            "Nouvel event",
            "",
        );
    }

    public static sorter(): (ce1:CalendarEvent, ce2:CalendarEvent) => number {
        return (c1, c2) => {
            if (c1.dayEvent === c2.dayEvent) {
                return 0;
            }

            if (c1.dayEvent === true) {
                return -1;
            }

            return 1;
        }
    }



    constructor(public id: string,
        public dayEvent: boolean,
        public startDate: CalendarFixedDate,
        public endDate: CalendarFixedDate,
        public description: string,
        public utf8IconsCode: string,
    ) {
    }
}
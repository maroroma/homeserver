export default class BootstrapBreakPoints {


    public static SMALL: BootstrapBreakPoints = new BootstrapBreakPoints("sm");
    public static MEDIUM: BootstrapBreakPoints = new BootstrapBreakPoints("md");
    public static LARGE: BootstrapBreakPoints = new BootstrapBreakPoints("lg");
    public static EXTRA_LARGE: BootstrapBreakPoints = new BootstrapBreakPoints("xl");
    public static EXTRA_EXTRA_LARGE: BootstrapBreakPoints = new BootstrapBreakPoints("xxl");

    constructor(public value:string){}
}
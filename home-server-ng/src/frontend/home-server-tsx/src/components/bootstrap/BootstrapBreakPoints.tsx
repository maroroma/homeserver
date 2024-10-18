export default class BootstrapBreakPoints {


    // 576
    public static SMALL: BootstrapBreakPoints = new BootstrapBreakPoints("sm");
    // 768
    public static MEDIUM: BootstrapBreakPoints = new BootstrapBreakPoints("md");
    // 992
    public static LARGE: BootstrapBreakPoints = new BootstrapBreakPoints("lg");
    // 1200
    public static EXTRA_LARGE: BootstrapBreakPoints = new BootstrapBreakPoints("xl");
    // 1400
    public static EXTRA_EXTRA_LARGE: BootstrapBreakPoints = new BootstrapBreakPoints("xxl");

    constructor(public value: string) { }
}
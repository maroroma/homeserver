package maroroma.homeserverng.iot.model;

public class SirenIotComponent extends AbstractIotComponent<SirenIotComponent> {
    public SirenIotComponent(IotComponentDescriptor componentDescriptor) {
        super(componentDescriptor, "no glyphicon");
    }

    public BeepResult beeeeeeep() {
        System.out.println("BEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEP");
        System.out.println("BEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEP");
        System.out.println("BEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEP");
        System.out.println("BEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEP");
        return BeepResult.builder()
                .beepResult(this.basicGet(uriBuilder -> uriBuilder.pathSegment("siren", "on")))
                .siren(this.getComponentDescriptor())
                .build();
    }

    public BeepResult unbeeeeeeep() {
        System.out.println("UNBEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEP");
        System.out.println("UNBEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEP");
        System.out.println("UNBEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEP");
        System.out.println("UNBEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEP");
        return BeepResult.builder()
                .beepResult(this.basicGet(uriBuilder -> uriBuilder.pathSegment("siren", "off")))
                .siren(this.getComponentDescriptor())
                .build();
    }
}

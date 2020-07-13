package maroroma.homeserverng.iot.model;

public class SirenIotComponent extends AbstractIotComponent<SirenIotComponent> {
    public SirenIotComponent(IotComponentDescriptor componentDescriptor) {
        super(componentDescriptor, "no glyphicon");
    }

    public void beeeeeeep() {
        System.out.println("BEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEP");
        System.out.println("BEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEP");
        System.out.println("BEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEP");
        System.out.println("BEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEP");
    }
}

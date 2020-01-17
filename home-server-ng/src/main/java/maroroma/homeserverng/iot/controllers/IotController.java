package maroroma.homeserverng.iot.controllers;

import maroroma.homeserverng.iot.IotModuleDescriptor;
import maroroma.homeserverng.iot.model.AbstractIotComponent;
import maroroma.homeserverng.iot.model.BuzzRequest;
import maroroma.homeserverng.iot.services.BuzzerService;
import maroroma.homeserverng.iot.services.IotServiceImpl;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Endpoints pour la gestion des composants IOTs
 */
@HomeServerRestController(moduleDescriptor = IotModuleDescriptor.class)
public class IotController {

    private final IotServiceImpl iotService;
    private final BuzzerService buzzerService;

    public IotController(IotServiceImpl iotService, BuzzerService buzzerService) {
        this.iotService = iotService;
        this.buzzerService = buzzerService;
    }

    /**
     * Appelé par composant iot pour s'enregistrer auprès du serveur lors de son démarrage
     * @param id address mac
     * @param componentType -
     * @param ipAddress -
     * @param name -
     * @return -
     */
    @GetMapping("${homeserver.api.path:}/iot/register")
    public ResponseEntity<Boolean> registerIotComponent(
            @RequestParam("id") String id,
            @RequestParam("componentType") String componentType,
            @RequestParam("ipAddress") String ipAddress,
            @RequestParam("name") String name
    ) {
        return ResponseEntity.ok(this.iotService.registerComponent(id, ipAddress, componentType, name));
    }

    @GetMapping("${homeserver.api.path:}/iot/components")
    public ResponseEntity<List<AbstractIotComponent<?>>> getAllIotComponents() {
        return ResponseEntity.ok(this.iotService.getAllIotComponents());
    }

    @PostMapping("${homeserver.api.path:}/iot/components/buzzers")
    public void buzz(@RequestBody  BuzzRequest buzzRequest) {
        this.buzzerService.buzz(buzzRequest);
    }
}

package maroroma.homeserverng.iot.controllers;

import maroroma.homeserverng.iot.IotModuleDescriptor;
import maroroma.homeserverng.iot.model.AbstractIotComponent;
import maroroma.homeserverng.iot.model.BuzzRequest;
import maroroma.homeserverng.iot.model.MiniSprite;
import maroroma.homeserverng.iot.model.UltraBasicIotComponentForRestExchange;
import maroroma.homeserverng.iot.services.BuzzerService;
import maroroma.homeserverng.iot.services.IotServiceImpl;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("${homeserver.api.path:}/iot/components")
    public ResponseEntity<List<AbstractIotComponent<?>>> updateComponent(@RequestBody UltraBasicIotComponentForRestExchange component) throws HomeServerException {
        return ResponseEntity.ok(this.iotService.updateComponent(component));
    }

    @PostMapping("${homeserver.api.path:}/iot/components/buzzers")
    public void buzz(@RequestBody  BuzzRequest buzzRequest) {
        this.buzzerService.buzz(buzzRequest);
    }

    @GetMapping("${homeserver.api.path:}/iot/minisprites")
    public ResponseEntity<List<MiniSprite>> getAllSprites() {
        return ResponseEntity.ok(this.iotService.getAllSprites());
    }

    @PostMapping("${homeserver.api.path:}/iot/minisprites")
    public ResponseEntity<List<MiniSprite>> createNewMiniSprite(@RequestBody MiniSprite newSprite) throws HomeServerException {
        return ResponseEntity.ok(this.iotService.createNewMiniSprite(newSprite));
    }
    @PutMapping("${homeserver.api.path:}/iot/minisprites")
    public ResponseEntity<List<MiniSprite>> updateMiniSprite(@RequestBody MiniSprite newSprite) throws HomeServerException {
        return ResponseEntity.ok(this.iotService.updateMiniSprite(newSprite));
    }

    @DeleteMapping("${homeserver.api.path:}/iot/minisprites/{id}")
    public ResponseEntity<List<MiniSprite>> deleteMiniSprite(@PathVariable("id") String spriteId) throws HomeServerException {
        return ResponseEntity.ok(this.iotService.deleteSprite(spriteId));
    }

    @DeleteMapping("${homeserver.api.path:}/iot/components/{id}")
    public ResponseEntity<List<AbstractIotComponent<?>>> deleteComponent(@PathVariable("id") String componentId) throws HomeServerException {
        return ResponseEntity.ok(this.iotService.removeComponent(componentId));
    }
}

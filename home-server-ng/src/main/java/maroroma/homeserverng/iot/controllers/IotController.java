package maroroma.homeserverng.iot.controllers;

import maroroma.homeserverng.iot.IotModuleDescriptor;
import maroroma.homeserverng.iot.model.*;
import maroroma.homeserverng.iot.services.AlarmManager;
import maroroma.homeserverng.iot.services.BuzzerService;
import maroroma.homeserverng.iot.services.IotServiceImpl;
import maroroma.homeserverng.iot.services.TriggerService;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import org.aspectj.weaver.ast.Test;
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
    private final TriggerService triggerService;
    private final AlarmManager alarmManager;

    public IotController(IotServiceImpl iotService, BuzzerService buzzerService, TriggerService triggerService, AlarmManager alarmManager) {
        this.iotService = iotService;
        this.buzzerService = buzzerService;
        this.triggerService = triggerService;
        this.alarmManager = alarmManager;
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

    /**
     * Retourne l'ensemble des composants IOT
     * @return
     */
    @GetMapping("${homeserver.api.path:}/iot/components")
    public ResponseEntity<List<AbstractIotComponent<?>>> getAllIotComponents() {
        return ResponseEntity.ok(this.iotService.getAllIotComponents());
    }

    /**
     * Mise à jour d'un composant.
     * @param component -
     * @return -
     * @throws HomeServerException -
     */
    @PutMapping("${homeserver.api.path:}/iot/components")
    public ResponseEntity<List<AbstractIotComponent<?>>> updateComponent(@RequestBody UltraBasicIotComponentForRestExchange component) throws HomeServerException {
        return ResponseEntity.ok(this.iotService.updateComponent(component));
    }

    @PutMapping("${homeserver.api.path:}/iot/components/triggers/{triggerId}")
    public void triggers(@PathVariable("triggerId") String triggerID, @RequestBody TriggeringDescription triggeringDescription) throws HomeServerException {
        this.triggerService.triggers(triggerID, triggeringDescription);
    }

    /**
     * Appelé par un composant de type trigger, permet de déclencher (pour l'instant) l'alarme
     * @param triggerID identifiant du trigger
     */
    @GetMapping("${homeserver.api.path:}/iot/components/triggered/{triggerId}")
    public void triggered(@PathVariable("triggerId") String triggerID) {
        this.triggerService.triggered(triggerID);
    }

    /**
     * Demande démission d'un buzz. La requête identifie le buzzer à solliciter
     * @param buzzRequest -
     */
    @PostMapping("${homeserver.api.path:}/iot/components/buzzers")
    public void buzz(@RequestBody  BuzzRequest buzzRequest) {
        this.buzzerService.buzz(buzzRequest);
    }

    /**
     * Retourne l'ensemble des triggers
     * @return -
     */
    @GetMapping("${homeserver.api.path:}/iot/components/triggers")
    public ResponseEntity<List<TriggerIotComponent>> getAllTriggers() {
        return ResponseEntity.ok(this.triggerService.getAllComponents());
    }

    /**
     * Retourne l'ensemble des sprites managés pour les composants buzzers
     * @return -
     */
    @GetMapping("${homeserver.api.path:}/iot/minisprites")
    public ResponseEntity<List<MiniSprite>> getAllSprites() {
        return ResponseEntity.ok(this.iotService.getAllSprites());
    }

    /**
     * Création d'un sprite
     * @param newSprite sprite à crééer
     * @return -
     * @throws HomeServerException -
     */
    @PostMapping("${homeserver.api.path:}/iot/minisprites")
    public ResponseEntity<List<MiniSprite>> createNewMiniSprite(@RequestBody MiniSprite newSprite) throws HomeServerException {
        return ResponseEntity.ok(this.iotService.createNewMiniSprite(newSprite));
    }

    /**
     * Mise à jour d'un sprite
     * @param newSprite -
     * @return -
     * @throws HomeServerException -
     */
    @PutMapping("${homeserver.api.path:}/iot/minisprites")
    public ResponseEntity<List<MiniSprite>> updateMiniSprite(@RequestBody MiniSprite newSprite) throws HomeServerException {
        return ResponseEntity.ok(this.iotService.updateMiniSprite(newSprite));
    }

    /**
     * Suppression d'un sprite
     * @param spriteId id du sprite à supprimer
     * @return -
     * @throws HomeServerException -
     */
    @DeleteMapping("${homeserver.api.path:}/iot/minisprites/{id}")
    public ResponseEntity<List<MiniSprite>> deleteMiniSprite(@PathVariable("id") String spriteId) throws HomeServerException {
        return ResponseEntity.ok(this.iotService.deleteSprite(spriteId));
    }

    /**
     * Suppression d'un composant IOT
     * @param componentId identifiant du composant à supprimer
     * @return -
     * @throws HomeServerException -
     */
    @DeleteMapping("${homeserver.api.path:}/iot/components/{id}")
    public ResponseEntity<List<AbstractIotComponent<?>>> deleteComponent(@PathVariable("id") String componentId) throws HomeServerException {
        return ResponseEntity.ok(this.iotService.removeComponent(componentId));
    }

    /**
     * Permet de connaitre le status de l'alarme
     * @return true si l'alarme est armée
     */
    @GetMapping("${homeserver.api.path:}/iot/alarm/status")
    public ResponseEntity<Boolean> alarmStatus() {
        return ResponseEntity.ok(this.alarmManager.isArmed());
    }

    /**
     * Produit un rapport avec les status des sirènes (les fait biper) et des triggers
     * @return -
     */
    @GetMapping("${homeserver.api.path:}/iot/alarm/testResults")
    public ResponseEntity<TestAlarmResults> testAlarmSystem() {
        return ResponseEntity.ok(this.alarmManager.testAlarmSystem());
    }

    /**
     * Tentative de changement de status de l'alarme (armement ou non)
     * @param alarmChangeStatusRequest -
     * @return -
     */
    @PutMapping("${homeserver.api.path:}/iot/alarm")
    public ResponseEntity<Boolean> armAlarm(@RequestBody AlarmChangeStatusRequest alarmChangeStatusRequest) {
        return ResponseEntity.ok(this.alarmManager.changeStatus(alarmChangeStatusRequest));
    }

}

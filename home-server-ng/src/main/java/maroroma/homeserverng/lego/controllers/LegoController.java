package maroroma.homeserverng.lego.controllers;

import maroroma.homeserverng.lego.LegoModuleDescriptor;
import maroroma.homeserverng.lego.model.Brick;
import maroroma.homeserverng.lego.services.LegoService;
import maroroma.homeserverng.tools.annotations.HomeServerRestController;
import maroroma.homeserverng.tools.exceptions.HomeServerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@HomeServerRestController(moduleDescriptor = LegoModuleDescriptor.class)
public class LegoController {

    private final LegoService legoService;

    public LegoController(LegoService legoService) {
        this.legoService = legoService;
    }

    @GetMapping("${homeserver.api.path:}/lego/bricks")
    public ResponseEntity<List<Brick>> getAllBricks() {
        return ResponseEntity.ok(this.legoService.getAllBricks());
    }

    @PostMapping("${homeserver.api.path:}/lego/bricks")
    public ResponseEntity<List<Brick>> createNewBrick(@RequestBody Brick newBrick) throws HomeServerException {
        return ResponseEntity.ok(this.legoService.createBrick(newBrick));
    }

    @PutMapping("${homeserver.api.path:}/lego/bricks")
    public ResponseEntity<List<Brick>> updateBricks(@RequestBody List<Brick> updatedBricks) {
        return ResponseEntity.ok(this.legoService.updateBricks(updatedBricks));
    }

    @DeleteMapping("${homeserver.api.path:}/lego/bricks/{id}")
    public ResponseEntity<List<Brick>> deleteOneBrick(@PathVariable("id") final String brickId) throws HomeServerException {
        return ResponseEntity.ok(this.legoService.deleteBrick(brickId));
    }

    @GetMapping("${homeserver.api.path:}/lego/bricks/{id}/picture")
    public void downloadFile(@PathVariable("id") final String brickId, final HttpServletResponse response) throws HomeServerException {
        this.legoService.getBrickPicture(brickId, response);
    }
}

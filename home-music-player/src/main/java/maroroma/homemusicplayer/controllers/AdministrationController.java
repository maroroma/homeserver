package maroroma.homemusicplayer.controllers;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.administration.ApplicationProperty;
import maroroma.homemusicplayer.services.AdministrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdministrationController {

    private final AdministrationService administrationService;

    @GetMapping("api/musicplayer/administration/properties")
    public ResponseEntity<List<ApplicationProperty>> getApplicationProperties() {
        return ResponseEntity.ok(administrationService.getApplicationProperties());
    }

}

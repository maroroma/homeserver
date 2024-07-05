package maroroma.homemusicplayer.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.services.LocalResourcesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LocalResourcesController {

    private final LocalResourcesService localResourcesService;

    @GetMapping("musicplayer/localresources/thumbs/{base64Path}")
    public void getThumb(@PathVariable("base64Path") final String base64FileName, final HttpServletResponse response) {
        this.localResourcesService.getThumb(base64FileName, response);
    }

    @GetMapping("musicplayer/localresources/fanarts/{base64Path}")
    public void getFanart(@PathVariable("base64Path") final String base64FileName, final HttpServletResponse response) {
        this.localResourcesService.getFanart(base64FileName, response);
    }



}

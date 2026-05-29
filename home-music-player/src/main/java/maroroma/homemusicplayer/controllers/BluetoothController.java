package maroroma.homemusicplayer.controllers;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.services.BluetoothManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BluetoothController {

    private final BluetoothManager bluetoothManager;

    @GetMapping("api/musicplayer/bluetooth/status/{session}")
    public ResponseEntity<String> getFullPlayerStatus(@PathVariable boolean session) {
        return ResponseEntity.ok("result1" + bluetoothManager.getstatus(session) + " result 2 " + bluetoothManager.bluetoothStatus());
    }

}

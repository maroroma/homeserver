package maroroma.homemusicplayer.controllers;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.bluetooth.BluetoothStatus;
import maroroma.homemusicplayer.services.BluetoothManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BluetoothController {

    private final BluetoothManager bluetoothManager;

    @GetMapping("api/musicplayer/bluetooth/status")
    public ResponseEntity<BluetoothStatus> getFullPlayerStatus() {
        return ResponseEntity.ok(bluetoothManager.getStatus());
    }


    @GetMapping("api/musicplayer/bluetooth/power/{status}")
    public ResponseEntity<BluetoothStatus> power(@PathVariable boolean status) {
        if (status) {
            return ResponseEntity.ok(bluetoothManager.powerOn());
        } else {
            return ResponseEntity.ok(bluetoothManager.powerOff());
        }
    }

}

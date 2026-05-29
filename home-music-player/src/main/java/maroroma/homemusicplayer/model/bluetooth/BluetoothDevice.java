package maroroma.homemusicplayer.model.bluetooth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BluetoothDevice {
    private String name;
    private boolean connected;
}

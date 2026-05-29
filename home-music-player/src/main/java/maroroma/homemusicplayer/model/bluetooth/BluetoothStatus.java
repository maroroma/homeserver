package maroroma.homemusicplayer.model.bluetooth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BluetoothStatus {

    public static BluetoothStatus off(final String detail) {
        return BluetoothStatus.builder()
                .discoverable(false)
                .on(false)
                .pairable(false)
                .detail(detail)
                .build();
    }

    private boolean discoverable;
    private boolean on;
    private boolean pairable;
    private String detail;
}

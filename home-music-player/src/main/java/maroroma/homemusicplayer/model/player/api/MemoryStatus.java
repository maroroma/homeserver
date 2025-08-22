package maroroma.homemusicplayer.model.player.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemoryStatus {
    private long heapSize;
    private long heapMaxSize;
    private long heapFreeSize;
    private int memoryCacheSize;
    private double percentageUsedMemory;
}

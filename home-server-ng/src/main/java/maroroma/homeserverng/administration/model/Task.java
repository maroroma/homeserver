package maroroma.homeserverng.administration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {
    String id;
    String supplierType;
    String title;
    boolean isRunning;
    float done;
    float remaining;
    String labelTotal;
    String labelDone;
    String labelRemaining;

    @JsonIgnore
    public String generateKey() {
        return new StringBuilder(this.supplierType)
                .append("#")
                .append(this.id)
                .append("#")
                .append(this.title)
                .append("#")
                .append(this.isRunning)
                .toString();
    }
}

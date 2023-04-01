package maroroma.homeserverng.remote.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class InvalidCommandDescriptor {
    private String emails;
    private String subject;
}

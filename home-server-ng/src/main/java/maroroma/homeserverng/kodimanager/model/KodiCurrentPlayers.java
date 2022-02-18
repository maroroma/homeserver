package maroroma.homeserverng.kodimanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KodiCurrentPlayers {
    private String kodiUrl;
    private List<CurrentPlayer> currentPlayers;
}

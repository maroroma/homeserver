package maroroma.homeserverng.tools.kodi.responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class Result<T> {

	private T item;
}

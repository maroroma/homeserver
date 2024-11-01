package maroroma.homeserverng.kiosk.model.weather;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// https://gist.github.com/stellasphere/9490c195ed2b53c707087c8c2db4ec0c

// http://openweathermap.org/img/wn/11d@2x.png


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherCode {
    int wmoCode;
    String description;
    String imageUrl;

}

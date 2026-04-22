package maroroma.homemusicplayer.services;

import lombok.RequiredArgsConstructor;
import maroroma.homemusicplayer.model.administration.ApplicationProperty;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AdministrationService {

    private final ConfigurableEnvironment configurableEnvironment;

    public List<ApplicationProperty> getApplicationProperties() {

        return configurableEnvironment.getPropertySources().stream()
                .filter(MapPropertySource.class::isInstance)
                .map(MapPropertySource.class::cast)
                .map(MapPropertySource::getPropertyNames)
                .flatMap(Stream::of)
                .map(aPropertyName -> ApplicationProperty.builder()
                        .propertyName(aPropertyName)
                        .propertyValue(configurableEnvironment.getProperty(aPropertyName))
                        .build()
                ).toList();
    }
}

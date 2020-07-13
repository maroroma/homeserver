package maroroma.homeserverng.iot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * UTilisé pour la sauvegarde des composants en base
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IotComponentDescriptor {
    private String id;
    private String componentType;
    private String ipAddress;
    private String name;
    private List<IotProperty> properties = new ArrayList<>();

    public IotComponentDescriptor withProperty(String propertyName, String propertyValue) {

        this.properties
                .removeIf(filterOnName(propertyName));

        this.properties.add(IotProperty.builder()
                .propertyName(propertyName)
                .propertyValue(propertyValue)
                .build());

        return this;
    }

    public IotComponentDescriptor withPropertyList(String propertyName, String valueToAdd) {
        List<String> listFromProperties = this.propertyList(propertyName)
                .orElseGet(ArrayList::new);

        listFromProperties.add(valueToAdd);

        return withProperty(propertyName, String.join(",", listFromProperties));
    }

    public Optional<String> property(String propertyName) {
        return this.properties.stream()
                .filter(filterOnName(propertyName))
                .findFirst()
                .map(IotProperty::getPropertyValue);
    }

    public Optional<List<String>> propertyList(String propertyName) {
        return this.property(propertyName)
                .map(rawList -> rawList.split(","))
                .map(Arrays::asList);
    }


    protected Predicate<IotProperty> filterOnName(String propertyName) {
        return oneProperty -> propertyName.equals(oneProperty.getPropertyName());
    }

}

package maroroma.homeserverng.iot.model;

import lombok.Data;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Définit les composants de base d'un IotComponent
 */
@Data
public abstract class AbstractIotComponent<T extends AbstractIotComponent<T>> {

    private final IotComponentDescriptor componentDescriptor;
    private final String glyphicon;
    private int timeout;
    private boolean available;

    public AbstractIotComponent(IotComponentDescriptor componentDescriptor, String glyphicon) {
        this.componentDescriptor = componentDescriptor;
        this.glyphicon = glyphicon;
    }

    public String ipAddress() {
        return this.componentDescriptor.getIpAddress();
    }

    public String id() {
        return this.componentDescriptor.getId();
    }

    public String name() {
        return this.componentDescriptor.getName();
    }

    public String componentType() {
        return this.componentDescriptor.getComponentType();
    }

    /**
     * Génère le restTemplate par défaut pour les appels https aux iotComponents
     *
     * @return -
     */
    protected RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(this.timeout))
                .setReadTimeout(Duration.ofMillis(this.timeout))
                .build();
    }

    protected String url() {
        return "http://" + this.ipAddress();
    }

    /**
     * Permet de pinger le component sur son url de base
     * Si un status 200 est renvoyé, on considère que le composant est utilisable
     *
     * @return this
     */
    public T updateStatus() {
        try {
            this.setAvailable(basicGet());
        } catch (Exception e) {
            this.setAvailable(false);
        }
        return returnThis();
    }

    protected boolean basicGet() {
        return basicGet(null);
    }

    protected boolean basicGet(Consumer<UriComponentsBuilder> updatesOnUriBuilder) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(url());

        if (updatesOnUriBuilder != null) {
            updatesOnUriBuilder.accept(builder);
        }

        return HttpStatus.OK.equals(restTemplate().getForEntity(builder.toUriString(), String.class).getStatusCode());
    }

    public T withTimeout(int timeout) {
        this.timeout = timeout;
        return returnThis();
    }

    public T withProperty(String propertyName, String propertyValue) {

        this.componentDescriptor.withProperty(propertyName, propertyValue);

        return returnThis();
    }

    public T withPropertyList(String propertyName, String valueToAdd) {
        this.componentDescriptor.withPropertyList(propertyName, valueToAdd);
        return returnThis();
    }

    public Optional<String> property(String propertyName) {
        return this.componentDescriptor.property(propertyName);
    }

    public Optional<List<String>> propertyList(String propertyName) {
        return this.componentDescriptor.propertyList(propertyName);
    }

    /**
     * Retourne l'instance castée dans l'implementation qui va bien
     *
     * @return this
     */
    @SuppressWarnings("unchecked")
    protected T returnThis() {
        return (T) this;
    }
}

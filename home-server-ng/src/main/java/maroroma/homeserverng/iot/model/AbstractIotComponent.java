package maroroma.homeserverng.iot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.function.Consumer;

/**
 * Définit les composants de base d'un IotComponent
 */
@Data
public abstract class AbstractIotComponent<T extends AbstractIotComponent<T>> {

    private final IotComponentDescriptor componentDescriptor;
    private final String glyphicon;
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
     * @return -
     */
    protected RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(1000)
                .setReadTimeout(1000)
                .build();
    }

    protected String url() {
        return "http://" + this.ipAddress();
    }

    /**
     * Permet de pinger le component sur son url de base
     * Si un status 200 est renvoyé, on considère que le composant est utilisable
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

        if(updatesOnUriBuilder != null) {
            updatesOnUriBuilder.accept(builder);
        }

        return HttpStatus.OK.equals(restTemplate().getForEntity(builder.toUriString(), String.class).getStatusCode());
    }

    /**
     * Retourne l'instance castée dans l'implementation qui va bien
     * @return this
     */
    @SuppressWarnings("unchecked")
    protected T returnThis() {
        return (T) this;
    }
}

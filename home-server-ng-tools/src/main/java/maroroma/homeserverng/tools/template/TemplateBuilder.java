package maroroma.homeserverng.tools.template;

import maroroma.homeserverng.tools.exceptions.RuntimeHomeServerException;
import maroroma.homeserverng.tools.exceptions.Traper;
import maroroma.homeserverng.tools.helpers.Tuple;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Classe custom pour la construction et la résolution de templates
 */
public class TemplateBuilder {

    private final Map<String, String> parameters = new HashMap<>();
    private String templatePath;

    public static TemplateBuilder create() {
        return new TemplateBuilder();
    }

    private TemplateBuilder() {

    }

    public TemplateBuilder addParameter(String key, String value) {
        this.parameters.put(key, value);
        return this;
    }

    public <REPEATED_ITEM> TemplateBuilder addArrayParameter(String key, List<REPEATED_ITEM> items, Function<REPEATED_ITEM, String> itemToLineTransformer) {
        return this.addParameter(key, items
                .stream()
                .map(itemToLineTransformer)
                .collect(Collectors.joining()));
    }

    public TemplateBuilder withTemplate(String templatePath) {
        this.templatePath = templatePath;
        return this;
    }

    /**
     * Retourne une chaine issue d'un template dont les variables ont été résolues
     * @return
     */
    public String resolve() {
        return Traper.trapWithOptional(() -> Files.readString(new ClassPathResource(this.templatePath).getFile().toPath()))
                .map(this::resolve)
                .orElseThrow(() -> new RuntimeHomeServerException("Impossible de charger le template " + this.templatePath));
    }

    private String resolve(String rawTemplate) {
        // résolution des token pour le remplacement
        return Tuple.fromMap(this.parameters,
                        oneKey -> "\\$\\{" + oneKey + "\\}",
                        Function.identity())
                .stream()
                .reduce(rawTemplate,
                        (oldString, oneTuple) -> oldString.replaceAll(oneTuple.getItem1(), oneTuple.getItem2()),
                        (string1, string2) -> string2);
    }

}

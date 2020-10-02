package maroroma.homeserverng.iot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import maroroma.homeserverng.tools.helpers.Predicates;
import maroroma.homeserverng.tools.helpers.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestAlarmResults {


    private AlarmHealth status;
    private List<String> messages;
    private List<BeepResult> beepResults;
    private List<TriggerIotComponent> triggerIotComponentsWithStatus;
    private List<SirenIotComponent> sirenIotComponentsWithStatus;

    public TestAlarmResults resolveStatus() {

        this.status = AlarmHealth.OK;

        this.messages = Stream.of(
                    resolveKO(this.beepResults, BeepResult::isBeepResult, "Aucune sirère n'a pu être émise"),
                    resolveKO(this.sirenIotComponentsWithStatus, SirenIotComponent::isAvailable, "Aucune sirène n'est disponible"),
                    resolveKO(this.triggerIotComponentsWithStatus, TriggerIotComponent::isAvailable, "Aucun trigger n'est disponible"),
                    resolveWarning(this.beepResults, BeepResult::isBeepResult, "Certaines sirènes n'ont pu être émises"),
                    resolveWarning(this.sirenIotComponentsWithStatus, SirenIotComponent::isAvailable, "Certaines sirènes sont injoignables"),
                    resolveWarning(this.triggerIotComponentsWithStatus, TriggerIotComponent::isAvailable, "Certaines triggers sont injoignables")
                ).filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return this;
    }

    private  <T> Optional<String> resolveKO(List<T> listToTest, Predicate<T> noneMatchPredicate, String message) {
        if (listToTest.stream().noneMatch(noneMatchPredicate)) {
            this.status = AlarmHealth.KO;
            return Optional.of(message);
        }
        return Optional.empty();
    }

    private  <T> Optional<String> resolveWarning(List<T> listToTest, Predicate<T> notMatching, String message) {
        if (listToTest.stream().anyMatch(Predicates.not(notMatching)) && !listToTest.stream().noneMatch(notMatching)) {
            this.status = this.status == AlarmHealth.KO ? AlarmHealth.KO : AlarmHealth.WARNING;
            return Optional.of(message);
        }
        return Optional.empty();
    }

    public enum AlarmHealth {
        OK,
        WARNING,
        KO
    }
}

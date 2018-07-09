package maroroma.homeserverng.tools.helpers;

import java.util.Optional;
import java.util.function.Consumer;


/**
 * à revoir ça me plait pas, usine à gaz pour rien
 * @param <T>
 * @param <U>
 */
public class ExclusifTuple<T, U> {


    private Tuple<Optional<T>, Optional<U>> innerTuple = Tuple.from(Optional.empty(), Optional.empty());

    private ExclusifTuple(Optional<T> option1, Optional<U> option2) {
        this.innerTuple = Tuple.from(option1, option2);
    }

    public static <T, U> ExclusifTuple<T, U> empty() {
        return new ExclusifTuple<>(Optional.empty(), Optional.empty());
    }

    public ExclusifTuple<T, U> option1(T option) {
        this.innerTuple = Tuple.from(Optional.of(option), Optional.empty());
        return this;
    }

    public ExclusifTuple<T, U> option2(U option) {
        this.innerTuple = Tuple.from(Optional.empty(), Optional.of(option));
        return this;
    }

    public T option1() {
        return this.innerTuple.getItem1().orElse(null);
    }

    public U option2() {
        return this.innerTuple.getItem2().orElse(null);
    }

    public boolean isOption1() {
        return this.innerTuple.getItem1().isPresent();
    }


    public boolean isOption2() {
        return this.innerTuple.getItem2().isPresent();
    }

    public boolean isEmpty() {
        return !this.innerTuple.getItem1().isPresent() && !this.innerTuple.getItem2().isPresent();
    }

    public ExclusifTupleApplyer<T, U> whenOption1(Consumer<T> consumer) {
        return new ExclusifTupleApplyer<>(consumer, this);
    }

    public static class ExclusifTupleApplyer<T, U> {
        private Consumer<T> forOpt1;
        private Consumer<U> forOpt2;
        ExclusifTuple<T, U> options;

        public ExclusifTupleApplyer(Consumer<T> forOpt1, ExclusifTuple<T, U> options) {
            this.forOpt1 = forOpt1;
            this.options = options;
        }

        public ExclusifTupleApplyer<T, U> whenOption2(Consumer<U> consumer) {
            this.forOpt2 = consumer;
            return this;
        }

        public void apply() {
            if(this.options.isOption1()) {
                this.forOpt1.accept(this.options.option1());
            } else if(this.options.isOption2()) {
                this.forOpt2.accept(this.options.option2());
            }

        }
    }





}

package tools;

import java.util.Arrays;
import java.util.List;

public interface DataBase {
    default boolean initialized(Class<?>... classes) {
        var ref = new Object() {
            Boolean answer = true;
        };
        List<Class<?>> checkNotNull = Arrays.asList(classes);
        checkNotNull.forEach(aClass -> {
                    if (aClass == null) {
                        ref.answer = true;
                    }
                }
        );
        return ref.answer;
    }

    // TODO GENERAE FUNCTIONS
/*    default void throwIfNull() {
        List<Class> checkNotNull = Arrays.asList(
                globalConfig.getClass(), colorConfig.getClass(), drawConfig.getClass());
        checkNotNull.forEach(aClass -> {
            if (aClass == null) {
                throw new RuntimeException("Uncompleted settings:" + aClass.getName());
            }
        });
    }*/

}

package snake.protocol;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.TemplateField;
import org.reflections.Reflections;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class MessageFactory {
    private static class MessageInfo {
        public String name;
        public Constructor<?> constructor;
        public HashMap<String, RecordComponent> recordComponents = new HashMap<>();
    }

    private static final HashMap<Class<?>, MessageInfo> messages = new HashMap<>();
    private static final HashMap<String, Class<?>> messageNames = new HashMap<>();

    static {
        loadMessages();
    }

    public static void loadMessages() {
        // Find all classes annotated with @Message
        Set<Class<?>> annotated = new Reflections("snake").getTypesAnnotatedWith(Message.class);

        for (Class<?> cls : annotated) {
            // Check if the class is a record
            if (!cls.isRecord()) {
                throw new RuntimeException("Class " + cls.getName() + " is annotated with @Message but is not a record");
            }

            MessageInfo info = new MessageInfo();
            info.name = cls.getAnnotation(Message.class).name();

            if (info.name.isEmpty()) {
                info.name = cls.getSimpleName();
            }

            // Find empty constructor
            Arrays.stream(cls.getConstructors()).findFirst().ifPresent(constructor -> {
                info.constructor = constructor;
            });

            // Check if the constructor is empty
            if (info.constructor == null) {
                throw new RuntimeException("Class " + cls.getName() + " is annotated with @Message but has no empty constructor");
            }

            // Find all fields
            for (var recordComponent : cls.getRecordComponents()) {
                info.recordComponents.put(recordComponent.getName(), recordComponent);
            }

            messages.put(cls, info);
            messageNames.put(info.name, cls);
        }
    }

    private static MessageInfo getMessageInfo(Class<?> type) {
        var info = messages.get(type);

        if (info == null) {
            throw new RuntimeException("Class " + type.getName() + " is not annotated with @Message");
        }

        return info;
    }

    private static MessageInfo getMessageInfo(String name) {
        return getMessageInfo(messageNames.get(name));
    }

    private static MessageInfo getMessageInfo(Object message) {
        return getMessageInfo(message.getClass());
    }

    public static TemplateField[] toTemplate(Object message) throws IllegalAccessException, InvocationTargetException {
        var info = getMessageInfo(message);

        TemplateField[] template = new TemplateField[info.recordComponents.size() + 1];
        template[0] = new ActualField(info.name);

        int i = 1;

        for (var recordComponent : info.recordComponents.values()) {
            var value = recordComponent.getAccessor().invoke(message);

            if (value == null) {
                template[i] = new FormalField(recordComponent.getType());
            } else {
                template[i] = new ActualField(value);
            }

            i++;
        }

        return template;
    }

    public static TemplateField[] toTemplateUnion(Object... messages) {
        // Find the most general type
        Integer desiredLength = null;

        return null;
    }

    public static Object[] toTuple(Object message) throws IllegalAccessException, InvocationTargetException {
        var info = getMessageInfo(message);

        Object[] tuple = new Object[info.recordComponents.size() + 1];

        tuple[0] = info.name;

        int i = 1;

        for (var recordComponent : info.recordComponents.values()) {
            tuple[i] = recordComponent.getAccessor().invoke(message);
            i++;
        }

        return tuple;
    }

    public static Record fromTuple(Object[] tuple) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        var info = getMessageInfo((String) tuple[0]);

        Object[] args = new Object[info.recordComponents.size()];

        int i = 0;

        for (var ignored : info.recordComponents.values()) {
            args[i] = tuple[i + 1];
            i++;
        }

        return (Record) info.constructor.newInstance(args);
    }
}

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
    private static class MessageValueEmpty {
    }

    private static class MessageInfo {
        public String name;
        public Constructor<?> constructor;
        public HashMap<String, RecordComponent> recordComponents = new HashMap<>();

        public boolean isCompatibleWith(MessageInfo other) {
            if (recordComponents.size() != other.recordComponents.size()) {
                return false;
            }

            for (var recordComponent : recordComponents.values()) {
                for (var otherRecordComponent : other.recordComponents.values()) {
                    // We only care about order and type
                    if (!recordComponent.getType().equals(otherRecordComponent.getType())) {
                        return false;
                    }
                }
            }

            return true;
        }
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

    public static TemplateField[] toTemplateUnion(Class<?>... messageTypes) {
        if (messageTypes.length == 0) {
            throw new RuntimeException("No messages provided");
        }

        // Ensure that all messages are of the same type
        for (var mi : messageTypes) {
            var infoi = getMessageInfo(mi);

            for (var mj : messageTypes) {
                if (mi == mj) {
                    continue;
                }

                var infoj = getMessageInfo(mj);

                if (!infoi.isCompatibleWith(infoj)) {
                    throw new RuntimeException("Messages are not compatible");
                }
            }
        }

        // Return fully generic template
        var info = getMessageInfo(messageTypes[0]);

        TemplateField[] template = new TemplateField[info.recordComponents.size() + 1];

        template[0] = new FormalField(String.class);

        int i = 1;

        for (var recordComponent : info.recordComponents.values()) {
            template[i] = new FormalField(recordComponent.getType());
            i++;
        }

        return template;
    }

    public static TemplateField[] toTemplateUnion(Object... messages) throws InvocationTargetException, IllegalAccessException {
        var types = Arrays.stream(messages).map(Object::getClass).toArray(Class<?>[]::new);
        var template = toTemplateUnion(types);

        Object[] valuesAtIndices = new Object[template.length - 1];

        Arrays.fill(valuesAtIndices, new MessageValueEmpty());

        for (var message : messages) {
            var info = getMessageInfo(message);

            int i = 0;

            for (var recordComponent : info.recordComponents.values()) {
                var previousValue = valuesAtIndices[i];
                var value = recordComponent.getAccessor().invoke(message);

                if (previousValue instanceof MessageValueEmpty) {
                    valuesAtIndices[i] = value;
                } else if (!previousValue.equals(value)) {
                    throw new RuntimeException("Messages are not compatible");
                }
                template[i + 1] = new ActualField(valuesAtIndices[i]);
                i++;
            }
        }

        return template;
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

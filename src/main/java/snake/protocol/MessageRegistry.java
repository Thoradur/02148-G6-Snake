package snake.protocol;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.TemplateField;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;

public class MessageRegistry {
    // Internal marker for empty values
    // To differentiate from null.
    private static class EmptyValue {
    }

    private static final HashMap<Class<? extends Record>, MessageFactory<? extends Record>> messages = new HashMap<>();
    private static final HashMap<String, Class<? extends Record>> messageNames = new HashMap<>();

    // Runtime initialization of message info
    static {
        var classes = new Reflections("snake").getTypesAnnotatedWith(Message.class);

        for (var cls : classes) {
            if (!Record.class.isAssignableFrom(cls)) {
                continue;
            }

            MessageFactory<?> factory = new MessageFactory<>(cls);
            messages.put((Class<? extends Record>) cls, factory);
            messageNames.put(factory.name, (Class<? extends Record>) cls);
        }
    }

    public static <T extends Record> MessageFactory<T> getMessageFactory(Class<T> type) {
        return (MessageFactory<T>) messages.get(type);
    }

    public static <T extends Record> MessageFactory<T> getMessageFactory(String name) {
        return (MessageFactory<T>) getMessageFactory(messageNames.get(name));
    }

    public static <T extends Record> MessageFactory<T> getMessageFactory(T message) {
        return (MessageFactory<T>) messages.get(message.getClass());
    }

    public static Record fromTuple(Object[] tuple) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        if (tuple.length == 0) {
            return null;
        }

        var name = (String) tuple[0];
        var messageFactory = getMessageFactory(name);

        if (messageFactory == null) {
            throw new RuntimeException("Unknown message type: " + name);
        }

        return messageFactory.fromTuple(tuple);
    }

    @SafeVarargs
    public static TemplateField[] getTemplateUnion(Class<? extends Record>... messageTypes) {
        if (messageTypes.length == 0) {
            throw new RuntimeException("Cannot create union template for empty list of message types");
        }

        for (var messageType : messageTypes) {
            if (!Record.class.isAssignableFrom(messageType)) {
                throw new RuntimeException("Class " + messageType.getName() + " is not a Record");
            }

            if (!messages.containsKey(messageType)) {
                throw new RuntimeException("Class " + messageType.getName() + " is not annotated with @Message");
            }

            for (var otherMessageType : messageTypes) {
                if (messageType == otherMessageType) {
                    continue;
                }

                var messageFactory = getMessageFactory(otherMessageType);
                var otherMessageFactory = getMessageFactory(messageType);

                if (!messageFactory.isCompatibleWith(otherMessageFactory)) {
                    throw new RuntimeException("Message types " + messageType.getName() + " and " + otherMessageType.getName() + " are not compatible");
                }
            }
        }

        var messageFactory = getMessageFactory(messageTypes[0]);
        var template = messageFactory.toTemplate();

        // Ensure that the first field is a string
        template[0] = new FormalField(String.class);

        return template;
    }

    public static TemplateField[] getTemplateUnion(Record... messages) throws InvocationTargetException, IllegalAccessException {
        var types = Arrays.stream(messages).map(Record::getClass).toArray(Class[]::new);
        var template = getTemplateUnion(types);

        Object[] valuesAtIndices = new Object[template.length - 1];
        Arrays.fill(valuesAtIndices, new EmptyValue());

        for (var message : messages) {
            var info = getMessageFactory(message);

            int i = 0;

            for (var recordComponent : info.recordComponents) {
                var previousValue = valuesAtIndices[i];
                var value = recordComponent.getAccessor().invoke(message);

                if (previousValue instanceof EmptyValue) {
                    if (value != null) valuesAtIndices[i] = value;
                } else if (!previousValue.equals(value)) {
                    throw new RuntimeException("Messages are not compatible");
                }

                template[i + 1] = new ActualField(valuesAtIndices[i]);
                i++;
            }
        }

        return template;
    }
}

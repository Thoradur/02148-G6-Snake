package snake.protocol;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.TemplateField;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;

public class MessageFactory<T extends Record> {
    public final String name;
    public final Class<T> type;
    public final Message messageAnnotation;
    public final Constructor<?> constructor;
    public final RecordComponent[] recordComponents;
    public final TemplateField[] templateFields;

    public MessageFactory(Class<?> type) {
        if (!type.isRecord()) {
            throw new RuntimeException("Class " + type.getName() + " is annotated with @Message but is not a record");
        }

        this.type = (Class<T>) type;

        this.messageAnnotation = type.getAnnotation(Message.class);

        if (this.messageAnnotation == null) {
            throw new RuntimeException("Class " + type.getName() + " is a record but is not annotated with @Message");
        }

        if (this.messageAnnotation.name().isEmpty()) {
            this.name = type.getSimpleName();
        } else {
            this.name = this.messageAnnotation.name();
        }

        var constructor = Arrays.stream(type.getConstructors()).findFirst();

        if (constructor.isEmpty()) {
            throw new RuntimeException("Class " + type.getName() + " is annotated with @Message but has no constructor");
        }

        this.constructor = constructor.get();

        this.recordComponents = type.getRecordComponents();

        if (this.messageAnnotation.compact()) {
            this.templateFields = new TemplateField[]{new ActualField(name), new FormalField(Record.class)};
            return;
        }

        this.templateFields = new TemplateField[recordComponents.length + 1];
        this.templateFields[0] = new ActualField(name);
        for (int i = 0; i < recordComponents.length; i++) {
            templateFields[i + 1] = new FormalField(recordComponents[i].getType());
        }
    }

    public boolean isCompatibleWith(MessageFactory<?> other) {
        if (other.messageAnnotation.compact() == messageAnnotation.compact()) {
            return true;
        }

        if (recordComponents.length != other.recordComponents.length) {
            return false;
        }

        for (int i = 0; i < recordComponents.length; i++) {
            if (recordComponents[i].getType().equals(other.recordComponents[i].getType())) {
                continue;
            }

            return false;
        }

        return true;
    }

    public TemplateField[] toTemplate() {
        return templateFields.clone();
    }

    public TemplateField[] toTemplate(T message) throws IllegalAccessException, InvocationTargetException {
        TemplateField[] template = this.toTemplate();

        for (int i = 0; i < recordComponents.length; i++) {
            var value = recordComponents[i].getAccessor().invoke(message);

            if (value == null) {
                continue;
            }

            template[i + 1] = new ActualField(value);
        }

        return template;
    }

    public Object[] toTuple(T message) throws IllegalAccessException, InvocationTargetException {
        Object[] tuple = new Object[templateFields.length];
        tuple[0] = name;

        if (messageAnnotation.compact()) {
            tuple[1] = message;
            return tuple;
        }

        for (int i = 0; i < recordComponents.length; i++) {
            tuple[i + 1] = recordComponents[i].getAccessor().invoke(message);
        }

        return tuple;
    }

    public T fromTuple(Object[] tuple) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (tuple.length == 0) {
            return null;
        }

        if (!tuple[0].equals(name)) {
            throw new RuntimeException("Expected message type " + name + " but got " + tuple[0]);
        }

        if (messageAnnotation.compact()) {
            return (T) tuple[1];
        }

        Object[] args = new Object[recordComponents.length];

        System.arraycopy(tuple, 1, args, 0, recordComponents.length);

        return (T) constructor.newInstance(args);
    }
}
package snake.protocol;

import org.jspace.Space;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class MessageSpace implements MessageSpaceInterface {
    private final Space wrappedSpace;

    public MessageSpace(Space wrappedSpace) {
        this.wrappedSpace = wrappedSpace;
    }

    @Override
    public boolean put(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException {
        var msg = MessageRegistry.getMessageFactory(message).toTuple(message);
        System.out.println("Putting: " + Arrays.toString(msg));
        return wrappedSpace.put(msg);
    }

    @Override
    public Record get(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException {
        var factory = MessageRegistry.getMessageFactory(message);
        return factory.fromTuple(wrappedSpace.get(factory.toTemplate(message)));
    }

    @Override
    public Record getp(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException {
        var factory = MessageRegistry.getMessageFactory(message);
        return factory.fromTuple(wrappedSpace.getp(factory.toTemplate(message)));
    }

    @Override
    public List<Record> getAll(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException {
        var factory = MessageRegistry.getMessageFactory(message);

        return wrappedSpace.getAll(factory.toTemplate(message)).stream().map(tuple -> {
            try {
                return factory.fromTuple(tuple);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                return null;
            }
        }).toList();
    }

    @Override
    public Record query(Record message) throws InterruptedException, IllegalAccessException, InvocationTargetException, InstantiationException {
        var factory = MessageRegistry.getMessageFactory(message);
        return factory.fromTuple(wrappedSpace.query(factory.toTemplate(message)));
    }

    @Override
    public Record queryp(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException {
        var factory = MessageRegistry.getMessageFactory(message);
        return factory.fromTuple(wrappedSpace.queryp(factory.toTemplate(message)));
    }

    @Override
    public List<Record> queryAll(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException {
        var factory = MessageRegistry.getMessageFactory(message);
        return wrappedSpace.queryAll(factory.toTemplate(message)).stream().map(tuple -> {
            try {
                return factory.fromTuple(tuple);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                return null;
            }
        }).toList();
    }
}

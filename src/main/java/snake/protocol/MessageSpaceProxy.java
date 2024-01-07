package snake.protocol;

import org.jspace.Space;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MessageSpaceProxy implements MessageSpaceInterface {
    private final Space wrappedSpace;

    MessageSpaceProxy(Space wrappedSpace) {
        this.wrappedSpace = wrappedSpace;
    }

    @Override
    public boolean put(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException {
        return wrappedSpace.put(MessageFactory.toTuple(message));
    }

    @Override
    public Record get(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException {
        return MessageFactory.fromTuple(wrappedSpace.get(MessageFactory.toTemplate(message)));
    }

    @Override
    public Record getp(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException {
        return MessageFactory.fromTuple(wrappedSpace.getp(MessageFactory.toTemplate(message)));
    }

    @Override
    public List<Record> getAll(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException {
        return wrappedSpace.getAll(MessageFactory.toTemplate(message)).stream().map(tuple -> {
            try {
                return MessageFactory.fromTuple(tuple);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                return null;
            }
        }).toList();
    }

    @Override
    public Record query(Record message) throws InterruptedException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return MessageFactory.fromTuple(wrappedSpace.query(MessageFactory.toTemplate(message)));
    }

    @Override
    public Record queryp(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException {
        return MessageFactory.fromTuple(wrappedSpace.queryp(MessageFactory.toTemplate(message)));
    }

    @Override
    public List<Record> queryAll(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException {
        return wrappedSpace.queryAll(MessageFactory.toTemplate(message)).stream().map(tuple -> {
            try {
                return MessageFactory.fromTuple(tuple);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                return null;
            }
        }).toList();
    }
}

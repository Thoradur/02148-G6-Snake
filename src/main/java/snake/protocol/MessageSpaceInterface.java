package snake.protocol;

import org.jspace.Space;
import org.jspace.TemplateField;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface MessageSpaceInterface {
    boolean put(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException;

    Record get(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException;

    Record getp(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException;

    List<Record> getAll(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException;

    Record query(Record message) throws InterruptedException, IllegalAccessException, InvocationTargetException, InstantiationException;

    Record queryp(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException, InstantiationException;

    List<Record> queryAll(Record message) throws InterruptedException, InvocationTargetException, IllegalAccessException;
}

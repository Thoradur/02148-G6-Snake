package snake.coordination;

import org.jspace.*;
import snake.protocol.Message;
import snake.protocol.MessageFactory;
import snake.protocol.MessageRegistry;
import snake.protocol.MessageSpaceProxy;
import snake.protocol.coordination.JoinLobby;
import snake.protocol.coordination.ListLobbies;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;

public class CoordinationServer implements Runnable {
    SpaceRepository repository = new SpaceRepository();
    HashMap<String, CoordinationLobby> lobbies = new HashMap<>();


    @Override
    public void run() {
        var coordinationSpace = new SequentialSpace();
        var wrappedSpace = new MessageSpaceProxy(coordinationSpace);

        var messageUnion = MessageRegistry.getTemplateUnion(JoinLobby.class, ListLobbies.class);

        System.out.println(Arrays.toString(messageUnion));

        try {
            wrappedSpace.put(new ListLobbies());
            wrappedSpace.put(new JoinLobby("test"));

            var g1 = coordinationSpace.get(messageUnion);
            var g2 = coordinationSpace.get(messageUnion);

            System.out.println(MessageRegistry.fromTuple(g1));
            System.out.println(MessageRegistry.fromTuple(g2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CoordinationServer().run();
    }
}

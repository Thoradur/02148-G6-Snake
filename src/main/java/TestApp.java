import snake.common.Direction;
import snake.common.Point;
import snake.protocol.MessageFactory;
import snake.protocol.coordination.JoinLobby;
import snake.protocol.coordination.LeaveLobby;
import snake.protocol.coordination.ListLobbies;
import snake.state.Snake;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.IntStream;

public class TestApp {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        System.out.println(Arrays.toString(MessageFactory.toTemplateUnion(JoinLobby.class, LeaveLobby.class)));

        String lobbyName = "test";

        var joinLobby = new JoinLobby(lobbyName);
        var leaveLobby = new LeaveLobby(lobbyName);

        System.out.println(Arrays.toString(MessageFactory.toTemplateUnion(joinLobby, leaveLobby)));
    }
}

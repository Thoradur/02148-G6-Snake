package snake.protocol.state;

import snake.protocol.Message;
import snake.state.Fragment;

@Message(name = "stateFragment", compact = false)
public record StateFragment(Fragment fragment) {
}

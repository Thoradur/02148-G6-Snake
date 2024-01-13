package snake.protocol.state;

import snake.protocol.Message;

@Message(name = "stepAck", compact = false)
public record StepAck(Integer step) {
}

package ru.knyazev.client.model;

import ru.knyazev.clientserver.Command;

public interface ReadCommandListener {

    void processReceivedCommand(Command command);

}
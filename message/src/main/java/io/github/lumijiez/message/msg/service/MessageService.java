package io.github.lumijiez.message.msg.service;

import io.github.lumijiez.message.msg.request.MessageQueryRequest;
import io.github.lumijiez.message.msg.request.MessageSendRequest;
import io.github.lumijiez.message.msg.response.MessageQueryResponse;
import io.github.lumijiez.message.msg.response.MessageResponse;
import io.github.lumijiez.message.msg.response.MessageSendResponse;
import io.github.lumijiez.message.msg.entity.Message;
import io.github.lumijiez.message.msg.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public MessageQueryResponse getMessages(String sub, MessageQueryRequest request) {
        String chatId = request.getChatId();
        String lastMessageId = request.getLastMessage();

        MessageQueryResponse response = new MessageQueryResponse();

        if (lastMessageId != null) {
            Optional<Message> lastMessage = messageRepository.findById(lastMessageId);

            if (lastMessage.isEmpty()) {
                response.setError("Last message not found");
                return response;
            }

            response.setMessageList(
                    messageRepository.findTop100ByChatIdAndTimestampAfterOrderByTimestampDesc(
                            chatId, lastMessage.get().getTimestamp())
                    .stream()
                    .map(MessageResponse::from)
                    .toList());
        } else {
            response.setMessageList(messageRepository.findTop100ByChatIdOrderByTimestampDesc(chatId)
                    .stream()
                    .map(MessageResponse::from)
                    .toList());
        }

        return response;
    }

    public MessageSendResponse sendMessage(String sub, MessageSendRequest request) {
        Message message = new Message();
        message.setChatId(request.getChatId());
        message.setTimestamp(Instant.now());
        message.setSender(UUID.fromString(sub));
        message.setContent(request.getContent());

        messageRepository.save(message);

        MessageSendResponse response = new MessageSendResponse();
        response.setMessage(message);

        return response;
    }
}

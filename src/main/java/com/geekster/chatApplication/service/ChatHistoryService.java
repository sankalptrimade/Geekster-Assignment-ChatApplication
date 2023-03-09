package com.geekster.chatApplication.service;

import com.geekster.chatApplication.dao.ChatHistoryRepository;
import com.geekster.chatApplication.model.ChatHistory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatHistoryService {
    @Autowired
    ChatHistoryRepository chatHistoryRepository;

    public int saveMessage(ChatHistory chat) {
        ChatHistory chatHistory = chatHistoryRepository.save(chat);
        return chatHistory.getChatId();
    }

    public JSONObject getChatsByUserId(int senderId) {
        List<ChatHistory> chatList = chatHistoryRepository.getChatsByUserId(senderId);
        JSONObject response = new JSONObject();

        if(!chatList.isEmpty()){
            response.put("senderId", chatList.get(0).getSender().getUserId());
            response.put("senderName", chatList.get(0).getSender().getFirstName());
        }

        JSONArray receivers = new JSONArray();
        for(ChatHistory chats : chatList){
            JSONObject receiverObj = new JSONObject();
            receiverObj.put("receiverId", chats.getReceiver().getUserId());
            receiverObj.put("receiverName", chats.getReceiver().getFirstName());
            receiverObj.put("message", chats.getMessage());
            receivers.put(receiverObj);
        }
        response.put("receivers", receivers);

        return response;
    }

    public JSONObject getConversations(int user1, int user2) {
        JSONObject response = new JSONObject();
        JSONArray conversations = new JSONArray();
        List<ChatHistory> chatList = chatHistoryRepository.getConversation(user1, user2);

        for(ChatHistory chatHistory : chatList){
            JSONObject messageObj = new JSONObject();
            messageObj.put("chatId", chatHistory.getChatId());
            messageObj.put("timestamp", chatHistory.getCreatedDate());
            messageObj.put("senderName", chatHistory.getSender().getFirstName());
            messageObj.put("message", chatHistory.getMessage());
            conversations.put(messageObj);
        }
        response.put("conversations",conversations);
        return response;
    }
}

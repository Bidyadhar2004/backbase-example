package com.backbase.example.accountservice.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class SessionManagementService {

    private final ConcurrentMap<String, String> activeSessions = new ConcurrentHashMap<>();

    public boolean isSessionActive(String username) {
        return activeSessions.containsKey(username);
    }

    public void addSession(String username, String token) {
        activeSessions.put(username, token);
    }

    public void removeSession(String username) {
        activeSessions.remove(username);
    }
}

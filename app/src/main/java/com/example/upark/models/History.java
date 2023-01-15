package com.example.upark.models;

import java.util.Date;
import java.util.Objects;

public class History {
    public final HistoryType historyType;
    public final String message;
    public final Date createdAt;

    public History(HistoryType historyType, String message){
        this.historyType = historyType;
        this.message = message;
        this.createdAt = new Date();
    }
    public enum HistoryType{
        MESSAGE_FULL("MESSAGE FULL"),
        SPACE_AVAILABLE("SPACE AVAILABLE"),
        NEW_GARAGE("NEW GARAGE"),
        AVAILABLE_SPACE_CHANGE("AVAILABLE SPACE CHANGE");
        private final String description;
        private HistoryType(String s) {
            description = s;
        }
        public String getDescription(){
            return description;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return historyType == history.historyType && message.equals(history.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyType, message);
    }
}

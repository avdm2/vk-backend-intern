package com.vk.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "logs")
@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    private LocalDateTime time;

    private String internalRequest;
    private String role;

    @Column(length = 10_000)
    private String requestBody;
    @Column(length = 10_000)
    private String responseBody;

    private Integer statusCode;

    @Override
    public int hashCode() {
        return 31 * id.hashCode() +
                username.hashCode() +
                time.hashCode() +
                internalRequest.hashCode() +
                role.hashCode() +
                requestBody.hashCode() +
                responseBody.hashCode() +
                statusCode.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Log other = (Log) obj;
        return id.equals(other.id) &&
                username.equals(other.username) &&
                time.equals(other.time) &&
                internalRequest.equals(other.internalRequest) &&
                role.equals(other.role) &&
                requestBody.equals(other.requestBody) &&
                responseBody.equals(other.responseBody) &&
                statusCode.equals(other.statusCode);
    }
}

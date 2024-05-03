package com.example.lab.dto.kafka.confirmation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConfirmationAnswer implements Serializable {

    private String confirmationId;

    private Boolean verdict;

}

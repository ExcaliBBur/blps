package com.example.confirmator.dto.kafka.confirmation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConfirmationRequest implements Serializable {

    private String confirmationId;

}

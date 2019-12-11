package ru.fds.tavrzcms3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class PrimaryIdentifier {
    private final String name;
    private final String typeOfPrimaryIdentifier;

}

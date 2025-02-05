package org.prog3.mapper;

import org.prog3.model.Sex;

import static org.prog3.model.Sex.FEMALE;
import static org.prog3.model.Sex.MALE;

public class SexMapper {
    //convert from the resultSet = database to enum value JAVA for SELECT
    public Sex fromResultSetDbValue(String stringDbValue){
        if(stringDbValue == null) {
            throw new IllegalArgumentException("unknown sex value : "+ stringDbValue);
        }
        return switch (stringDbValue){
            case "MALE" -> MALE;
            case "FEMALE" -> FEMALE;
            default -> throw new IllegalStateException("Unexpected value: " + stringDbValue);
        };

        //List<Sex> sexList = Arrays.stream(Sex.valueOf())

        /*faire un Arrays.stream(Sex.valueof to strin)*/
    }

    //Convert enum value to database value for Insert
    public String toDataBaseValue(Sex sex){
        if (sex == null) {
            throw new IllegalArgumentException("sex value cannot be null");
        }
        return switch (sex){
            case MALE -> "MALE";
            case FEMALE -> "FEMALE";
            default -> throw new IllegalStateException("Unexpected Enum value: " + sex);

        };
    }
}

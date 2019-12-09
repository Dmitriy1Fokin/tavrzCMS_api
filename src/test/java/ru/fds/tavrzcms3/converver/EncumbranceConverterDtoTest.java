package ru.fds.tavrzcms3.converver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.fds.tavrzcms3.dictionary.TypeOfEncumbrance;
import ru.fds.tavrzcms3.domain.Encumbrance;
import ru.fds.tavrzcms3.domain.PledgeSubject;
import ru.fds.tavrzcms3.dto.EncumbranceDto;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EncumbranceConverterDtoTest {

    @Autowired
    EncumbranceConverterDto encumbranceConverter;

    @Test
    public void toEntity() {
        EncumbranceDto encumbranceDto = EncumbranceDto.builder()
                .encumbranceId(1L)
                .nameEncumbrance("QWE")
                .typeOfEncumbrance(TypeOfEncumbrance.PLEDGE)
                .inFavorOfWhom("ASD")
                .dateBegin(new Date())
                .dateEnd(new Date())
                .numOfEncumbrance("ZXC")
                .pledgeSubjectId(1L)
                .build();

        Encumbrance encumbrance = encumbranceConverter.toEntity(encumbranceDto);

        assertEquals(encumbrance.getEncumbranceId(), encumbranceDto.getEncumbranceId());
        assertEquals(encumbrance.getNameEncumbrance(), encumbranceDto.getNameEncumbrance());
        assertEquals(encumbrance.getTypeOfEncumbrance(), encumbranceDto.getTypeOfEncumbrance());
        assertEquals(encumbrance.getInFavorOfWhom(), encumbranceDto.getInFavorOfWhom());
        assertEquals(encumbrance.getDateBegin(), encumbranceDto.getDateBegin());
        assertEquals(encumbrance.getNumOfEncumbrance(), encumbranceDto.getNumOfEncumbrance());
        assertEquals(encumbrance.getPledgeSubject().getPledgeSubjectId(), encumbranceDto.getPledgeSubjectId());

    }

    @Test
    public void toDto() {
        Encumbrance encumbrance = new Encumbrance().builder()
                .encumbranceId(1L)
                .nameEncumbrance("QWE")
                .typeOfEncumbrance(TypeOfEncumbrance.PLEDGE)
                .inFavorOfWhom("ASD")
                .dateBegin(new Date())
                .dateEnd(new Date())
                .numOfEncumbrance("ZXC")
                .pledgeSubject(new PledgeSubject().builder().pledgeSubjectId(2L).build())
                .build();

        EncumbranceDto encumbranceDto = encumbranceConverter.toDto(encumbrance);

        assertEquals(encumbrance.getEncumbranceId(), encumbranceDto.getEncumbranceId());
        assertEquals(encumbrance.getNameEncumbrance(), encumbranceDto.getNameEncumbrance());
        assertEquals(encumbrance.getTypeOfEncumbrance(), encumbranceDto.getTypeOfEncumbrance());
        assertEquals(encumbrance.getInFavorOfWhom(), encumbranceDto.getInFavorOfWhom());
        assertEquals(encumbrance.getDateBegin(), encumbranceDto.getDateBegin());
        assertEquals(encumbrance.getNumOfEncumbrance(), encumbranceDto.getNumOfEncumbrance());
        assertEquals(encumbrance.getPledgeSubject().getPledgeSubjectId(), encumbranceDto.getPledgeSubjectId());
    }
}
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyTest {

    void moneyExist(){
        Money pris = new Money(100, "SEK");
        assertNotNull(pris);
    }

    void correctAmountOfMoney(){
        Money pris = new Money(1050, "SEK");
        assertEquals(10.5, pris.formattedValue(pris));
    }

}

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyTest {

    void moneyExist(){
        money pris = new money(100, "SEK");
        assertNotNull(pris);
    }

    void correctAmountOfMoney(){
        money pris = new money(1050, "SEK");
        assertEquals(10.5, pris.formattedValue());
    }

}

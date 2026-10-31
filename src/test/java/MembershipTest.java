import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MembershipTest {

    @Test
    void giltigtMedlemskap() {
        Membership membership = new Membership(
            LocalDate.of(2026, 3, 9),
            LocalDate.of(2026, 12, 31)
        );

        assertTrue(membership.isValid(LocalDate.of(2026, 6, 15)));
    }

    @Test
    void medlemskapOgiltigForeStart() {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );

        assertFalse(membership.isValid(LocalDate.of(2025, 12, 31)));
    }

    @Test
    void medlemskapOgiltigEfterSlut() {
        Membership membership = new Membership(
            LocalDate.of(2026, 2, 9),
            LocalDate.of(2026, 12, 31)
        );

        assertFalse(membership.isValid(LocalDate.of(2027, 1, 1)));
    }

    @Test
    void forlangMedlemskap() {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );

        membership.extend(30);

        assertEquals(LocalDate.of(2027, 1, 30), membership.getSlutDatum());
    }

    @Test
    void aterStaendeDagarRaknasKorrekt() {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 1, 10)
        );

        assertEquals(5, membership.getDaysRemaining(LocalDate.of(2026, 1, 5)));
    }

    @Test
    void slutDatumForeStartArFel() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Membership(
                LocalDate.of(2026, 12, 31),
                LocalDate.of(2026, 1, 1)
            )
        );
    }

    @ParameterizedTest //
    @ValueSource(ints = {0, -1, -10})
    void forandrarInteSlutdatumOmFel(int dagar) {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );

        LocalDate ursprungligtSlutdatum = membership.getSlutDatum();

        assertThrows(IllegalArgumentException.class, () -> membership.extend(dagar));

        assertEquals(ursprungligtSlutdatum, membership.getSlutDatum());
    }

    @Test
    void buyingGivesPoints() {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );

        membership.registerPoints(
            new Money(2500, "SEK"),
            CustomerRole.STANDARD,
            false
        );

        assertEquals(50, membership.getPoints());
    }

    @Test
    void pensionerGetsExtraPoints() {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );

        membership.registerPoints(
            new Money(10000, "SEK"),
            CustomerRole.PENSIONER,
            false
        );

        assertEquals(300, membership.getPoints());
    }

    @Test
    void studentGetsExtraPointsOnThursday() {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );

        membership.registerPoints(
            new Money(10000, "SEK"),
            CustomerRole.STUDENT,
            true
        );

        assertEquals(300, membership.getPoints());
    }

    @Test
    void invalidPurchasesGiveNoPoints() {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );

        membership.registerPoints(
            new Money(10000, "SEK"),
            CustomerRole.STANDARD,
            false
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> membership.registerPoints(
                new Money(-2000, "SEK"),
                CustomerRole.STANDARD,
                false
            )
        );

        assertEquals(200, membership.getPoints());
    }

    @Test
    void pointsFromMultiplePurchases() {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );

        membership.registerPoints(
            new Money(45300, "SEK"),
            CustomerRole.STANDARD,
            false
        );

        membership.registerPoints(
            new Money(98000, "SEK"),
            CustomerRole.STANDARD,
            false
        );

        assertEquals(2866, membership.getPoints());
    }

    @Test
    void redeemPointsShouldDeductPoints() {
        Membership membership = new Membership(
            LocalDate.now(),
            LocalDate.now().plusYears(1)
        );

        Money purchase = new Money(5000, "SEK");
        membership.registerPoints(
            purchase,
            CustomerRole.STANDARD,
            false
        );

        membership.redeemPoints(40);

        assertEquals(60, membership.getPoints());
    }

    @Test
    void redeemPointsShouldThrowExceptionIfInsufficientPoints() {
        Membership membership = new Membership(
            LocalDate.now(),
            LocalDate.now().plusYears(1)
        );

        Money purchase = new Money(1000, "SEK");
        membership.registerPoints(
            purchase,
            CustomerRole.STANDARD,
            false
        );

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> membership.redeemPoints(50));

        assertEquals("Not enough points on membership", exception.getMessage());
    }
}
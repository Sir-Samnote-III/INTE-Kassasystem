import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MembershipTest {

    @Test
    void validMembership() {
        Membership membership = new Membership(
            LocalDate.of(2026, 3, 9),
            LocalDate.of(2026, 12, 31)
        );

        assertTrue(membership.isValid(LocalDate.of(2026, 6, 15)));
    }

    @Test
    void membershipInvalidBeforeStart() {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );

        assertFalse(membership.isValid(LocalDate.of(2025, 12, 31)));
    }

    @Test
    void membershipInvalidAfterEnd() {
        Membership membership = new Membership(
            LocalDate.of(2026, 2, 9),
            LocalDate.of(2026, 12, 31)
        );

        assertFalse(membership.isValid(LocalDate.of(2027, 1, 1)));
    }

    @Test
    void isValidWithNullDateThrowsException() {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );

        assertThrows(IllegalArgumentException.class, () -> membership.isValid(null));
    }

    @Test
    void isValidTodayReturnsTrueForValidMembership() {
        Membership membership = new Membership(
            LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(1)
        );

        assertTrue(membership.isValidToday());
    }

    @Test
    void extendMembership() {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );

        membership.extend(30);

        assertEquals(LocalDate.of(2027, 1, 30), membership.getEndDate());
    }

    @Test
    void remainingDaysCalculatedCorrectly() {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 1, 10)
        );

        assertEquals(5, membership.getDaysRemaining(LocalDate.of(2026, 1, 5)));
    }

    @Test
    void getDaysRemainingWithNullDateThrowsException() {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 1, 10)
        );

        assertThrows(IllegalArgumentException.class, () -> membership.getDaysRemaining(null));
    }

    @Test
    void endDateBeforeStartThrowsException() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Membership(
                LocalDate.of(2026, 12, 31),
                LocalDate.of(2026, 1, 1)
            )
        );
    }

    @Test
    void nullDatesInConstructorThrowException() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Membership(null, LocalDate.of(2026, 12, 31))
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> new Membership(LocalDate.of(2026, 1, 1), null)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void endDateUnchangedWhenExtensionIsInvalid(int days) {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );

        LocalDate originalEndDate = membership.getEndDate();

        assertThrows(IllegalArgumentException.class, () -> membership.extend(days));

        assertEquals(originalEndDate, membership.getEndDate());
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
    void registerPointsWithNullPurchaseThrowsException() {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> membership.registerPoints(null, CustomerRole.STANDARD, false)
        );
    }

    @Test
    void registerPointsWithNullRoleThrowsException() {
        Membership membership = new Membership(
            LocalDate.of(2026, 1, 1),
            LocalDate.of(2026, 12, 31)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> membership.registerPoints(new Money(10000, "SEK"), null, false)
        );
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
    void removePointsShouldDeductPoints() {
        Membership membership = new Membership(
            LocalDate.now(),
            LocalDate.now().plusYears(1)
        );

        membership.registerPoints(
            new Money(5000, "SEK"),
            CustomerRole.STANDARD,
            false
        );

        membership.removePoints(40);

        assertEquals(60, membership.getPoints());
    }

    @Test
    void removePointsThrowsExceptionWhenInsufficientPoints() {
        Membership membership = new Membership(
            LocalDate.now(),
            LocalDate.now().plusYears(1)
        );

        membership.registerPoints(
            new Money(1000, "SEK"),
            CustomerRole.STANDARD,
            false
        );

        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> membership.removePoints(50)
        );

        assertEquals("Not enough points on membership", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void removePointsWithInvalidAmountThrowsException(int points) {
        Membership membership = new Membership(
            LocalDate.now(),
            LocalDate.now().plusYears(1)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> membership.removePoints(points)
        );
    }

    @Test
    void getBonusReturnsOnlyCompleteBonusIntervals() {
        Membership membership = new Membership(
            LocalDate.now(),
            LocalDate.now().plusYears(1)
        );

        membership.registerPoints(
            new Money(3750, "SEK"),
            CustomerRole.STANDARD,
            false
        );

        Money bonus = membership.getBonus();

        assertEquals(50, bonus.getValue());
        assertEquals(75, membership.getPoints());
    }

    @Test
    void getBonusDoesNotDeductPoints() {
        Membership membership = new Membership(
            LocalDate.now(),
            LocalDate.now().plusYears(1)
        );

        membership.registerPoints(
            new Money(5000, "SEK"),
            CustomerRole.STANDARD,
            false
        );

        Money bonus = membership.getBonus();

        assertEquals(100, bonus.getValue());
        assertEquals(100, membership.getPoints());
    }
}
package example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
class AppTest {
    /**
     * Rigorous Test.
     */
    @Test
    public void shouldAnswerWithTrue() {
        List<Double> db = new ArrayList<>();
        db.add(1.0);
        db.add(0.0);
        db.add(2.0);
        assertEquals(calculateAverage(db), 1.0);
        assertTrue(true);
    }

    private double calculateAverage(List<Double> list) {
        return list.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}

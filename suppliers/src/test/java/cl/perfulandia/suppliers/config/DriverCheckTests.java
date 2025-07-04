package cl.perfulandia.suppliers.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DriverCheckTests {

    @Test
    void databaseDriverIsPresent() {
        assertDoesNotThrow(() -> Class.forName("org.postgresql.Driver"));
    }

    @Test
    void missingDriverCausesException() {
        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("non.existent.Driver"));
    }
}

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Casos de prueba compartidos del ciclo 2.
 * Rs: Rojas Solano. S: Salazar.
 * Todas las pruebas corren con la maquina invisible.
 */
public class SlotMachineCC2Test
{
    private SlotMachine machine;

    /**
     * Arma una maquina de tres simbolos y tres ruedas,
     * cada rueda en un simbolo distinto.
     */
    @Before
    public void setUp()
    {
        machine = new SlotMachine();
        machine.addSymbol(1, "red");
        machine.addSymbol(2, "blue");
        machine.addSymbol(3, "green");
        machine.addWheel(1);
        machine.addWheel(2);
        machine.addWheel(3);
        machine.placeSymbol(1, "red");
        machine.placeSymbol(2, "blue");
        machine.placeSymbol(3, "green");
    }

    /**
     * Requisito 9. Las dos ruedas quedan cambiadas de puesto.
     */
    @Test
    public void accordingRsSShouldSwapTwoWheels()
    {
        machine.swap(1, 3);
        assertTrue(machine.ok());
        String[] config = machine.configuration();
        assertEquals("green", config[0]);
        assertEquals("red", config[2]);
    }

    /**
     * Requisito 10. Una rueda fija no gira.
     */
    @Test
    public void accordingRsSShouldNotSpinALockedWheel()
    {
        machine.lock(2);
        machine.spin(2);
        assertFalse(machine.ok());
        assertEquals("blue", machine.configuration()[1]);
    }

    /**
     * Requisito 11. La rueda avanza los pasos pedidos y gira en circulo.
     */
    @Test
    public void accordingRsSShouldRotateAWheelTheGivenSteps()
    {
        machine.spin(1, 2);
        assertTrue(machine.ok());
        assertEquals("green", machine.configuration()[0]);
        machine.spin(1, 3);                        // una vuelta completa
        assertEquals("green", machine.configuration()[0]);
    }

    /**
     * Requisito 12. La maquina queda en la configuracion pedida,
     * y si el simbolo no existe no cambia nada.
     */
    @Test
    public void accordingRsSShouldLeaveTheMachineInTheGivenConfiguration()
    {
        String[] wanted = {"green", "red", "blue"};
        machine.spin(wanted);
        assertTrue(machine.ok());
        assertArrayEquals(wanted, machine.configuration());

        machine.spin(new String[]{"red", "yellow", "green"});
        assertFalse(machine.ok());
        assertArrayEquals(wanted, machine.configuration());
    }
    
    
    



}
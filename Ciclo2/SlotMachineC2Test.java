import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas de unidad del simulador, ciclo 2.
 * Todas corren con la maquina invisible.
 * La maquina de prueba tiene tres simbolos (red, blue, green)
 * y tres ruedas, una en cada simbolo.
 */
public class SlotMachineC2Test
{
    private SlotMachine machine;

    /**
     * Arma la maquina de prueba, invisible y en una configuracion conocida.
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
    
    //Requisito 9
    
        /**
     * Las dos ruedas deben quedar cambiadas de puesto.
     */
    @Test
    public void shouldSwapTwoWheels()
    {
        machine.swap(1, 3);
        assertTrue(machine.ok());
        String[] config = machine.configuration();
        assertEquals("green", config[0]);
        assertEquals("blue", config[1]);
        assertEquals("red", config[2]);
    }

    /**
     * La rueda fija sigue fija despues de cambiar de puesto.
     */
    @Test
    public void shouldSwapWheelsKeepingTheLock()
    {
        machine.lock(1);
        machine.swap(1, 3);
        machine.spin(3);                           // ahi quedo la que estaba fija
        assertFalse(machine.ok());
        machine.spin(1);                           // esta si se puede girar
        assertTrue(machine.ok());
    }

    /**
     * No debe cambiar una rueda consigo misma.
     */
    @Test
    public void shouldNotSwapAWheelWithItself()
    {
        machine.swap(2, 2);
        assertFalse(machine.ok());
    }

    /**
     * No debe cambiar ruedas si la maquina tiene menos de dos.
     */
    @Test
    public void shouldNotSwapWheelsWhenThereAreNotTwo()
    {
        SlotMachine small = new SlotMachine();
        small.addSymbol(1, "red");
        small.addWheel(1);
        small.swap(1, 1);
        assertFalse(small.ok());
    }
    
    //Requisito 10
    
        /**
     * Una rueda fija no debe girar ni cambiar de simbolo.
     */
    @Test
    public void shouldNotSpinALockedWheel()
    {
        machine.lock(2);
        machine.spin(2);
        assertFalse(machine.ok());
        assertEquals("blue", machine.configuration()[1]);
    }

    /**
     * Al girar todas, la rueda fija debe conservar su simbolo.
     */
    @Test
    public void shouldKeepTheLockedWheelWhenSpinningAll()
    {
        machine.lock(2);
        machine.spin();
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[1]);
    }

    /**
     * Despues de soltarla, la rueda debe volver a girar.
     */
    @Test
    public void shouldSpinAgainAfterUnlock()
    {
        machine.lock(1);
        machine.unlock(1);
        machine.spin(1, 1);
        assertTrue(machine.ok());
        assertEquals("blue", machine.configuration()[0]);
    }
    
    
    //Requisito 11
    
        /**
     * La rueda debe avanzar los pasos pedidos.
     */
    @Test
    public void shouldRotateAWheelTheGivenSteps()
    {
        machine.spin(1, 2);
        assertTrue(machine.ok());
        assertEquals("green", machine.configuration()[0]);
    }

    /**
     * Con pasos negativos la rueda debe girar al reves.
     */
    @Test
    public void shouldRotateAWheelBackwards()
    {
        machine.spin(1, -1);
        assertTrue(machine.ok());
        assertEquals("green", machine.configuration()[0]);
    }

    /**
     * La rueda gira en circulo: una vuelta completa la deja igual.
     */
    @Test
    public void shouldRotateAWheelInCircles()
    {
        machine.spin(1, 3);
        assertEquals("red", machine.configuration()[0]);
        machine.spin(1, 7);                        // dos vueltas y un paso
        assertEquals("blue", machine.configuration()[0]);
    }

    /**
     * No debe rotar si la maquina no tiene simbolos.
     */
    @Test
    public void shouldNotRotateAWheelWithoutSymbols()
    {
        SlotMachine empty = new SlotMachine();
        empty.addWheel(1);
        empty.spin(1, 2);
        assertFalse(empty.ok());
    }
    
    //Requisito 12
    
        /**
     * La maquina debe quedar en la configuracion pedida.
     */
    @Test
    public void shouldLeaveTheMachineInTheGivenConfiguration()
    {
        String[] wanted = {"green", "red", "blue"};
        machine.spin(wanted);
        assertTrue(machine.ok());
        assertArrayEquals(wanted, machine.configuration());
    }

    /**
     * Una configuracion de un solo color debe ser jackpot.
     */
    @Test
    public void shouldReachTheJackpot()
    {
        String[] wanted = {"blue", "blue", "blue"};
        machine.spin(wanted);
        assertTrue(machine.isJackpot());
        assertEquals(1, machine.distinctSymbols());
    }

    /**
     * Con un simbolo que la maquina no tiene, no debe cambiar nada.
     */
    @Test
    public void shouldNotApplyAConfigurationWithAnUnknownSymbol()
    {
        String[] before = machine.configuration();
        machine.spin(new String[]{"red", "yellow", "green"});
        assertFalse(machine.ok());
        assertArrayEquals(before, machine.configuration());
    }

    /**
     * La configuracion debe tener un simbolo por rueda.
     */
    @Test
    public void shouldNotApplyAConfigurationOfAnotherSize()
    {
        machine.spin(new String[]{"red", "blue"});
        assertFalse(machine.ok());
    }

    /**
     * No debe aplicarse una configuracion que mueva una rueda fija.
     */
    @Test
    public void shouldNotApplyAConfigurationThatMovesALockedWheel()
    {
        machine.lock(3);
        machine.spin(new String[]{"red", "red", "red"});
        assertFalse(machine.ok());
        assertEquals("green", machine.configuration()[2]);
    }

    /**
     * Si la rueda fija ya tiene ese simbolo, la configuracion si se aplica.
     */
    @Test
    public void shouldApplyAConfigurationThatRespectsTheLockedWheel()
    {
        machine.lock(3);
        String[] wanted = {"green", "green", "green"};
        machine.spin(wanted);
        assertTrue(machine.ok());
        assertArrayEquals(wanted, machine.configuration());
    }
    
    //Simbolos
        /**
     * Al borrar un simbolo debe desaparecer de todas las ruedas.
     */
    @Test
    public void shouldDeleteASymbolFromEveryWheel()
    {
        machine.delSymbol("blue");
        assertTrue(machine.ok());
        assertEquals(2, machine.symbols().length);
        String[] config = machine.configuration();
        for (int i = 0; i < config.length; i++) {
            assertFalse(config[i].equals("blue"));
        }
    }

    /**
     * Los simbolos deben ser de colores diferentes.
     */
    @Test
    public void shouldNotAddARepeatedSymbol()
    {
        machine.addSymbol(1, "RED");
        assertFalse(machine.ok());
    }

    /**
     * El color del simbolo debe existir.
     */
    @Test
    public void shouldNotAddAnInvalidColor()
    {
        machine.addSymbol(1, "rojo");
        assertFalse(machine.ok());
    }

    /**
     * No debe pasarse del tope de simbolos.
     */
    @Test
    public void shouldNotAddMoreSymbolsThanAllowed()
    {
        SlotMachine full = new SlotMachine();
        String[] palette = Colors.palette(SlotMachine.MAX_SYMBOLS);
        for (int i = 0; i < palette.length; i++) {
            full.addSymbol(i + 1, palette[i]);
        }
        assertTrue(full.ok());
        full.addSymbol(1, "dimgray");              // uno mas ya no cabe
        assertFalse(full.ok());
        assertEquals(SlotMachine.MAX_SYMBOLS, full.symbols().length);
    }

    /**
     * No debe borrar un simbolo que no tiene.
     */
    @Test
    public void shouldNotDeleteASymbolThatIsNotThere()
    {
        machine.delSymbol("yellow");
        assertFalse(machine.ok());
    }

    /**
     * No debe dejar visible un simbolo que no tiene.
     */
    @Test
    public void shouldNotPlaceASymbolThatIsNotThere()
    {
        machine.placeSymbol(1, "yellow");
        assertFalse(machine.ok());
    }
    
    
}
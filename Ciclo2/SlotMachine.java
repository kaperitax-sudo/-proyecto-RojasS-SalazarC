import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;
import java.util.Collections;

/**
 * Simulador de una maquina tragamonedas.
 * La maquina tiene ruedas y todas las ruedas tienen los mismos simbolos.
 * Cada simbolo se identifica por su color y todos son diferentes.
 */
public class SlotMachine
{
    public static final int MAX_WHEELS = 50;
    public static final int MAX_SYMBOLS = 50;


    private static final int BODY_X = 10;          // esquina del cuerpo
    private static final int BODY_Y = 60;
    private static final int BODY_WIDTH = 280;     // el canvas mide 300
    private static final int BODY_HEIGHT = 110;
    private static final int GAP = 8;              // espacio entre ruedas
    private static final int STEP_DELAY = 120;     // milisegundos por paso

    private ArrayList<Wheel> wheels;
    private ArrayList<String> symbols;             // los colores, en orden
    private Rectangle body;                        // el cuerpo de la maquina
    private Random random;
    private boolean isVisible;
    private boolean ok;                            // si se pudo la ultima operacion

    /**
     * Crea una maquina vacia e invisible.
     */
    public SlotMachine()
    {
        wheels = new ArrayList<Wheel>();
        symbols = new ArrayList<String>();
        random = new Random();
        body = new Rectangle();
        body.changeSize(BODY_HEIGHT, BODY_WIDTH);
        body.moveTo(BODY_X, BODY_Y);
        body.changeColor("dimgray");
        isVisible = false;
        ok = true;
    }

    /**
     * Adiciona una rueda en la posicion dada.
     * @param pos posicion de la rueda, empezando en 1
     */
    public void addWheel(int pos)
    {
        if (wheels.size() >= MAX_WHEELS) {
            error("No puede tener mas de " + MAX_WHEELS + " ruedas.");
            return;
        }
        Wheel wheel = new Wheel(symbols);          // nace con los simbolos actuales
        wheels.add(index(pos, wheels.size() + 1), wheel);
        if (isVisible) {
            wheel.makeVisible();
        }
        layout();
        ok = true;
    }

    /**
     * Elimina la rueda de la posicion dada.
     * @param pos posicion de la rueda, empezando en 1
     */
    public void delWheel(int pos)
    {
        if (wheels.isEmpty()) {
            error("La maquina no tiene ruedas.");
            return;
        }
        Wheel wheel = wheels.remove(index(pos, wheels.size()));
        wheel.makeInvisible();
        layout();
        ok = true;
    }

    
    /**
     * Intercambia dos ruedas de posicion.
     * @param wheel1 posicion de la primera rueda, empezando en 1
     * @param wheel2 posicion de la segunda rueda, empezando en 1
     */
    public void swap(int wheel1, int wheel2)
    {
        if (wheels.size() < 2) {
            error("La maquina necesita al menos dos ruedas.");
            return;
        }
        
        int first = index(wheel1, wheels.size());
        int second = index(wheel2, wheels.size());
        
        if (first == second) {
            error("Las dos ruedas deben ser diferentes.");
            return;
        }
        
        
        Collections.swap(wheels, first, second);   
        layout();
        ok = true;
    }
    
    
    
    
    /**
     * Adiciona un simbolo en la posicion dada.
     * @param pos posicion del simbolo, empezando en 1
     * @param color nombre de un color CSS, diferente a los actuales 
     */
    public void addSymbol(int pos, String color)
    {
        if (symbols.size() >= MAX_SYMBOLS) {
            error("No puede tener mas de " + MAX_SYMBOLS + " simbolos.");
            return;
        }
        if (!Colors.isValid(color)) {
            error("El color " + color + " no existe.");
            return;
        }
        String value = color.toLowerCase();
        if (symbols.contains(value)) {
            error("Los simbolos deben ser de colores diferentes.");
            return;
        }
        insert(index(pos, symbols.size() + 1), value);
        ok = true;
    }

    /**
     * Elimina el simbolo de ese color.
     * @param symbol nombre del color
     */
    public void delSymbol(String symbol)
    {
        String value = "";
        if (symbol != null) {
            value = symbol.toLowerCase();
        }
        if (!symbols.remove(value)) {              // remove avisa si no estaba
            error("La maquina no tiene el simbolo " + symbol + ".");
            return;
        }
        for (Wheel wheel : wheels) {
            wheel.delSymbol(value);
        }
        refresh();
        ok = true;
    }

    /**
     * Deja visible un simbolo en una rueda.
     * @param wheel posicion de la rueda, empezando en 1
     * @param symbol nombre del color
     */
    public void placeSymbol(int wheel, String symbol)
    {
        if (wheels.isEmpty()) {
            error("La maquina no tiene ruedas.");
            return;
        }
        String value = "";
        if (symbol != null) {
            value = symbol.toLowerCase();
        }
        if (!symbols.contains(value)) {
            error("La maquina no tiene el simbolo " + symbol + ".");
            return;
        }
        wheels.get(index(wheel, wheels.size())).place(value);
        refresh();
        ok = true;
    }

    
        /**
     * Gira una rueda, si no esta fija.
     * @param wheel posicion de la rueda, empezando en 1
     */

    public void spin(int wheel)
    {
        if (!canSpin()) {
            return;
        }
        Wheel target = wheels.get(index(wheel, wheels.size()));
        if (target.getLock()) {
            error("La rueda " + wheel + " esta fija.");
            return;
        }
        target.spin(random);
        refresh();
        ok = true;
    }
    

    /**
     * Gira todas las ruedas que no esten fijas.
     * Las ruedas fijas conservan su simbolo visible.
     */
    public void spin()
    {
        if (!canSpin()) {
            return;
        }
        for (Wheel wheel : wheels) {
            if (!wheel.getLock()) {
                wheel.spin(random);
            }
        }
        refresh();
        ok = true;
    }
    
    /**
     * Rota una rueda un numero de pasos, si no esta fija.
     * Si la maquina es visible el movimiento se ve paso a paso.
     * @param wheel posicion de la rueda, empezando en 1
     * @param steps numero de pasos, puede ser negativo
     */
    public void spin(int wheel, int steps)
    {
        if (!canSpin()) {
            return;
        }
        Wheel target = wheels.get(index(wheel, wheels.size()));
        if (target.getLock()) {
            error("La rueda " + wheel + " esta fija.");
            return;
        }
        if (isVisible) {
            int one = 1;
            if (steps < 0) {
                one = -1;
            }
            for (int i = 0; i < Math.abs(steps); i++) {
                target.rotate(one);
                refresh();
                Canvas.getCanvas().wait(STEP_DELAY);
            }
        }
        else {
            target.rotate(steps);
        }
        refresh();
        ok = true;
    }
    
    
    
    /**
     * Deja la maquina en la configuracion dada.
     * O se aplica completa, o no se aplica nada.
     * @param setSymbols colores deseados, uno por rueda, de izquierda a derecha
     */
    public void spin(String[] setSymbols)
    {
        if (!canSpin()) {
            return;
        }
        if (setSymbols == null || setSymbols.length != wheels.size()) {
            error("La configuracion debe tener " + wheels.size() + " simbolos.");
            return;
        }
        String[] values = new String[setSymbols.length];
        for (int i = 0; i < setSymbols.length; i++) {
            if (setSymbols[i] == null) {
                error("La configuracion tiene un simbolo vacio.");
                return;
            }
            values[i] = setSymbols[i].toLowerCase();
            if (!symbols.contains(values[i])) {
                error("La maquina no tiene el simbolo " + setSymbols[i] + ".");
                return;
            }
            if (wheels.get(i).getLock() && !wheels.get(i).symbol().equals(values[i])) {
                error("La rueda " + (i + 1) + " esta fija.");
                return;
            }
        }
        for (int i = 0; i < values.length; i++) {
            wheels.get(i).place(values[i]);
        }
        refresh();
        ok = true;
    }
    

    /**
     * Consulta los simbolos de la maquina, en el orden de la rueda.
     * @return los colores de los simbolos
     */
    public String[] symbols()
    {
        ok = true;
        return symbols.toArray(new String[0]);     // lista -> arreglo
    }

    /**
     * Consulta cuantos colores diferentes hay en la configuracion.
     * @return numero de simbolos distintos visibles
     */
    public int distinctSymbols()
    {
        ArrayList<String> found = new ArrayList<String>();
        for (Wheel wheel : wheels) {
            String color = wheel.symbol();
            if (!color.equals("") && !found.contains(color)) {
                found.add(color);                  // solo si no estaba
            }
        }
        ok = true;
        return found.size();
    }

    /**
     * Consulta los colores visibles, de izquierda a derecha.
     * @return la configuracion actual
     */
    public String[] configuration()
    {
        String[] result = new String[wheels.size()];
        for (int i = 0; i < wheels.size(); i++) {
            result[i] = wheels.get(i).symbol();
        }
        ok = true;
        return result;
    }

    /**
     * Indica si la configuracion es la ganadora.
     * @return true si todas las ruedas muestran lo mismo
     */
    public boolean isJackpot()
    {
        ok = true;
        return jackpot();
    }

    /**
     * Hace visible el simulador.
     */
    public void makeVisible()
    {
        isVisible = true;
        body.makeVisible();
        for (Wheel wheel : wheels) {
            wheel.makeVisible();
        }
        refresh();
        ok = true;
    }

    /**
     * Hace invisible el simulador. La maquina sigue funcionando.
     */
    public void makeInvisible()
    {
        for (Wheel wheel : wheels) {
            wheel.makeInvisible();
        }
        body.makeInvisible();
        isVisible = false;
        ok = true;
    }

    /**
     * Termina el simulador.
     */
    public void exit()
    {
        makeInvisible();
        System.exit(0);
    }

    /**
     * Indica si se logro realizar la ultima operacion.
     * @return true si se pudo
     */
    public boolean ok()
    {
        return ok;
    }

    /**
     * Mete el simbolo en la maquina y en todas las ruedas.
     */
    private void insert(int position, String color)
    {
        symbols.add(position, color);
        for (Wheel wheel : wheels) {
            wheel.addSymbol(position, color);
        }
        refresh();
    }

    /**
     * Revisa si se puede girar y avisa si no.
     */
    private boolean canSpin()
    {
        if (wheels.isEmpty()) {
            error("La maquina no tiene ruedas.");
            return false;
        }
        if (symbols.isEmpty()) {
            error("La maquina no tiene simbolos.");
            return false;
        }
        return true;
    }

    /**
     * Calcula si todas las ruedas muestran el mismo simbolo.
     */
    private boolean jackpot()
    {
        if (wheels.isEmpty() || symbols.isEmpty()) {
            return false;
        }
        String first = wheels.get(0).symbol();
        for (Wheel wheel : wheels) {
            if (!first.equals(wheel.symbol())) {
                return false;                      // uno distinto y ya no gana
            }
        }
        return true;
    }

    /**
     * Reparte las ruedas centradas dentro del cuerpo.
     */
    private void layout()
    {
        int used = wheels.size() * Wheel.WIDTH + (wheels.size() - 1) * GAP;
        int x = BODY_X + (BODY_WIDTH - used) / 2;
        int y = BODY_Y + (BODY_HEIGHT - Wheel.HEIGHT) / 2;
        for (Wheel wheel : wheels) {
            wheel.moveTo(x, y);
            x = x + Wheel.WIDTH + GAP;             // la siguiente mas a la derecha
        }
        refresh();
    }

    /**
     * La maquina luce diferente si llego a un estado ganador.
     */
    private void refresh()
    {
        if (jackpot()) {
            paint("gold", "gold");
        }
        else {
            paint("dimgray", "black");
        }
    }

    /**
     * Pinta el cuerpo y los marcos de las ruedas.
     */
    private void paint(String bodyColor, String wheelColor)
    {
        body.changeColor(bodyColor);
        for (Wheel wheel : wheels) {
            wheel.changeColor(wheelColor);
            if (isVisible) {
                wheel.makeVisible();               // la redibuja encima del cuerpo
            }
        }
    }

    /**
     * Pasa una posicion del usuario (desde 1) a un indice de lista (desde 0).
     * Si es menor que 1 usa 1, si es mayor que max usa max.
     */
    private int index(int pos, int max)
    {
        int value = Math.max(1, Math.min(pos, max));
        return value - 1;
    }

    /**
     * Avisa al usuario, solo si el simulador esta visible.
     */
    private void error(String message)
    {
        ok = false;
        if (isVisible) {
            JOptionPane.showMessageDialog(null, message);
        }
    }
    
        /**
     * Fija una rueda para que no gire.
     * @param wheel posicion de la rueda, empezando en 1
     */
    public void lock(int wheel)
    {
        setLock(wheel, true);
    }

    /**
     * Suelta una rueda para que vuelva a girar.
     * @param wheel posicion de la rueda, empezando en 1
     */
    public void unlock(int wheel)
    {
        setLock(wheel, false);
    }

    /**
     * Fija o suelta la rueda de la posicion dada.
     */
    private void setLock(int wheel, boolean value)
    {
        if (wheels.isEmpty()) {
            error("La maquina no tiene ruedas.");
            return;
        }
        wheels.get(index(wheel, wheels.size())).setLock(value);
        ok = true;
    }

}
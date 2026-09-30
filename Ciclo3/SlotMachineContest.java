import java.util.ArrayList;                                  // para poder usar listas

/**
 * Resuelve el problema de la maraton.
 * Solo usa SlotMachine(n), spin(wheel, steps) y distinctSymbols().
 */
public class SlotMachineContest
{
    /**
     * Resuelve una maquina de n ruedas, sin mostrarla.
     * @param n numero de ruedas y de colores
     * @return las jugadas {rueda, pasos} para ganar
     */
    public static int[][] solve(int n)
    {
        if (n < 3 || n > SlotMachine.MAX_WHEELS) {    // n no sirve
          return new int[0][];                      // no hay jugadas
        }
        SlotMachine machine = new SlotMachine(n);            // crea una maquina de n ruedas, sin mostrarla
        return play(machine, n);                             // la resuelve y devuelve las jugadas
    }

    /**
     * Muestra la solucion con la maquina visible.
     * @param n numero de ruedas y de colores
     */
    public static void simulate(int n)
    {
        SlotMachine machine = new SlotMachine(n);            // crea una maquina de n ruedas
        machine.makeVisible();                               // la muestra en pantalla
        play(machine, n);                                    // la resuelve y se ve como gira
    }

    /**
     * Juega hasta ganar.
     * @param machine la maquina que se va a resolver
     * @param n numero de ruedas y de colores
     * @return las jugadas {rueda, pasos} que se hicieron
     */
    static int[][] play(SlotMachine machine, int n)
    {
        ArrayList<int[]> actions = new ArrayList<int[]>();   // lista vacia para anotar las jugadas

        // 1. differentColors: cada rueda queda en un color que nadie mas tiene
        
        for (int wheel = 1; wheel <= n; wheel++) {           // para cada rueda, de la 1 a la n
            int step = lap(machine, n, wheel, actions);      // da una vuelta y busca donde se ven mas colores
            move(machine, wheel, step, actions);             // la deja en ese lugar
        }

        // 2. wheel1Guide: la rueda 1 se aparta y su color queda libre
        move(machine, 1, 1, actions);                        // la rueda 1 avanza un paso

        // 3. search: cada rueda busca el color libre y anota los pasos
        int[] steps = new int[n + 1];                        // aqui se anotan los pasos de cada rueda
        for (int wheel = 2; wheel <= n; wheel++) {           // para cada rueda, de la 2 a la n
            steps[wheel] = lap(machine, n, wheel, actions);  // da una vuelta y anota donde esta el color libre
        }

        // 4. equalize: la rueda 1 vuelve y las demas llegan a su color
        move(machine, 1, -1, actions);                       // la rueda 1 retrocede un paso, vuelve a su color
        for (int wheel = 2; wheel <= n; wheel++) {           // para cada rueda, de la 2 a la n
            move(machine, wheel, steps[wheel], actions);     // avanza los pasos que anoto
        }

        
        // pasa la lista a arreglo, porque solve debe devolver int[][]
        int[][] result = new int[actions.size()][];          // arreglo del mismo tamano que la lista
        for (int i = 0; i < actions.size(); i++) {           // recorre la lista
            result[i] = actions.get(i);                      // copia cada jugada al arreglo
        }
        
        
        
        return result;                                      // devuelve todas las jugadas
    }

    /**
     * La rueda da una vuelta completa, un paso a la vez,
     * y termina donde empezo.
     * @param machine la maquina
     * @param n numero de colores de cada rueda
     * @param wheel la rueda que da la vuelta
     * @param actions lista donde se anotan las jugadas
     * @return el primer paso donde se vieron mas colores
     */
    private static int lap(SlotMachine machine, int n, int wheel, ArrayList<int[]> actions)
    {
        int bestStep = 0;                                    // en que paso se vieron mas colores
        int bestCount = 0;                                   // cuantos colores se vieron en ese paso
        for (int step = 1; step <= n; step++) {              // n pasos = una vuelta completa
            move(machine, wheel, 1, actions);                // gira la rueda un paso
            int count = machine.distinctSymbols();           // pregunta cuantos colores distintos se ven
            if (count > bestCount) {                         // si es mas que el mejor hasta ahora
                bestCount = count;                           // guarda ese numero
                bestStep = step;                             // y guarda en que paso fue
            }
        }
        return bestStep;                                     // devuelve el paso con mas colores
    }

    /**
     * Gira una rueda y guarda la jugada.
     * @param machine la maquina
     * @param wheel la rueda que se gira, desde 1
     * @param steps cuantos pasos, pueden ser negativos
     * @param actions lista donde se anotan las jugadas
     */
    private static void move(SlotMachine machine, int wheel, int steps, ArrayList<int[]> actions)
    {
        machine.spin(wheel, steps);                          // gira la rueda esos pasos
        int[] action = {wheel, steps};                       // arma la jugada: que rueda y cuantos pasos
        actions.add(action);                                 // la anota en la lista
    }
}
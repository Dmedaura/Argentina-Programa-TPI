import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProdeDeportivo {
    public static void main(String[] args) {

        // Obtener las rutas de los archivos de partidos y resultados

        String achivoPartidos = "D:\Daniel\Curso java\Trabajo Practico Integrador\Prode\Archivos\Partidos.txt";
        String archivoResultados = "D:\\Daniel\\Curso java\\Trabajo Practico Integrador\\Prode\\Archivos\\Pronosticos.txt";

        // Leemos el archivo de partidos
        Partido[] partidos = new Partido[2]; // el 2 indica la cantidad de elementos, en este caso 2 PARTIDOS (0 y 1)
        try (BufferedReader br = new BufferedReader(new FileReader(archivoPartidos))) {
            String linea;
            int i = 0;
            while ((linea = br.readLine()) != null && i < 2) {
                String[] campos = linea.split(",");
                Equipo equipoLocal = new Equipo(campos[0]);
                Equipo equipoVisitante = new Equipo(campos[1]);
                partidos[i] = new Partido(equipoLocal, equipoVisitante);
                i++;
            }
        } catch (IOException e) {
            System.err.println("Error al leer archivo partidos: " + e.getMessage());
            return;
        }

        // Leer el archivo de resultados y crear un objeto de la clase Pronostico
        Pronostico pronostico;
        try (BufferedReader br = new BufferedReader(new FileReader(archivoResultados))) {
            String line = br.readLine();
            String[] tokens = line.split(",");
            String resultadoPartido1 = tokens[0];
            String resultadoPartido2 = tokens[1];
            pronostico = new Pronostico(resultadoPartido1, resultadoPartido2);
        } catch (IOException e) {
            System.err.println("Error al leer archivo resultados: " + e.getMessage());
            return;
        }

        // Calcular el puntaje del pronóstico
        int puntaje = pronostico.calcularPuntaje(partidos);

        // Imprimir el puntaje por pantalla
        System.out.println("Puntaje: " + puntaje);
    }
}

class Partido {
    public Equipo equipoLocal;
    public Equipo equipoVisitante;

    public Partido(Equipo equipoLocal, Equipo equipoVisitante) {
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
    }
}

class Equipo {
    public String nombre;
    public int goles;

    public Equipo(String nombre) {
        this.nombre = nombre;
        this.goles = 0;
    }
}

class Pronostico {
    String resultadoPartido1;
    String resultadoPartido2;

    public Pronostico(String resultadoPartido1, String resultadoPartido2) {
        this.resultadoPartido1 = resultadoPartido1;
        this.resultadoPartido2 = resultadoPartido2;
    }

    public int calcularPuntaje(Partido[] partidos) {
        int puntajeTotal = 0;

        // Iteramos por cada partido
        for (int i = 0; i < partidos.length; i++) {
            Partido partido = partidos[i];

            // Obtenemos el resultado real del partido
            int golesLocal = partido.equipoLocal.goles;
            int golesVisitante = partido.equipoVisitante.goles;

            // Obtenemos el resultado pronosticado del partido
            String resultadoPronosticado = i == 0 ? resultadoPartido1 : resultadoPartido2;
            String[] golesPronosticados = resultadoPronosticado.split("-");
            int golesLocalPronosticados = Integer.parseInt(golesPronosticados[0]);
            int golesVisitantePronosticados = Integer.parseInt(golesPronosticados[1]);

            // Calculamos el puntaje del partido
            int puntajePartido = 0;
            if (golesLocal == golesLocalPronosticados && golesVisitante == golesVisitantePronosticados) {
                puntajePartido = 3; // Acertó el resultado exacto
            } else if ((golesLocal > golesVisitante && golesLocalPronosticados > golesVisitantePronosticados)
                    || (golesLocal == golesVisitante && golesLocalPronosticados == golesVisitantePronosticados)
                    || (golesLocal < golesVisitante && golesLocalPronosticados < golesVisitantePronosticados)) {
                puntajePartido = 1; // Acertó el ganador o el empate
            }

            // Sumamos el puntaje del partido al puntaje total
            puntajeTotal += puntajePartido;
        }

        return puntajeTotal;
    }
}
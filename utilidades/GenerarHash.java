import java.io.Console;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarHash {

    public static void main(String[] args) {
        Console consola = System.console();

        if (consola == null) {
            System.out.println(
                "Ejecuta este programa desde una terminal."
            );
            return;
        }

        char[] clave = consola.readPassword(
            "Escribe la nueva contraseña: "
        );

        if (clave == null || clave.length == 0) {
            System.out.println("La contraseña no puede estar vacía.");
            return;
        }

        String hash = new BCryptPasswordEncoder()
            .encode(new String(clave));

        System.out.println("HASH_GENERADO=" + hash);

        java.util.Arrays.fill(clave, '\0');
    }
}
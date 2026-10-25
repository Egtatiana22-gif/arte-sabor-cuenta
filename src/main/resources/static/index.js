const API = "/api/auth";

        const formularioLogin = document.getElementById("formulario-login");
        const formularioRegistro = document.getElementById("formulario-registro");
        const pestanaLogin = document.getElementById("pestana-login");
        const pestanaRegistro = document.getElementById("pestana-registro");
        const mensaje = document.getElementById("mensaje");
        const titulo = document.getElementById("titulo-formulario");
        const subtitulo = document.getElementById("subtitulo-formulario");

        const anio = document.getElementById("anio");

        if (anio) {
            anio.textContent = new Date().getFullYear();
}

        function mostrarMensaje(texto, tipo) {
            mensaje.textContent = texto;
            mensaje.className = "mensaje " + tipo;
        }

        function limpiarMensaje() {
            mensaje.textContent = "";
            mensaje.className = "mensaje";
        }

        function cambiarFormulario(modo) {
            limpiarMensaje();
            const esLogin = modo === "login";

            formularioLogin.classList.toggle("oculto", !esLogin);
            formularioRegistro.classList.toggle("oculto", esLogin);

            pestanaLogin.classList.toggle("activa", esLogin);
            pestanaRegistro.classList.toggle("activa", !esLogin);
            pestanaLogin.setAttribute("aria-selected", String(esLogin));
            pestanaRegistro.setAttribute("aria-selected", String(!esLogin));

            titulo.textContent = esLogin
                ? "Qué gusto tenerte aquí"
                : "Forma parte de nuestra casa";

            subtitulo.textContent = esLogin
                ? "Ingresa tus datos para acceder a tu cuenta."
                : "Crea tu cuenta para comenzar tu experiencia.";

            (esLogin
                ? document.getElementById("login-correo")
                : document.getElementById("registro-nombre")).focus();
        }

        pestanaLogin.addEventListener("click", () => cambiarFormulario("login"));
        pestanaRegistro.addEventListener("click", () => cambiarFormulario("registro"));

        async function enviarSolicitud(url, datos) {
            const respuesta = await fetch(url, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(datos)
            });

            const texto = await respuesta.text();
            let resultado = {};

            try {
                resultado = texto ? JSON.parse(texto) : {};
            } catch {
                resultado = { mensaje: texto };
            }

            if (!respuesta.ok) {
                throw new Error(
                    resultado.mensaje ||
                    resultado.error ||
                    (respuesta.status === 401
                        ? "Correo o contraseña incorrectos."
                        : "No fue posible completar la solicitud.")
                );
            }

            return resultado;
        }

        formularioRegistro.addEventListener("submit", async (evento) => {
            evento.preventDefault();
            limpiarMensaje();
            
            const campoNombre = document.getElementById("registro-nombre");
            const nombre = campoNombre.value.trim();

            const patronNombre = /^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ]+(?:[ '-][A-Za-zÁÉÍÓÚÜÑáéíóúüñ]+)*$/;

            if (!patronNombre.test(nombre)) {
                mostrarMensaje(
                    "El nombre solo puede contener letras, espacios, guiones y apóstrofes. No se permiten números.",
                    "error"
                );
                campoNombre.focus();
                return;
        }
            const boton = document.getElementById("boton-registro");
            boton.disabled = true;
            boton.textContent = "Creando cuenta...";

            const datos = {
                nombre: document.getElementById("registro-nombre").value.trim(),
                correo: document.getElementById("registro-correo").value.trim(),
                contrasena: document.getElementById("registro-contrasena").value
            };

            try {
                const resultado = await enviarSolicitud(
                    `${API}/registro`, datos
                );

                formularioRegistro.reset();
                cambiarFormulario("login");

                mostrarMensaje(
                    resultado.mensaje ||
                    "¡Tu cuenta fue creada correctamente! Ahora inicia sesión.",
                    "exito"
                );

            document.getElementById("login-correo").value = datos.correo;
            } catch (error) {
                mostrarMensaje(error.message, "error");
            } finally {
                boton.disabled = false;
                boton.textContent = "Crear mi cuenta";
            }
        });

        formularioLogin.addEventListener("submit", async (evento) => {
            evento.preventDefault();
            limpiarMensaje();

            const boton = document.getElementById("boton-login");
            boton.disabled = true;
            boton.textContent = "Verificando datos...";

            const datos = {
                correo: document.getElementById("login-correo").value.trim(),
                contrasena: document.getElementById("login-contrasena").value
            };

            try {
                const resultado = await enviarSolicitud(
                    `${API}/login`, datos
                );

                if (resultado.token) {
                    sessionStorage.setItem("arteSaborToken", resultado.token);
                }

                mostrarMensaje(
                    resultado.mensaje || "Inicio de sesión correcto.",
                    "exito"
                );

                // Aún no se redirige: podemos conectar aquí el panel
                // correspondiente al rol cuando desarrollemos esa pantalla.
            } catch (error) {
                mostrarMensaje(error.message, "error");
            } finally {
                boton.disabled = false;
                boton.textContent = "Ingresar a mi cuenta";
            }
        });
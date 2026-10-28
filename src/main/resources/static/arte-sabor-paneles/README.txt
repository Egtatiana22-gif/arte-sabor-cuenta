ARTE SABOR — PANELES POR ROL (BASE VISUAL)
==========================================

Incluye:
- administrador.html
- empleado.html
- cliente.html
- dashboard.css
- dashboard.js

Importante:
1. Estos archivos son una base visual para copiar dentro de:
   arte-sabor-cuenta/src/main/resources/static/
2. No reemplaces index.html ni index.js todavía.
3. El inicio de sesión actual debe guardar el JWT en sessionStorage con la clave:
   arteSaborToken
4. El backend ya conocido expone:
   GET /api/perfil
   GET /api/admin/usuarios (solo ADMINISTRADOR)
5. Las tarjetas de pedidos, reservas, inventario, mensajes y estadísticas son visuales.
   Esas APIs de negocio todavía deben implementarse y por eso no muestran datos reales.
6. Para probar manualmente una pantalla, abre su URL después de iniciar sesión:
   http://localhost:8081/administrador.html
   http://localhost:8081/empleado.html
   http://localhost:8081/cliente.html

La página verifica el perfil y el rol. Si el usuario no tiene permiso, lo devuelve
al inicio de sesión. Los paneles comparten colores y estilo tipográfico: encabezados
con letra manuscrita elegante, texto legible y paleta verde oscuro, crema y terracota.

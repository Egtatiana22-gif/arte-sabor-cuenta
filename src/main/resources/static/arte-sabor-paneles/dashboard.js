(() => {
  "use strict";
  const tokenKey = "arteSaborToken";
  const token = sessionStorage.getItem(tokenKey);
  const expectedRole = document.body.dataset.role;
  const page = document.body.dataset.page;

  function goToLogin() {
    sessionStorage.removeItem(tokenKey);
    window.location.replace("/index.html");
  }

  document.querySelectorAll("[data-logout]").forEach(button => {
    button.addEventListener("click", goToLogin);
  });

  function text(el, value) {
    if (el) el.textContent = value;
  }

  async function loadProfile() {
    if (!token) {
      window.location.replace("/index.html");
      return;
    }
    try {
      const response = await fetch("/api/perfil", {
        headers: { Authorization: `Bearer ${token}` }
      });
      if (response.status === 401 || response.status === 403) {
        goToLogin();
        return;
      }
      if (!response.ok) throw new Error("No fue posible verificar el perfil.");
      const profile = await response.json();
      const role = String(profile.rol || "").replace(/^ROLE_/, "");
      if (role !== expectedRole) {
        window.location.replace("/index.html");
        return;
      }
      text(document.getElementById("user-email"), profile.correo || "Sesión activa");
      text(document.getElementById("client-email-inline"), profile.correo || "—");
      if (page === "admin") {
        const button = document.getElementById("load-users");
        if (button) button.addEventListener("click", loadUsers);
      }
    } catch (error) {
      console.error(error);
      const feedback = document.getElementById("users-feedback");
      if (feedback) text(feedback, "No se pudo verificar la sesión. Comprueba que la API esté activa.");
    }
  }

  async function loadUsers() {
    const feedback = document.getElementById("users-feedback");
    const tbody = document.getElementById("users-table");
    const button = document.getElementById("load-users");
    text(feedback, "Consultando usuarios…");
    button.disabled = true;
    try {
      const response = await fetch("/api/admin/usuarios", {
        method: "GET",
        headers: { Authorization: `Bearer ${token}` }
      });
      if (response.status === 401) throw new Error("La sesión expiró. Cierra sesión e inicia de nuevo.");
      if (response.status === 403) throw new Error("Tu cuenta no tiene permisos de administrador.");
      if (!response.ok) throw new Error(`La API respondió ${response.status}.`);
      const users = await response.json();
      tbody.replaceChildren();
      if (!Array.isArray(users) || users.length === 0) {
        const row = document.createElement("tr");
        const cell = document.createElement("td");
        cell.colSpan = 3;
        cell.textContent = "No hay usuarios para mostrar.";
        row.appendChild(cell);
        tbody.appendChild(row);
      } else {
        users.forEach(user => {
          const row = document.createElement("tr");
          const nameCell = document.createElement("td");
          const emailCell = document.createElement("td");
          const roleCell = document.createElement("td");
          nameCell.textContent = user.nombre || user.nombres || `Usuario ${user.id ?? ""}`;
          emailCell.textContent = user.correo || "—";
          const role = String(user.rol || "SIN ROL").replace(/^ROLE_/, "");
          const pill = document.createElement("span");
          pill.className = "role-pill";
          pill.textContent = role;
          roleCell.appendChild(pill);
          row.append(nameCell, emailCell, roleCell);
          tbody.appendChild(row);
        });
      }
      text(feedback, `Usuarios recibidos: ${Array.isArray(users) ? users.length : 0}.`);
      const count = document.getElementById("users-count");
      text(count, Array.isArray(users) ? String(users.length) : "—");
    } catch (error) {
      text(feedback, error.message || "No se pudo cargar el listado.");
    } finally {
      button.disabled = false;
    }
  }

  loadProfile();
})();

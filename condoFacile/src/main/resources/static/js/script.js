function openRegisterModal() {
    document.getElementById('registrationModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('registrationModal').style.display = 'none';
}

function openLoginModal() {
    document.getElementById('loginModal').style.display = 'block';
}

function closeLoginModal() {
    document.getElementById('loginModal').style.display = 'none';
}

function openRecoverPasswordModal() {
    document.getElementById('recoverPasswordModal').style.display = 'block';
}

function closeRecoverPasswordModal() {
    document.getElementById('recoverPasswordModal').style.display = 'none';
}

function closeMessageModal() {
    document.getElementById('messageModal').style.display = 'none';
}

function showMessageModal(type, title, message) {
    const modal = document.getElementById('messageModal');
    const content = document.getElementById('messageModalContent');
    const titleElem = document.getElementById('messageTitle');
    const textElem = document.getElementById('messageText');

    content.className = 'modal-content ' + type;
    titleElem.textContent = title;
    textElem.textContent = message;

    modal.style.display = 'block';
}

window.onclick = function(event) {
    const regModal = document.getElementById('registrationModal');
    const logModal = document.getElementById('loginModal');
    const msgModal = document.getElementById('messageModal');
    const recModal = document.getElementById('recoverPasswordModal');

    if (event.target === regModal) closeModal();
    if (event.target === logModal) closeLoginModal();
    if (event.target === msgModal) closeMessageModal();
    if (event.target === recModal) closeRecoverPasswordModal();
};

async function hashPassword(password) {
    const encoder = new TextEncoder();
    const data = encoder.encode(password);
    const hashBuffer = await crypto.subtle.digest('SHA-256', data);
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    return hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
}

document.addEventListener('DOMContentLoaded', () => {
    const appartamentoSelect = document.getElementById('appartamentoSelect');

    async function caricaAppartamentiDisponibili() {
        const TOKEN = "Bearer eyJzdGF0aWMiOiAiY29uZG9mYWNpbGVfYXBwIiwgInJvbGUiOiAiYWRtaW4iLCAiZXhwaXJlcyI6ICIyMDI3LTEyLTMxIn0=";
        try {
            const res = await fetch('/condofacile/api/register/appartamentiList', {
                method: 'GET',
                headers: {
                    'Authorization': TOKEN,
                    'Content-Type': 'application/json'
                }
            });

            if (!res.ok) throw new Error("Errore nel caricamento appartamenti");

            const response = await res.json();
            const appartamenti = response.data;

            appartamentoSelect.innerHTML = '<option value="" disabled selected>Seleziona Appartamento</option>';
            appartamenti.forEach(app => {
                if (!app.occupato) {
                    const opt = document.createElement('option');
                    opt.value = app.codice;
                    opt.textContent = app.codice;
                    appartamentoSelect.appendChild(opt);
                }
            });

            appartamentoSelect.disabled = false;
        } catch (error) {
            console.error(error);
            appartamentoSelect.disabled = true;
            appartamentoSelect.innerHTML = '<option value="" disabled>Errore nel caricamento</option>';
        }
    }

    caricaAppartamentiDisponibili();

    document.getElementById('registrationForm').addEventListener('submit', async function (e) {
        e.preventDefault();
        const formData = new FormData(this);
        const payload = Object.fromEntries(formData.entries());
        payload.password = await hashPassword(payload.password);

        try {
            const TOKEN = "Bearer eyJzdGF0aWMiOiAiY29uZG9mYWNpbGVfYXBwIiwgInJvbGUiOiAiYWRtaW4iLCAiZXhwaXJlcyI6ICIyMDI3LTEyLTMxIn0=";
            const res = await fetch('/condofacile/api/utenti', {
                method: 'POST',
                headers: {
                    'Authorization': TOKEN,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            if (res.ok) {
                showMessageModal("success", "Registrazione riuscita", "Benvenuto su CondoFacile!");
                closeModal();
                this.reset();
            } else {
                const data = await res.json();
                showMessageModal("error", "Errore", data.error || "Registrazione fallita");
            }
        } catch (err) {
            showMessageModal("error", "Errore di rete", "Si è verificato un errore di connessione.");
        }
    });

    document.getElementById('loginForm').addEventListener('submit', async function (e) {
        e.preventDefault();
        const formData = new FormData(this);
        const payload = Object.fromEntries(formData.entries());
        payload.password = await hashPassword(payload.password);

        try {
            const TOKEN = "Bearer eyJzdGF0aWMiOiAiY29uZG9mYWNpbGVfYXBwIiwgInJvbGUiOiAiYWRtaW4iLCAiZXhwaXJlcyI6ICIyMDI3LTEyLTMxIn0=";
            const res = await fetch('/condofacile/api/login/validate', {
                method: 'POST',
                headers: {
                    'Authorization': TOKEN,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            const data = await res.json();

        if (res.ok) {
            const utente = data.data;

            localStorage.setItem("userData", JSON.stringify({
                email: utente.email,
                nome: utente.nome,
                cognome: utente.cognome
            }));

            showMessageModal("success", "Accesso effettuato", "Benvenuto! Verrai reindirizzato...");
            closeLoginModal();
            this.reset();
            setTimeout(() => location.href = "/html/dashboard.html", 1500);
        } else {
                showMessageModal("error", "Errore di accesso", data.message || "Email o password non valide.");
            }
        } catch (err) {
            console.error("Errore nel login:", err);
            showMessageModal("error", "Errore di rete", "Impossibile contattare il server.");
        }
    });

  document.getElementById('recoverPasswordForm').addEventListener('submit', async function (e) {
      e.preventDefault();

      const email = this.email.value;
      const newPassword = this.newPassword.value;

      // Hashiamo la nuova password
      const hashedPassword = await hashPassword(newPassword);

      const payload = {
          recoveryMethod: 'email',  // fisso a email
          email: email,
          newPassword: hashedPassword
      };

      try {
          const TOKEN = "Bearer eyJzdGF0aWMiOiAiY29uZG9mYWNpbGVfYXBwIiwgInJvbGUiOiAiYWRtaW4iLCAiZXhwaXJlcyI6ICIyMDI3LTEyLTMxIn0=";
          const res = await fetch('/condofacile/restore/password/recover', {
              method: 'POST',
              headers: {
                  'Authorization': TOKEN,
                  'Content-Type': 'application/json'
              },
              body: JSON.stringify(payload)
          });

          if (res.ok) {
              showMessageModal("success", "Password aggiornata", "Abbiamo inviato un link alla tua email per completare il reset della password. Clicca sul link e accedi con la nuova password.");
              closeRecoverPasswordModal();
              this.reset();
          } else {
              const data = await res.json();
              showMessageModal("error", "Errore", data.message || "Impossibile inviare la nuova password.");
          }
      } catch (err) {
          showMessageModal("error", "Errore di rete", "Si è verificato un errore di connessione.");
      }
  });
});
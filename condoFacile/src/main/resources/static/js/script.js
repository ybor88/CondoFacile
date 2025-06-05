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

    if (event.target === regModal) closeModal();
    if (event.target === logModal) closeLoginModal();
    if (event.target === msgModal) closeMessageModal();
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
        try {
            const res = await fetch('/condofacile/api/register/appartamentiList');
            if (!res.ok) throw new Error("Errore nel caricamento appartamenti");

            const response = await res.json();
            const appartamenti = response.data;

            if (!Array.isArray(appartamenti)) throw new Error("Formato dati non valido");

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
            const res = await fetch('/condofacile/api/utenti', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
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

        try {
            const res = await fetch('/api/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (res.ok) {
                showMessageModal("success", "Accesso effettuato", "Verrai reindirizzato...");
                closeLoginModal();
                this.reset();
                setTimeout(() => location.href = "/dashboard", 1500);
            } else {
                const data = await res.json();
                showMessageModal("error", "Errore di accesso", data.message || "Email o password non valide.");
            }
        } catch (err) {
            showMessageModal("error", "Errore di rete", "Impossibile contattare il server.");
        }
    });
});
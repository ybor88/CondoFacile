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

window.onclick = function(event) {
    const regModal = document.getElementById('registrationModal');
    const logModal = document.getElementById('loginModal');

    if (event.target === regModal) {
        closeModal();
    } else if (event.target === logModal) {
        closeLoginModal();
    }
};

async function hashPassword(password) {
    const encoder = new TextEncoder();
    const data = encoder.encode(password);
    const hashBuffer = await crypto.subtle.digest('SHA-256', data);
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    const hashHex = hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
    return hashHex;
}

document.addEventListener('DOMContentLoaded', () => {

    const appartamentoSelect = document.getElementById('appartamentoSelect');

    async function caricaAppartamentiDisponibili() {
        try {
            const res = await fetch('/condofacile/api/register/appartamentiList');
            if (!res.ok) throw new Error("Errore nel caricamento appartamenti");

            const response = await res.json();
            console.log("Risposta API appartamenti:", response);

            // Estraggo l'array di appartamenti dal campo data
            const appartamenti = response.data;

            if (!Array.isArray(appartamenti)) {
                throw new Error("Dati appartamenti non sono un array");
            }

            appartamentoSelect.innerHTML = '<option value="" disabled selected>Seleziona Appartamento</option>';

            appartamenti.forEach(appartamento => {
                // Prendo solo i codici di appartamento non occupati
                if (!appartamento.occupato) {
                    const option = document.createElement('option');
                    option.value = appartamento.codice;
                    option.textContent = appartamento.codice;
                    appartamentoSelect.appendChild(option);
                }
            });

            appartamentoSelect.disabled = false; // abilita la select

        } catch (error) {
            console.error(error);
            appartamentoSelect.disabled = true;
            appartamentoSelect.innerHTML = '<option value="" disabled>Errore nel caricamento</option>';
        }
    }

    caricaAppartamentiDisponibili();

    const regForm = document.getElementById('registrationForm');
    regForm.addEventListener('submit', async function(e) {
        e.preventDefault();

        const formData = new FormData(this);
        const payload = Object.fromEntries(formData.entries());

        // Hash della password con SHA-256
        const hashedPassword = await hashPassword(payload.password);
        payload.password = hashedPassword;

        try {
            const res = await fetch('/condofacile/api/utenti', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (res.ok) {
                alert("Registrazione completata!");
                closeModal();
                this.reset();
            } else {
                const data = await res.json();
                alert("Errore: " + (data.message || "Registrazione fallita"));
            }
        } catch (err) {
            alert("Errore di rete o server.");
        }
    });

    const loginForm = document.getElementById('loginForm');
    loginForm.addEventListener('submit', async function(e) {
        e.preventDefault();

        const formData = new FormData(this);
        const payload = Object.fromEntries(formData.entries());

        // Se vuoi, qui puoi aggiungere la cifratura della password come sopra

        try {
            const res = await fetch('/api/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (res.ok) {
                alert("Accesso effettuato!");
                closeLoginModal();
                this.reset();
                location.href = "/dashboard"; // pagina post-login
            } else {
                const data = await res.json();
                alert("Errore: " + (data.message || "Accesso fallito"));
            }
        } catch (err) {
            alert("Errore di rete o server.");
        }
    });
});
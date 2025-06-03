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

document.addEventListener('DOMContentLoaded', () => {
    const regForm = document.getElementById('registrationForm');
    regForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        const formData = new FormData(this);
        const payload = Object.fromEntries(formData.entries());

        try {
            const res = await fetch('/api/register', {
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
                location.href = "/dashboard"; // o la tua pagina post-login
            } else {
                const data = await res.json();
                alert("Errore: " + (data.message || "Accesso fallito"));
            }
        } catch (err) {
            alert("Errore di rete o server.");
        }
    });
});
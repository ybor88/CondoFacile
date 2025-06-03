// register.js

function openModal() {
    document.getElementById('registrationModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('registrationModal').style.display = 'none';
}

window.onclick = function(event) {
    const modal = document.getElementById('registrationModal');
    if (event.target === modal) {
        closeModal();
    }
};

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('registrationForm');
    form.addEventListener('submit', async function(e) {
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
});

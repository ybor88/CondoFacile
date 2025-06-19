document.addEventListener("DOMContentLoaded", () => {
  const userData = JSON.parse(localStorage.getItem("userData"));
  if (!userData || !userData.nome || !userData.email) {
    alert("Sessione non valida. Effettua nuovamente il login.");
    window.location.href = "/Condofacile";
  } else {
    document.getElementById("welcomeText").textContent =
      `Ciao ${userData.nome} ${userData.cognome} (${userData.email})`;
  }
});

function logout() {
  localStorage.removeItem("userData");
  localStorage.removeItem("jwt");
  window.location.href = "/Condofacile";
}

async function navigateTo(section) {
  if (section === "bollette") {
    await caricaBollette();
    return;
  }

  const content = {
    avvisi: "<h3>📢 Avvisi</h3><p>Leggi gli avvisi generali e personali del condominio.</p>",
    chat: "<h3>💬 Chat</h3><p>Comunica con altri condomini o con l'amministratore.</p>",
    documenti: "<h3>📁 Documenti</h3><p>Scarica i documenti condivisi del condominio.</p>",
    vetrina: "<h3>🌟 Iniziative</h3><p>Proponi o vota iniziative condominiali.</p>",
    bilancio: "<h3>📊 Bilancio</h3><p>Consulta il bilancio generale e quello personale mensile.</p>"
  };

  document.getElementById("mainContent").innerHTML = content[section] || "<p>Sezione non disponibile.</p>";
}

async function caricaBollette() {
  try {
    const TOKEN = "Bearer eyJzdGF0aWMiOiAiY29uZG9mYWNpbGVfYXBwIiwgInJvbGUiOiAiYWRtaW4iLCAiZXhwaXJlcyI6ICIyMDI3LTEyLTMxIn0=";
    const res = await fetch("http://localhost:9090/condofacile/api/bollette", {
      method: "GET",
      headers: {
        Authorization: TOKEN,
        "Content-Type": "application/json"
      }
    });

    if (!res.ok) throw new Error("Errore nel recupero delle bollette");

    const result = await res.json();
    const bollette = result.data; // <-- CORRETTO QUI

    if (!Array.isArray(bollette)) throw new Error("Formato risposta non valido");

    let html = `<h3>📄 Le tue Bollette</h3><ul style="list-style:none;padding:0;">`;

   bollette.forEach(b => {
        html += `
          <li style="background:#fff;padding:1rem;margin-bottom:1rem;border-radius:10px;">
            <strong>Descrizione:</strong> ${b.descrizione}<br/>
            <strong>Importo:</strong> €${b.importo.toFixed(2)}<br/>
            <strong>Data Emissione:</strong> ${b.dataEmissione}<br/>
            <strong>Data Scadenza:</strong> ${b.dataScadenza}<br/>
            <strong>Stato:</strong> ${b.pagata ? "✅ Pagata" : "❌ Da pagare"}<br/>
            <a href="${b.fileUrl}" target="_blank">📎 Scarica PDF</a>
          </li>
        `;
      });

    html += "</ul>";
    document.getElementById("mainContent").innerHTML = html;
  } catch (err) {
    document.getElementById("mainContent").innerHTML =
      `<p style="color:red;">Errore: ${err.message}</p>`;
  }
}
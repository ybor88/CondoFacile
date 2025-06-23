// dashboard.js

let tutteLeBollette = [];

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
    const res = await fetch("/condofacile/api/bollette", {
      method: "GET",
      headers: {
        Authorization: TOKEN,
        "Content-Type": "application/json"
      }
    });

    if (!res.ok) throw new Error("Errore nel recupero delle bollette");

    const result = await res.json();
    const bollette = result.data;

    if (!Array.isArray(bollette)) throw new Error("Formato risposta non valido");

    tutteLeBollette = bollette;

    let html = `
      <h3>📄 Le tue Bollette</h3>

      <div id="filtri" style="margin-bottom:1rem;background:#f0f0f0;padding:1rem;border-radius:10px;">
        <h4>🔍 Filtra Bollette</h4>
        <input type="text" id="filtroDescrizione" placeholder="Descrizione" />
        <input type="number" id="filtroImportoMin" placeholder="Importo Min" />
        <input type="number" id="filtroImportoMax" placeholder="Importo Max" />
        <input type="date" id="filtroDataEmissione" />
        <input type="date" id="filtroDataScadenza" />
        <select id="filtroStato">
          <option value="">Tutti gli stati</option>
          <option value="pagata">✅ Pagata</option>
          <option value="non_pagata">❌ Da pagare</option>
        </select>
        <button onclick="applicaFiltri()">Applica Filtri</button>
        <button onclick="resetFiltri()">Reset Filtri</button>
      </div>

      <ul id="listaBollette" style="list-style:none;padding:0;">
    `;

    bollette.forEach(b => {
      html += `
        <li style="background:#fff;padding:1rem;margin-bottom:1rem;border-radius:10px;">
          <strong>Descrizione:</strong> ${b.descrizione}<br/>
          <strong>Importo:</strong> €${b.importo.toFixed(2)}<br/>
          <strong>Data Emissione:</strong> ${b.dataEmissione}<br/>
          <strong>Data Scadenza:</strong> ${b.dataScadenza}<br/>
          <strong>Stato:</strong> ${b.pagata ? "✅ Pagata" : "❌ Da pagare"}<br/>
           <button onclick='scaricaPdf(${JSON.stringify(b)})'>📎 Scarica PDF</button>
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

function resetFiltri() {
  document.getElementById("filtroDescrizione").value = "";
  document.getElementById("filtroImportoMin").value = "";
  document.getElementById("filtroImportoMax").value = "";
  document.getElementById("filtroDataEmissione").value = "";
  document.getElementById("filtroDataScadenza").value = "";
  document.getElementById("filtroStato").value = "";

  aggiornaLista(tutteLeBollette);
}

function applicaFiltri() {
  const descrizione = document.getElementById("filtroDescrizione").value.toLowerCase();
  const minImporto = parseFloat(document.getElementById("filtroImportoMin").value);
  const maxImporto = parseFloat(document.getElementById("filtroImportoMax").value);
  const dataEm = document.getElementById("filtroDataEmissione").value;
  const dataSc = document.getElementById("filtroDataScadenza").value;
  const stato = document.getElementById("filtroStato").value;

  const filtrate = tutteLeBollette.filter(b => {
    const descrMatch = b.descrizione.toLowerCase().includes(descrizione);
    const importoMatch = (isNaN(minImporto) || b.importo >= minImporto) &&
                         (isNaN(maxImporto) || b.importo <= maxImporto);
    const emMatch = !dataEm || b.dataEmissione >= dataEm;
    const scMatch = !dataSc || b.dataScadenza <= dataSc;
    const statoMatch = !stato || (stato === "pagata" && b.pagata) || (stato === "non_pagata" && !b.pagata);

    return descrMatch && importoMatch && emMatch && scMatch && statoMatch;
  });

  aggiornaLista(filtrate);
}

function aggiornaLista(bollette) {
  const ul = document.getElementById("listaBollette");
  if (!ul) return;

  ul.innerHTML = "";

  if (bollette.length === 0) {
    ul.innerHTML = "<li>Nessuna bolletta trovata con questi filtri.</li>";
    return;
  }

  bollette.forEach(b => {
    ul.innerHTML += `
      <li style="background:#fff;padding:1rem;margin-bottom:1rem;border-radius:10px;">
        <strong>Descrizione:</strong> ${b.descrizione}<br/>
        <strong>Importo:</strong> €${b.importo.toFixed(2)}<br/>
        <strong>Data Emissione:</strong> ${b.dataEmissione}<br/>
        <strong>Data Scadenza:</strong> ${b.dataScadenza}<br/>
        <strong>Stato:</strong> ${b.pagata ? "✅ Pagata" : "❌ Da pagare"}<br/>
         <button onclick='scaricaPdf(${JSON.stringify(b)})'>📎 Scarica PDF</button>
      </li>
    `;
  });
}

async function scaricaPdf(bolletta) {
  const TOKEN = "Bearer eyJzdGF0aWMiOiAiY29uZG9mYWNpbGVfYXBwIiwgInJvbGUiOiAiYWRtaW4iLCAiZXhwaXJlcyI6ICIyMDI3LTEyLTMxIn0=";

  try {
    const response = await fetch("/condofacile/api/bollette/pdf", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": TOKEN
      },
      body: JSON.stringify(bolletta)
    });

    if (!response.ok) {
      throw new Error("Errore nella generazione del PDF");
    }

    const blob = await response.blob();
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;

    // Costruisci un nome file custom basato su descrizione + dataEmissione (o email, o altro)
    // Sostituisci spazi e caratteri non validi nel filename
    const safeDescrizione = (bolletta.descrizione || "bolletta")
      .replace(/\s+/g, "_")
      .replace(/[^\w\-]/g, "");
    const dataEmissione = bolletta.dataEmissione ? bolletta.dataEmissione : "";
    const fileName = `${safeDescrizione}_${dataEmissione}.pdf`;

    a.download = fileName;
    a.target = "_blank";
    a.click();
    URL.revokeObjectURL(url);

  } catch (error) {
    alert("Errore nel download del PDF: " + error.message);
  }
}
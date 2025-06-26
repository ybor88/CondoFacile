let tutteLeBollette = [];
let tuttiGliAvvisi = [];

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

  if (section === "avvisi") {
    await caricaAvvisi();
    return;
  }

  const content = {
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

    const safeDescrizione = (bolletta.descrizione || "bolletta")
      .replace(/\s+/g, "_")
      .replace(/[^\w\-]/g, "");
    const dataEmissione = bolletta.dataEmissione ? bolletta.dataEmissione : "";
    const fileName = `${safeDescrizione}_${dataEmissione}.pdf`;

    a.href = url;
    a.download = fileName;
    a.target = "_blank";
    a.click();
    URL.revokeObjectURL(url);

  } catch (error) {
    alert("Errore nel download del PDF: " + error.message);
  }
}

async function caricaAvvisi() {
  const TOKEN = "Bearer eyJzdGF0aWMiOiAiY29uZG9mYWNpbGVfYXBwIiwgInJvbGUiOiAiYWRtaW4iLCAiZXhwaXJlcyI6ICIyMDI3LTEyLTMxIn0=";

  const content = `
    <h3>📢 Avvisi del Condominio</h3>

    <div style="background:#f0f0f0;padding:1rem;border-radius:10px;margin-bottom:1rem;">
      <input type="text" id="filtroTitoloAvvisi" placeholder="Filtra per titolo..." style="margin-right:0.5rem;" oninput="filtraAvvisi()" />
      <input type="text" id="titoloAvviso" placeholder="Titolo" style="margin-right:0.5rem;" />
      <input type="text" id="messaggioAvviso" placeholder="Messaggio" style="margin-right:0.5rem;" />
      <button onclick="aggiungiAvviso()">Aggiungi Avviso</button>
    </div>

    <table style="width:100%;border-collapse:collapse;">
      <thead>
        <tr style="background:#ddd;">
          <th style="padding:0.5rem;border:1px solid #ccc;">Titolo</th>
          <th style="padding:0.5rem;border:1px solid #ccc;">Messaggio</th>
          <th style="padding:0.5rem;border:1px solid #ccc;">Data Pubblicazione</th>
        </tr>
      </thead>
      <tbody id="tabellaAvvisi"></tbody>
    </table>
  `;

  document.getElementById("mainContent").innerHTML = content;

  try {
    const res = await fetch("/condofacile/api/avvisi", {
      method: "GET",
      headers: {
        "Authorization": TOKEN,
        "Content-Type": "application/json"
      }
    });

    if (!res.ok) throw new Error("Errore nel recupero degli avvisi");

    const json = await res.json();
    tuttiGliAvvisi = json.data || [];
    aggiornaTabellaAvvisi(tuttiGliAvvisi);
  } catch (error) {
    document.getElementById("tabellaAvvisi").innerHTML = `
      <tr><td colspan="3" style="color:red;">Errore: ${error.message}</td></tr>
    `;
  }
}

function aggiornaTabellaAvvisi(avvisi) {
  const tbody = document.getElementById("tabellaAvvisi");
  tbody.innerHTML = "";

  if (!avvisi.length) {
    tbody.innerHTML = `
      <tr><td colspan="3" style="text-align:center;padding:1rem;">Nessun avviso disponibile.</td></tr>
    `;
    return;
  }

  avvisi.forEach(avv => {
    const data = avv.dataPubblicazione ? new Date(avv.dataPubblicazione).toLocaleString() : "-";
    tbody.innerHTML += `
      <tr>
        <td style="padding:0.5rem;border:1px solid #ccc;">${avv.titolo}</td>
        <td style="padding:0.5rem;border:1px solid #ccc;">${avv.messaggio}</td>
        <td style="padding:0.5rem;border:1px solid #ccc;">${data}</td>
      </tr>
    `;
  });
}

function filtraAvvisi() {
  const filtro = document.getElementById("filtroTitoloAvvisi").value.toLowerCase();
  const filtrati = tuttiGliAvvisi.filter(avv => avv.titolo.toLowerCase().includes(filtro));
  aggiornaTabellaAvvisi(filtrati);
}

async function aggiungiAvviso() {
  const titolo = document.getElementById("titoloAvviso").value.trim();
  const messaggio = document.getElementById("messaggioAvviso").value.trim();
  const TOKEN = localStorage.getItem("jwt");

  if (!titolo || !messaggio) {
    alert("Compila sia il titolo che il messaggio.");
    return;
  }

  const nuovoAvviso = {
    titolo,
    messaggio,
    soloPersonale: false,
    destinatarioId: null
  };

  try {
    const res = await fetch("/condofacile/api/avvisi", {
      method: "POST",
      headers: {
        "Authorization": TOKEN,
        "Content-Type": "application/json"
      },
      body: JSON.stringify(nuovoAvviso)
    });

    if (!res.ok) throw new Error("Errore nella creazione dell'avviso");

    alert("Avviso creato con successo!");
    await caricaAvvisi();
  } catch (err) {
    alert("Errore: " + err.message);
  }
}
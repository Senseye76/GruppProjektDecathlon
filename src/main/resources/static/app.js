const el = (id) => document.getElementById(id);
const err = el('error');
const msg = el('msg');

const MODE = { DEC: 'DEC', HEP: 'HEP' };

const DEC_EVENTS = [
  { id: '100m', label: '100m (s)' },
  { id: 'longJump', label: 'Long Jump (cm)' },
  { id: 'shotPut', label: 'Shot Put (m)' },
  { id: 'highJump', label: 'High Jump (cm)' },
  { id: '400m', label: '400m (s)' },
  { id: '110mHurdles', label: '110m Hurdles (s)' },
  { id: 'discusThrow', label: 'Discus Throw (m)' },
  { id: 'poleVault', label: 'Pole Vault (cm)' },
  { id: 'javelinThrow', label: 'Javelin Throw (m)' },
  { id: '1500m', label: '1500m (s)' }
];

const HEP_EVENTS = [
  { id: 'hep100mHurdles', label: '100m Hurdles (s)' },
  { id: 'hepHighJump', label: 'High Jump (cm)' },
  { id: 'hepShotPut', label: 'Shot Put (m)' },
  { id: '200m', label: '200m (s)' },
  { id: 'hepLongJump', label: 'Long Jump (cm)' },
  { id: 'hepJavelinThrow', label: 'Javelin Throw (m)' },
  { id: '800m', label: '800m (s)' }
];

function currentEvents() {
  return el('mode').value === MODE.DEC ? DEC_EVENTS : HEP_EVENTS;
}

function setError(text) { err.textContent = text; }
function setMsg(text) { msg.textContent = text; }

function escapeHtml(s){
  return String(s).replace(/[&<>"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c]));
}

async function fetchJsonStrict(url){
  const res = await fetch(url);
  const contentType = res.headers.get('content-type') || '';
  const body = await res.text();
  let data = null;
  if (contentType.includes('application/json')) {
    try { data = JSON.parse(body); } catch (e) {}
  }
  if (!res.ok) {
    const m = (body && body.trim()) ? body.trim() : `${res.status} ${res.statusText}`;
    throw new Error(m);
  }
  if (!data) {
    throw new Error(`Expected JSON but got: ${body.slice(0,200)}`);
  }
  return data;
}

function populateEventSelect() {
  const sel = el('event');
  sel.innerHTML = '';
  for (const ev of currentEvents()) {
    const o = document.createElement('option');
    o.value = ev.id;
    o.textContent = ev.label;
    sel.appendChild(o);
  }
}

function renderHeader() {
  const head = el('headRow');
  const cols = ['Name', ...currentEvents().map(e => e.label), 'Total'];
  head.innerHTML = cols.map(h => `<th>${escapeHtml(h)}</th>`).join('');
}

async function renderStandings() {
  try {
    setMsg('');
    const data = await fetchJsonStrict('/com/example/decathlon/api/standings');
    if (!Array.isArray(data)) {
      setError('Standings format error (not an array).');
      el('standings').innerHTML = '';
      return;
    }

    const cols = currentEvents().map(e => e.id);

    const rows = data.map(r => {
      const scores = r.scores || {};
      const perCols = cols.map(k => Number(scores[k] ?? 0));
      const total = perCols.reduce((a,b)=>a+b,0);
      return {
        name: r.name || '',
        cells: cols.map(k => scores[k] ?? ''),
        total
      };
    }).sort((a,b)=> b.total - a.total);

    const html = rows.map(r => `
      <tr>
        <td>${escapeHtml(r.name)}</td>
        ${r.cells.map(c => `<td>${c}</td>`).join('')}
        <td>${r.total}</td>
      </tr>
    `).join('');

    el('standings').innerHTML = html || `<tr><td colspan="${currentEvents().length+2}" style="opacity:.7">No data yet</td></tr>`;
    setMsg(`Standings updated (${rows.length} rows)`);
  } catch (e) {
    setError(`Could not load standings: ${e.message}`);
    el('standings').innerHTML = '';
  }
}

el('add').addEventListener('click', async () => {
  const name = el('name').value;
  try {
    const res = await fetch('/com/example/decathlon/api/competitors', {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name })
    });
    if (!res.ok) {
      const t = await res.text();
      setError(t || 'Failed to add competitor');
    } else {
      setMsg('Added');
    }
    await renderStandings();
  } catch (e) {
    setError('Network error');
  }
});

el('save').addEventListener('click', async () => {
  const body = {
    name: el('name2').value,
    event: el('event').value,
    raw: parseFloat(el('raw').value)
  };
  try {
    const res = await fetch('/com/example/decathlon/api/score', {
      method: 'POST', headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });
    const json = await res.json();
    setMsg(`Saved: ${json.points} pts`);
    await renderStandings();
  } catch (e) {
    setError('Score failed');
  }
});

let sortBroken = false;

el('export').addEventListener('click', async () => {
  try {
    const res = await fetch('/com/example/decathlon/api/export.csv');
    const text = await res.text();
    const blob = new Blob([text], { type: 'text/csv;charset=utf-8' });
    const a = document.createElement('a');
    a.href = URL.createObjectURL(blob);
    a.download = 'results.csv';
    a.click();
    sortBroken = true;
  } catch (e) {
    setError('Export failed');
  }
});

el('mode').addEventListener('change', () => {
  populateEventSelect();
  renderHeader();
  renderStandings();
});

populateEventSelect();
renderHeader();
renderStandings();

const el = (id) => document.getElementById(id);
const err = el('error');
const msg = el('msg');


const COLUMNS = [
  { key: '100m',         header: '100m' },
  { key: 'longJump',     header: 'Long Jump' },
  { key: 'shotPut',      header: 'Shot Put' },
  { key: '400m',         header: '400m' },
  { key: '1500m',        header: '1500m' },
  { key: '110mHurdles',  header: '110m Hurdles' },
  { key: 'highJump',     header: 'High jump' },
  { key: 'poleVault',    header: 'Pole vault' },
  { key: 'discusThrow',  header: 'Discus throw' },
  { key: 'javelinThrow', header: 'Javelin Throw' }
];

function setError(text) { err.textContent = text; }
function setMsg(text) { msg.textContent = text; /* err.textContent not always cleared */ }

el('add').addEventListener('click', async () => {
  const name = el('name').value; // NOTE: no trim here (intentional)
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
      // sometimes forget to clear error -> students can assert stale error
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

let sortBroken = false; // becomes true after export -> sorting bug

el('export').addEventListener('click', async () => {
  try {
    const res = await fetch('/com/example/decathlon/api/export.csv');
    const text = await res.text();
    const blob = new Blob([text], { type: 'text/csv;charset=utf-8' });
    const a = document.createElement('a');
    a.href = URL.createObjectURL(blob);
    a.download = 'results.csv';
    a.click();
    sortBroken = true; // trigger sorting issue after export
  } catch (e) {
    setError('Export failed');
  }
});

function escapeHtml(s){
  return String(s).replace(/[&<>"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c]));
}

async function fetchJsonStrict(url){
  const res = await fetch(url);
  const contentType = res.headers.get('content-type') || '';
  const body = await res.text(); // läs en gång

  // Försök tolka JSON om möjligt
  let data = null;
  if (contentType.includes('application/json')) {
    try { data = JSON.parse(body); } catch (e) { /* fall through */ }
  }

  if (!res.ok) {
    // Visa serverns feltext om det finns
    const msg = (body && body.trim()) ? body.trim() : `${res.status} ${res.statusText}`;
    throw new Error(msg);
  }

  if (!data) {
    throw new Error(`Expected JSON but got: ${body.slice(0,200)}`);
  }

  return data;
}

async function renderStandings() {
  try {
    setMsg(''); // rensa ev. gammalt success
    // Hämta data robust
    const data = await fetchJsonStrict('/com/example/decathlon/api/standings');

    if (!Array.isArray(data)) {
      console.error('Standings payload is not an array:', data);
      setError('Standings format error (not an array).');
      el('standings').innerHTML = '';
      return;
    }

    // Sortera (ignorera sortBroken här om du vill garantera korrekt sort)
    const sorted = data.slice().sort((a,b)=> (b.total||0)-(a.total||0));

    const rowsHtml = sorted.map(r => `
      <tr>
        <td>${escapeHtml(r.name ?? '')}</td>
        <td>${r.scores?.["100m"] ?? ''}</td>
        <td>${r.scores?.["longJump"] ?? ''}</td>
        <td>${r.scores?.["shotPut"] ?? ''}</td>
        <td>${r.scores?.["highJump"] ?? ''}</td>
        <td>${r.scores?.["400m"] ?? ''}</td>
        <td>${r.scores?.["110mHurdles"] ?? ''}</td>
        <td>${r.scores?.["discusThrow"] ?? ''}</td>
        <td>${r.scores?.["poleVault"] ?? ''}</td>
        <td>${r.scores?.["javelinThrow"] ?? ''}</td>
        <td>${r.scores?.["1500m"] ?? ''}</td>
        <td>${r.total ?? 0}</td>
      </tr>
    `).join('');

    el('standings').innerHTML = rowsHtml || `
      <tr><td colspan="12" style="opacity:.7">No data yet</td></tr>
    `;

    // Visa liten positiv feedback + logga
    setMsg(`Standings updated (${sorted.length} rows)`);
    console.debug('Standings OK:', sorted);
  } catch (e) {
    console.error('renderStandings failed:', e);
    setError(`Could not load standings: ${e.message}`);
    el('standings').innerHTML = '';
  }
}



renderStandings();
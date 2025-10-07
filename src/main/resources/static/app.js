const el = (id) => document.getElementById(id);
const err = el('error');
const msg = el('msg');

const LIMITS = {
  '100m': [5, 20],
  '110mHurdles': [10, 30],
  '400m': [20, 100],
  '1500m': [150, 400],
  'discusThrow': [0, 85],
  'highJump': [0, 300],
  'javelinThrow': [0, 110],
  'longJump': [0, 1000],
  'poleVault': [0, 1000],
  'shotPut': [0, 30]
};

function applyLimits() {
  const ev = el('event').value;
  const [lo, hi] = LIMITS[ev] || [0, 999999];
  const r = el('raw');
  r.min = String(lo);
  r.max = String(hi);
  r.placeholder = `${lo}–${hi}`;
}

function setError(text) { err.textContent = text; }
function setMsg(text) { msg.textContent = text; }

el('event').addEventListener('change', () => {
  applyLimits();
  setError('');
});

el('raw').addEventListener('keydown', (e) => {
  if (['e','E','+','-'].includes(e.key)) e.preventDefault();
});

el('raw').addEventListener('input', () => {
  const v = el('raw').value;
  if (v === '') return;
  if (!/^\d*([.]\d*)?$/.test(v)) {
    el('raw').value = v.replace(/[^0-9.]/g, '');
  }
});

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
  const ev = el('event').value;
  const rawStr = el('raw').value.trim();
  if (rawStr === '') { setError('Enter result'); return; }
  const rawNum = Number(rawStr);
  if (!Number.isFinite(rawNum)) { setError('Wrong number'); return; }
  const [lo, hi] = LIMITS[ev] || [0, 999999];
  if (rawNum < lo || rawNum > hi) { setError('Number outside range'); return; }
  setError('');
  const body = {
    name: el('name2').value,
    event: ev,
    raw: rawNum
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
    setMsg('');
    const data = await fetchJsonStrict('/com/example/decathlon/api/standings');
    if (!Array.isArray(data)) {
      setError('Standings format error (not an array).');
      el('standings').innerHTML = '';
      return;
    }
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
    setMsg(`Standings updated (${sorted.length} rows)`);
  } catch (e) {
    setError(`Could not load standings: ${e.message}`);
    el('standings').innerHTML = '';
  }
}

applyLimits();
renderStandings();

const API_BASE = '/api/blocked-extensions';
const pinnedListEl = document.getElementById('pinned-list');
const customForm = document.getElementById('custom-form');
const customInput = document.getElementById('custom-input');
const customListEl = document.getElementById('custom-list');
const customEmptyEl = document.getElementById('custom-empty');
const toastEl = document.getElementById('toast');

const showToast = (message) => {
    toastEl.textContent = message;
    toastEl.classList.add('show');
    setTimeout(() => toastEl.classList.remove('show'), 2000);
};

const request = async (url, options = {}) => {
    const res = await fetch(url, {
        headers: {
            'Content-Type': 'application/json',
        },
        ...options,
    });
    if (!res.ok) {
        const body = await res.json().catch(() => ({}));
        throw new Error(body.msg || '요청이 실패했습니다.');
    }
    return res.json();
};

const renderPinned = (items) => {
    pinnedListEl.innerHTML = '';
    items.forEach(({ name, valid }) => {
        const label = document.createElement('label');
        label.className = `checkbox-tile ${valid ? 'active' : ''}`;
        const input = document.createElement('input');
        input.type = 'checkbox';
        input.checked = Boolean(valid);
        input.setAttribute('aria-label', `${name} 확장자 차단 토글`);
        input.addEventListener('change', async () => {
            label.classList.toggle('active', input.checked);
            try {
                if (input.checked) {
                    await request(API_BASE, {
                        method: 'POST',
                        body: JSON.stringify({ name }),
                    });
                    showToast(`${name} 차단을 활성화했습니다.`);
                } else {
                    await request(`${API_BASE}/${name}`, { method: 'DELETE' });
                    showToast(`${name} 차단을 비활성화했습니다.`);
                }
            } catch (err) {
                input.checked = !input.checked;
                label.classList.toggle('active', input.checked);
                showToast(err.message);
            } finally {
                await fetchList();
            }
        });
        const text = document.createElement('span');
        text.textContent = name;
        label.append(input, text);
        pinnedListEl.appendChild(label);
    });
};

const renderCustom = (items) => {
    customListEl.innerHTML = '';
    if (!items.length) {
        customEmptyEl.style.display = 'block';
        return;
    }
    customEmptyEl.style.display = 'none';
    items.forEach((name) => {
        const li = document.createElement('li');
        li.className = 'custom-item';
        const title = document.createElement('span');
        title.textContent = name;
        const removeBtn = document.createElement('button');
        removeBtn.type = 'button';
        removeBtn.textContent = '×';
        removeBtn.addEventListener('click', async () => {
            try {
                await request(`${API_BASE}/${name}`, { method: 'DELETE' });
                showToast(`${name}을(를) 삭제했습니다.`);
                await fetchList();
            } catch (err) {
                showToast(err.message);
            }
        });
        li.append(title, removeBtn);
        customListEl.appendChild(li);
    });
};

const fetchList = async () => {
    const { data } = await request(API_BASE);
    renderPinned(data.pinnedExtensions || []);
    renderCustom(data.customExtensions || []);
};

customForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    const name = customInput.value.trim();
    if (!name) {
        showToast('확장자를 입력하세요.');
        return;
    }
    try {
        await request(API_BASE, {
            method: 'POST',
            body: JSON.stringify({ name }),
        });
        customInput.value = '';
        showToast(`${name}을(를) 추가했습니다.`);
        await fetchList();
    } catch (err) {
        showToast(err.message);
    }
});

fetchList().catch((err) => {
    console.error(err);
    showToast('목록을 불러오지 못했습니다.');
});

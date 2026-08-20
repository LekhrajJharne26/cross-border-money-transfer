const API_BASE = '/api';

const state = {
    page: location.hash.slice(1) || 'dashboard',
    user: JSON.parse(localStorage.getItem('cbmt-user') || 'null'),
    beneficiaries: [],
    transactions: [],
    countries: [],
    partners: []
};

const $ = selector => document.querySelector(selector);

const nav = [
    ['dashboard', '▦', 'Dashboard'],
    ['beneficiaries', '♙', 'Beneficiaries'],
    ['countries', '◎', 'Countries'],
    ['partners', '⌘', 'Banking Partners'],
    ['send-money', '↗', 'Send Money'],
    ['transactions', '≡', 'Transaction History'],
    ['profile', '◉', 'Profile']
];

function toast(message, error = false) {
    const el = $('#toast');

    if (!el) {
        alert(message);
        return;
    }

    el.textContent = message;
    el.className = `toast show ${error ? 'error' : ''}`;

    setTimeout(() => {
        el.className = 'toast';
    }, 3500);
}

function money(value, currency = 'USD') {
    const amount = Number(value || 0);

    try {
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency,
            maximumFractionDigits: 2
        }).format(amount);
    } catch {
        return `${currency} ${amount.toFixed(2)}`;
    }
}

function date(value) {
    if (!value) return '-';

    return new Date(value).toLocaleDateString('en-IN', {
        day: '2-digit',
        month: 'short',
        year: 'numeric'
    });
}

/* =========================================================
   API
   ========================================================= */

async function apiRequest(url, options = {}) {
    const token = localStorage.getItem('cbmt-token');

    const headers = {
        ...(options.body ? { 'Content-Type': 'application/json' } : {}),
        ...(options.headers || {})
    };

    if (token) {
        headers.Authorization = `Bearer ${token}`;
    }

    const response = await fetch(`${API_BASE}${url}`, {
        ...options,
        headers
    });

    if (response.status === 401) {
        logout();
        throw new Error('Session expired. Please login again.');
    }

    let result = null;

    const contentType = response.headers.get('content-type') || '';

    if (contentType.includes('application/json')) {
        result = await response.json();
    } else {
        const text = await response.text();
        result = text ? { message: text } : {};
    }

    if (!response.ok || result.success === false) {
        throw new Error(
            result.message ||
            `Request failed with status ${response.status}`
        );
    }

    return result;
}

/* =========================================================
   LOAD DATA
   ========================================================= */

async function loadInitialData() {
    if (!state.user) return;

    try {
        await Promise.all([
            loadBeneficiaries(),
            loadCountries(),
            loadTransactions()
        ]);
    } catch (error) {
        console.error(error);
        toast(error.message || 'Unable to load application data', true);
    }
}

async function loadBeneficiaries() {
    const result = await apiRequest('/v1/beneficiaries');

    state.beneficiaries = result.data || [];
}

async function loadCountries() {
    const result = await apiRequest('/v1/countries');

    state.countries = result.data || [];
}

async function loadTransactions() {
    const result = await apiRequest('/v1/transactions');

    state.transactions = result.data || [];
}

async function loadPartnersByCountry(countryId) {
    const result = await apiRequest(`/v1/partners/country/${countryId}`);

    return result.data || [];
}

/* =========================================================
   LAYOUT
   ========================================================= */

function shell(content, title) {
    const firstName = state.user?.firstName || '';
    const lastName = state.user?.lastName || '';

    const initials =
        `${firstName[0] || ''}${lastName[0] || ''}`.toUpperCase();

    return `
        <div class="shell">

            <aside class="sidebar" id="sidebar">

                <div class="side-brand">
                    Cross<span>Border</span>
                </div>

                <div class="nav-label">MAIN MENU</div>

                <nav class="nav">
                    ${nav.map(n => `
                        <button
                            class="${state.page === n[0] ? 'active' : ''}"
                            onclick="go('${n[0]}')">

                            <i class="nav-icon">${n[1]}</i>
                            ${n[2]}

                        </button>
                    `).join('')}
                </nav>

                <div class="sidebar-bottom">
                    <button onclick="logout()">
                        ⇥ &nbsp; Sign out
                    </button>
                </div>

            </aside>

            <main class="main">

                <header class="topbar">

                    <button
                        class="mobile-menu"
                        onclick="$('#sidebar').classList.toggle('open')">
                        ☰
                    </button>

                    <span class="title">
                        Secure international transfers
                    </span>

                    <div class="profile-chip">

                        <div class="avatar">
                            ${initials}
                        </div>

                        ${firstName} ${lastName}

                    </div>

                </header>

                <section class="content">
                    ${content}
                </section>

            </main>

        </div>
    `;
}

function pageHead(title, text, action = '') {
    return `
        <div class="page-head">

            <div>
                <h1>${title}</h1>
                <p>${text}</p>
            </div>

            ${action}

        </div>
    `;
}

/* =========================================================
   DASHBOARD
   ========================================================= */

function dashboard() {
    const pending = state.transactions.filter(
        t => t.status === 'PENDING'
    ).length;

    const recent = state.transactions.slice(0, 5);

    return shell(`
        ${pageHead(
            'Dashboard',
            'Welcome back. Here is your transfer overview.'
        )}

        <div class="stats">

            <div class="card stat">
                <small>TOTAL BENEFICIARIES</small>
                <strong>${state.beneficiaries.length}</strong>
                <span>Ready to receive transfers</span>
            </div>

            <div class="card stat">
                <small>TOTAL TRANSACTIONS</small>
                <strong>${state.transactions.length}</strong>
                <span>All transfer activity</span>
            </div>

            <div class="card stat">
                <small>PENDING TRANSACTIONS</small>
                <strong>${pending}</strong>
                <span>Awaiting processing</span>
            </div>

        </div>

        <div class="grid-2">

            <div class="card section-card">

                <h3 class="section-title">
                    Recent transactions
                </h3>

                ${table(recent, true)}

            </div>

            <div class="card section-card">

                <h3 class="section-title">
                    Quick actions
                </h3>

                <div class="quick-actions">

                    <button
                        class="quick"
                        onclick="go('beneficiaries')">

                        ＋ Add beneficiary

                        <span>
                            Create a recipient profile
                        </span>

                    </button>

                    <button
                        class="quick"
                        onclick="go('send-money')">

                        ↗ Send money

                        <span>
                            Start a new transfer
                        </span>

                    </button>

                </div>

            </div>

        </div>
    `, 'Dashboard');
}

/* =========================================================
   TRANSACTION TABLE
   ========================================================= */

function table(rows, compact = false) {

    if (!rows.length) {
        return `
            <div class="empty">

                <b>No transactions yet</b>

                Create a transfer to see it here.

            </div>
        `;
    }

    return `
        <div class="table-wrap">

            <table class="data-table">

                <thead>

                    <tr>
                        <th>Transaction</th>
                        <th>Beneficiary</th>
                        <th>Partner</th>
                        <th>Amount</th>
                        <th>Status</th>
                        <th>Created</th>
                    </tr>

                </thead>

                <tbody>

                    ${rows.map(t => {

                        const number =
                            t.transactionNumber ||
                            t.number ||
                            '-';

                        const beneficiary =
                            t.beneficiaryName ||
                            t.beneficiary ||
                            '-';

                        const partner =
                            t.partnerName ||
                            t.partner ||
                            '-';

                        return `
                            <tr>

                                <td>
                                    <b>${number}</b>
                                </td>

                                <td>
                                    ${beneficiary}
                                </td>

                                <td>
                                    ${partner}
                                </td>

                                <td>
                                    ${money(
                                        t.amount,
                                        t.currency || 'USD'
                                    )}
                                </td>

                                <td>
                                    <span class="badge ${t.status}">
                                        ${(t.status || '')
                                            .replace('_', ' ')}
                                    </span>
                                </td>

                                <td>
                                    ${date(
                                        t.createdAt ||
                                        t.createdDate
                                    )}
                                </td>

                            </tr>
                        `;

                    }).join('')}

                </tbody>

            </table>

        </div>
    `;
}

/* =========================================================
   BENEFICIARIES
   ========================================================= */

function beneficiaries() {

    return shell(`
        ${pageHead(
            'Beneficiaries',
            'Manage the people you send money to.',
            '<button class="primary" onclick="beneficiaryForm()">＋ Add beneficiary</button>'
        )}

        <div class="card section-card">

            ${
                state.beneficiaries.length
                    ? `
                        <div class="table-wrap">

                            <table class="data-table">

                                <thead>
                                    <tr>
                                        <th>Name</th>
                                        <th>Country</th>
                                        <th>Mobile</th>
                                        <th>Relationship</th>
                                        <th></th>
                                    </tr>
                                </thead>

                                <tbody>

                                    ${state.beneficiaries.map(b => `

                                        <tr>

                                            <td>

                                                <b>
                                                    ${b.firstName || ''}
                                                    ${b.lastName || ''}
                                                </b>

                                                <br>

                                                <small>
                                                    ${b.email || ''}
                                                </small>

                                            </td>

                                            <td>
                                                ${b.country || '-'}
                                            </td>

                                            <td>
                                                ${b.mobileNumber || b.mobile || '-'}
                                            </td>

                                            <td>
                                                ${b.relationship || '-'}
                                            </td>

                                            <td>

                                                <button
                                                    class="icon-button"
                                                    onclick="beneficiaryForm(${b.id})">
                                                    Edit
                                                </button>

                                                <button
                                                    class="icon-button danger"
                                                    onclick="deleteBeneficiary(${b.id})">
                                                    Delete
                                                </button>

                                            </td>

                                        </tr>

                                    `).join('')}

                                </tbody>

                            </table>

                        </div>
                    `
                    : `
                        <div class="empty">

                            <b>No beneficiaries found</b>

                            Add a beneficiary to start a transfer.

                        </div>
                    `
            }

        </div>
    `, 'Beneficiaries');
}

/* =========================================================
   BENEFICIARY FORM
   ========================================================= */

function beneficiaryForm(id) {

    const b =
        state.beneficiaries.find(x => x.id === id) || {};

    state.page = 'beneficiary-form';

    render(`
        <div class="card form-card">

            ${pageHead(
                id ? 'Edit beneficiary' : 'Add beneficiary',
                'Enter recipient details carefully.'
            )}

            <form
                onsubmit="saveBeneficiary(event, ${id || 'null'})">

                <div class="form-grid">

                    ${[
                        ['firstName', 'First name'],
                        ['lastName', 'Last name'],
                        ['mobileNumber', 'Mobile number'],
                        ['email', 'Email'],
                        ['address', 'Address', 'wide'],
                        ['city', 'City'],
                        ['state', 'State'],
                        ['postalCode', 'Postal code'],
                        ['country', 'Country'],
                        ['governmentIdNumber', 'Government ID number'],
                        ['relationship', 'Relationship']
                    ].map(([key, label, width]) => `

                        <div class="field ${width || ''}">

                            <label>${label}</label>

                            <input
                                required
                                name="${key}"
                                value="${b[key] || ''}"
                                ${key === 'email' ? 'type="email"' : ''}
                            >

                        </div>

                    `).join('')}

                </div>

                <button
                    class="primary"
                    type="submit">

                    ${id ? 'Save changes' : 'Add beneficiary'}

                </button>

                <button
                    class="secondary"
                    type="button"
                    onclick="go('beneficiaries')">

                    Cancel

                </button>

            </form>

        </div>
    `, 'Beneficiaries');
}

async function saveBeneficiary(e, id) {

    e.preventDefault();

    const v = Object.fromEntries(
        new FormData(e.target)
    );

    const payload = {
        firstName: v.firstName,
        lastName: v.lastName,
        mobileNumber: v.mobileNumber,
        email: v.email,
        address: v.address,
        city: v.city,
        state: v.state,
        postalCode: v.postalCode,
        country: v.country,
        governmentIdNumber: v.governmentIdNumber,
        relationship: v.relationship
    };

    try {

        if (id) {

            await apiRequest(
                `/v1/beneficiaries/${id}`,
                {
                    method: 'PUT',
                    body: JSON.stringify(payload)
                }
            );

            toast('Beneficiary updated');

        } else {

            await apiRequest(
                '/v1/beneficiaries',
                {
                    method: 'POST',
                    body: JSON.stringify(payload)
                }
            );

            toast('Beneficiary added');
        }

        await loadBeneficiaries();

        go('beneficiaries');

    } catch (error) {

        console.error(error);

        toast(
            error.message || 'Unable to save beneficiary',
            true
        );
    }
}

async function deleteBeneficiary(id) {

    if (!confirm('Delete this beneficiary?')) {
        return;
    }

    try {

        await apiRequest(
            `/v1/beneficiaries/${id}`,
            {
                method: 'DELETE'
            }
        );

        await loadBeneficiaries();

        toast('Beneficiary deleted');

        render();

    } catch (error) {

        console.error(error);

        toast(
            error.message || 'Unable to delete beneficiary',
            true
        );
    }
}

/* =========================================================
   COUNTRIES
   ========================================================= */

function countries() {

    return shell(`
        ${pageHead(
            'Countries',
            'Available destinations for international transfers.'
        )}

        <div class="card section-card list">

            ${
                state.countries.length
                    ? state.countries.map(c => `

                        <div class="country-row">

                            <div>

                                <b>
                                    ${c.name || c.countryName || '-'}
                                </b>

                                <br>

                                <small class="subtext">
                                    ${
                                        c.code ||
                                        c.countryCode ||
                                        'ACTIVE'
                                    }
                                </small>

                            </div>

                            <span class="country-code">
                                ACTIVE
                            </span>

                        </div>

                    `).join('')
                    : `
                        <div class="empty">
                            No countries available.
                        </div>
                    `
            }

        </div>
    `, 'Countries');
}

/* =========================================================
   BANKING PARTNERS
   ========================================================= */

function partners() {

    return shell(`
        ${pageHead(
            'Banking Partners',
            'Active payout partners by destination country.'
        )}

        <div class="card section-card">

            <div
                class="field"
                style="max-width:360px">

                <label>
                    Select country
                </label>

                <select
                    onchange="renderPartners(this.value)">

                    <option value="">
                        Choose a country
                    </option>

                    ${state.countries.map(c => `
                        <option value="${c.id}">
                            ${c.name || c.countryName}
                        </option>
                    `).join('')}

                </select>

            </div>

            <div
                id="partner-list"
                class="list">

                <div class="empty">
                    Select a country to see partners.
                </div>

            </div>

        </div>
    `, 'Partners');
}

async function renderPartners(countryId) {

    const container = $('#partner-list');

    if (!container) return;

    if (!countryId) {

        container.innerHTML = `
            <div class="empty">
                Select a country to see partners.
            </div>
        `;

        return;
    }

    container.innerHTML = `
        <div class="empty">
            Loading partners...
        </div>
    `;

    try {

        const partners =
            await loadPartnersByCountry(countryId);

        state.partners = partners;

        container.innerHTML = partners.length
            ? partners.map(p => `
                <div class="country-row">

                    <b>
                        ${p.name || p.partnerName || '-'}
                    </b>

                    <span class="country-code">
                        ACTIVE
                    </span>

                </div>
            `).join('')
            : `
                <div class="empty">
                    No active partners found.
                </div>
            `;

    } catch (error) {

        container.innerHTML = `
            <div class="empty">
                Unable to load banking partners.
            </div>
        `;

        toast(
            error.message || 'Unable to load partners',
            true
        );
    }
}

/* =========================================================
   SEND MONEY
   ========================================================= */

function sendMoney() {

    return shell(`
        ${pageHead(
            'Send Money',
            'Create a secure international money transfer.'
        )}

        <div class="card form-card">

            <form onsubmit="sendTransaction(event)">

                <div class="form-grid">

                    <div class="field">

                        <label>
                            Beneficiary
                        </label>

                        <select
                            name="beneficiaryId"
                            required>

                            <option value="">
                                Select beneficiary
                            </option>

                            ${state.beneficiaries.map(b => `

                                <option value="${b.id}">
                                    ${b.firstName || ''}
                                    ${b.lastName || ''}
                                    — ${b.country || ''}
                                </option>

                            `).join('')}

                        </select>

                    </div>

                    <div class="field">

                        <label>
                            Destination country
                        </label>

                        <select
                            name="countryId"
                            required
                            onchange="populatePartners(this.value)">

                            <option value="">
                                Select country
                            </option>

                            ${state.countries.map(c => `

                                <option value="${c.id}">
                                    ${c.name || c.countryName}
                                </option>

                            `).join('')}

                        </select>

                    </div>

                    <div class="field">

                        <label>
                            Banking partner
                        </label>

                        <select
                            name="bankingPartnerId"
                            id="partner-select"
                            required>

                            <option value="">
                                Select country first
                            </option>

                        </select>

                    </div>

                    <div class="field">

                        <label>
                            Amount
                        </label>

                        <input
                            name="amount"
                            type="number"
                            min="0.01"
                            step="0.01"
                            required
                            placeholder="0.00">

                    </div>

                    <div class="field">

                        <label>
                            Currency
                        </label>

                        <select name="currency">

                            <option>USD</option>
                            <option>INR</option>
                            <option>NPR</option>
                            <option>PHP</option>

                        </select>

                    </div>

                    <div class="field">

                        <label>
                            Purpose
                        </label>

                        <input
                            name="purpose"
                            required
                            placeholder="e.g. Family support">

                    </div>

                    <div class="field wide">

                        <label>
                            Remarks
                            <small>(optional)</small>
                        </label>

                        <textarea
                            name="remarks"
                            placeholder="Add a note for this transfer">
                        </textarea>

                    </div>

                </div>

                <button
                    class="primary"
                    type="submit">

                    Continue transfer

                </button>

            </form>

        </div>
    `, 'Send Money');
}

async function populatePartners(countryId) {

    const select = $('#partner-select');

    if (!select) return;

    select.innerHTML = `
        <option value="">
            Loading partners...
        </option>
    `;

    if (!countryId) {

        select.innerHTML = `
            <option value="">
                Select country first
            </option>
        `;

        return;
    }

    try {

        const partners =
            await loadPartnersByCountry(countryId);

        state.partners = partners;

        select.innerHTML = `
            <option value="">
                Select partner
            </option>

            ${partners.map(p => `
                <option value="${p.id}">
                    ${p.name || p.partnerName}
                </option>
            `).join('')}
        `;

    } catch (error) {

        select.innerHTML = `
            <option value="">
                Unable to load partners
            </option>
        `;

        toast(
            error.message || 'Unable to load partners',
            true
        );
    }
}

async function sendTransaction(e) {

    e.preventDefault();

    const v = Object.fromEntries(
        new FormData(e.target)
    );

    const payload = {
        beneficiaryId: Number(v.beneficiaryId),
        bankingPartnerId: Number(v.bankingPartnerId),
        amount: Number(v.amount),
        currency: v.currency,
        purpose: v.purpose,
        remarks: v.remarks
    };

    try {

        await apiRequest(
            '/v1/transactions',
            {
                method: 'POST',
                body: JSON.stringify(payload)
            }
        );

        await loadTransactions();

        toast('Transfer created successfully');

        go('transactions');

    } catch (error) {

        console.error(error);

        toast(
            error.message || 'Unable to create transaction',
            true
        );
    }
}

/* =========================================================
   TRANSACTIONS
   ========================================================= */

function transactions() {

    return shell(`
        ${pageHead(
            'Transaction History',
            'Review all money transfers sent from your account.'
        )}

        <div class="card section-card">

            <div class="toolbar">

                <input
                    class="search"
                    placeholder="Search transaction or beneficiary"
                    oninput="filterTransactions(this.value)">

                <select
                    onchange="filterTransactions('', this.value)">

                    <option value="">
                        All statuses
                    </option>

                    <option>PENDING</option>
                    <option>IN_PROGRESS</option>
                    <option>SUCCESS</option>
                    <option>FAILED</option>
                    <option>CANCELLED</option>

                </select>

            </div>

            <div id="transaction-table">
                ${table(state.transactions)}
            </div>

            <div class="pagination">

                <button>‹</button>
                <button>1</button>
                <button>›</button>

            </div>

        </div>
    `, 'Transaction History');
}

function filterTransactions(q = '', status = '') {

    const rows = state.transactions.filter(t => {

        const number =
            t.transactionNumber ||
            t.number ||
            '';

        const beneficiary =
            t.beneficiaryName ||
            t.beneficiary ||
            '';

        const partner =
            t.partnerName ||
            t.partner ||
            '';

        const searchable =
            `${number}${beneficiary}${partner}`
                .toLowerCase();

        return (
            (!q || searchable.includes(q.toLowerCase())) &&
            (!status || t.status === status)
        );
    });

    const container = $('#transaction-table');

    if (container) {
        container.innerHTML = table(rows);
    }
}

/* =========================================================
   PROFILE
   ========================================================= */

function profile() {

    return shell(`
        ${pageHead(
            'Profile',
            'Your account and security details.'
        )}

        <div class="card form-card">

            <div class="field">

                <label>
                    Full name
                </label>

                <input
                    value="${state.user?.firstName || ''}
                    ${state.user?.lastName || ''}"
                    disabled>

            </div>

            <div class="field">

                <label>
                    Email
                </label>

                <input
                    value="${state.user?.email || ''}"
                    disabled>

            </div>

            <div class="field">

                <label>
                    Role
                </label>

                <input
                    value="${state.user?.role || 'USER'}"
                    disabled>

            </div>

        </div>
    `, 'Profile');
}

/* =========================================================
   LOGIN / REGISTER
   ========================================================= */

function login(register = false) {

    return `
        <div class="auth-shell">

            <section class="auth-hero">

                <div class="brand">
                    Cross<span>Border</span>
                </div>

                <h1>
                    Move money across borders with confidence.
                </h1>

                <p>
                    A simple, secure workspace for managing your
                    beneficiaries and international transfers.
                </p>

                <ul class="auth-points">
                    <li>Bank-grade security</li>
                    <li>Track every transfer</li>
                    <li>Global banking partners</li>
                </ul>

            </section>

            <section class="auth-panel">

                <div class="auth-card">

                    <div
                        class="brand"
                        style="color:var(--blue)">

                        Cross<span>Border</span>

                    </div>

                    <h2>
                        ${register
                            ? 'Create your account'
                            : 'Welcome back'}
                    </h2>

                    <p class="subtext">

                        ${register
                            ? 'Start sending money across borders today.'
                            : 'Sign in to securely manage your transfers.'}

                    </p>

                    <form
                        onsubmit="${
                            register
                                ? 'register(event)'
                                : 'signIn(event)'
                        }">

                        ${
                            register
                                ? `
                                    <div class="form-grid">

                                        <div class="field">

                                            <label>
                                                First name
                                            </label>

                                            <input
                                                required
                                                name="firstName">

                                        </div>

                                        <div class="field">

                                            <label>
                                                Last name
                                            </label>

                                            <input
                                                required
                                                name="lastName">

                                        </div>

                                    </div>
                                `
                                : ''
                        }

                        <div class="field">

                            <label>
                                Email address
                            </label>

                            <input
                                required
                                type="email"
                                name="email"
                                placeholder="you@example.com">

                        </div>

                        <div class="field">

                            <label>
                                Password
                            </label>

                            <input
                                required
                                type="password"
                                minlength="8"
                                name="password"
                                placeholder="••••••••">

                        </div>

                        <button
                            class="primary full"
                            type="submit">

                            ${
                                register
                                    ? 'Create account'
                                    : 'Sign in'
                            }

                        </button>

                    </form>

                    <p class="auth-switch">

                        ${
                            register
                                ? 'Already have an account?'
                                : 'New to CrossBorder?'
                        }

                        <a
                            class="text-link"
                            href="#${
                                register
                                    ? 'login'
                                    : 'register'
                            }">

                            ${
                                register
                                    ? 'Sign in'
                                    : 'Create an account'
                            }

                        </a>

                    </p>

                </div>

            </section>

        </div>
    `;
}

async function signIn(e) {

    e.preventDefault();

    const d = Object.fromEntries(
        new FormData(e.target)
    );

    try {

        const result = await apiRequest(
            '/v1/auth/login',
            {
                method: 'POST',
                body: JSON.stringify({
                    email: d.email,
                    password: d.password
                })
            }
        );

        const data = result.data;

        localStorage.setItem(
            'cbmt-token',
            data.accessToken
        );

        state.user = {
            firstName: data.firstName || '',
            lastName: data.lastName || '',
            email: data.email,
            role: data.role,
            userId: data.userId
        };

        localStorage.setItem(
            'cbmt-user',
            JSON.stringify(state.user)
        );

        toast('Signed in successfully');

        await loadInitialData();

        go('dashboard');

    } catch (error) {

        console.error(error);

        toast(
            error.message || 'Login failed',
            true
        );
    }
}

async function register(e) {

    e.preventDefault();

    const d = Object.fromEntries(
        new FormData(e.target)
    );

    try {

        const result = await apiRequest(
            '/v1/auth/register',
            {
                method: 'POST',
                body: JSON.stringify({
                    firstName: d.firstName,
                    lastName: d.lastName,
                    email: d.email,
                    password: d.password
                })
            }
        );

        const data = result.data || {};

        if (data.accessToken) {

            localStorage.setItem(
                'cbmt-token',
                data.accessToken
            );

            state.user = {
                firstName: data.firstName || d.firstName,
                lastName: data.lastName || d.lastName,
                email: data.email || d.email,
                role: data.role || 'USER',
                userId: data.userId
            };

            localStorage.setItem(
                'cbmt-user',
                JSON.stringify(state.user)
            );

            await loadInitialData();

            toast('Account created successfully');

            go('dashboard');

        } else {

            toast(
                'Account created. Please login.'
            );

            go('login');
        }

    } catch (error) {

        console.error(error);

        toast(
            error.message || 'Registration failed',
            true
        );
    }
}

/* =========================================================
   LOGOUT
   ========================================================= */

function logout() {

    localStorage.removeItem('cbmt-token');
    localStorage.removeItem('cbmt-user');

    state.user = null;
    state.beneficiaries = [];
    state.transactions = [];
    state.countries = [];

    state.page = 'login';

    location.hash = 'login';

    render();
}

/* =========================================================
   NAVIGATION
   ========================================================= */

function go(page) {

    state.page = page;

    location.hash = page;

    render();
}

async function render(custom) {

    const app = $('#app');

    if (!app) return;

    if (
        !state.user &&
        !['login', 'register'].includes(state.page)
    ) {

        state.page = 'login';

        location.hash = 'login';
    }

    if (custom) {

        app.innerHTML = shell(
            custom,
            state.page
        );

        return;
    }

    const pages = {
        dashboard,
        beneficiaries,
        countries,
        partners,
        'send-money': sendMoney,
        transactions,
        profile,
        login: () => login(),
        register: () => login(true)
    };

    app.innerHTML =
        (pages[state.page] || pages.dashboard)();

    if (
        state.user &&
        ['countries', 'partners', 'dashboard',
         'beneficiaries', 'send-money',
         'transactions', 'profile'].includes(state.page)
    ) {
        // Data is loaded on login.
    }
}

/* =========================================================
   HASH CHANGE
   ========================================================= */

window.addEventListener(
    'hashchange',
    async () => {

        state.page =
            location.hash.slice(1) ||
            'dashboard';

        if (state.user) {

            try {

                if (state.page === 'beneficiaries') {
                    await loadBeneficiaries();
                }

                if (
                    state.page === 'countries' ||
                    state.page === 'send-money' ||
                    state.page === 'partners'
                ) {
                    await loadCountries();
                }

                if (
                    state.page === 'transactions' ||
                    state.page === 'dashboard'
                ) {
                    await loadTransactions();
                }

            } catch (error) {

                console.error(error);
                toast(
                    error.message || 'Unable to load data',
                    true
                );
            }
        }

        render();
    }
);

/* =========================================================
   GLOBAL FUNCTIONS FOR HTML onclick/onchange
   ========================================================= */

window.go = go;
window.logout = logout;
window.beneficiaryForm = beneficiaryForm;
window.saveBeneficiary = saveBeneficiary;
window.deleteBeneficiary = deleteBeneficiary;
window.renderPartners = renderPartners;
window.populatePartners = populatePartners;
window.sendTransaction = sendTransaction;
window.filterTransactions = filterTransactions;
window.signIn = signIn;
window.register = register;

/* =========================================================
   START APPLICATION
   ========================================================= */

(async function init() {

    if (state.user) {

        try {
            await loadInitialData();
        } catch (error) {
            console.error(error);
        }
    }

    render();

})();
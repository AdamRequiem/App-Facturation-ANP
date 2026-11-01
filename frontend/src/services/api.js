const API_BASE_URL = '/api';

export const api = {
  // Navires (Database API)
  getNavires: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/navires`);
      if (!response.ok) return [];
      return await response.json();
    } catch (e) {
      console.error('Error fetching navires from DB:', e);
      return [];
    }
  },
  createNavire: async (navire) => {
    const response = await fetch(`${API_BASE_URL}/navires`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(navire)
    });
    if (!response.ok) throw new Error('Erreur lors de la création du navire dans la BDD');
    return await response.json();
  },
  updateNavire: async (id, navire) => {
    const response = await fetch(`${API_BASE_URL}/navires/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(navire)
    });
    if (!response.ok) throw new Error('Erreur lors de la modification du navire');
    return await response.json();
  },
  deleteNavire: async (id) => {
    await fetch(`${API_BASE_URL}/navires/${id}`, { method: 'DELETE' });
  },

  // Marchandises (Database API)
  getMarchandises: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/marchandises`);
      if (!response.ok) return [];
      return await response.json();
    } catch (e) {
      console.error('Error fetching marchandises from DB:', e);
      return [];
    }
  },

  // Ports (Database API)
  getPorts: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/ports`);
      if (!response.ok) return [];
      return await response.json();
    } catch (e) {
      console.error('Error fetching ports from DB:', e);
      return [];
    }
  },
  createPort: async (port) => {
    const response = await fetch(`${API_BASE_URL}/ports`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(port)
    });
    if (!response.ok) throw new Error('Erreur lors de la création du port dans la BDD');
    return await response.json();
  },
  updatePort: async (id, port) => {
    const response = await fetch(`${API_BASE_URL}/ports/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(port)
    });
    if (!response.ok) throw new Error('Erreur lors de la modification du port');
    return await response.json();
  },
  deletePort: async (id) => {
    await fetch(`${API_BASE_URL}/ports/${id}`, { method: 'DELETE' });
  },

  // Factures (Database API)
  getFactures: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/entete-factures`);
      if (!response.ok) return [];
      return await response.json();
    } catch (e) {
      console.error('Error fetching factures from DB:', e);
      return [];
    }
  },
  createFacture: async (factureData) => {
    const response = await fetch(`${API_BASE_URL}/entete-factures`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(factureData)
    });
    if (!response.ok) throw new Error('Erreur lors de la création de la facture dans la BDD');
    return await response.json();
  },
  deleteFacture: async (id) => {
    await fetch(`${API_BASE_URL}/entete-factures/${id}`, { method: 'DELETE' });
  },

  // Tarifs (Database API)
  getTarifs: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/tarifs`);
      if (!response.ok) return [];
      return await response.json();
    } catch (e) {
      console.error('Error fetching tarifs from DB:', e);
      return [];
    }
  },
  createTarif: async (tarif) => {
    const response = await fetch(`${API_BASE_URL}/tarifs`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(tarif)
    });
    if (!response.ok) throw new Error('Erreur lors de la création du tarif');
    return await response.json();
  },
  deleteTarif: async (id) => {
    await fetch(`${API_BASE_URL}/tarifs/${id}`, { method: 'DELETE' });
  },

  // TarifTranches (Database API)
  getTarifTranches: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/tarif-tranches`);
      if (!response.ok) return [];
      return await response.json();
    } catch (e) {
      console.error('Error fetching tarif tranches from DB:', e);
      return [];
    }
  },
  createTarifTranche: async (tranche) => {
    const response = await fetch(`${API_BASE_URL}/tarif-tranches`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(tranche)
    });
    if (!response.ok) throw new Error('Erreur lors de la création de la tranche tarifaire');
    return await response.json();
  },
  updateTarifTranche: async (id, tranche) => {
    const response = await fetch(`${API_BASE_URL}/tarif-tranches/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(tranche)
    });
    if (!response.ok) throw new Error('Erreur lors de la modification de la tranche');
    return await response.json();
  },
  deleteTarifTranche: async (id) => {
    await fetch(`${API_BASE_URL}/tarif-tranches/${id}`, { method: 'DELETE' });
  },

  // Escales (Database API) — créer l'escale est la 1ère étape du vrai flux de facturation
  // Escales (Database API)
  getEscales: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/escales`);
      if (!response.ok) return [];
      return await response.json();
    } catch (e) {
      console.error('Error fetching escales from DB:', e);
      return [];
    }
  },
  createEscale: async (escale) => {
    const response = await fetch(`${API_BASE_URL}/escales`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(escale)
    });
    if (!response.ok) throw new Error("Erreur lors de la création de l'escale");
    return await response.json();
  },
  updateEscale: async (id, escale) => {
    const response = await fetch(`${API_BASE_URL}/escales/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(escale)
    });
    if (!response.ok) throw new Error("Erreur lors de la modification de l'escale");
    return await response.json();
  },
  deleteEscale: async (id) => {
    await fetch(`${API_BASE_URL}/escales/${id}`, { method: 'DELETE' });
  },

  // Déclenche le vrai moteur de calcul côté serveur (FactureOrchestrationService) pour une escale existante
  genererFacture: async (idEscale) => {
    const response = await fetch(`${API_BASE_URL}/escales/${idEscale}/facture`, { method: 'POST' });
    if (!response.ok) {
      const message = await response.text().catch(() => '');
      throw new Error(message || 'Erreur lors de la génération de la facture par le moteur de calcul');
    }
    return await response.json();
  },

  // Passe une facture BROUILLON au statut VALIDEE — c'est ce qui la fait apparaître
  // dans l'historique et le tableau de bord
  validerFacture: async (idEnteteFacture) => {
    const response = await fetch(`${API_BASE_URL}/entete-factures/${idEnteteFacture}/valider`, { method: 'PUT' });
    if (!response.ok) {
      const message = await response.text().catch(() => '');
      throw new Error(message || 'Erreur lors de la validation de la facture');
    }
    return await response.json();
  },

  // Entrees (Art 5)
  createEntree: async (entree) => {
    const response = await fetch(`${API_BASE_URL}/entrees`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(entree)
    });
    if (!response.ok) throw new Error("Erreur lors de la création de l'entrée");
    return await response.json();
  },

  // Mouillages (Art 4)
  createMouillage: async (mouillage) => {
    const response = await fetch(`${API_BASE_URL}/mouillages`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(mouillage)
    });
    if (!response.ok) throw new Error('Erreur lors de la création du mouillage');
    return await response.json();
  },

  // Stationnements (Art 2/3)
  createStationnement: async (stationnement) => {
    const response = await fetch(`${API_BASE_URL}/stationnements`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(stationnement)
    });
    if (!response.ok) throw new Error('Erreur lors de la création du stationnement');
    return await response.json();
  }
};

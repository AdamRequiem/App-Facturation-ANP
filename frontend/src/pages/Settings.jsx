import React, { useState } from 'react';
import { Settings, Plus, Edit, Trash2, X, Check, Layers, Tag } from 'lucide-react';
import { api } from '../services/api';

const TYPE_OPERATION_LABELS = {
  STATIONNEMENT: 'Stationnement (Art 2.1)',
  MOUILLAGE: 'Mouillage (Art 4)',
  ENTREE_PORT: 'Entrée au port (Art 5)',
  STATIONNEMENT_EMBARQUEMENT_PETROLIER: 'Mohammédia — Stationnement embarquement',
  ENTREE_PETROLIER: 'Mohammédia — Entrée débarquement',
  SORTIE_PETROLIER: 'Mohammédia — Sortie débarquement',
  SEJOUR_PETROLIER: 'Mohammédia — Séjour > 72h',
  ENTREE_CHARBON_JORF: 'Jorf-Lasfar — Entrée',
  SORTIE_CHARBON_JORF: 'Jorf-Lasfar — Sortie',
  SEJOUR_CHARBON_JORF: 'Jorf-Lasfar — Séjour journalier',
  ENTREE_CHARBON_SAFI: 'Safi Atlantique — Entrée',
  SORTIE_CHARBON_SAFI: 'Safi Atlantique — Sortie',
  SEJOUR_CHARBON_SAFI: 'Safi Atlantique — Séjour journalier'
};

const TYPE_NAVIRE_LABELS = {
  TOUS: 'Tous navires (pas de distinction)',
  TRANSBORDEUR: 'Transbordeurs uniquement',
  AUTRE: 'Autres navires uniquement'
};

const UNITE_LABELS = {
  TJB_JOUR: 'Euro / TJB / jour',
  VG: 'Euro / m³ (volume géométrique)'
};

export function SettingsPage({ tarifTranches, tarifs, ports, onRefresh }) {
  const [activeSubTab, setActiveSubTab] = useState('tranches'); // 'tranches' | 'tarifs'

  // Modal State for TarifTranches
  const [isTrancheModalOpen, setIsTrancheModalOpen] = useState(false);
  const [editingTranche, setEditingTranche] = useState(null);
  const [valeurMin, setValeurMin] = useState('');
  const [valeurMax, setValeurMax] = useState('');
  const [montant, setMontant] = useState('');
  const [selectedTarifId, setSelectedTarifId] = useState('');

  // Modal State for Tarifs
  const [isTarifModalOpen, setIsTarifModalOpen] = useState(false);
  const [description, setDescription] = useState('');
  const [typeOperation, setTypeOperation] = useState('STATIONNEMENT');
  const [typeNavire, setTypeNavire] = useState('TOUS');
  const [unite, setUnite] = useState('TJB_JOUR');
  const [selectedPortId, setSelectedPortId] = useState('');

  const [saving, setSaving] = useState(false);

  // Open Tranche Modal
  const openTrancheAddModal = () => {
    setEditingTranche(null);
    setValeurMin('');
    setValeurMax('');
    setMontant('');
    setSelectedTarifId(tarifs[0]?.idTarif || '');
    setIsTrancheModalOpen(true);
  };

  const openTrancheEditModal = (tranche) => {
    setEditingTranche(tranche);
    setValeurMin(tranche.valeurMin ?? '');
    setValeurMax(tranche.valeurMax ?? '');
    setMontant(tranche.montant ?? '');
    setSelectedTarifId(tranche.idTarif || tarifs[0]?.idTarif || '');
    setIsTrancheModalOpen(true);
  };

  const handleTrancheSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);

    const payload = {
      valeurMin: parseFloat(valeurMin),
      valeurMax: parseFloat(valeurMax),
      montant: parseFloat(montant),
      idTarif: selectedTarifId ? parseInt(selectedTarifId) : null
    };

    if (editingTranche) {
      await api.updateTarifTranche(editingTranche.idTarifTranche, payload);
    } else {
      await api.createTarifTranche(payload);
    }

    setSaving(false);
    setIsTrancheModalOpen(false);
    onRefresh();
  };

  const handleTrancheDelete = async (id) => {
    if (window.confirm('Voulez-vous supprimer cette tranche tarifaire ?')) {
      await api.deleteTarifTranche(id);
      onRefresh();
    }
  };

  // Open Tarif Modal
  const openTarifAddModal = () => {
    setDescription('');
    setTypeOperation('STATIONNEMENT');
    setTypeNavire('TOUS');
    setUnite('TJB_JOUR');
    setSelectedPortId('');
    setIsTarifModalOpen(true);
  };

  const handleTarifSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);

    const payload = {
      description,
      typeOperation,
      typeNavire,
      unite,
      idPort: selectedPortId ? parseInt(selectedPortId) : null
    };

    await api.createTarif(payload);
    setSaving(false);
    setIsTarifModalOpen(false);
    onRefresh();
  };

  const handleTarifDelete = async (id) => {
    if (window.confirm('Voulez-vous supprimer ce tarif ?')) {
      await api.deleteTarif(id);
      onRefresh();
    }
  };

  return (
    <div className="settings-page">
      {/* Top Header Bar */}
      <div className="page-header card">
        <div className="page-title">
          <div className="title-icon bg-navy">
            <Settings size={26} color="#FFFFFF" />
          </div>
          <div>
            <h2>Paramètres & Grilles Tarifaires ANP</h2>
            <p>Configuration des Tranches Tarifaires (TarifTranches) et Barèmes des Droits Portuaires</p>
          </div>
        </div>
      </div>

      {/* Sub Tabs Navigation */}
      <div className="settings-tabs card">
        <button
          className={`tab-btn ${activeSubTab === 'tranches' ? 'active' : ''}`}
          onClick={() => setActiveSubTab('tranches')}
        >
          <Layers size={16} /> Tranches Tarifaires (TarifTranche) [{tarifTranches.length}]
        </button>
        <button
          className={`tab-btn ${activeSubTab === 'tarifs' ? 'active' : ''}`}
          onClick={() => setActiveSubTab('tarifs')}
        >
          <Tag size={16} /> Tarifs de Base [{tarifs.length}]
        </button>
      </div>

      {/* TAB 1: TRANCHES TARIFAIRES */}
      {activeSubTab === 'tranches' && (
        <div className="tab-content">
          <div className="section-header-bar">
            <div>
              <h3>Gestion des Tranches Tarifaires (`TarifTranche`)</h3>
              <p>Tranches de tonnage ou de durée associées aux calculs des droits de port</p>
            </div>
            <button className="btn btn-gold" onClick={openTrancheAddModal}>
              <Plus size={16} /> Nouvelle Tranche Tarifaire
            </button>
          </div>

          <div className="table-responsive card">
            <table className="table">
              <thead>
                <tr>
                  <th>ID Tranche</th>
                  <th>Valeur Min (TJB / Durée)</th>
                  <th>Valeur Max (TJB / Durée)</th>
                  <th>Montant Tarifaire (€)</th>
                  <th>Tarif de Base Associé</th>
                  <th style={{ textAlign: 'right' }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {tarifTranches.length > 0 ? (
                  tarifTranches.map((t) => (
                    <tr key={t.idTarifTranche}>
                      <td><strong>#{t.idTarifTranche}</strong></td>
                      <td>{t.valeurMin?.toLocaleString('fr-FR')}</td>
                      <td>{t.valeurMax?.toLocaleString('fr-FR')}</td>
                      <td>
                        <strong style={{ color: 'var(--anp-navy)', fontSize: '1rem' }}>
                          {t.montant?.toLocaleString('fr-FR', { minimumFractionDigits: 2 })} €
                        </strong>
                      </td>
                      <td>
                        <span className="badge badge-ocean">
                          {t.descriptionTarif || `Tarif #${t.idTarif || 1}`}
                        </span>
                      </td>
                      <td style={{ textAlign: 'right' }}>
                        <div className="action-btns">
                          <button className="btn btn-outline btn-sm" onClick={() => openTrancheEditModal(t)}>
                            <Edit size={14} /> Modifier
                          </button>
                          <button className="btn btn-danger btn-sm" onClick={() => handleTrancheDelete(t.idTarifTranche)}>
                            <Trash2 size={14} />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="6" style={{ textAlign: 'center', padding: '3rem 1rem', color: '#64748B' }}>
                      Aucune tranche tarifaire enregistrée.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* TAB 2: TARIFS DE BASE */}
      {activeSubTab === 'tarifs' && (
        <div className="tab-content">
          <div className="section-header-bar">
            <div>
              <h3>Gestion des Tarifs de Base (`Tarif`)</h3>
              <p>Définition des opérations portuaires assujetties et des types de navires</p>
            </div>
            <button className="btn btn-primary" onClick={openTarifAddModal}>
              <Plus size={16} /> Nouveau Tarif de Base
            </button>
          </div>

          <div className="table-responsive card">
            <table className="table">
              <thead>
                <tr>
                  <th>ID Tarif</th>
                  <th>Désignation / Description</th>
                  <th>Type d'Opération</th>
                  <th>Type de Navire</th>
                  <th>Unité de Facturation</th>
                  <th>Port Associé</th>
                  <th style={{ textAlign: 'right' }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {tarifs.length > 0 ? (
                  tarifs.map((tr) => (
                    <tr key={tr.idTarif}>
                      <td><strong>#{tr.idTarif}</strong></td>
                      <td><strong>{tr.description}</strong></td>
                      <td>
                        <span className="badge badge-navy">{TYPE_OPERATION_LABELS[tr.typeOperation] || tr.typeOperation}</span>
                      </td>
                      <td>
                        <span className="badge badge-gold">{TYPE_NAVIRE_LABELS[tr.typeNavire] || tr.typeNavire}</span>
                      </td>
                      <td>{UNITE_LABELS[tr.unite] || tr.unite}</td>
                      <td>{tr.nomPort || `Port #${tr.idPort || 1}`}</td>
                      <td style={{ textAlign: 'right' }}>
                        <button className="btn btn-danger btn-sm" onClick={() => handleTarifDelete(tr.idTarif)}>
                          <Trash2 size={14} /> Supprimer
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="7" style={{ textAlign: 'center', padding: '3rem 1rem', color: '#64748B' }}>
                      Aucun tarif de base enregistré.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Modal Add/Edit TarifTranche */}
      {isTrancheModalOpen && (
        <div className="modal-overlay" onClick={() => setIsTrancheModalOpen(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>
                <Layers size={18} color="#C5A028" /> {editingTranche ? 'Modifier la Tranche Tarifaire' : 'Ajouter une Tranche Tarifaire'}
              </h3>
              <button className="modal-close" onClick={() => setIsTrancheModalOpen(false)}>
                <X size={18} />
              </button>
            </div>

            <form onSubmit={handleTrancheSubmit}>
              <div className="modal-body">
                <div className="form-row">
                  <div className="form-group">
                    <label>Valeur Minimum (Min) *</label>
                    <input
                      type="number"
                      step="0.01"
                      className="form-control"
                      placeholder="Ex: 0"
                      value={valeurMin}
                      onChange={(e) => setValeurMin(e.target.value)}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Valeur Maximum (Max) *</label>
                    <input
                      type="number"
                      step="0.01"
                      className="form-control"
                      placeholder="Ex: 5000"
                      value={valeurMax}
                      onChange={(e) => setValeurMax(e.target.value)}
                      required
                    />
                  </div>
                </div>

                <div className="form-row">
                  <div className="form-group">
                    <label>Montant / Taux Fixé (€) *</label>
                    <input
                      type="number"
                      step="0.01"
                      className="form-control"
                      placeholder="Ex: 28.50"
                      value={montant}
                      onChange={(e) => setMontant(e.target.value)}
                      required
                    />
                  </div>

                  <div className="form-group">
                    <label>Tarif de Base Associé *</label>
                    <select
                      className="form-control"
                      value={selectedTarifId}
                      onChange={(e) => setSelectedTarifId(e.target.value)}
                      required
                    >
                      {tarifs.map((t) => (
                        <option key={t.idTarif} value={t.idTarif}>
                          #{t.idTarif} - {t.description}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
              </div>

              <div className="modal-footer">
                <button type="button" className="btn btn-outline" onClick={() => setIsTrancheModalOpen(false)}>
                  Annuler
                </button>
                <button type="submit" className="btn btn-gold" disabled={saving}>
                  <Check size={16} /> {saving ? 'Enregistrement...' : 'Enregistrer la Tranche'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Modal Add Tarif */}
      {isTarifModalOpen && (
        <div className="modal-overlay" onClick={() => setIsTarifModalOpen(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>
                <Tag size={18} color="#C5A028" /> Nouveau Tarif de Base
              </h3>
              <button className="modal-close" onClick={() => setIsTarifModalOpen(false)}>
                <X size={18} />
              </button>
            </div>

            <form onSubmit={handleTarifSubmit}>
              <div className="modal-body">
                <div className="form-group">
                  <label>Description / Libellé du Tarif *</label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Ex: Tarif Stationnement Navires de Commerce"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    required
                  />
                </div>

                <div className="form-row">
                  <div className="form-group">
                    <label>Type d'Opération *</label>
                    <select className="form-control" value={typeOperation} onChange={(e) => setTypeOperation(e.target.value)} required>
                      {Object.entries(TYPE_OPERATION_LABELS).map(([value, label]) => (
                        <option key={value} value={value}>{label}</option>
                      ))}
                    </select>
                  </div>

                  <div className="form-group">
                    <label>Type de Navire Cible *</label>
                    <select className="form-control" value={typeNavire} onChange={(e) => setTypeNavire(e.target.value)} required>
                      {Object.entries(TYPE_NAVIRE_LABELS).map(([value, label]) => (
                        <option key={value} value={value}>{label}</option>
                      ))}
                    </select>
                  </div>
                </div>

                <div className="form-row">
                  <div className="form-group">
                    <label>Unité de Facturation *</label>
                    <select className="form-control" value={unite} onChange={(e) => setUnite(e.target.value)} required>
                      {Object.entries(UNITE_LABELS).map(([value, label]) => (
                        <option key={value} value={value}>{label}</option>
                      ))}
                    </select>
                  </div>

                  <div className="form-group">
                    <label>Port d'Application</label>
                    <select className="form-control" value={selectedPortId} onChange={(e) => setSelectedPortId(e.target.value)}>
                      <option value="">-- Tarif par défaut ("Autres Ports", sans port spécifique) --</option>
                      {ports.map((p) => (
                        <option key={p.idPort} value={p.idPort}>
                          {p.nom}
                        </option>
                      ))}
                    </select>
                    <p className="field-hint">
                      Laisse vide pour un tarif générique (ex. "Autres Ports" Art 2.1.a / Art 5) : le moteur
                      applique un port spécifique en priorité, sinon retombe sur le tarif sans port.
                    </p>
                  </div>
                </div>
              </div>

              <div className="modal-footer">
                <button type="button" className="btn btn-outline" onClick={() => setIsTarifModalOpen(false)}>
                  Annuler
                </button>
                <button type="submit" className="btn btn-primary" disabled={saving}>
                  <Check size={16} /> {saving ? 'Création...' : 'Créer le Tarif'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <style>{`
        .field-hint {
          font-size: 0.75rem;
          color: var(--anp-text-muted);
          margin-top: 0.35rem;
          line-height: 1.4;
        }

        .settings-page {
          display: flex;
          flex-direction: column;
          gap: 1.25rem;
        }

        .settings-tabs {
          display: flex;
          gap: 0.5rem;
          padding: 0.5rem;
          background: #FFFFFF;
        }

        .tab-btn {
          display: flex;
          align-items: center;
          gap: 0.4rem;
          background: transparent;
          border: none;
          padding: 0.6rem 1rem;
          font-size: 0.875rem;
          font-weight: 600;
          color: var(--anp-text-muted);
          border-radius: 4px;
          cursor: pointer;
          transition: background-color 0.15s ease, color 0.15s ease;
          font-family: inherit;
        }

        .tab-btn:hover {
          background: #F1F5F9;
          color: var(--anp-navy);
        }

        .tab-btn.active {
          background: var(--anp-navy);
          color: #FFFFFF;
        }

        .section-header-bar {
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-bottom: 1rem;
        }

        .section-header-bar h3 {
          font-size: 1.15rem;
          color: var(--anp-navy);
          font-weight: 700;
        }

        .section-header-bar p {
          font-size: 0.825rem;
          color: var(--anp-text-muted);
        }
      `}</style>
    </div>
  );
}

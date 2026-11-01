import React, { useState, useMemo } from 'react';
import { Navigation, Plus, X, AlertTriangle, Info, CheckCircle2 } from 'lucide-react';
import { api } from '../services/api';

const RAISON_ENTREE_LABELS = {
  '': 'Entrée normale (facturée)',
  DEBARQUEMENT_CLANDESTINS_MALADES_CADAVRES: 'Débarquement clandestins / malades / cadavres',
  ALLEGEMENT_OU_PRELEVEMENT_ECHANTILLON: 'Allègement ou prélèvement d\u2019échantillon',
  DEPART_CONDITIONS_CLIMATIQUES_OU_CONTRAINTE_PORTUAIRE: 'Départ pour conditions climatiques / contrainte portuaire',
  RELACHE: 'Relâche',
  OPERATIONS_IMPORT_EXPORT_PARTIELLES: 'Opérations import/export partielles (refacturée, Art 5 dernier §)'
};

const TYPE_RISTOURNE_LABELS = {
  '': 'Aucune',
  A_CABOTEURS: 'A — Caboteurs (Art 6.1)',
  B_NOMBRE_TOUCHEES: 'B — Nombre de touchées (Art 6.2)',
  C_LIGNES_REGULIERES: 'C — Lignes régulières (Art 6.3)',
  D_TRANSBORDEURS: 'D — Transbordeurs (Art 6.4)'
};

export function EscalesList({ escales, navires, ports, marchandises, onRefresh }) {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [step, setStep] = useState('');
  const [error, setError] = useState(null);
  const [filterStatut, setFilterStatut] = useState('ALL');

  // Champs escale
  const [selectedNavireId, setSelectedNavireId] = useState('');
  const [selectedPortId, setSelectedPortId] = useState('');
  const [provenance, setProvenance] = useState('');
  const [destination, setDestination] = useState('');
  const [dateEntree, setDateEntree] = useState(new Date().toISOString().slice(0, 16));
  const [dateSortie, setDateSortie] = useState(new Date(Date.now() + 86400000 * 2).toISOString().slice(0, 16));

  // Régime standard
  const [raisonEntree, setRaisonEntree] = useState('');
  const [passageMouillage, setPassageMouillage] = useState(false);
  const [dateAccostageMouillage, setDateAccostageMouillage] = useState(new Date().toISOString().slice(0, 16));
  const [abonnement, setAbonnement] = useState(false);
  const [typeRistourne, setTypeRistourne] = useState('');

  // Régime terminaux spéciaux
  const [sensOperationPetrolier, setSensOperationPetrolier] = useState('DEBARQUEMENT');
  const [selectedMarchandiseId, setSelectedMarchandiseId] = useState('');

  const [exoneration, setExoneration] = useState(false);

  const selectedPort = useMemo(() => ports.find(p => p.idPort === Number(selectedPortId)) || {}, [ports, selectedPortId]);
  const regime = selectedPort.typeTerminal || null;
  const estRegimeSpecial = regime && regime !== 'STANDARD';

  const resetForm = () => {
    setSelectedNavireId('');
    setSelectedPortId('');
    setProvenance('');
    setDestination('');
    setDateEntree(new Date().toISOString().slice(0, 16));
    setDateSortie(new Date(Date.now() + 86400000 * 2).toISOString().slice(0, 16));
    setRaisonEntree('');
    setPassageMouillage(false);
    setAbonnement(false);
    setTypeRistourne('');
    setSensOperationPetrolier('DEBARQUEMENT');
    setSelectedMarchandiseId('');
    setExoneration(false);
    setError(null);
  };

  const openModal = () => {
    resetForm();
    setIsModalOpen(true);
  };

  const closeModal = () => {
    if (loading) return;
    setIsModalOpen(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    if (!selectedNavireId || !selectedPortId) {
      setError('Sélectionne un navire et un port.');
      return;
    }
    if (new Date(dateSortie) <= new Date(dateEntree)) {
      setError('La date de sortie doit être postérieure à la date d\u2019entrée.');
      return;
    }

    setLoading(true);
    try {
      setStep('Création de l\u2019escale...');
      const escale = await api.createEscale({
        provenance,
        destination,
        dateEntree,
        dateSortie,
        statut: 'EN_COURS',
        idNavire: Number(selectedNavireId),
        idPort: Number(selectedPortId)
      });
      const idEscale = escale.idEscale;
      const idMarchandise = selectedMarchandiseId ? Number(selectedMarchandiseId) : null;

      if (regime === 'STANDARD' || !regime) {
        setStep('Enregistrement de l\u2019entrée (Art 5)...');
        await api.createEntree({
          raison: raisonEntree || null,
          facturationNext: true,
          exoneration,
          typeRistourne: typeRistourne || null,
          idNavire: Number(selectedNavireId),
          idEscale
        });

        if (passageMouillage) {
          setStep('Enregistrement du mouillage (Art 4)...');
          await api.createMouillage({
            dateAccostage: dateAccostageMouillage,
            abonnement: false,
            exoneration,
            typeRistourne: typeRistourne || null,
            idNavire: Number(selectedNavireId),
            idMarchandise,
            idEscale
          });
        }

        setStep('Enregistrement du stationnement (Art 2.1)...');
        await api.createStationnement({
          embarquement: false,
          abonnement,
          exoneration,
          typeRistourne: typeRistourne || null,
          idNavire: Number(selectedNavireId),
          idMarchandise,
          idEscale
        });
      } else if (regime === 'PETROLIER_MOHAMMEDIA') {
        setStep('Enregistrement du stationnement (Art 2.2)...');
        await api.createStationnement({
          embarquement: sensOperationPetrolier === 'EMBARQUEMENT',
          abonnement: false,
          exoneration,
          typeRistourne: null,
          idNavire: Number(selectedNavireId),
          idMarchandise,
          idEscale
        });
      } else {
        setStep('Enregistrement du stationnement (Art 2.3/2.4)...');
        await api.createStationnement({
          embarquement: false,
          abonnement: false,
          exoneration,
          typeRistourne: null,
          idNavire: Number(selectedNavireId),
          idMarchandise,
          idEscale
        });
      }

      setLoading(false);
      setStep('');
      setIsModalOpen(false);
      if (onRefresh) onRefresh();
    } catch (err) {
      setLoading(false);
      setStep('');
      setError(err.message || 'Erreur lors de la création de l\u2019escale.');
    }
  };

  const filteredEscales = escales.filter(esc => {
    if (filterStatut === 'ALL') return true;
    if (filterStatut === 'A_FACTURER') return !esc.idEnteteFacture;
    if (filterStatut === 'FACTUREE') return !!esc.idEnteteFacture;
    return true;
  });

  return (
    <div className="escales-container">
      <div className="page-header card">
        <div className="page-title">
          <div className="title-icon bg-navy">
            <Navigation size={26} color="#FFFFFF" />
          </div>
          <div>
            <h2>Escales</h2>
            <p>Enregistre l'escale et les droits applicables — la facture se génère ensuite en un clic</p>
          </div>
        </div>
        <button className="btn btn-gold" onClick={openModal}>
          <Plus size={16} /> Nouvelle Escale
        </button>
      </div>

      <div className="card filter-card">
        <select className="form-control" value={filterStatut} onChange={(e) => setFilterStatut(e.target.value)}>
          <option value="ALL">Toutes les escales</option>
          <option value="A_FACTURER">À facturer</option>
          <option value="FACTUREE">Déjà facturées</option>
        </select>
      </div>

      <div className="card table-card">
        <table className="data-table">
          <thead>
            <tr>
              <th>#</th>
              <th>Navire</th>
              <th>Port</th>
              <th>Entrée</th>
              <th>Sortie</th>
              <th>Provenance</th>
              <th>Destination</th>
              <th>Statut Facturation</th>
            </tr>
          </thead>
          <tbody>
            {filteredEscales.length === 0 && (
              <tr><td colSpan={8} className="empty-row">Aucune escale à afficher.</td></tr>
            )}
            {filteredEscales.map(esc => (
              <tr key={esc.idEscale}>
                <td>#{esc.idEscale}</td>
                <td>{esc.nomNavire}</td>
                <td>{esc.nomPort}</td>
                <td>{new Date(esc.dateEntree).toLocaleString('fr-FR')}</td>
                <td>{new Date(esc.dateSortie).toLocaleString('fr-FR')}</td>
                <td>{esc.provenance || '—'}</td>
                <td>{esc.destination || '—'}</td>
                <td>
                  {esc.idEnteteFacture ? (
                    <span className="badge badge-navy">Facturée (#{esc.numeroEnteteFacture ?? esc.idEnteteFacture})</span>
                  ) : (
                    <span className="badge badge-gold">À facturer</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {isModalOpen && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h3>Nouvelle Escale</h3>
              <button className="icon-btn" onClick={closeModal}><X size={18} /></button>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                <div className="form-row">
                  <div className="form-group">
                    <label>Navire *</label>
                    <select className="form-control" value={selectedNavireId} onChange={(e) => setSelectedNavireId(e.target.value)} required>
                      <option value="">-- Sélectionner --</option>
                      {navires.map(n => (
                        <option key={n.idNavire} value={n.idNavire}>{n.nom} — TJB {n.tjb}</option>
                      ))}
                    </select>
                  </div>
                  <div className="form-group">
                    <label>Port *</label>
                    <select className="form-control" value={selectedPortId} onChange={(e) => setSelectedPortId(e.target.value)} required>
                      <option value="">-- Sélectionner --</option>
                      {ports.map(p => (
                        <option key={p.idPort} value={p.idPort}>{p.nom}</option>
                      ))}
                    </select>
                  </div>
                </div>

                {selectedPort.nom && (
                  <div className="regime-banner">
                    <Info size={16} />
                    <span>
                      Régime : <strong>{
                        regime === 'PETROLIER_MOHAMMEDIA' ? 'Terminal Pétrolier de Mohammédia (Art 2.2)' :
                        regime === 'CHARBONNIER_JORF' ? 'Terminal Charbonnier de Jorf-Lasfar (Art 2.3)' :
                        regime === 'CHARBONNIER_SAFI' ? 'Terminal Charbonnier de Safi Atlantique (Art 2.4)' :
                        'Port standard (Art 2.1 / 4 / 5)'
                      }</strong>
                    </span>
                  </div>
                )}

                <div className="form-row">
                  <div className="form-group">
                    <label>Provenance</label>
                    <input type="text" className="form-control" value={provenance} onChange={(e) => setProvenance(e.target.value)} />
                  </div>
                  <div className="form-group">
                    <label>Destination</label>
                    <input type="text" className="form-control" value={destination} onChange={(e) => setDestination(e.target.value)} />
                  </div>
                </div>

                <div className="form-row">
                  <div className="form-group">
                    <label>Date & Heure d'Entrée *</label>
                    <input type="datetime-local" className="form-control" value={dateEntree} onChange={(e) => setDateEntree(e.target.value)} required />
                  </div>
                  <div className="form-group">
                    <label>Date & Heure de Sortie *</label>
                    <input type="datetime-local" className="form-control" value={dateSortie} onChange={(e) => setDateSortie(e.target.value)} required />
                  </div>
                </div>

                {!estRegimeSpecial && (
                  <>
                    <div className="form-row">
                      <div className="form-group">
                        <label>Raison de l'Entrée (Art 5)</label>
                        <select className="form-control" value={raisonEntree} onChange={(e) => setRaisonEntree(e.target.value)}>
                          {Object.entries(RAISON_ENTREE_LABELS).map(([value, label]) => (
                            <option key={value} value={value}>{label}</option>
                          ))}
                        </select>
                      </div>
                      <div className="form-group">
                        <label>Ristourne demandée (Art 6)</label>
                        <select className="form-control" value={typeRistourne} onChange={(e) => setTypeRistourne(e.target.value)}>
                          {Object.entries(TYPE_RISTOURNE_LABELS).map(([value, label]) => (
                            <option key={value} value={value}>{label}</option>
                          ))}
                        </select>
                      </div>
                    </div>

                    {marchandises.length > 0 && (
                      <div className="form-group">
                        <label>Marchandise (optionnel)</label>
                        <select className="form-control" value={selectedMarchandiseId} onChange={(e) => setSelectedMarchandiseId(e.target.value)}>
                          <option value="">-- Non renseignée --</option>
                          {marchandises.map(m => (
                            <option key={m.idMarchandise} value={m.idMarchandise}>{m.nom}</option>
                          ))}
                        </select>
                      </div>
                    )}

                    <div className="checkbox-row">
                      <label className="custom-checkbox">
                        <input type="checkbox" checked={passageMouillage} onChange={(e) => setPassageMouillage(e.target.checked)} />
                        <span>Navire passé par le mouillage avant accostage (Art 4)</span>
                      </label>
                      {passageMouillage && (
                        <div className="form-group" style={{ marginLeft: '1.5rem' }}>
                          <label>Date & Heure d'accostage à quai</label>
                          <input type="datetime-local" className="form-control" value={dateAccostageMouillage} onChange={(e) => setDateAccostageMouillage(e.target.value)} />
                        </div>
                      )}
                      <label className="custom-checkbox">
                        <input type="checkbox" checked={abonnement} onChange={(e) => setAbonnement(e.target.checked)} />
                        <span>Régime d'abonnement au stationnement (Art 3)</span>
                      </label>
                      <label className="custom-checkbox danger-text">
                        <input type="checkbox" checked={exoneration} onChange={(e) => setExoneration(e.target.checked)} />
                        <span>Exonération réglementaire totale (Art 7)</span>
                      </label>
                    </div>
                  </>
                )}

                {regime === 'PETROLIER_MOHAMMEDIA' && (
                  <>
                    <div className="form-row">
                      <div className="form-group">
                        <label>Sens de l'opération *</label>
                        <select className="form-control" value={sensOperationPetrolier} onChange={(e) => setSensOperationPetrolier(e.target.value)}>
                          <option value="DEBARQUEMENT">Débarquement (entrée + sortie, VG)</option>
                          <option value="EMBARQUEMENT">Embarquement (stationnement TJB)</option>
                        </select>
                      </div>
                      <div className="form-group">
                        <label>Marchandise</label>
                        <select className="form-control" value={selectedMarchandiseId} onChange={(e) => setSelectedMarchandiseId(e.target.value)}>
                          <option value="">-- Non renseignée --</option>
                          {marchandises.map(m => (
                            <option key={m.idMarchandise} value={m.idMarchandise}>{m.nom}</option>
                          ))}
                        </select>
                      </div>
                    </div>
                    <label className="custom-checkbox danger-text">
                      <input type="checkbox" checked={exoneration} onChange={(e) => setExoneration(e.target.checked)} />
                      <span>Exonération réglementaire totale (Art 7)</span>
                    </label>
                  </>
                )}

                {(regime === 'CHARBONNIER_JORF' || regime === 'CHARBONNIER_SAFI') && (
                  <>
                    <div className="form-group">
                      <label>Marchandise</label>
                      <select className="form-control" value={selectedMarchandiseId} onChange={(e) => setSelectedMarchandiseId(e.target.value)}>
                        <option value="">-- Non renseignée --</option>
                        {marchandises.map(m => (
                          <option key={m.idMarchandise} value={m.idMarchandise}>{m.nom}</option>
                        ))}
                      </select>
                    </div>
                    <label className="custom-checkbox danger-text">
                      <input type="checkbox" checked={exoneration} onChange={(e) => setExoneration(e.target.checked)} />
                      <span>Exonération réglementaire totale (Art 7)</span>
                    </label>
                  </>
                )}

                {error && (
                  <div className="error-banner">
                    <AlertTriangle size={16} />
                    <span>{error}</span>
                  </div>
                )}
              </div>

              <div className="modal-footer">
                <button type="button" className="btn btn-outline" onClick={closeModal} disabled={loading}>Annuler</button>
                <button type="submit" className="btn btn-gold" disabled={loading}>
                  {loading ? (step || 'Enregistrement...') : (<><CheckCircle2 size={16} /> Créer l'Escale</>)}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <style>{`
        .escales-container { display: flex; flex-direction: column; gap: 1.25rem; }
        .page-header { display: flex; align-items: center; justify-content: space-between; padding: 1.25rem; }
        .page-title { display: flex; align-items: center; gap: 0.85rem; }
        .page-title h2 { font-size: 1.25rem; color: var(--anp-navy); font-weight: 700; }
        .page-title p { color: var(--anp-text-muted); font-size: 0.85rem; }
        .title-icon { width: 44px; height: 44px; border-radius: 4px; display: flex; align-items: center; justify-content: center; }
        .filter-card { padding: 0.85rem 1.25rem; }
        .filter-card select { max-width: 260px; }
        .table-card { padding: 0; overflow-x: auto; }
        .data-table { width: 100%; border-collapse: collapse; font-size: 0.85rem; }
        .data-table th { text-align: left; padding: 0.75rem 1rem; background: #F8FAFC; color: var(--anp-text-muted); font-weight: 700; border-bottom: 1px solid var(--anp-border); white-space: nowrap; }
        .data-table td { padding: 0.75rem 1rem; border-bottom: 1px solid var(--anp-border); white-space: nowrap; }
        .empty-row { text-align: center; color: var(--anp-text-muted); padding: 2rem !important; }
        .regime-banner { display: flex; align-items: center; gap: 0.5rem; background: #EFF6FF; border: 1px solid #BFDBFE; color: var(--anp-navy); border-radius: 4px; padding: 0.6rem 0.85rem; font-size: 0.8rem; margin-bottom: 1rem; }
        .checkbox-row { display: flex; flex-direction: column; gap: 0.6rem; margin-top: 0.5rem; background: #F8FAFC; padding: 0.85rem; border-radius: 4px; border: 1px solid var(--anp-border); }
        .custom-checkbox { display: flex; align-items: center; gap: 0.5rem; font-size: 0.875rem; font-weight: 600; cursor: pointer; }
        .custom-checkbox input { width: 16px; height: 16px; accent-color: var(--anp-navy); }
        .danger-text { color: var(--danger); }
        .error-banner { display: flex; align-items: flex-start; gap: 0.5rem; background: #FEF2F2; border: 1px solid #FECACA; color: var(--danger); border-radius: 4px; padding: 0.6rem 0.85rem; font-size: 0.8rem; margin-top: 1rem; }
        .modal-overlay { position: fixed; inset: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; padding: 1rem; }
        .modal-content { background: #FFF; border-radius: 6px; width: 100%; max-width: 680px; max-height: 90vh; overflow-y: auto; }
        .modal-header { display: flex; align-items: center; justify-content: space-between; padding: 1rem 1.25rem; border-bottom: 1px solid var(--anp-border); }
        .modal-header h3 { font-size: 1.05rem; color: var(--anp-navy); font-weight: 700; }
        .icon-btn { background: none; border: none; cursor: pointer; color: var(--anp-text-muted); padding: 0.25rem; }
        .modal-body { padding: 1.25rem; }
        .modal-footer { display: flex; justify-content: flex-end; gap: 0.6rem; padding: 1rem 1.25rem; border-top: 1px solid var(--anp-border); }
      `}</style>
    </div>
  );
}

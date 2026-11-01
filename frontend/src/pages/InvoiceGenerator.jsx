import React, { useState, useMemo } from 'react';
import { FileText, Calculator, CheckCircle2, AlertTriangle, Info, Ship, Building2, Calendar } from 'lucide-react';
import { api } from '../services/api';

export function InvoiceGenerator({ escales, onInvoiceGenerated }) {
  const [selectedEscaleId, setSelectedEscaleId] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Seules les escales sans facture existante peuvent être générées
  const escalesAFacturer = useMemo(() => escales.filter(e => !e.idEnteteFacture), [escales]);

  const selectedEscale = useMemo(
    () => escalesAFacturer.find(e => e.idEscale === Number(selectedEscaleId)) || null,
    [escalesAFacturer, selectedEscaleId]
  );

  const handleGenerate = async (e) => {
    e.preventDefault();
    setError(null);
    if (!selectedEscaleId) {
      setError('Sélectionne une escale.');
      return;
    }
    setLoading(true);
    try {
      const facture = await api.genererFacture(Number(selectedEscaleId));
      setLoading(false);
      setSelectedEscaleId('');
      if (onInvoiceGenerated) onInvoiceGenerated(facture);
    } catch (err) {
      setLoading(false);
      setError(err.message || 'Erreur lors de la génération de la facture.');
    }
  };

  return (
    <div className="generator-container">
      <div className="page-header card">
        <div className="page-title">
          <div className="title-icon bg-gold">
            <Calculator size={26} color="#FFFFFF" />
          </div>
          <div>
            <h2>Générer une Facture</h2>
            <p>Choisis une escale déjà enregistrée — le moteur ANP calcule le montant à partir des données existantes</p>
          </div>
        </div>
      </div>

      <div className="generator-grid">
        <div className="card picker-card">
          <h3 className="section-head"><Ship size={18} color="#003366" /> Escale à facturer</h3>

          {escalesAFacturer.length === 0 ? (
            <div className="empty-state">
              <Info size={18} />
              <span>Aucune escale en attente de facturation. Crée d'abord une escale depuis l'onglet "Escales".</span>
            </div>
          ) : (
            <div className="form-group">
              <label>Sélectionner une escale *</label>
              <select className="form-control" value={selectedEscaleId} onChange={(e) => setSelectedEscaleId(e.target.value)} required>
                <option value="">-- Choisir --</option>
                {escalesAFacturer.map(esc => (
                  <option key={esc.idEscale} value={esc.idEscale}>
                    #{esc.idEscale} — {esc.nomNavire} @ {esc.nomPort} ({new Date(esc.dateEntree).toLocaleDateString('fr-FR')})
                  </option>
                ))}
              </select>
            </div>
          )}

          {selectedEscale && (
            <div className="escale-preview-box">
              <div className="preview-item"><Ship size={14} /> <span className="lbl">Navire :</span> <strong>{selectedEscale.nomNavire}</strong></div>
              <div className="preview-item"><Building2 size={14} /> <span className="lbl">Port :</span> <strong>{selectedEscale.nomPort}</strong></div>
              <div className="preview-item"><Calendar size={14} /> <span className="lbl">Entrée :</span> <strong>{new Date(selectedEscale.dateEntree).toLocaleString('fr-FR')}</strong></div>
              <div className="preview-item"><Calendar size={14} /> <span className="lbl">Sortie :</span> <strong>{new Date(selectedEscale.dateSortie).toLocaleString('fr-FR')}</strong></div>
              <div className="preview-item"><span className="lbl">Provenance :</span> <strong>{selectedEscale.provenance || '—'}</strong></div>
              <div className="preview-item"><span className="lbl">Destination :</span> <strong>{selectedEscale.destination || '—'}</strong></div>
            </div>
          )}
        </div>

        <div className="calculation-column">
          <div className="card calc-summary-card">
            <h3 className="calc-title"><FileText size={20} color="#C5A028" /> Génération</h3>

            <div className="info-panel">
              <p>
                Le montant n'est pas pré-calculé ici : il est déterminé par le moteur de tarification ANP
                (tranches TJB, volume géométrique, ristournes, abonnement, TVA) à partir des droits déjà
                enregistrés pour cette escale (entrée, mouillage, stationnement).
              </p>
            </div>

            {error && (
              <div className="error-banner">
                <AlertTriangle size={16} />
                <span>{error}</span>
              </div>
            )}

            <button
              className="btn btn-gold btn-lg submit-btn"
              onClick={handleGenerate}
              disabled={loading || !selectedEscaleId}
            >
              {loading ? 'Génération en cours...' : (<><CheckCircle2 size={18} /> Générer la Facture</>)}
            </button>
          </div>
        </div>
      </div>

      <style>{`
        .generator-container { display: flex; flex-direction: column; gap: 1.5rem; }
        .page-header { display: flex; align-items: center; justify-content: space-between; padding: 1.25rem; }
        .page-title { display: flex; align-items: center; gap: 0.85rem; }
        .page-title h2 { font-size: 1.25rem; color: var(--anp-navy); font-weight: 700; }
        .page-title p { color: var(--anp-text-muted); font-size: 0.85rem; }
        .title-icon { width: 44px; height: 44px; border-radius: 4px; display: flex; align-items: center; justify-content: center; }
        .generator-grid { display: grid; grid-template-columns: 1fr 380px; gap: 1.25rem; align-items: start; }
        .section-head { font-size: 1rem; color: var(--anp-navy); font-weight: 700; margin-bottom: 1rem; display: flex; align-items: center; gap: 0.5rem; border-bottom: 1px solid var(--anp-border); padding-bottom: 0.4rem; }
        .empty-state { display: flex; align-items: center; gap: 0.5rem; background: #F8FAFC; border: 1px solid var(--anp-border); border-radius: 4px; padding: 0.85rem; font-size: 0.85rem; color: var(--anp-text-muted); }
        .escale-preview-box { background: #F8FAFC; border: 1px solid var(--anp-border); border-radius: 4px; padding: 0.85rem; display: grid; grid-template-columns: 1fr 1fr; gap: 0.5rem; font-size: 0.85rem; margin-top: 1rem; }
        .preview-item { display: flex; align-items: center; gap: 0.35rem; }
        .lbl { color: var(--anp-text-muted); }
        .calc-summary-card { background: #FFFFFF; border-top: 3px solid var(--anp-gold); position: sticky; top: 80px; }
        .calc-title { font-size: 1.1rem; color: var(--anp-navy); font-weight: 700; display: flex; align-items: center; gap: 0.4rem; margin-bottom: 1rem; border-bottom: 1px solid var(--anp-border); padding-bottom: 0.5rem; }
        .info-panel { font-size: 0.85rem; color: var(--anp-text-dark); margin-bottom: 1.25rem; line-height: 1.5; }
        .error-banner { display: flex; align-items: flex-start; gap: 0.5rem; background: #FEF2F2; border: 1px solid #FECACA; color: var(--danger); border-radius: 4px; padding: 0.6rem 0.85rem; font-size: 0.8rem; margin-bottom: 1rem; }
        .submit-btn { width: 100%; padding: 0.75rem; font-size: 0.95rem; }
        @media (max-width: 992px) { .generator-grid { grid-template-columns: 1fr; } }
      `}</style>
    </div>
  );
}

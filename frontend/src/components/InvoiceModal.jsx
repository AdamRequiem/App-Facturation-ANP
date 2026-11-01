import React, { useState } from 'react';
import { X, Printer, Download, CheckCircle, Anchor, FileText, ShieldCheck, AlertTriangle } from 'lucide-react';

export function InvoiceModal({ facture, onClose, onValidate }) {
  const [validating, setValidating] = useState(false);
  const [validateError, setValidateError] = useState(null);

  if (!facture) return null;

  const estBrouillon = facture.statut === 'BROUILLON';

  const handlePrint = () => {
    window.print();
  };

  const handleValidate = async () => {
    if (!onValidate) return;
    setValidateError(null);
    setValidating(true);
    try {
      await onValidate(facture.idEnteteFacture);
    } catch (err) {
      setValidateError(err.message || 'Erreur lors de la validation.');
    } finally {
      setValidating(false);
    }
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    return d.toLocaleDateString('fr-FR') + ' à ' + d.toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' });
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content invoice-modal-wrapper" onClick={(e) => e.stopPropagation()}>
        {/* Header Bar */}
        <div className="modal-header">
          <h3>
            <FileText size={20} color="#D4AF37" /> Facture № {facture.numero || facture.idEnteteFacture}
          </h3>
          <button className="modal-close" onClick={onClose}>
            <X size={20} />
          </button>
        </div>

        {/* Modal Body - Printable ANP Invoice Layout */}
        <div className="modal-body invoice-printable-area">
          {/* Official ANP Header Stamp */}
          <div className="anp-invoice-header">
            <div className="anp-invoice-logo">
              <Anchor size={36} color="#002B49" />
              <div>
                <h2>AGENCE NATIONALE DES PORTS</h2>
                <p className="sub">ROYAUME DU MAROC</p>
                <p className="address">Direction des Opérations Portuaires — Service Facturation</p>
              </div>
            </div>
            <div className="anp-invoice-meta">
              <div className={estBrouillon ? 'invoice-badge-brouillon' : 'invoice-badge-payee'}>
                {estBrouillon ? 'BROUILLON — NON VALIDÉE' : 'FACTURE OFFICIELLE'}
              </div>
              <p><strong>N° Facture :</strong> {facture.numero || `FAC-${facture.idEnteteFacture}`}</p>
              <p><strong>Date d'Émission :</strong> {formatDate(facture.date)}</p>
              <p><strong>Statut :</strong> <span className={estBrouillon ? 'statut-tag-brouillon' : 'statut-tag'}>{facture.statut || 'BROUILLON'}</span></p>
            </div>
          </div>

          <hr className="invoice-divider" />

          {/* Client & Escale Info */}
          <div className="invoice-client-row">
            <div className="client-box">
              <h4>INFORMATIONS CLIENT / ARMATEUR</h4>
              <p className="client-name">{facture.client || 'Client Non Renseigné'}</p>
              <p>Redevable des Droits et Taxe Portuaire</p>
              <p>Royaume du Maroc</p>
            </div>
            <div className="escale-box">
              <h4>DÉTAILS ESCALE & NAVIRE</h4>
              <p><strong>N° Escale :</strong> #{facture.idEscale ?? 'N/A'}</p>
              <p><strong>Devise :</strong> Euro (EUR / €)</p>
              <p><strong>Mode de Règlement :</strong> Virement / Chèque ANP</p>
            </div>
          </div>

          {/* Line Items Table */}
          <div className="table-responsive invoice-table-container">
            <table className="table invoice-table">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Désignation des Prestations / Droits</th>
                  <th>Quantité</th>
                  <th>Unité</th>
                  <th>Taux HT (€)</th>
                  <th style={{ textStyle: 'right' }}>Montant HT (€)</th>
                </tr>
              </thead>
              <tbody>
                {facture.details && facture.details.length > 0 ? (
                  facture.details.map((item, idx) => (
                    <tr key={idx}>
                      <td>{idx + 1}</td>
                      <td>
                        <strong>{item.typeDroit || 'Droit Portuaire'}</strong>
                        <div className="item-desc">{item.description}</div>
                      </td>
                      <td>{item.quantiteHeure ?? '—'}</td>
                      <td>{item.unite || 'Unité'}</td>
                      <td>{item.tauxHoraire != null ? item.tauxHoraire.toLocaleString('fr-FR', { minimumFractionDigits: 2 }) : '—'}</td>
                      <td style={{ fontWeight: '700', textAlign: 'right' }}>
                        {(item.montantHT || 0).toLocaleString('fr-FR', { minimumFractionDigits: 2 })} €
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td>1</td>
                    <td>
                      <strong>DROIT DE PORT SUR NAVIRE & ESCALE</strong>
                      <div className="item-desc">Forfait d'accès portuaire et droits de séjour</div>
                    </td>
                    <td>1</td>
                    <td>Forfait</td>
                    <td>{(facture.totalHT || 0).toLocaleString('fr-FR', { minimumFractionDigits: 2 })}</td>
                    <td style={{ fontWeight: '700', textAlign: 'right' }}>
                      {(facture.totalHT || 0).toLocaleString('fr-FR', { minimumFractionDigits: 2 })} €
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          {/* Totals & Calculations */}
          <div className="invoice-footer-totals">
            <div className="stamp-box">
              {estBrouillon ? (
                <div className="anp-seal-warning">
                  <AlertTriangle size={28} color="#B45309" />
                  <div>
                    <p className="seal-title-warning">BROUILLON NON VALIDÉ</p>
                    <p className="seal-sub-warning">N'apparaît pas dans l'historique tant que non validée</p>
                  </div>
                </div>
              ) : (
                <div className="anp-seal">
                  <CheckCircle size={28} color="#10B981" />
                  <div>
                    <p className="seal-title">VISÉ ET VALIDÉ ANP</p>
                    <p className="seal-sub">Système Intégré de Facturation</p>
                  </div>
                </div>
              )}
            </div>

            <div className="totals-table">
              <div className="total-row">
                <span>Total HT :</span>
                <strong>{(facture.totalHT || 0).toLocaleString('fr-FR', { minimumFractionDigits: 2 })} €</strong>
              </div>
              <div className="total-row">
                <span>TVA ({facture.taux_TVA || 20}%) :</span>
                <span>{(facture.montant_TVA || (facture.totalHT * 0.20)).toLocaleString('fr-FR', { minimumFractionDigits: 2 })} €</span>
              </div>
              <div className="total-row grand-total">
                <span>MONTANT TOTAL TTC :</span>
                <strong>{(facture.totalTTC || (facture.totalHT * 1.20)).toLocaleString('fr-FR', { minimumFractionDigits: 2 })} €</strong>
              </div>
            </div>
          </div>
        </div>

        {/* Modal Footer Actions */}
        <div className="modal-footer">
          {estBrouillon && validateError && (
            <div className="validate-error">
              <AlertTriangle size={14} /> {validateError}
            </div>
          )}
          <button className="btn btn-outline" onClick={onClose}>
            Fermer
          </button>
          <button className="btn btn-secondary" onClick={handlePrint}>
            <Download size={16} /> Télécharger PDF
          </button>
          <button className="btn btn-primary" onClick={handlePrint}>
            <Printer size={16} /> Imprimer la Facture
          </button>
          {estBrouillon && (
            <button className="btn btn-gold" onClick={handleValidate} disabled={validating}>
              <ShieldCheck size={16} /> {validating ? 'Validation...' : 'Valider la Facture'}
            </button>
          )}
        </div>
      </div>

      <style>{`
        .invoice-modal-wrapper {
          max-width: 850px;
        }

        .anp-invoice-header {
          display: flex;
          justify-content: space-between;
          align-items: flex-start;
          margin-bottom: 1.25rem;
        }

        .anp-invoice-logo {
          display: flex;
          gap: 1rem;
          align-items: center;
        }

        .anp-invoice-logo h2 {
          font-family: 'Outfit', sans-serif;
          color: var(--anp-navy);
          font-size: 1.25rem;
          font-weight: 800;
        }

        .anp-invoice-logo .sub {
          color: var(--anp-gold-dark);
          font-weight: 700;
          font-size: 0.8rem;
          letter-spacing: 0.05em;
        }

        .anp-invoice-logo .address {
          color: var(--anp-text-muted);
          font-size: 0.775rem;
        }

        .anp-invoice-meta {
          text-align: right;
          font-size: 0.85rem;
        }

        .invoice-badge-payee {
          display: inline-block;
          background: var(--anp-navy);
          color: #FFFFFF;
          font-weight: 800;
          font-size: 0.725rem;
          padding: 0.25rem 0.75rem;
          border-radius: var(--radius-sm);
          margin-bottom: 0.5rem;
          letter-spacing: 0.05em;
        }

        .invoice-badge-brouillon {
          display: inline-block;
          background: #B45309;
          color: #FFFFFF;
          font-weight: 800;
          font-size: 0.725rem;
          padding: 0.25rem 0.75rem;
          border-radius: var(--radius-sm);
          margin-bottom: 0.5rem;
          letter-spacing: 0.05em;
        }

        .statut-tag {
          color: var(--success);
          font-weight: 800;
        }

        .statut-tag-brouillon {
          color: #B45309;
          font-weight: 800;
        }

        .anp-seal-warning {
          display: flex;
          align-items: center;
          gap: 0.75rem;
          background: rgba(180, 83, 9, 0.08);
          border: 2px dashed #B45309;
          padding: 0.85rem 1.25rem;
          border-radius: var(--radius-md);
        }

        .seal-title-warning {
          font-weight: 800;
          color: #92400E;
          font-size: 0.85rem;
        }

        .seal-sub-warning {
          font-size: 0.725rem;
          color: #B45309;
        }

        .validate-error {
          display: flex;
          align-items: center;
          gap: 0.4rem;
          color: var(--danger);
          font-size: 0.8rem;
          margin-right: auto;
        }

        .invoice-divider {
          border: none;
          border-top: 2px solid var(--anp-navy);
          margin: 1rem 0 1.5rem 0;
        }

        .invoice-client-row {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 1.5rem;
          background: #F8FAFC;
          padding: 1.25rem;
          border-radius: var(--radius-md);
          border: 1px solid var(--anp-border);
          margin-bottom: 1.5rem;
          font-size: 0.875rem;
        }

        .invoice-client-row h4 {
          font-size: 0.8rem;
          color: var(--anp-navy);
          font-weight: 700;
          margin-bottom: 0.5rem;
          text-transform: uppercase;
        }

        .client-name {
          font-size: 1.05rem;
          font-weight: 700;
          color: var(--anp-text-dark);
        }

        .invoice-table-container {
          margin-bottom: 1.5rem;
        }

        .invoice-table th {
          background: var(--anp-navy);
          color: #FFFFFF;
        }

        .item-desc {
          font-size: 0.775rem;
          color: var(--anp-text-muted);
          font-weight: normal;
        }

        .invoice-footer-totals {
          display: flex;
          justify-content: space-between;
          align-items: flex-end;
          gap: 2rem;
          margin-top: 1rem;
        }

        .anp-seal {
          display: flex;
          align-items: center;
          gap: 0.75rem;
          background: rgba(16, 185, 129, 0.08);
          border: 2px dashed #10B981;
          padding: 0.85rem 1.25rem;
          border-radius: var(--radius-md);
        }

        .seal-title {
          font-weight: 800;
          color: #065F46;
          font-size: 0.85rem;
        }

        .seal-sub {
          font-size: 0.725rem;
          color: #047857;
        }

        .totals-table {
          width: 320px;
          display: flex;
          flex-direction: column;
          gap: 0.5rem;
          font-size: 0.9rem;
        }

        .total-row {
          display: flex;
          justify-content: space-between;
          padding: 0.35rem 0;
          border-bottom: 1px solid var(--anp-border);
        }

        .grand-total {
          background: var(--anp-navy);
          color: #FFFFFF;
          padding: 0.75rem 1rem;
          border-radius: var(--radius-sm);
          font-size: 1.05rem;
          margin-top: 0.5rem;
        }
      `}</style>
    </div>
  );
}

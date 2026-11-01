import React, { useState, useMemo } from 'react';
import { History, Search, Eye, Trash2, X, FileText, CheckCircle, Clock } from 'lucide-react';
import { api } from '../services/api';

export function InvoiceHistory({ factures, onRefresh, onOpenInvoiceDetails }) {
  const [searchTerm, setSearchTerm] = useState('');
  const [statutFilter, setStatutFilter] = useState('ALL');

  // Filtered Invoices
  const filteredFactures = useMemo(() => {
    return factures.filter(f => {
      const numStr = String(f.numero || f.idEnteteFacture);
      const matchSearch = numStr.includes(searchTerm) || (f.client || '').toLowerCase().includes(searchTerm.toLowerCase());
      const matchStatut = statutFilter === 'ALL' || f.statut === statutFilter;
      return matchSearch && matchStatut;
    });
  }, [factures, searchTerm, statutFilter]);

  const handleDelete = async (id) => {
    if (window.confirm('Voulez-vous supprimer cette facture de l\'historique ?')) {
      await api.deleteFacture(id);
      onRefresh();
    }
  };

  return (
    <div className="history-page">
      {/* Top Header Bar */}
      <div className="page-header card">
        <div className="page-title">
          <div className="title-icon bg-navy">
            <History size={28} color="#FFFFFF" />
          </div>
          <div>
            <h2>Historique Général des Factures</h2>
            <p>Registre comptable officiel et archives des droits de port émis par l'ANP</p>
          </div>
        </div>
        <div className="badge badge-navy">
          <FileText size={14} /> Total: {factures.length} Facture{factures.length > 1 ? 's' : ''}
        </div>
      </div>

      {/* Search & Filter Bar */}
      <div className="filter-bar card">
        <div className="search-input-box">
          <Search size={18} color="#64748B" />
          <input
            type="text"
            placeholder="Rechercher par N° de facture ou nom du client/armateur..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          {searchTerm && (
            <button className="clear-btn" onClick={() => setSearchTerm('')}>
              <X size={16} />
            </button>
          )}
        </div>

        <div className="filter-group">
          <select value={statutFilter} onChange={(e) => setStatutFilter(e.target.value)} className="form-control" style={{ width: '200px' }}>
            <option value="ALL">Tous les Statuts</option>
            <option value="PAYEE">Payée (Réglée)</option>
            <option value="EN_ATTENTE">En attente de règlement</option>
            <option value="ANNULEE">Annulée</option>
          </select>
        </div>
      </div>

      {/* Data Table */}
      <div className="table-responsive card">
        <table className="table">
          <thead>
            <tr>
              <th>N° Facture</th>
              <th>Date d'Émission</th>
              <th>Client / Armateur</th>
              <th>Total HT (€)</th>
              <th>TVA (20%)</th>
              <th>Total TTC (€)</th>
              <th>Statut</th>
              <th style={{ textAlign: 'right' }}>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredFactures.length > 0 ? (
              filteredFactures.map((f) => (
                <tr key={f.idEnteteFacture}>
                  <td>
                    <strong>№ {f.numero || f.idEnteteFacture}</strong>
                  </td>
                  <td>
                    {new Date(f.date).toLocaleDateString('fr-FR')} {new Date(f.date).toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' })}
                  </td>
                  <td>
                    <strong style={{ color: 'var(--anp-navy)' }}>{f.client || 'Client ANP'}</strong>
                  </td>
                  <td>{(f.totalHT || 0).toLocaleString('fr-FR', { minimumFractionDigits: 2 })} €</td>
                  <td>{(f.montant_TVA || (f.totalHT * 0.2)).toLocaleString('fr-FR', { minimumFractionDigits: 2 })} €</td>
                  <td>
                    <strong style={{ color: 'var(--anp-navy)', fontSize: '1.05rem' }}>
                      {(f.totalTTC || (f.totalHT * 1.2)).toLocaleString('fr-FR', { minimumFractionDigits: 2 })} €
                    </strong>
                  </td>
                  <td>
                    <span className={`badge ${
                      f.statut === 'PAYEE' ? 'badge-success' :
                      f.statut === 'EN_ATTENTE' ? 'badge-warning' : 'badge-danger'
                    }`}>
                      {f.statut === 'PAYEE' ? <CheckCircle size={12} /> : <Clock size={12} />} {f.statut || 'PAYEE'}
                    </span>
                  </td>
                  <td style={{ textAlign: 'right' }}>
                    <div className="action-btns">
                      <button className="btn btn-gold btn-sm" onClick={() => onOpenInvoiceDetails(f)}>
                        <Eye size={14} /> Voir / Imprimer
                      </button>
                      <button className="btn btn-danger btn-sm" onClick={() => handleDelete(f.idEnteteFacture)}>
                        <Trash2 size={14} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="8" style={{ textAlign: 'center', padding: '3rem 1rem', color: '#64748B' }}>
                  Aucune facture trouvée dans l'historique.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <style>{`
        .history-page {
          display: flex;
          flex-direction: column;
          gap: 1.5rem;
        }
      `}</style>
    </div>
  );
}

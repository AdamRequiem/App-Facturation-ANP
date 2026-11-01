import React, { useMemo } from 'react';
import { Ship, FileText, DollarSign, Building2, History, Settings, PlusCircle, ArrowRight, TrendingUp, Navigation } from 'lucide-react';

export function Dashboard({ navires, factures, ports, setActiveTab, onOpenInvoiceDetails }) {
  // Stat 1: Total ships count
  const totalNavires = navires.length;

  // Stat 2: Invoices generated today
  const todayStr = new Date().toISOString().split('T')[0];
  const facturesAujourdhui = useMemo(() => {
    return factures.filter(f => {
      if (!f.date) return false;
      return f.date.startsWith(todayStr);
    }).length;
  }, [factures, todayStr]);

  // Stat 3: Total revenue (Recettes Totales en €)
  const totalRecettes = useMemo(() => {
    return factures.reduce((sum, f) => sum + (f.totalTTC || 0), 0);
  }, [factures]);

  const recentFactures = factures.slice(0, 5);

  return (
    <div className="dashboard-container">
      {/* Welcome Hero Banner */}
      <div className="hero-banner card">
        <div className="hero-content">
          <h1>Système de Facturation des Droits Portuaires</h1>
          <p>
            Gestion centralisée des redevances navires, droits de stationnement, mouillage et tarification au niveau des ports du Royaume du Maroc.
          </p>
          <div className="hero-actions">
            <button className="btn btn-gold btn-lg" onClick={() => setActiveTab('escales')}>
              <PlusCircle size={18} /> Nouvelle Escale
            </button>
            <button className="btn btn-secondary btn-lg" onClick={() => setActiveTab('history')}>
              <History size={18} /> Consulter l'Historique
            </button>
          </div>
        </div>
      </div>

      {/* 📊 3 KEY STATS CARDS REQUIRED BY USER */}
      <div className="stats-grid">
        {/* Stat 1: Nombre de Navires */}
        <div className="stat-card card" onClick={() => setActiveTab('ships')} style={{ cursor: 'pointer' }}>
          <div className="stat-header">
            <span className="stat-label">Nombre de Navires</span>
            <div className="stat-icon-wrapper navire-icon">
              <Ship size={24} color="#003366" />
            </div>
          </div>
          <div className="stat-value">{totalNavires}</div>
          <div className="stat-footer">
            <span className="stat-link">Voir tous <ArrowRight size={12} /></span>
          </div>
        </div>

        {/* Stat 2: Factures Générées Aujourd'hui */}
        <div className="stat-card card" onClick={() => setActiveTab('history')} style={{ cursor: 'pointer' }}>
          <div className="stat-header">
            <span className="stat-label">Factures Générées Aujourd'hui</span>
            <div className="stat-icon-wrapper facture-icon">
              <FileText size={24} color="#C5A028" />
            </div>
          </div>
          <div className="stat-value">{facturesAujourdhui}</div>
          <div className="stat-footer">
            <span className="stat-link">Voir registre <ArrowRight size={12} /></span>
          </div>
        </div>

        {/* Stat 3: Montant Total des Recettes */}
        <div className="stat-card card">
          <div className="stat-header">
            <span className="stat-label">Montant Total des Recettes</span>
            <div className="stat-icon-wrapper recette-icon">
              <DollarSign size={24} color="#006699" />
            </div>
          </div>
          <div className="stat-value">
            {totalRecettes.toLocaleString('fr-FR', { minimumFractionDigits: 2 })} <span className="currency">EUR (€)</span>
          </div>
          <div className="stat-footer">
            <span className="stat-link"><TrendingUp size={14} /> Recettes Totales</span>
          </div>
        </div>
      </div>

      {/* 🚀 QUICK NAVIGATION HUB */}
      <div className="section-title">
        <h2>Accès Rapide aux Modules</h2>
        <p>Sélectionnez un domaine d'action pour accéder à la gestion des données</p>
      </div>

      <div className="modules-grid">
        <div className="module-card card" onClick={() => setActiveTab('escales')}>
          <div className="module-icon bg-navy">
            <Navigation size={28} color="#FFFFFF" />
          </div>
          <h3>Escales</h3>
          <p>Enregistrer une escale et les droits applicables (entrée, mouillage, stationnement).</p>
          <button className="btn btn-secondary btn-sm">Accéder <ArrowRight size={14} /></button>
        </div>

        <div className="module-card card" onClick={() => setActiveTab('generator')}>
          <div className="module-icon bg-gold">
            <FileText size={28} color="#FFFFFF" />
          </div>
          <h3>Générer une Facture</h3>
          <p>Calculer les droits de port, stationnement et émettre le reçu officiel d'escale.</p>
          <button className="btn btn-gold btn-sm">Accéder <ArrowRight size={14} /></button>
        </div>

        <div className="module-card card" onClick={() => setActiveTab('ships')}>
          <div className="module-icon bg-navy">
            <Ship size={28} color="#FFFFFF" />
          </div>
          <h3>Tous les Navires</h3>
          <p>Consulter la flotte, enregistrer de nouveaux navires et mettre à jour le TJB.</p>
          <button className="btn btn-primary btn-sm">Accéder <ArrowRight size={14} /></button>
        </div>

        <div className="module-card card" onClick={() => setActiveTab('ports')}>
          <div className="module-icon bg-ocean">
            <Building2 size={28} color="#FFFFFF" />
          </div>
          <h3>Tous les Ports</h3>
          <p>Gestion des infrastructures portuaires et des terminaux spécialisés.</p>
          <button className="btn btn-secondary btn-sm">Accéder <ArrowRight size={14} /></button>
        </div>

        <div className="module-card card" onClick={() => setActiveTab('history')}>
          <div className="module-icon bg-navy">
            <History size={28} color="#FFFFFF" />
          </div>
          <h3>Historique Factures</h3>
          <p>Registre comptable des factures émises, détails des prestations et impression.</p>
          <button className="btn btn-primary btn-sm">Accéder <ArrowRight size={14} /></button>
        </div>

        <div className="module-card card" onClick={() => setActiveTab('settings')}>
          <div className="module-icon bg-dark">
            <Settings size={28} color="#FFFFFF" />
          </div>
          <h3>Paramètres Tarifs</h3>
          <p>Configuration des grilles tarifaires et gestion des tranches de facturation.</p>
          <button className="btn btn-outline btn-sm">Accéder <ArrowRight size={14} /></button>
        </div>
      </div>

      {/* RECENT INVOICES ACTIVITY */}
      <div className="recent-section card">
        <div className="recent-header">
          <div>
            <h3>Factures Récentes Émises</h3>
            <p>Dernières facturations enregistrées dans le système</p>
          </div>
          <button className="btn btn-outline btn-sm" onClick={() => setActiveTab('history')}>
            Voir tout l'historique
          </button>
        </div>

        <div className="table-responsive">
          <table className="table">
            <thead>
              <tr>
                <th>N° Facture</th>
                <th>Client / Armateur</th>
                <th>Date</th>
                <th>Montant HT</th>
                <th>Total TTC</th>
                <th>Statut</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {recentFactures.map((fac) => (
                <tr key={fac.idEnteteFacture}>
                  <td><strong>№ {fac.numero || fac.idEnteteFacture}</strong></td>
                  <td>{fac.client || 'Client ANP'}</td>
                  <td>{new Date(fac.date).toLocaleDateString('fr-FR')}</td>
                  <td>{(fac.totalHT || 0).toLocaleString('fr-FR')} €</td>
                  <td><strong style={{ color: 'var(--anp-navy)' }}>{(fac.totalTTC || 0).toLocaleString('fr-FR')} €</strong></td>
                  <td>
                    <span className={`badge ${fac.statut === 'PAYEE' ? 'badge-success' : 'badge-warning'}`}>
                      {fac.statut || 'PAYEE'}
                    </span>
                  </td>
                  <td>
                    <button className="btn btn-outline btn-sm" onClick={() => onOpenInvoiceDetails(fac)}>
                      Détails
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <style>{`
        .dashboard-container {
          display: flex;
          flex-direction: column;
          gap: 1.5rem;
        }

        .hero-banner {
          background: var(--anp-navy);
          color: #FFFFFF;
          padding: 2rem;
          border-left: 5px solid var(--anp-gold);
        }

        .hero-content h1 {
          font-size: 1.75rem;
          font-weight: 700;
          margin-bottom: 0.5rem;
        }

        .hero-content p {
          max-width: 700px;
          color: rgba(255, 255, 255, 0.85);
          font-size: 0.95rem;
          margin-bottom: 1.25rem;
        }

        .hero-actions {
          display: flex;
          gap: 0.75rem;
          flex-wrap: wrap;
        }

        .stats-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
          gap: 1.25rem;
        }

        .stat-card {
          padding: 1.25rem;
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          border-top: 3px solid var(--anp-navy);
        }

        .stat-header {
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-bottom: 0.75rem;
        }

        .stat-label {
          font-size: 0.85rem;
          font-weight: 700;
          color: var(--anp-text-muted);
          text-transform: uppercase;
        }

        .stat-icon-wrapper {
          width: 42px;
          height: 42px;
          border-radius: 4px;
          display: flex;
          align-items: center;
          justify-content: center;
        }

        .navire-icon { background: #EBF1F5; }
        .facture-icon { background: #FFF8E1; }
        .recette-icon { background: #E6F4EA; }

        .stat-value {
          font-size: 1.85rem;
          font-weight: 700;
          color: var(--anp-navy);
          margin-bottom: 0.75rem;
        }

        .stat-value .currency {
          font-size: 1rem;
          color: var(--anp-text-muted);
          font-weight: 600;
        }

        .stat-footer {
          display: flex;
          align-items: center;
          justify-content: flex-end;
          border-top: 1px solid var(--anp-border);
          padding-top: 0.6rem;
        }

        .stat-link {
          font-size: 0.8rem;
          font-weight: 700;
          color: var(--anp-blue);
          display: flex;
          align-items: center;
          gap: 0.25rem;
        }

        .section-title h2 {
          font-size: 1.3rem;
          color: var(--anp-navy);
          font-weight: 700;
        }

        .section-title p {
          color: var(--anp-text-muted);
          font-size: 0.85rem;
        }

        .modules-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
          gap: 1rem;
        }

        .module-card {
          display: flex;
          flex-direction: column;
          align-items: flex-start;
          gap: 0.75rem;
          cursor: pointer;
        }

        .module-icon {
          width: 48px;
          height: 48px;
          border-radius: 4px;
          display: flex;
          align-items: center;
          justify-content: center;
        }

        .bg-gold { background: var(--anp-gold); }
        .bg-navy { background: var(--anp-navy); }
        .bg-ocean { background: var(--anp-blue); }
        .bg-dark { background: #475569; }

        .module-card h3 {
          font-size: 1rem;
          font-weight: 700;
          color: var(--anp-navy);
        }

        .module-card p {
          font-size: 0.8rem;
          color: var(--anp-text-muted);
          flex: 1;
        }

        .recent-header {
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-bottom: 1rem;
        }

        .recent-header h3 {
          font-size: 1.15rem;
          color: var(--anp-navy);
          font-weight: 700;
        }
      `}</style>
    </div>
  );
}

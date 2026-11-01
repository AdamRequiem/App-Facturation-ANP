import React from 'react';
import { Anchor, Ship, Building2, FileText, History, Settings, Home, Navigation } from 'lucide-react';

export function Header({ activeTab, setActiveTab }) {
  const navItems = [
    { id: 'dashboard', label: 'Accueil', icon: Home },
    { id: 'escales', label: 'Escales', icon: Navigation },
    { id: 'generator', label: 'Générer Facture', icon: FileText },
    { id: 'ships', label: 'Tous les Navires', icon: Ship },
    { id: 'ports', label: 'Tous les Ports', icon: Building2 },
    { id: 'history', label: 'Historique Factures', icon: History },
    { id: 'settings', label: 'Paramètres', icon: Settings }
  ];

  return (
    <header className="anp-header">
      <div className="anp-nav-bar">
        <div className="anp-nav-container">
          {/* Logo & Branding */}
          <div className="anp-brand" onClick={() => setActiveTab('dashboard')} style={{ cursor: 'pointer' }}>
            <div className="anp-logo-icon">
              <Anchor size={26} color="#FFFFFF" />
            </div>
            <div className="anp-brand-text">
              <h1>ANP FACTURATION</h1>
              <p>Agence Nationale des Ports</p>
            </div>
          </div>

          {/* Navigation Items */}
          <nav className="anp-nav-menu">
            {navItems.map((item) => {
              const IconComponent = item.icon;
              const isActive = activeTab === item.id;
              return (
                <button
                  key={item.id}
                  className={`anp-nav-item ${isActive ? 'active' : ''}`}
                  onClick={() => setActiveTab(item.id)}
                >
                  <IconComponent size={16} />
                  <span>{item.label}</span>
                </button>
              );
            })}
          </nav>
        </div>
      </div>

      <style>{`
        .anp-header {
          width: 100%;
          background: var(--anp-navy);
          border-bottom: 2px solid var(--anp-blue);
          position: sticky;
          top: 0;
          z-index: 900;
        }

        .anp-nav-bar {
          padding: 0.6rem 0;
          background: var(--anp-navy);
        }

        .anp-nav-container {
          max-width: 1300px;
          margin: 0 auto;
          padding: 0 1rem;
          display: flex;
          align-items: center;
          justify-content: space-between;
          gap: 1.5rem;
        }

        .anp-brand {
          display: flex;
          align-items: center;
          gap: 0.75rem;
        }

        .anp-logo-icon {
          width: 40px;
          height: 40px;
          background: var(--anp-blue);
          border-radius: 4px;
          display: flex;
          align-items: center;
          justify-content: center;
        }

        .anp-brand-text h1 {
          color: #FFFFFF;
          font-size: 1.15rem;
          font-weight: 700;
          letter-spacing: 0.03em;
          line-height: 1.1;
        }

        .anp-brand-text p {
          color: rgba(255, 255, 255, 0.75);
          font-size: 0.75rem;
        }

        .anp-nav-menu {
          display: flex;
          align-items: center;
          gap: 0.25rem;
          overflow-x: auto;
        }

        .anp-nav-item {
          display: flex;
          align-items: center;
          gap: 0.4rem;
          background: transparent;
          color: rgba(255, 255, 255, 0.85);
          border: none;
          padding: 0.55rem 0.85rem;
          border-radius: 4px;
          font-size: 0.875rem;
          font-weight: 600;
          cursor: pointer;
          transition: background-color 0.15s ease, color 0.15s ease;
          white-space: nowrap;
          font-family: inherit;
        }

        .anp-nav-item:hover {
          background: rgba(255, 255, 255, 0.12);
          color: #FFFFFF;
        }

        .anp-nav-item.active {
          background: var(--anp-blue);
          color: #FFFFFF;
        }

        @media (max-width: 992px) {
          .anp-nav-container {
            flex-direction: column;
            align-items: flex-start;
          }
          .anp-nav-menu {
            width: 100%;
          }
        }
      `}</style>
    </header>
  );
}
